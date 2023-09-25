
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

    function checkScrollPosition() {
        if (window.scrollY <= 1800) {
            scrollUpButton.style.display = "none";
        } else {
            scrollUpButton.style.display = "block";
        }
    }

    window.addEventListener("load", checkScrollPosition);
    window.addEventListener("scroll", checkScrollPosition);
});

 function toggleElement() {
            var screenWidth = window.innerWidth;
            var element = document.querySelector('#footer_title');

            if (screenWidth < 850 && element) {
                element.style.display = 'none';
            } else {
                element.style.display = 'block';
            }
}

window.addEventListener('load', toggleElement);
window.addEventListener('resize', toggleElement);

