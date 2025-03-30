


function createCaregiver()
{
	$("#each").prop("disabled", true);
	$.ajax({
		type: "POST",
		data: JSON.stringify({
			name: $('input[name=name]').val(),
			isShared: $('input:radio[name="isShared"]:checked').val(),
			birthday: birthdayFormatter.sixDigitToStrDate($('input[name=birth1]').val(), $('input[name=birth2]').val()),
			genderCode: $('input[name=birth2]').val(),
			startDate: $('#datepicker1').val(),
		}),
		url: `/api/v1/users/${authTokenManager.getUserId()}/insurance/caregivers`,
		success: function (data) {

		},
		error: function (jqXHR) {
			data = jqXHR.responseJSON;
			SemiPopup(data.errors[0].message);
		},
		complete: function () {
			$("#each").prop("disabled", false);
		}
	});
}

function preview(type2)
{
	var arrays = new Array;

	var temp = $("#" + type2 + "box").val();


    val = new String(temp);
	//var regex = /[^0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣\n[-] ]/g;
    var regex = /[^0-9,-A|A-Z|a-z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣\n,* ]/g;
    val = val.replace(regex, ',');
	val = val.replace('     ', ',');
	val = val.replaceAll('    ', ',');
	val = val.replaceAll('   ', ',');
	//alert(val);
	var lines = val.split('\n');
	var data = "";
	for (var i = 0; i < lines.length; i++) {
		var afterStr = lines[i].split(',');
		for(i2=0;i2<afterStr.length;i2++)
		{
			afterStr[i2] = afterStr[i2].trim();
		}
		if (afterStr.length == 3 || (afterStr.length == 4 && afterStr[3] == ""))
		{
			afterStr[1] = afterStr[1].replace('-', '');
			afterStr[1] = afterStr[1].substring(0,7);
			var date = Date.parse( afterStr[2]);
			data += "이름(" + afterStr[0] + "), 주민번호(" + afterStr[1] + "), 시작일(" + yyyymmdd(new Date(date)) + ")<br>";
			var set =
			{
				name:afterStr[0],
				birth:afterStr[1].substring(0,7),
				start: yyyymmdd(new Date(date)),
				type: type2
			}
				arrays.push(set);
		}
		else if (afterStr.length == 4)
		{
			afterStr[1] = afterStr[1].replace('-', '');
			afterStr[1] = afterStr[1].substring(0,7);
			var date = Date.parse( afterStr[2]);
			var date2 = Date.parse( afterStr[3]);
			data += "이름(" + afterStr[0] + "), 주민번호(" + afterStr[1] + "), 시작일(" + yyyymmdd(new Date(date)) + "), 해지일(" + yyyymmdd(new Date(date2)) + ")<br>";
			var set =
			{
				name:afterStr[0],
				birth:afterStr[1].substring(0,7),
				start: yyyymmdd(new Date(date)),
				end: yyyymmdd(new Date(date2)),
				type: type2
			}
				arrays.push(set);
		}
		

	}
	$("#preview_"+type2).html(data);
	return arrays;
}

function summit_all(type)
{	
	var data = preview("personal").concat(preview("public"));

		$("#all").prop("disabled", true);
		if (data.length >= 20) {
                        	SemiPopup("[처리중] 대량 데이터는 시간이 소요됩니다. 잠시만 기다려주세요. (" + data.length + " 건)");
		}
		$.ajax({
			type: "POST",
			data: {
				d: data
			},
			url:'/function/ListAdd.php',
			success:function(data){
				if (data.report != null) SemiDialog("제출 결과", data.report);
				if (data.message != null)
				{
					data.message.forEach( function( v, i ){
					SemiPopup(v);
					});
				} else { 
					alert("에러 발생 (관리자에게 문의해주세요)" + ": " + data);
                                                } 
				$("#all").prop("disabled", false);
			},
			error:function(jqXHR, textStatus, errorThrown){
				alert("에러 발생 \n" + textStatus + " : " + errorThrown);
				console.log(jqXHR);
				console.log(textStatus);
				$("#all").prop("disabled", false);
			}
		});
	
}

$(document).ready(()=> {
	// 현재 날짜 구하기
	const today = new Date();
	const yyyy = today.getFullYear();
	const mm = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
	const dd = String(today.getDate()).padStart(2, '0');

	const formattedDate = `${yyyy}-${mm}-${dd}`;

	$('#datepicker1').val(formattedDate);
	requestEstimate();
})

function requestEstimate() {
	startDate = $('#datepicker1').val();
	isShared = $('input:radio[name="isShared"]:checked').val();
	$("#insuranceEndDate").text("")
	$("#insuranceAmount").text("")
	$.ajax({
		type: "GET",
		url: `/api/v1/users/${authTokenManager.getUserId()}/insurance/caregivers/estimate?startDate=${startDate}&isShared=${isShared}`,
		success: function (data) {
			$("#insuranceEndDate").text(`${data.endDate} (${data.contractDays}일)`);
			$("#insuranceAmount").text(`${numberWithCommas(data.insuranceAmount)}원`);
		},
		error: function (jqXHR) {
			data = jqXHR.responseJSON;
			$("#insuranceEndDate").text("계산 불가")
		}
	});
}