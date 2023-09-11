const adminCreateFields = [
  { inputId: 'admin_CreatePhoneInput', errorId: 'admin_phoneError' },
];

const adminCreateBtn = document.getElementById('admin_CreateBtn');
const adminCreateCancelBtn = document.getElementById('admin_Create-CancelBtn');
const adminOpenCreateModal = document.getElementById('admin_createOpenBtn');

let adminCreateNames = [];

adminOpenCreateModal.addEventListener('click', () => {
  adminCreateNames = [];
  const selectedOption = document.querySelector('#department_NameSelect option:checked');
  adminCreateNames = JSON.parse(selectedOption.value).adm;
  adminCreateBtn.disabled = true;
});

function hideError(error) {
  error.style.display = 'none';
}

function setError(error) {
  error.style.display = 'block';
}

function validateField(input, error) {
  const trimmedValue = input.value.trim();

  if (trimmedValue === '') {
    setError(error);
    return false;
  } else {
    hideError(error);
    return true;
  }
}

function checkAdminExists(input) {
  const value = input.value.trim();

  if (adminCreateNames.includes(value)) {
    setError(document.getElementById('duplicateAdminError'));
    return true;
  } else {
    hideError(document.getElementById('duplicateAdminError'));
    return false;
  }
}

function validateForm() {
  let valid = true;

  for (const field of adminCreateFields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateField(input, error)) {
      valid = false;
    }

    if (input.value.trim() !== '') {
      if (checkAdminExists(input)) {
        valid = false;
      }
    }

    if (input.value.length < 11) {
        setError(document.getElementById('admin_validPhoneError'));
        adminCreateBtn.disabled = true;
        return;
    } else {
        hideError(document.getElementById('admin_validPhoneError'));
    }
  }

  adminCreateBtn.disabled = !valid;
}

function resetForm() {
  for (const field of adminCreateFields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    hideError(error);
    hideError(document.getElementById('duplicateAdminError'));
    hideError(document.getElementById('admin_validPhoneError'));
  }
}

function initializeForm() {
  resetForm();
  adminCreateBtn.disabled = true;
}

function handleInputChange(input, error) {
  validateField(input, error);
  validateForm();
}

/* Create Form */
for (const field of adminCreateFields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', () => {
    handleInputChange(input, error);
  });
}

adminCreateCancelBtn.addEventListener('click', () => {
  initializeForm();
});

initializeForm();

function validatePhoneNumber(input) {
  input.value = input.value.replace(/[^0-9]/g, '');
  const trimmedValue = input.value.trim();

  if (trimmedValue.length === 0 || trimmedValue.charAt(0) !== '+') {
    input.value = '+' + trimmedValue;
  }
}