function getParameters(paramName, defaultValue = null) {
    const url = location.href;
    const parameters = new URLSearchParams(url.slice(url.indexOf('?') + 1));

    const value = [...parameters.entries()]
        .find(([key]) => key.localeCompare(paramName, undefined, {sensitivity: 'accent'}) === 0);

    return value ? decodeURIComponent(value[1]) : defaultValue;
}

function updateURLParameter(url, param, paramVal){
    var newAdditionalURL = "";
    var tempArray = url.split("?");
    var baseURL = tempArray[0];
    var additionalURL = tempArray[1];
    var temp = "";
    if (additionalURL) {
        tempArray = additionalURL.split("&");
        for (var i=0; i<tempArray.length; i++){
            if(tempArray[i].split('=')[0] != param){
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }

    var rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
}

function changeOrder(select)
{
	location.href = updateURLParameter(document.URL,'order', select.value);
}

function makeDOM(no, item) {
	// tr 태그 생성
	const $domdata = $(`
				  <tr class="hoverok">
					  <td class="item_no"></td>
					  <td class="item_status"></td>
					  <td class="item_name"></td>
					  <td class="item_birth"></td>
					  <td class="item_startdate"></td>
					  <td class="item_enddate"></td>
					  <td class="insurance_amount"></td>
					  <td class="refund_amount"></td>
					  <td class="difference_amount"></td>
					  <td class="item_memo"></td>

				  </tr>
				`);

	$domdata
		.attr("data-value", item.id)
		.attr("title", `${item.createdAt}에 등록된 데이터입니다.`);

	// 상태별로 클래스 추가
	if (item.isApproved == false) {
		$domdata.addClass("new");
	} else if (item.status == "삭제됨") {
		$domdata.addClass("delete");
	} else if (item.status == "퇴사") {
		$domdata.addClass("end");
	}
	let insuranceName = item.isShared ? "공동": "개인"
	$domdata.find("td.item_no").text(no);
	$domdata.find("td.item_status").text(item.status + " (" + insuranceName + ")");
	$domdata.find("td.item_name").text(item.name);
	$domdata.find("td.item_birth").text(item.birth);
	$domdata.find("td.item_startdate").text(`${item.startDate} (${item.contractDays}일)`);

	if (item.endDate) {
		$domdata.find("td.item_enddate").text(`${item.endDate} (${item.effectiveDays}일)`);
	} else {
		$domdata.find("td.item_enddate").html(`
      <a onclick="openPopup(${item.id})" class="button_style1">
        해지 신청
      </a>
    `);
	}

	$domdata.find("td.insurance_amount")
		.text(numberWithCommas(item.insuranceAmount));
	$domdata.find("td.refund_amount")
		.text(numberWithCommas(item.refundAmount));
	$domdata.find("td.difference_amount")
		.text(numberWithCommas(item.insuranceAmount - item.refundAmount));

	$domdata.find("td.item_memo")
		.attr("onclick", `memo_click(this, ${item.id})`)
		.css("width", "200px")
		.text(item.memo);
	return $domdata
}

function refreshTable(){
	let sortBy = $(`input[name="order"]`).val();
	$.ajax({
		type: "GET",
		url: `/api/v1/users/${authTokenManager.getUserId()}/insurance/caregivers?sortBy=${sortBy}`,
		success: function(data){
			ListData = [];
			const $view = $("#view");
			$view.empty();

			let no = 1;
			data.data.forEach((item) => {
				ListData.push(item);
				$view.append(makeDOM(no++, item));
			});

			// 검색된 키워드 반영
			search();

		},
		error:function(jqXHR, textStatus, errorThrown){
			$("#view").empty();
			$("#view").text("Not Working");
		}
	});
}

function openPopup(no, name)
{
	var now_item = null;
	for (element_idx in ListData) {
		if (ListData[element_idx].hide_no == no)
		{
			now_item = ListData[element_idx];
			break;
		}
	}
	SetCenter($('.dialog'));
	$("form").find("input[name=date]").val(yyyymmdd(new Date()));
	$("input[name=no]").val(no);
	if ($(".admin_input").length > 0)
	{// 관리자

		$("input[name=name]").val(now_item.name);
		var temp = now_item.birth.replace(/[^0-9]/g,'');
		$("input[name=birth]").val(temp);
		$("input[name=start]").val(now_item.start);
		$("input[name=type]").val(now_item.type);
		$("input[name=date]").val(now_item.end);
	}
	else
	{
		$("#id").text(name);
	}
	$(".popup_area").show();
	
}
function closePopup()
{
	$(".popup_area").hide();
}
function Remove()
{
	var input = prompt("정말 ["+$("input[name=name]").val()+"]를 삭제하고싶다면 이름을 \"" + $("input[name=name]").val() + "삭제" + "\" 라고 입력해주세요.\r\n\r\n * 참고\r\n    데이터베이스에서 완전히 삭제하는 것이 아닌 \r\n    추적만 종료(숨김)하기 때문에 복구가 가능합니다.", '');
	if (input==$("input[name=name]").val() + "삭제")
	{
		$.ajax({
			type: "POST",
			data: {
				type: "Remove",
				no: $("input[name=no]").val()
			},
			url:'/function/List_Update.php',
			success:function(data){
				if (data.error == null)
				{
					alert("처리되었습니다.");
					refreshTable();
				}
				else
				{
					alert(data.error);
				}
				closePopup();
			},
			error:function(jqXHR, textStatus, errorThrown){
				$("#view").text("Not Working");
				closePopup();
			}
		});
	}
}
function Termination()
{
	// 관리자 체크는 서버단에서 한번더 확인
	if ($(".admin_input").length > 0)
	{
		$.ajax({
		type: "POST",
		data: {
			type: "AllChangeByAdmin",
			no: $("input[name=no]").val(),
			name: $("input[name=name]").val(),
			birth: $("input[name=birth]").val(),
			start: $("input[name=start]").val(),
			item_type: $("input[name=type]").val(),
			end: $("input[name=date]").val()
		},
		url:'/function/List_Update.php',
		success:function(data){
			if (data.error == null)
			{
				alert("처리되었습니다.");
				refreshTable();
			}
			else
			{
				alert(data.error);
			}
			closePopup();
		},
		error:function(jqXHR, textStatus, errorThrown){
			
			$("#view").text("Not Working");
			closePopup();
		}
	});
	}
	else
	{
		$.ajax({
		type: "POST",
		data: {
			type: "End",
			no: $("input[name=no]").val(),
			end: $("input[name=date]").val()
		},
		url:'/function/List_Update.php',
		success:function(data){
			if (data.error == null)
			{
				alert("처리되었습니다.");
				refreshTable();
			}
			else
			{
				alert(data.error);
			}
			closePopup();
		},
		error:function(jqXHR, textStatus, errorThrown){
			
			$("#view").text("Not Working");
			closePopup();
		}
	});
	}
}

function memo_click(box,first_no)
{
	if ($(box).find("input").length == 0)
	{	
		inputbox = $("<input style=\"width:100%;\" type='text'></input>");
		inputbox.val($(box).text());
		$(box).empty();
		// 새로운 박스 생성
		$(box).append("<input style=\"display: none\" type='text' value="+first_no+"></input>");
		$(box).append(inputbox);
		inputbox.focus();
		inputbox.keydown(function (key) {
			if (key.keyCode == 9) { 
				next_td = $(this).closest('tr').next('tr').find("td:eq(9)");
				console.log(next_td);
				next_td.click();
		    }
			if(key.keyCode == 13){//키가 13이면 실행 (엔터는 13)
				inputbox.blur();
			}
	 
		});
		inputbox.blur(function() { 
			message=this.value;
			this.value = "변경 내용 처리중...";
			$.ajax({
			type: "POST",
			data: {
				type: "MemoUpdate",
				first_no: $(this).siblings().val(),
				text: message  
			},
			url:'/function/ListUpdatev2.php',
			success:function(data){
				if (data.error == null)
				{
					//page_load();
					memo_update(data.first_no,data.no,data.text);
				}
				else
				{
					SemiPopup(data.error);
				}
			},
			error:function(jqXHR, textStatus, errorThrown){
				
				$("#view").text("Not Working");
				closePopup();
			}
		});


		});
		
		}
}

function memo_update(first_no, no, text)
{
	$.ajax({
		type: "POST",
		data: {
			item_no:no,
			return_value:first_no
		},
		url:'/function/listview.php',
		success:function(data){
			var view = $("#view");
			if (data.errer == null)
			{
				var item = data.list[0];
				ListData.push(item);
				var temp_text = "";
				var domdata = $("<tr first_no="+item.first_no+" data-value="+item.hide_no+" title=\"" + item.log_time + "에 등록된 데이터입니다.\" class=\"hoverok\"></tr>");
				if (item.ok == 0) domdata.addClass("new");
				else if (item.state.match("삭제됨")) domdata.addClass("delete");
				else if (item.state.match("퇴사")) domdata.addClass("end");

				if ($(".admin_input").length > 0) {
					temp_text += ("<td class=\"date_button_width\"><a onclick=\"openPopup(" + item.hide_no + ",'" + item.name + "')\" class=\"button_style1\">강제 수정</a></td>");
				}
				else {
					var td_with_item_no = $("#view > tr[first_no=" + data.return_value +"] > td.item_no");
					console.log(td_with_item_no);
					temp_text += ("<td class='item_no'>" + td_with_item_no.text() + "</td>"); // 기존과 같은 번호 사용
				}

				temp_text += ("<td>" + item.state + "</td>");
				temp_text += ("<td class='item_name'>" + item.name + "</td>");
				temp_text += ("<td>" + item.birth + "</td>");

				temp_text += ("<td class=\"date_button_width\">" + item.start + " (" + item.contract_day + "일)</td>");
				if (item.end != undefined)
				{
				temp_text += ("<td class=\"date_button_width\">" + item.end + " (" + item.real_day + "일)</td>");
				}
				else
				{
				temp_text += ("<td class=\"date_button_width\"><a onclick=\"openPopup(" + item.hide_no + ",'" + item.name + "')\" class=\"button_style1\">해지 신청</a></td>");
				}
				temp_text += ("<td>" + item.pay + "</td>");



				if (item.return_pay != undefined)
				{
				temp_text += ("<td>" + item.return_pay + "</td>");
				}
				else
				{
				temp_text += ("<td></td>");
				}
				temp_text += ("<td>" + item.total_pay + "</td>");

				temp_text += ("<td onclick=\"memo_click(this," + item.first_no + ")\" style=\"width:200px\" class='item_memo'>" + item.memo + "</td>");

				domdata.append(temp_text);
				// 이 데이터를 기존 데이터와 교체

				v = $("#view > tr[first_no=" + data.return_value +"]");
				v.html(domdata.html());
				v[0].setAttribute('data-value', item.hide_no);
			}
		}
	});
}

$(document).ready(function() {
	let sortOrder = getParameters("order");
	sortOrder = sortOrder ? sortOrder : "id";

	$(`input[name="order"][value=${sortOrder}]`).prop("checked", true);
	refreshTable();
}); 

function search() {
	var value = $("input[name='search_keyword']").val()
	try {
		$("#view tr").each(function (index, item) {
			if ($(item).find('.item_memo').text().search(value) != -1 || $(item).find('.item_name').text().search(value) != -1) {
				$(item).show()
			} else {
				$(item).hide()
			}
			//$(item).hide()
		});
		$("input[name='search_keyword']").css( "outline", "none" );
	} catch (err) {
		$("input[name='search_keyword']").css( "outline", "1px solid rgb(255, 11, 11)" );
	}
}

function search_save() {
	var items = Array();
	$("#view tr").each(function (index, item) {
		if ($(item).is(':visible')) {
			items.push($(item).attr('first_no'))
		}
	});
	location.href = "/function/ExcelDownloadv3.php?items=" + items.join(',');
}