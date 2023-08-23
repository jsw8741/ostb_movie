function updatePoster() {
			const movieSelect = document.getElementById("movie");
			const poster = document.getElementById("poster");

			const selectedMovie = movieSelect.options[movieSelect.selectedIndex];

			const imgUrl = selectedMovie.getAttribute("data-imgurl");
			const imgAlt = selectedMovie.getAttribute("data-imgalt");

			poster.src = imgUrl;
			poster.alt = imgAlt;
		}

function updateCapacity() {
			const theaterInfoSelect = document.getElementById("theaterInfo");
			const capacityInput = document.getElementById("capacityInput");

			const selectedTheater = theaterInfoSelect.value;

			if (selectedTheater.includes("6층")) {
				capacityInput.value = 92;
			} else if (selectedTheater.includes("7층")) {
				capacityInput.value = 94;
			} else {
				capacityInput.value = "";
			}
		}


function updateEndTime() {
			const startTimeInput = document.getElementById("startTimeInput");
			const endTimeInput = document.getElementById("endTimeInput");
			const movieSelect = document.getElementById("movie");

			const startTime = new Date(startTimeInput.value);
			const runTimeString = movieSelect.options[movieSelect.selectedIndex].getAttribute("data-runtime");
			const runTime = parseInt(runTimeString);

			const endTime = new Date(startTime.getTime() + runTime * 60 * 1000);

			const year = endTime.getFullYear();
			const month = String(endTime.getMonth() + 1).padStart(2, '0');
			const day = String(endTime.getDate()).padStart(2, '0');
			const hours = String(endTime.getHours()).padStart(2, '0');
			const minutes = String(endTime.getMinutes()).padStart(2, '0');

			const endTimeFormatted = `${year}-${month}-${day}T${hours}:${minutes}`;

			endTimeInput.value = endTimeFormatted;
		}