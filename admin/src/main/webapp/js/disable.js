const create_fields = [
  { inputId: 'service_Create-servNameInput', errorId: 'nameError'},
  { inputId: 'service_Create-servDurationInput', errorId: 'durationError' },
  { inputId: 'service_Create-servPriceInput', errorId: 'priceError' }
];

const createBtn = document.getElementById('service_CreateBtn');
const create_cancelBtn = document.getElementById('service_Create-CancelBtn');
const errorMessages = document.getElementsByClassName('error-message');

const update_fields = [
  { inputId: 'update-service_newNameInput', errorId: 'update_nameError'},
  { inputId: 'update-service_newDurationInput', errorId: 'update_durationError' },
  { inputId: 'update-service_newPriceInput', errorId: 'update_priceError' }
];

const updateBtn = document.getElementById('update-service_UpdateBtn');
const update_cancelBtn = document.getElementById('update-service_CancelBtn');
const update_errorMessages = document.getElementsByClassName('update_error-message');

var names = [];

$('#service_createOpenBtn').click(function() {
    if (create_fields.some(field => document.getElementById(field.inputId).value.trim() === '')) {
        createBtn.disabled = true;
    }
    names = [];
    JSON.parse($('option:checked','#department_NameSelect').val())[0].s.forEach(function(option) {
        names.push(option.name)
    });
});

var tableContainer = document.getElementById('servicesTable');
tableContainer.addEventListener('click', (event) => {
    if (event.target.matches('.sub-button.service_updateOpenBtn')) {
        names = [];
        JSON.parse($('option:checked','#department_NameSelect').val())[0].s.forEach(function(option) {
            names.push(option.name)
        });
    }
});

function validateField(input, error) {
  if (input.value.trim() === '') {
    error.style.display = 'block';
    return false;
  } else {
    error.style.display = 'none';
    return true;
  }
}

function checkUserExists(input, error) {
  var value = input.value;

  for (var i = 0; i < names.length; i++) {
    if (names[i] === value) {
      error.style.display = 'block';
      return true;
    }
  }

  error.style.display = 'none';
  return false;
}

function setError(error, message) {
  error.textContent = message;
  error.style.display = 'block';
}

function hideError(error) {
  error.style.display = 'none';
}

function validateForm(fields, btn) {
  let valid = true;

  for (const field of fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (input.dataset.interacted === 'true') {
        if (input.value.trim() === '') {
          setError(error, 'Field is required');
          valid = false;
        } else if (checkUserExists(input, error)) {
          setError(error, 'User already exists');
          valid = false;
        } else {
          hideError(error);
        }
    } else {
      hideError(error);
      valid = false;
    }
  }

  btn.disabled = !valid;
}

function resetForm(fields, errorMessages) {
  for (const field of fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
  }

  for (const error of errorMessages) {
    error.style.display = 'none';
  }
}

/* Action for Cancel button */
create_cancelBtn.addEventListener('click', function () {
  resetForm(create_fields, errorMessages);
});

update_cancelBtn.addEventListener('click', function () {
  resetForm(update_fields, update_errorMessages);
});

/* Show Error */
for (const field of create_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    input.dataset.interacted = 'true';
    validateField(input, error);
    checkUserExists(input, error);
    validateForm(create_fields, createBtn);
  });
}

for (const field of update_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    input.dataset.interacted = 'true';
    validateField(input, error);
    checkUserExists(input, error);
    validateForm(update_fields, updateBtn);
  });
}

resetForm(create_fields, errorMessages);
resetForm(update_fields, update_errorMessages);