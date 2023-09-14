var forgotPassword_Label = document.getElementById('sendCode_label');
var email_Input = document.getElementById('emailInput_Code');

var close_SendCodeModal = document.getElementById('close_SendCodeModal');
var sendCode_Btn = document.getElementById('sendCodeForReset');

sendCode_Btn.addEventListener("click", function() {
    var userEmail = new Object();
    var email = email_Input.value
    userEmail.email = email;
    localStorage.setItem('customer', email);
    sendCode('https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/password-reset?email=' + email);
    email_Input.value = '';
    sendCode_Btn.disabled = true;
});

forgotPassword_Label.addEventListener("click", function() {
    $('#sendCode').modal('show');
    sendCode_Btn.disabled = true;
});

close_SendCodeModal.addEventListener("click", function() {
    $('#sendCode').modal('hide');
    sendCode_Btn.disabled = true;
    email_Input.value = '';
});


email_Input.addEventListener('input', function() {
    var emailValue = email_Input.value;
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (emailPattern.test(emailValue)) {
        sendCode_Btn.disabled = false;
    } else {
        sendCode_Btn.disabled = true;
    }
});


function sendCode(url) {
    $.ajax({
        type: 'get',
        url: url,
        contentType: "application/json",
        success: function (data) {
            $('#sendCode').modal('hide')
            $('#resetModal').modal('show');
            alert("Check your email and paste the code in the field")
        },
        error: function (data) {
            $('#sendCode').modal('hide');
            if (data.responseText == undefined || data.responseText == '') {
                showDataError("Unsuccessful operation");
            } else {
                showDataError(JSON.parse(data.responseText).body);
            }
        }
    });
}

