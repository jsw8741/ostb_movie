var seatCnt = 0; // 선택한 좌석수 저장
var movieId = 0; // 선택한 영화id 저장
var seatNum = 0; // 총 좌석수 저장

$("#paySelectBtn").on("click", function() {
	// 예매 정보를 담는 data 객체를 생성한다
	var data = {
		theaterId: movieId,
		totalPrice: seatCnt * 10000,
		seatList: [],
		seatCnt: seatCnt
	}

	// 선택한 좌석 정보를 data 객체에 추가
	for (var i = 1; i <= Number(seatNum); i++) {
		if ($("#seat" + i).hasClass("changeRed")) data.seatList.push(i);
	}
	// ajax로 티켓 예매 정보 전송
	$.ajax({
		type: "POST",
		url: "/book/tickeing",
		contentType: "application/json",
		data: JSON.stringify(data),
		beforeSend: function(jqXHR, setting) {
			// CSRF 토큰 헤더에 추가
			var header = $("meta[name='_csrf_header']").attr("content");
			var token = $("meta[name='_csrf']").attr("content");
			jqXHR.setRequestHeader(header, token);
		},
		success: function(data) {
			// debugger
			var redirectUrl = "/";

			window.location.href = redirectUrl;
		},
		error: function(error) {
			debugger
			console.log(error);
		}
	});
});

// 좌석 클릭할때 호출되는 함수
function clickSeat(idx) {
	// 이미 예약된 좌석인 경우
	if ($("#seat" + idx).hasClass("changeGreen")) {
		alert("이미 예약된 좌석입니다.");
		return false;
	}

	// 좌석 선택 또는 취소
	if ($("#seat" + idx).hasClass("changeRed")) {
		$("#seat" + idx).removeClass("changeRed");
		seatCnt--;
	} else {
		$("#seat" + idx).addClass("changeRed");
		seatCnt++;
	}

	// 선택한 좌석 수와 합계 업데이트
	$("#reservationCnt").text(seatCnt);
	var totalAmount = Number(seatCnt) * 10000;
	$("#total").text(totalAmount + "원");
}

// 영화 정보 가져오는 함수
function getMovieData(idx) {
	// 초기화
	seatCnt = 0;
	$("#reservationCnt").text(0);
	$("#total").text(0);

	// 영화 정보 가져와서 저장
	movieId = $("#movieId" + idx).text();
	var seat = $("#capacity" + idx).text();
	seatNum = seat.replace("석", "").replace("총 ", "");

	// 영화정보 표시
	$(".getTitle").text($("#title" + idx).text());
	$(".getWhere").text($("#where" + idx).text());
	$(".getDate").text($("#startTime" + idx).text());
	$(".getSeat").text(seat);
	$("#getImgUrl").attr("src", $("#movieImg" + idx).text());

	$("#seatDiv").html("");

	// 선택한 영화 좌석정보 조회
	getSeatData(seatNum);
}

// 알파벳과 숫자로 이루어진 좌석 이름 생성
function createSeatName(number) {
  var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  var column = Math.ceil(number / 10); // 열 번호 (10개마다 열 번호 증가)
  var row = (number - 1) % 10 + 1; // 행 번호 (1부터 10까지 순환)
  return alphabet.charAt(column - 1) + row; // 열과 행 번호 조합해서 좌석 이름 생성
}

// 좌석정보 조회하는 함수
function getSeatData(seatNum) {
  var data = {
    theaterId: movieId,
  };
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

      for (var i = 1; i <= Number(seatNum); i++) {
        var seatName = createSeatName(i); // 좌석 이름 생성
        if (data.list[getIdx] == i) {
          html += '<span class="boxClr changeGreen" onclick="clickSeat(' + i + ')" id="seat' + i + '">' + seatName + '</span>';
          getIdx++;
        } else {
          html += '<span class="boxClr" onclick="clickSeat(' + i + ')" id="seat' + i + '">' + seatName + '</span>';
        }
        if (i % 10 == 0) {
          html += '<br>';
        }
      }

      $("#seatDiv").html(html);
      $("#seatSelect").css("display", "block");
      $("#movieSelect").css("width", "33.33%");

    },
    error: function(error) {
      debugger
      console.log(error);
    }
  });
}
