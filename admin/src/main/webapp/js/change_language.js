var selectLanguage = document.getElementsByClassName('change-lang')[0];
var allLang = ['en', 'ua', 'pl', 'ru'];

selectLanguage.addEventListener('change', changeUrlLanguage);

function changeUrlLanguage() {
    var lang = selectLanguage.value;
    location.href = window.location.pathname + '#' + lang;
    location.reload();
}

function changeLanguage() {
    var hash = window.location.hash;
    hash = hash.substr(1);
    if(!allLang.includes(hash)) {
         location.href = window.location.pathname + '#en';
         location.reload();
    }
    selectLanguage.value = hash;
    for (var key in langArr) {
        var elem = document.querySelectorAll('.lng-' + key);
        if(elem) {
            for(var element of elem) {
                element.innerHTML = langArr[key][hash];
            }
        }
    }
}

async function waitOnButtonsLoad() {
    while (window.dataLoaded !== true) {
        await new Promise(resolve => setTimeout(resolve, 200));
    }
    changeLanguage();
}
waitOnButtonsLoad();
