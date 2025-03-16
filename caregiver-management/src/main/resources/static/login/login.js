function login() {
    $("#login_message").text("로그인 시도중...");
    $.ajax({
        type: "POST",
        data: JSON.stringify({
            loginId: $("#id").val(),
            password: $("#pass").val()
        }),
        url: '/api/v1/auth/login',
        success: function (data) {
            location.href = "/account/info";
            authTokenManager.setTokens({
                accessToken: data.accessToken,
                refreshToken: data.refreshToken,
                expireAt: data.expireAt
            });
        },
        error: function (jqXHR) {
            data = jqXHR.responseJSON;
            $("#login_message").text(data.errors[0].message);
        }
    });
}

function getQueryParam(param) {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

$(document).ready(function () {
    var tenantId = getQueryParam("tenant_id")

    $.ajax({
        type: "GET",
        url: `/api/v1/tenants/${tenantId}`,
        success: function (data) {
            $(".login_title").text(`${data.name}`);
        },
        error: function () {
            $(".login_title").text("로그인");
        }
    });
});