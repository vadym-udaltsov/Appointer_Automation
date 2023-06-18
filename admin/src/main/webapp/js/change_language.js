var selectLanguage = document.getElementsByClassName('change-lang')[0];
var allLang = ['ru', 'en'];

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
        var elem = document.querySelector('.lng-' + key);
        if(elem) {
            elem.innerHTML = langArr[key][hash];
        }
    }
}

changeLanguage();