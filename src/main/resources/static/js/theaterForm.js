// 영화 선택하면 포스터 띄우기
function updatePoster() {
    const movieSelect = document.getElementById("movie");
    const poster = document.getElementById("poster");

    // 선택된 영화정보 가져오기
    const selectedMovie = movieSelect.options[movieSelect.selectedIndex];

    // 선택된 영화 이미지의 url, alt 가져오기
    const imgUrl = selectedMovie.getAttribute("data-imgurl");
    const imgAlt = selectedMovie.getAttribute("data-imgalt");

    // 이미지와 alt 업데이트
    poster.src = imgUrl;
    poster.alt = imgAlt;
}

// 상영관정보 선택하면 수용인원 바꿔주기
function updateCapacity() {
    const theaterInfoSelect = document.getElementById("theaterInfo");
    const capacityInput = document.getElementById("capacityInput");

    // 선택된 상영관정보를 가져온다
    const selectedTheater = theaterInfoSelect.value;

    // 상영관에 따라 수용인원 변경
    if (selectedTheater.includes("6층")) {
        capacityInput.value = 48;
    } else if (selectedTheater.includes("7층")) {
        capacityInput.value = 56;
    } else {
        capacityInput.value = "";
    }
}

// 상영시작시간 선택하면 종료시간 업데이트 해주는 함수
function updateEndTime() {
    const startTimeInput = document.getElementById("startTimeInput");
    const endTimeInput = document.getElementById("endTimeInput");
    const movieSelect = document.getElementById("movie");

    // 선택된 시작시간 정보와 영화의 상영시간정보 가져오기
    const startTime = new Date(startTimeInput.value);
    const runTimeString = movieSelect.options[movieSelect.selectedIndex].getAttribute("data-runtime");
    const runTime = parseInt(runTimeString);

    // 종료시간 계산
    const endTime = new Date(startTime.getTime() + runTime * 60 * 1000);

    // 종료시간 포맷해줌
    const year = endTime.getFullYear();
    const month = String(endTime.getMonth() + 1).padStart(2, '0');
    const day = String(endTime.getDate()).padStart(2, '0');
    const hours = String(endTime.getHours()).padStart(2, '0');
    const minutes = String(endTime.getMinutes()).padStart(2, '0');

    const endTimeFormatted = `${year}-${month}-${day}T${hours}:${minutes}`;

    // 종료시간 입력란 업데이트
    endTimeInput.value = endTimeFormatted;
}
