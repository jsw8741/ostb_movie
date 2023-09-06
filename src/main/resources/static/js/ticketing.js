// 좌석수, theaterId, 좌석 총 개수, memberId 초기화
var seatCnt = 0;
var theaterId = 0;
var seatNum = 0;
var memberId = 0;
var discount = 0;

// 선택된 좌석을 담을 배열 초기화
var selectedSeats = [];

$("#paySelectWrap").on("click", function() {
	$("#movieSelect").css("display", "none");
	$("#paySelectWrap").css("display", "none");
	$("#seatSelect").css("display", "none");
	$("#totalPrice").css("display", "none");
	$("#total").css("display", "none");
	$("#paySelect").css("display", "block");
	$("#paySelect").css("width", "66.67%");
	$("#payBtnWrap").css("display", "block");
});


// 결제 버튼 클릭 시
$("#payBtnWrap").on("click", function() {
	// 예매 정보를 데이터로 준비
	var data = {
		theaterId: theaterId,
		totalPrice: seatCnt * 10000,
		seatList: selectedSeats.join(", "), // 배열을 문자열로 변환하여 할당
		seatCnt: seatCnt,
		memberId: memberId
	}

/*	// 서버로 예매 정보 전송
	$.ajax({
		type: "POST",
		url: "/book/ticketing",
		contentType: "application/json",
		data: JSON.stringify(data),
		beforeSend: function(jqXHR, setting) {
			var header = $("meta[name='_csrf_header']").attr("content");
			var token = $("meta[name='_csrf']").attr("content");
			jqXHR.setRequestHeader(header, token);
		},
		success: function(data) {
			
			alert('예매가 완료되었습니다.');
			window.location.href = '/members/reserv';
			
			//debugger
		},
		error: function(error) {
			debugger
			console.log(error);
		}
	});*/
	
		$.ajax({
		type: 'GET',
		url: '/bookpay/ready',
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		data: data,
		success: function(res) {
			var box = res.next_redirect_pc_url;
			var popupWidth = 500;
			var popupHeight = 800;
			var left = (screen.width - popupWidth) / 2;
			var top = (screen.height - popupHeight) / 2;
			var popupOptions = "width=" + popupWidth + ",height=" + popupHeight + ",top=" + top + ",left=" + left;
			window.open(box, "KakaoPay", popupOptions);
	/*	
			alert('예매가 완료되었습니다.');
			window.location.href = '/members/reserv';*/
		}
	});
});

// 좌석 클릭 시
function clickSeat(idx) {
	if ($("#seat" + idx).hasClass("done")) {
		alert("이미 예매된 좌석입니다.");
		return false;
	}

	if ($("#seat" + idx).hasClass("selectSeat")) {
		// 좌석 해제
		$("#seat" + idx).removeClass("selectSeat");
		seatCnt--;
		// 배열에서 해당하는 좌석 제거
		var index = selectedSeats.indexOf(idx);
		if (index !== -1) {
			selectedSeats.splice(index, 1);
		}
	} else {
		if (seatCnt >= 6) {
			// 좌석수 제한
			alert("최대 6명까지만 선택 가능합니다.");
			return false;
		}
		// 좌석 선택
		$("#seat" + idx).addClass("selectSeat");
		seatCnt++;
		// 배열에서 해당하는 좌석 추가
		selectedSeats.push(idx);
		// 오름차순 정렬
		selectedSeats.sort(function(a, b) {
			return a - b;
		});
	}

	// 선택한 좌석수와 총 금액 업데이트
	$("#reservationCnt").text(seatCnt + "명");
	var totalAmount = Number(seatCnt) * 10000;
	$("#total").text(totalAmount + "원");
	$("#payTotalPrice").text(totalAmount + "원");
	$("#finalPrice").text(totalAmount - Number(discount) + "원");
	
	

	// 선택한 좌석 업데이트
	var selectSeat = getSelectSeat();
	$(".getSeat").text(selectSeat);
}

// 선택한 좌석을 문자열로 변환
function getSelectSeat() {
	return selectedSeats.join(", ");
}

// 영화 정보 가져오는 함수
function getMovieData(idx) {
	// 초기화
	seatCnt = 0;
	$("#reservationCnt").text(`0명`);
	$("#total").text(`0원`);
	$(".getSeat").text("좌석 선택 가능");

	// 좌석 초기화
	selectedSeats = [];

	// 영화정보 가져와서 저장
	theaterId = $("#theaterId" + idx).text();
	var seat = $("#capacity" + idx).text();
	seatNum = seat.replace("석", "").replace("총 ", "");
	memberId = $("#memberId").text();
	
	// 영화정보 표시
	$(".getTitle").text($("#title" + idx).text());
	$(".getWhere").text($("#where" + idx).text());
	$(".getDate").text($("#startTime" + idx).text());
	$("#getImgUrl").attr("src", $("#movieImg" + idx).text());

	$("#seatDiv").html("");

	// 선택한 영화 좌석정보 조회
	getSeatData(seatNum);
}

// 서버로부터 좌석정보 가져와서 화면에 표시하는 함수
function getSeatData(seatNum) {
	var data = {
		theaterId: theaterId,
	}
	$.ajax({
		type: "POST",
		url: "/book/getSeatData",
		contentType: "application/json",
		data: JSON.stringify(data),
		beforeSend: function(jqXHR, setting) {
			var header = $("meta[name='_csrf_header']").attr("content");
			var token = $("meta[name='_csrf']").attr("content");
			jqXHR.setRequestHeader(header, token);
		},
		success: function(data) {
			var html = "";
			var getIdx = 0;

			// 좌석정보를 화면에 표시
			for (var i = 1; i <= Number(seatNum); i++) {
				if (data.list[getIdx] == i) {
					html += '<span class="boxClr done" onclick="clickSeat(' + i + ')" id="seat' + i + '">' + i + '</span>';
					getIdx++;
					
				} else {
					html += '<span class="boxClr" onclick="clickSeat(' + i + ')" id="seat' + i + '">' + i + '</span>';
				}
				if (i % 10 == 0) {
					html += '<br>';
				}
			}

			$("#seatDiv").html(html);
			$("#movieSelect").css("width", "33.33%");
			$("#seatSelect").css("display", "block");

		},
		error: function(error) {
			debugger
			console.log(error);
		}
	});
}

// 예매 초기화 버튼
$("#resetBtn").on("click", function() {
	window.location.href = '/book/ticketing';
});