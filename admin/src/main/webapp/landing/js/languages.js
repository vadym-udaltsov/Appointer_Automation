const languageSelect = document.getElementById("languageSelect");
const circleContainer = document.getElementsByClassName('circle-container')[0];
let isOpen = false;

languageSelect.addEventListener('click', function(event) {
  event.stopPropagation();

  if (isOpen) {
    circleContainer.style.display = 'none';
    isOpen = false;
  } else {
    circleContainer.style.display = 'flex';
    isOpen = true;
  }
});

document.addEventListener('click', function(event) {
  if (!languageSelect.contains(event.target) && !circleContainer.contains(event.target)) {
    circleContainer.style.display = 'none';
    isOpen = false;
  }
});

circleContainer.addEventListener('click', function(event) {
  event.stopPropagation();
});

document.addEventListener("DOMContentLoaded", function () {
    const currentPageUrl = window.location.href;

    const languageSelect = document.getElementById("languageSelect");
    const circleContainer = document.querySelector('.circle-container');

    const languageFromUrl = currentPageUrl.split('/').pop().split('landing')[1].split('.')[0];

    if (languageFromUrl) {
        const selectedLanguage = document.getElementById(languageFromUrl);
        if (selectedLanguage) {
            const url = selectedLanguage.getAttribute("value_url");
            languageSelect.textContent = selectedLanguage.textContent;
            languageSelect.setAttribute("value_url", url);
        }
    }

    document.querySelectorAll('.circle').forEach(circle => {
        const paragraphs = circle.querySelectorAll('a');
        paragraphs.forEach(paragraph => {
            paragraph.addEventListener('click', function (event) {
                const url = paragraph.getAttribute('value_url');
                const language = paragraph.textContent;
                languageSelect.textContent = language;
                languageSelect.setAttribute('value_url', url);
                window.location.href = url;
            });
        });
    });
});


document.getElementById('registrationButton').addEventListener('click', function() {
    window.location.href = `https://appointer-ui-${accountId}.s3.eu-central-1.amazonaws.com/html/login.html`;
});

document.getElementById('registrationButtonBottom').addEventListener('click', function() {
    window.location.href = `https://appointer-ui-${accountId}.s3.eu-central-1.amazonaws.com/html/login.html`;
});