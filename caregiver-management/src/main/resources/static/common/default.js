// 팝업창 드래그 관련 MSIE 정의
jQuery.browser = {};
(function () {
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
})();

// 날짜 선택 한글화
$(function () {
    $.datepicker.regional['ko'] = {
        closeText: '닫기',
        prevText: '이전달',
        nextText: '다음달',
        currentText: '오늘',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        weekHeader: 'Wk',
        dateFormat: 'yy-mm-dd',
        firstDay: 0,
        isRTL: false,
        duration: 200,
        showAnim: 'show',
        showMonthAfterYear: true,
        yearSuffix: '년'
    };
    $.datepicker.setDefaults($.datepicker.regional['ko']);
});

// dateinput-> 날짜 선택 기능 활성화
// numberinput -> 숫자 선택 기능 활성화
$(document).ready(function () {
    var dateinputs = $("input.dateinput").datepicker();
    var numberinputs = $("input.numberinput");
    numberinputs.numeric();
    numberinputs.css("ime-mode", "disabled");
});

function numberWithCommas(x) {
    if (x == null) {
        return '';
    }
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// dialog 클래스에 대해 드래그 속성 추가
$(function () {
    $(".dialog").draggable(
        {
            cursor: "pointer",

            containment: 'window',
            handle: ".title"
        });
});
jQuery.curCSS = function (element, prop, val) {
    return jQuery(element).css(prop, val);
};

function SetCenter(dom) {
    var left = (($(window).width() - dom.width()) / 2);
    var top = window.innerHeight / 2 - dom.actual("height") / 2;

    dom.css({'left': left, 'top': top, 'position': 'absolute'});
}

function yyyymmdd(dateIn) {
    var yyyy = dateIn.getFullYear();
    var mm = dateIn.getMonth() + 1; // getMonth() is zero-based
    var dd = dateIn.getDate();
    return String(yyyy + "-" + ("0" + mm).slice(-2) + "-" + ("0" + dd).slice(-2)); // Leading zeros for mm and dd
}

function PopupClass(text) {
    this.text = text;
    // 기존 목록에서 같은 내용 확인
    var list = $(".semibox");
    if (text.includes("[처리중]") == false) {
        for (var i = 0; i < list.length; i++) {
            var value = list[i];
            if (value.real.text.includes("[처리중]")) {
                clearInterval(value.real.timer);
                document.body.removeChild(value.real.box);
                // value.real.timevalue = 99999;
            }
        }
    }
    for (var i = 0; i < list.length; i++) {
        var value = list[i];
        if (value.real.text == text) {
            value.real.count_up();
            return;
        }
    }
    this.timevalue = 0;
    this.count = 1;
    this.count_up = function () {
        this.timevalue = 28;
        this.count++;
        this.box.innerText = this.text + " (동일 메세지 " + this.count + "건)";
    };
    this.box = document.createElement("div");
    this.box.className = 'semibox';
    this.Update = function () {
        this.timevalue++;
        if (this.timevalue < 30) {
            this.box.style.opacity = this.timevalue / 30.0;
        } else if (this.timevalue < 500) {
            if (this.text.includes("[처리중]")) {
                this.timevalue = 100;
            }
        } else if (this.timevalue < 530) {
            this.box.style.opacity = (530 - this.timevalue) / 30.0;
        } else {
            clearInterval(this.timer);
            document.body.removeChild(this.box);
        }
    };
    this.box.real = this;
    this.timer = null;
    var node = document.createTextNode(text);
    this.box.appendChild(node);
    if ($(".semibox").length >= 1) {
        console.log($(".semibox").get(-1));
        this.box.style.top = $($(".semibox").get(-1)).offset().top + 50;
    }
    this.timer = setInterval(this.Update.bind(this), 10);
    document.body.appendChild(this.box);
}

function SemiPopup(text) {
    new PopupClass(text);
}

function SemiDialog(title, message) {
    message = message.replaceAll("\n", "<BR>");
    $('#dialog-message').attr("title", title);
    $('#dialog-message-content')[0].innerHTML = message;
    $('#dialog-message').dialog({
        modal: true,
        width: "500px",
        buttons: {
            "확인": function () {
                $(this).dialog('close');
            }
        }
    });
    SetCenter($('.ui-dialog'));
    $('#dialog-message-content')[0].scrollTop = 0;
}

function print_page_go() {
    var popUrl = "/function/PrintPopup.php";
    var popOption = "width=600, height=400, resizable=no, scrollbars=no, status=no;";
    window.open(popUrl, "", popOption);
}

function getFormattedBirthDate(birth1, birth2) {
    if (!/^\d{6}$/.test(birth1) || !/^\d{1,}$/.test(birth2)) return null;

    const genderCode = parseInt(birth2.charAt(0), 10);
    let century;

    switch (genderCode) {
        case 1: case 2: case 5: case 6:
            century = '19';
            break;
        case 3: case 4: case 7: case 8:
            century = '20';
            break;
        case 9: case 0:
            century = '18';
            break;
        default:
            return null; // 잘못된 코드
    }

    const year = century + birth1.slice(0, 2);
    const month = birth1.slice(2, 4);
    const day = birth1.slice(4, 6);

    return `${year}-${month}-${day}`;
}