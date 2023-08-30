$(window).on('load', function () {
    // 이벤트 핸들러 함수를 정의합니다.
    function filterItems(filter) {
        $('.grid .box').hide(); // 모든 아이템을 숨김
        $(filter).show(); // 선택한 필터에 해당하는 아이템을 보임
    }

    $('.filters_movieChart li').click(function () {
        $('.filters_movieChart li').removeClass('active');
        $(this).addClass('active');

        var dataFilter = $(this).attr('data-filter');
        filterItems(dataFilter); // 필터링 함수를 호출하여 아이템을 보여줌
    });

    
});