const fields = [
  { inputId: 'specialist_servName', errorId: 'nameError'},
  { inputId: 'specialist_servDuration', errorId: 'durationError' },
  { inputId: 'specialist_servPrice', errorId: 'priceError' }
];
const createBtn = document.getElementById('specialist_servCreateBtn');
const cancelBtn = document.getElementById('specialist_servCancelBtn');
const errorMessages = document.getElementsByClassName('error-message');

var customerData = JSON.parse(window.localStorage.getItem('customerData'));
var data = customerData.customerDepartments[0].s;

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

  for (var i = 0; i < data.length; i++) {
    if (data[i].name === value) {
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

function validateForm() {
  let valid = true;

  for (const field of fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (input.dataset.interacted === 'true') {
      if (field.inputId === 'specialist_servName') {
        if (input.value.trim() === '') {
          setError(error, 'Field is required');
          valid = false;
        } else if (checkUserExists(input, error)) {
          setError(error, 'User already exists');
          valid = false;
        } else {
          hideError(error);
        }
      } else if (!validateField(input, error)) {
        valid = false;
      }
    } else {
      hideError(error);
      valid = false;
    }
  }

  createBtn.disabled = !valid;
}

function resetForm() {
  createBtn.disabled = true;
  for (const error of errorMessages) {
    error.style.display = 'none';
  }
}

/* Action for Cancel button */
cancelBtn.addEventListener('click', function () {
  resetForm();
});

/* Show Error */
for (const field of fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    input.dataset.interacted = 'true';
    validateField(input, error);
    checkUserExists(input, error);
    validateForm();
  });
}

resetForm();

/*
var customerData = JSON.parse(window.localStorage.getItem('customerData'));
var data = customerData.customerDepartments[0].s;
*/
