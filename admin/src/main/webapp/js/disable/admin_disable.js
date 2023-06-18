var admin_create_fields = [
  { inputId: 'admin_CreatePhoneInput', errorId: 'admin_phoneError' },
];

var admin_createBtn = document.getElementById('admin_CreateBtn');
var admin_create_cancelBtn = document.getElementById('admin_Create-CancelBtn');
var admin_openCreateModal = document.getElementById('admin_createOpenBtn');

var admin_createNames = [];

admin_openCreateModal.addEventListener('click', function () {
  admin_createNames = [];
  var selectedOption = document.querySelector('#department_NameSelect option:checked');
  JSON.parse(selectedOption.value).adm.forEach(function (option) {
    admin_createNames.push(option);
  });
  initializeAdminCreateForm();
  admin_createBtn.disabled = true;
});

function adminHideError(error) {
  error.style.display = 'none';
}

function adminSetError(error, message) {
  error.textContent = message;
  error.style.display = 'block';
}

function validateAdminCreateField(input, error) {
  if (input.value.trim() === '') {
    adminSetError(error, 'Field is required');
    return false;
  } else {
    adminHideError(error);
    return true;
  }
}

function checkCreateAdminExists(input, error) {
  var value = input.value.trim();
  for (var i = 0; i < admin_createNames.length; i++) {
    if (admin_createNames[i] === value) {
      adminSetError(error, 'Phone Number already exists');
      return true;
    }
  }
    if (value.length < 11) {
        adminSetError(error, 'Number is not valid. Minimum length must be 8 number');
        return true;
    }
  adminHideError(error);
  return false;
}

function validateAdminCreateForm() {
  let valid = true;

  for (const field of admin_create_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateAdminCreateField(input, error)) {
      valid = false;
    }

    if (input.value.trim() !== '') {
      if (checkCreateAdminExists(input, error)) {
        valid = false;
      }
    }
  }

  admin_createBtn.disabled = !valid;
}

function resetAdminCreateForm() {
  for (const field of admin_create_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    adminHideError(error);
  }
}

function initializeAdminCreateForm() {
  resetAdminCreateForm();
  admin_createBtn.disabled = true;
}

function handleAdminCreateInputChange(input, error) {
  validateAdminCreateField(input, error);
  validateAdminCreateForm();
}

/* Create Form */
for (const field of admin_create_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    handleAdminCreateInputChange(input, error);
  });
}

admin_create_cancelBtn.addEventListener('click', function () {
  initializeAdminCreateForm();
});

initializeAdminCreateForm();