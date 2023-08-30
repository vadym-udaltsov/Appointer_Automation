var openRefreshTokenModal = document.getElementById('department_UpdateTokenPopup');
var refreshTokenPopup = document.getElementById('department_UpdateTokenPopup');
var updateDepPopup = document.getElementById('department_UpdateModal');

var updateTokenInput = document.getElementById('update_TokenInput');
var tokenNameError = document.getElementById('token_nameError');
var cancelRefreshBtn = document.getElementById('update_Token_CancelBtn');
var updateTokenBtn = document.getElementById('token_updateBtn');


$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    $("#token_updateBtn").click(function() {
            if (updateTokenBtn.classList.contains('disabled')) {
                return false;
            }
            var token = new Object();
            token.departmentId = JSON.parse($("#department_NameSelect").val()).id;
            token.email = localStorage.getItem('customer');
            token.departmentName = $("#department_NameSelect option:selected").text().trim();
            token.botToken = updateTokenInput.value;
            executePut(JSON.stringify(token), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
            refreshTokenPopup.style.display = 'none';
            location.reload();
            return false;
    });
});


function closeUpdateDepPopup() {
   updateDepPopup.style.display = 'none';
}

function openUpdatePopup() {
   updateDepPopup.style.display = 'block';
}

openRefreshTokenModal.addEventListener('click', function () {
    if(updateTokenInput.value === '') {
       updateTokenBtn.disabled = true;
    }
});

function validateInput() {
    if (updateTokenInput.value === '') {
        tokenNameError.style.display = 'block';
        updateTokenBtn.disabled = true;
    } else {
        tokenNameError.style.display = 'none';
        updateTokenBtn.disabled = false;
    }
}

function resetTokenData() {
   updateTokenInput.value = '';
   updateTokenBtn.disabled = false;
   tokenNameError.style.display = 'none';
}

updateTokenInput.addEventListener("input", validateInput);

refreshTokenPopup.addEventListener("click", closeUpdateDepPopup);
cancelRefreshBtn.addEventListener("click", openUpdatePopup);
cancelRefreshBtn.addEventListener("click", resetTokenData);
updateTokenBtn.addEventListener("click", openUpdatePopup);
updateTokenBtn.addEventListener("click", resetTokenData);


/*Styles for Refresh Token Btn*/
var updateDepartmentPopup = document.getElementById('department_UpdatePopup');
updateDepartmentPopup.addEventListener('click', function() {
    var selectedLanguage = localStorage.getItem('selectedLanguage');

    var languageMargins = {
        'en': '140px',
        'ua': '100px',
        'pl': '80px',
        'ru': '100px',
        'cnr': '100px'
    };

   var openRefreshTokenModal = document.getElementById('department_UpdateTokenPopup');

   if (selectedLanguage && languageMargins[selectedLanguage]) {
       openRefreshTokenModal.style.setProperty('margin-right', languageMargins[selectedLanguage], 'important');
   } else {
       openRefreshTokenModal.style.setProperty('margin-right', '140px', 'important');
   }
});