
            $(".footer__menu-link").click(function (event) {
                event.preventDefault();
                var target = $(this).attr("href");
                $('html, body').animate({
                    scrollTop: $(target).offset().top
                }, 1000);
            });


document.addEventListener("DOMContentLoaded", function () {
    const scrollDownButton = document.getElementById("scroll-down-button");
    const scrollUpButton = document.getElementById("scroll-up-button");

    scrollDownButton.addEventListener("click", function () {
        window.scrollTo({
            top: document.documentElement.scrollHeight,
            behavior: "smooth"
        });
    });

    scrollUpButton.addEventListener("click", function () {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });

    // Функция для проверки положения прокрутки и скрытия/отображения стрелки
    function checkScrollPosition() {
        if (window.scrollY <= 1800) {
            scrollUpButton.style.display = "none";
        } else {
            scrollUpButton.style.display = "block";
        }
    }

    // Вызываем функцию при загрузке страницы и при прокрутке
    window.addEventListener("load", checkScrollPosition);
    window.addEventListener("scroll", checkScrollPosition);
});