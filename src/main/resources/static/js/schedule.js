// 달력 생성
		function makeCalendar() {
			const calendar = document.querySelector('.calendar');
			// 현재 날짜를 가져온다
			const currentDate = new Date();
			const nextWeek = new Date(currentDate);
			nextWeek.setDate(nextWeek.getDate() + 7);
			let calendarHtml = '';
			let firstDayFormattedDate = '';
			while (currentDate <= nextWeek) {
				const year = currentDate.getFullYear();
				const month = currentDate.getMonth() + 1;
				const day = currentDate.getDate();
				const formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
				if (!firstDayFormattedDate) {
					firstDayFormattedDate = formattedDate;
				}
				// 날짜 생성
				calendarHtml += `<div class="calendar-day" data-date="${formattedDate}">${day}</div>`;
				currentDate.setDate(currentDate.getDate() + 1);
			}
			calendar.innerHTML = calendarHtml;
			// 모든 날짜 선택
			const calendarDays = document.querySelectorAll('.calendar-day');
			calendarDays.forEach(day => {
				day.addEventListener('click', () => {
					// 선택한 날짜 가져온다
					const selectedDate = day.getAttribute('data-date');
					document.getElementById('selectedDate').value = selectedDate;
					const yearMonthElement = document.querySelector('.year-month');
					const [year, month] = selectedDate.split('-');
					yearMonthElement.textContent = `${year}-${month}`;
					// 폼 전송
					document.querySelector('form.date').submit();
				});
			});
			const yearMonthElement = document.querySelector('.year-month');
			const [year, month] = firstDayFormattedDate.split('-');
			yearMonthElement.textContent = `${year}-${month}`;
		}
		makeCalendar();

		// 모든 날짜 선택
		const calendarDays = document.querySelectorAll('.calendar-day');
		//url에서 선택한 날짜 정보 가져온다 
		const urlParams = new URLSearchParams(window.location.search);
		// 선택한 날짜에만 클래스 추가
		const selectedDate = urlParams.get('viewDate');
		calendarDays.forEach(day => {
			const dataDate = day.getAttribute('data-date');
			if (dataDate === selectedDate) {
				day.classList.add('selected-day');
			}
		});

		function addAgeClass(elementClass) {
			const elements = document.querySelectorAll('.' + elementClass);
			const addAgeClassCallback = (element) => {
				const ageValue = element.textContent;
				const ageClass = ageValue === "전체" ? "age_all" : "age_" + ageValue;
				element.classList.add(ageClass);
			};
			elements.forEach(addAgeClassCallback);
		}
		addAgeClass("age");