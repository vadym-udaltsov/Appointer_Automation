var selectLanguage = document.getElementsByClassName('change-lang')[0];
var allLang = ['en', 'ua', 'pl', 'ru'];

var selectedLanguage = localStorage.getItem('selectedLanguage') || 'en';
selectLanguage.value = selectedLanguage;

function changeUrlLanguage() {
    var lang = selectLanguage.value;
    localStorage.setItem('selectedLanguage', lang);
    changeLanguage();
}

function changeLanguage() {
    var selectedLanguage = localStorage.getItem('selectedLanguage') || 'en';

    for (var key in langArr) {
        var elem = document.querySelectorAll('.lng-' + key);
        if (elem) {
            for (var element of elem) {
                element.innerHTML = langArr[key][selectedLanguage];
            }
        }
    }
}

async function waitOnButtonsLoad() {
    while (window.dataLoaded !== true) {
        await new Promise(resolve => setTimeout(resolve, 200));
    }
}
waitOnButtonsLoad();

selectLanguage.addEventListener('change', changeUrlLanguage);
