$.ajaxSetup({
    contentType: "application/json",
    dataType: "json"
});

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
            if (data.success == 0) {
                $("#login_message").text(data.error);
            } else {
                location.href = "/account/info/";
            }
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