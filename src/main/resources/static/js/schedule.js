// 달력 생성 함수
function makeCalendar() {
	const calendar = document.querySelector('.calendar');
	const calendarDays = []; // 달력의 날짜를 저장하는 배열
	const currentDate = new Date(); // 현재 날짜
	const nextWeek = new Date(currentDate);
	nextWeek.setDate(nextWeek.getDate() + 7); // 현재 날짜에서 일주일 뒤 날짜
	const urlParams = new URLSearchParams(window.location.search);
	const selectedDate = urlParams.get('viewDate'); // URL에서 선택된 날짜 읽음
	const yearMonthElement = document.querySelector('.year-month'); // 연월을 나타낼 요소 선택

	// 선택된 날짜가 없으면 현재 날짜 사용, 있는 경우 선택된 날짜 사용
	let firstDayFormattedDate = selectedDate || formatDate(currentDate);

	// 현재 날짜부터 일주일 뒤까지의 날짜를 달력에 추가
	while (currentDate <= nextWeek) {
		const year = currentDate.getFullYear();
		const month = currentDate.getMonth() + 1;
		const day = currentDate.getDate();
		const formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;

		if (!calendarDays.includes(formattedDate)) {
			calendarDays.push(formattedDate);
			const dayHtml = `<div class="calendar-day" data-date="${formattedDate}">${day}</div>`;
			calendar.innerHTML += dayHtml;
		}

		currentDate.setDate(currentDate.getDate() + 1);
	}

	// 달력의 날짜별로 클릭 이벤트 추가
	calendarDays.forEach((day, index) => {
		const dayElement = calendar.querySelectorAll('.calendar-day')[index];
		dayElement.addEventListener('click', () => {
			document.getElementById('selectedDate').value = day; // 선택한 날짜를 hidden input에 설정
			const [year, month] = day.split('-');
			yearMonthElement.textContent = `${year}-${month}`;
			document.querySelector('form.date').submit(); // 폼 제출
		});

		if (day === selectedDate) {
			dayElement.classList.add('selected-day'); // 선택된 날짜에 클래스 추가
		}
	});

	// 선택된 날짜에 따라 연월 표시 업데이트
	if (selectedDate) {
		const [year, month] = selectedDate.split('-');
		yearMonthElement.textContent = `${year}-${month}`;
	} else {
		const [year, month] = firstDayFormattedDate.split('-');
		yearMonthElement.textContent = `${year}-${month}`;
	}
}

// 날짜를 형식에 맞게 포맷해줌
function formatDate(date) {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	return `${year}-${month.toString().padStart(2, '0')}`;
}

makeCalendar();

// 클래스에 연령등급에 맞는 클래스를 추가
function addAgeClass(elementClass) {
	const elements = document.querySelectorAll('.' + elementClass);
	elements.forEach(element => {
		const ageValue = element.textContent;
		const ageClass = ageValue === "전체" ? "age_all" : "age_" + ageValue;
		element.classList.add(ageClass);
	});
}

addAgeClass("age"); // age 클래스에 등급에 맞는 클래스 추가
