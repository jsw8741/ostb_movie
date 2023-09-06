// 달력 생성
function makeCalendar() {
	// 달력 요소를 가져옴
	const calendar = document.querySelector('.calendar');
	const calendarDays = []; // 달력에 표시할 날짜를 저장할 배열
	const currentDate = new Date(); // 현재 날짜를 가져온다
	const nextWeek = new Date(currentDate); // 현재 날짜로부터 7일 뒤 날짜 계산
	nextWeek.setDate(nextWeek.getDate() + 7);
	const urlParams = new URLSearchParams(window.location.search);
	const selectedDate = urlParams.get('viewDate'); // url에서 viewDate 매개변수를 가져온다
	const yearMonthElement = document.querySelector('.year-month'); // 년월을 표시할 요소를 가져온다

	// 선택된 날짜가 없으면 현재 날짜 사용
	let firstDayFormattedDate = selectedDate || formatDate(currentDate);

	// 현재 날짜부터 다음 주까지의 날짜를 반복
	while (currentDate <= nextWeek) {
		const year = currentDate.getFullYear(); // 년 
		const month = currentDate.getMonth() + 1; // 월
		const day = currentDate.getDate(); // 일을 가져온다
		const formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;

		// 중복되지 않는 날짜를 배열에 추가하고 달력에 날짜 추가
		if (!calendarDays.includes(formattedDate)) {
			calendarDays.push(formattedDate);
			const dayHtml = `<div class="calendar-day" data-date="${formattedDate}">${day}</div>`;
			calendar.innerHTML += dayHtml;
		}

		// 다음 날짜로 이동
		currentDate.setDate(currentDate.getDate() + 1);
	}

	// 각 날짜에 클릭 이벤트 추가, 선택된 날짜에 클래스 추가
	calendarDays.forEach((day, index) => {
		const dayElement = calendar.querySelectorAll('.calendar-day')[index];
		dayElement.addEventListener('click', () => {
			document.getElementById('selectedDate').value = day;
			const [year, month] = day.split('-');
			yearMonthElement.textContent = `${year}-${month}`;
			document.querySelector('form.date').submit();
		});

		if (day === selectedDate) {
			dayElement.classList.add('selected-day');
		}
	});

	// 선택된 날짜가 있으면 해당 년월을 표시, 없으면 첫번째 날짜의 년월을 표시
	if (selectedDate) {
		const [year, month] = selectedDate.split('-');
		yearMonthElement.textContent = `${year}-${month}`;
	} else {
		const [year, month] = firstDayFormattedDate.split('-');
		yearMonthElement.textContent = `${year}-${month}`;
	}
}

// 날짜를 YYYY-MM 형식으로 포맷
function formatDate(date) {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	return `${year}-${month.toString().padStart(2, '0')}`;
}

// 나이에 따른 클래스 추가
function addAgeClass(elementClass) {
	const elements = document.querySelectorAll('.' + elementClass); // 클래스가 있는 모든 요소 선택
	elements.forEach(element => {
		const ageValue = element.textContent; // 텍스트 내용을 가져온다
		const ageClass = ageValue === "전체" ? "age_all" : "age_" + ageValue; // 나이에 따라 클래스 이름 생성
		element.classList.add(ageClass); // 생성된 클래스를 요소에 추가
	});
}

// 나이에 따른 클래스 추가 함수 호출
addAgeClass("age");

// 달력 생성하는 함수 호출
makeCalendar();
