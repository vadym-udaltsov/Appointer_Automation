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
  names = [];
  JSON.parse($('option:checked','#department_NameSelect').val()).s.forEach(function(option) {
    names.push(option.name)
  });
  resetCreateForm();
  createBtn.disabled = true;
});

var tableContainer = document.getElementById('servicesTable');
tableContainer.addEventListener('click', (event) => {
  if (event.target.matches('.sub-button.service_updateOpenBtn')) {
    names = [];
    JSON.parse($('option:checked','#department_NameSelect').val()).s.forEach(function(option) {
      names.push(option.name)
    });
    resetUpdateForm();
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

function checkServiceExists(input, error) {
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

function validateCreateForm() {
  let valid = true;
  let errorExists = false;

  for (const field of create_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (input.dataset.interacted === 'true') {
      if (input.value.trim() === '') {
        setError(error, 'Field is required');
        valid = false;
        errorExists = true;
      } else {
        hideError(error);
      }
    } else {
      hideError(error);
    }
  }

  if (!valid || errorExists) {
    createBtn.disabled = true;
  } else {
    createBtn.disabled = false;
  }
}

function validateUpdateForm() {
  let valid = true;
  let errorExists = false;

  for (const field of update_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (input.dataset.interacted === 'true') {
      if (input.value.trim() === '') {
        setError(error, 'Field is required');
        valid = false;
        errorExists = true;
      } else {
        hideError(error);
      }
    } else {
      hideError(error);
    }
  }

  if (!valid || errorExists) {
    updateBtn.disabled = true;
  } else {
    updateBtn.disabled = false;
  }
}

function resetCreateForm() {
  for (const field of create_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    input.dataset.interacted = 'false';
  }

  for (const error of errorMessages) {
    error.style.display = 'none';
  }
  createBtn.disabled = false;
}

function resetUpdateForm() {
  for (const field of update_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    input.dataset.interacted = 'false';
  }

  for (const error of update_errorMessages) {
    error.style.display = 'none';
  }
  updateBtn.disabled = false;
}

/* Show Error */
for (const field of create_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    input.dataset.interacted = 'true';
    validateField(input, error);
    checkServiceExists(input, error);
    validateCreateForm();
  });
}

for (const field of update_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    input.dataset.interacted = 'true';
    validateField(input, error);
    checkServiceExists(input, error);
    validateUpdateForm();
  });
}

create_cancelBtn.addEventListener('click', function() {
  resetCreateForm();
});

update_cancelBtn.addEventListener('click', function() {
  resetUpdateForm();
});

resetCreateForm();
resetUpdateForm();