var authTokenManager = (function() {
    var accessToken = null;
    var refreshToken = null;
    var expireAt = null;

    function loadTokens() {
        accessToken = localStorage.getItem("accessToken");
        refreshToken = localStorage.getItem("refreshToken");
        expireAt = localStorage.getItem("expireAt");
    }
    loadTokens();

    return {
        setTokens: function(tokens) {
            accessToken = tokens.accessToken;
            refreshToken = tokens.refreshToken;
            expireAt = tokens.expireAt;
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            localStorage.setItem("expireAt", expireAt);
        },
        getAccessToken: function() {
            return accessToken;
        },
        // refreshToken도 조회할 수 있도록 추가
        getRefreshToken: function() {
            return refreshToken;
        },
        logout: function() {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            localStorage.removeItem("expireAt");
            location.href = "/login";
        },
        isTokenExpired: function() {
            var currentTime = Math.floor(Date.now() / 1000);
            return expireAt && currentTime > expireAt;
        }
    };
})();


$.ajaxSetup({
    contentType: "application/json",
    dataType: "json",
    beforeSend: function(xhr, settings) {
        var token = authTokenManager.getAccessToken();
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    }
});

function refreshAccessToken() {
    // refreshToken을 이용해 새로운 access token 발급 요청
    return $.ajax({
        type: "POST",
        url: "/api/v1/auth/refresh",
        data: JSON.stringify({
            refreshToken: authTokenManager.getRefreshToken()
        }),
        contentType: "application/json"
    })
    .done(function(data) {
        // 응답 받은 토큰들을 저장
        authTokenManager.setTokens(data);
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        // 400 혹은 401 에러 발생 시 강제 로그아웃
        console.log(jqXHR);
        if (jqXHR.status === 400 || jqXHR.status === 401) {
            authTokenManager.logout();
        }
    });
}

$(document).ready(function() {
    (function($) {
        var originalAjax = $.ajax;
        $.ajax = function(options) {
            var originalError = options.error;
            options._retry = options._retry || false;

            options.error = function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401 && !options._retry) {
                    options._retry = true;
                    refreshAccessToken().done(function() {
                        $.ajax(options);
                    }).fail(function() {
                        if (originalError) {
                            originalError(jqXHR, textStatus, errorThrown);
                        }
                    });
                } else {
                    if (originalError) {
                        originalError(jqXHR, textStatus, errorThrown);
                    }
                }
            };

            return originalAjax(options);
        };
    })(jQuery);
});
