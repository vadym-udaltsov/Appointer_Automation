var enterPasswordInput = document.getElementById('enterPassword_Input');
var confirmPasswordInput = document.getElementById('confirmPassword_Input');
var togglePasswordBtn = document.getElementById('togglePasswordBtn');
var confirmTogglePasswordBtn = document.getElementById('confirmTogglePasswordBtn');

var codeInput = document.getElementById('enterCode_Input');

var cancelBtn = document.getElementById('resetPassword_Cancel');
var resetPassword = document.getElementById('resetPassword_btn');

enterPasswordInput.type = 'password';
confirmPasswordInput.type = 'password';
resetPassword.disabled = true;

resetPassword.addEventListener("click", function() {
    var resetModal = new Object();
    resetModal.email = localStorage.getItem('customer');
    resetModal.password = enterPasswordInput.value;
    resetModal.code = codeInput.value;
    executePost(JSON.stringify(resetModal), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/password-confirm');
    $('#resetModal').modal('hide');
});

function togglePasswordVisibility(inputField, toggleButton) {
    const type = inputField.type === 'password' ? 'text' : 'password';
    inputField.type = type;
    if (type === 'password') {
        toggleButton.classList.remove('fa-eye-slash');
        toggleButton.classList.add('fa-eye');
    } else {
        toggleButton.classList.remove('fa-eye');
        toggleButton.classList.add('fa-eye-slash');
    }
}

function validatePassword() {
    const password = enterPasswordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    const validationMessages = document.getElementById('validationMessages');

    const hasLength = password.length >= 8;
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasSpecialChar = /[!@#$%^&*()_+{}\[\]:;<>,.?~\\-]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasLeadingTrailingSpace = /^\s|\s$/.test(password);

    const messages = [
        { isValid: hasLength, message: '✓ Password must contain at least 8 characters', color: 'green' },
        { isValid: hasUppercase, message: '✓ Password must contain an upper case letter', color: 'green' },
        { isValid: hasLowercase, message: '✓ Password must contain a lower case letter', color: 'green' },
        { isValid: hasNumber, message: '✓ Password must contain a number', color: 'green' },
        { isValid: hasSpecialChar, message: '✓ Password must contain a special character', color: 'green' },
        { isValid: !hasLeadingTrailingSpace, message: '✓ Password must not contain leading or trailing spaces', color: 'green' }
    ];

    let isValid = true;
    validationMessages.innerHTML = '';

    for (const message of messages) {
        document.getElementById('togglePasswordBtn').classList.add('error');
        document.getElementById('togglePasswordHiddenBtn').classList.add('error');
        document.getElementById('confirmTogglePasswordBtn').classList.add('confirmError');
        document.getElementById('confirmTogglePasswordHiddenBtn').classList.add('confirmError');
        if (!message.isValid) {
            isValid = false;
            validationMessages.innerHTML += `<p style="color: red">${message.message}</p>`;
        } else {
            validationMessages.innerHTML += `<p style="color: green">${message.message}</p>`;
        }
    }

    if (isValid && password === confirmPassword && codeInput.value !== '') {
        resetPassword.disabled = false;
    } else {
        resetPassword.disabled = true;
    }
}

cancelBtn.addEventListener("click", function() {
    document.getElementById('validationMessages').style.display = 'none';
    document.getElementById('togglePasswordBtn').classList.remove('error');
    document.getElementById('togglePasswordHiddenBtn').classList.remove('error');
    document.getElementById('confirmTogglePasswordBtn').classList.remove('confirmError');
    document.getElementById('confirmTogglePasswordHiddenBtn').classList.remove('confirmError');
    enterPasswordInput.value = '';
    confirmPasswordInput.value = '';
    codeInput.value = '';
    resetPassword.disabled = true;
    $('#resetModal').modal('hide');
});

togglePasswordBtn.addEventListener('click', () => {
    togglePasswordVisibility(enterPasswordInput, togglePasswordBtn);
});

confirmTogglePasswordBtn.addEventListener('click', () => {
    togglePasswordVisibility(confirmPasswordInput, confirmTogglePasswordBtn);
});

enterPasswordInput.addEventListener('input', validatePassword);
confirmPasswordInput.addEventListener('input', validatePassword);
codeInput.addEventListener('input', validatePassword);