var selectLanguage = document.getElementsByClassName('change-lang')[0];
var allLang = ['en', 'ua', 'pl', 'ru', 'cnr'];

var selectedLanguage = localStorage.getItem('selectedLanguage') || 'en';
selectLanguage.value = selectedLanguage;

function changeUrlLanguage() {
    var lang = selectLanguage.value;
    localStorage.setItem('selectedLanguage', lang);
    changeLanguage();
}

$(window).on('load', function() {
var elementsWithLangKey = document.querySelectorAll('[data-lang-key]');
        if (elementsWithLangKey) {
            elementsWithLangKey.forEach(function(element) {
                var langKey = element.getAttribute('data-lang-key');
                var cleanLangKey = langKey.replace('lng-', '');
                if (langArr[cleanLangKey] && langArr[cleanLangKey][selectedLanguage]) {
                    element.setAttribute('title', langArr[cleanLangKey][selectedLanguage]);
                    var adjacentImg = element.previousElementSibling;
                    if (adjacentImg && adjacentImg.tagName.toLowerCase() === 'img') {
                        adjacentImg.setAttribute('title', langArr[cleanLangKey][selectedLanguage]);
                    }
                }
            });
        }
});

function changeLanguage() {
    var selectedLanguage = localStorage.getItem('selectedLanguage') || 'en';

    var enterName = document.querySelectorAll('.enterName');
    enterName.forEach(function(element) {
        element.placeholder = langArr.namePlaceholder[selectedLanguage];
    });

    var enterNewName = document.querySelectorAll('.enterNewName');
    enterNewName.forEach(function(element) {
        element.placeholder = langArr.newNamePlaceholder[selectedLanguage];
    });

    var enterDuration = document.querySelectorAll('.enterDuration');
    enterDuration.forEach(function(element) {
        element.placeholder = langArr.durationPlaceholder[selectedLanguage];
    });

    var enterPrice = document.querySelectorAll('.enterPrice');
    enterPrice.forEach(function(element) {
        element.placeholder = langArr.pricePlaceholder[selectedLanguage];
    });

    var enterPhone = document.querySelectorAll('.enterPhone');
    enterPhone.forEach(function(element) {
        element.placeholder = langArr.phonePlaceholder[selectedLanguage];
    });

    var enterNewPhone = document.querySelectorAll('.enterNewPhone');
    enterNewPhone.forEach(function(element) {
        element.placeholder = langArr.newPhonePlaceholder[selectedLanguage];
    });

    var enterNewToken = document.querySelectorAll('.enterToken');
    enterNewToken.forEach(function(element) {
        element.placeholder = langArr.tokenPlaceholder[selectedLanguage];
    });

    for (var key in langArr) {
        var elem = document.querySelectorAll('.lng-' + key);
        if (elem) {
            for (var element of elem) {
                element.innerHTML = langArr[key][selectedLanguage];
            }
        }
        var elementsWithLangKey = document.querySelectorAll('[data-lang-key]');
        if (elementsWithLangKey) {
            elementsWithLangKey.forEach(function(element) {
                var langKey = element.getAttribute('data-lang-key');
                var cleanLangKey = langKey.replace('lng-', '');
                if (langArr[cleanLangKey] && langArr[cleanLangKey][selectedLanguage]) {
                    element.setAttribute('title', langArr[cleanLangKey][selectedLanguage]);
                }
            });
        }
    }
    //fetchCountriesAndCities();
}

async function waitOnButtonsLoad() {
    while (window.dataLoaded !== true) {
        await new Promise(resolve => setTimeout(resolve, 200));
    }
}
waitOnButtonsLoad();

selectLanguage.addEventListener('change', changeUrlLanguage);
