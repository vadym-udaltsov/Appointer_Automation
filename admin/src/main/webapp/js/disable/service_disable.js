var create_fields = [
  { inputId: 'service_Create-servNameInput', errorId: 'nameError' },
  { inputId: 'service_Create-servDurationInput', errorId: 'durationError' },
  //{ inputId: 'service_Create-servPriceInput', errorId: 'priceError' }
];

var createBtn = document.getElementById('service_CreateBtn');
var create_cancelBtn = document.getElementById('service_Create-CancelBtn');
var openCreateModal = document.getElementById('service_createOpenBtn');

var createNames = [];

openCreateModal.addEventListener('click', function () {
  createNames = [];
  var selectedOption = document.querySelector('#department_NameSelect option:checked');
  JSON.parse(selectedOption.value).s.forEach(function (option) {
    createNames.push(option.name);
  });
  initializeCreateForm();
  createBtn.disabled = true;
});

function hideError(error) {
  error.style.display = 'none';
}

function setError(error) {
  error.style.display = 'block';
}

function validateCreateField(input, error) {
  if (input.value.trim() === '') {
    setError(error);
    return false;
  } else {
    hideError(error);
    return true;
  }
}

function checkCreateServiceExists(input, error) {
  var value = input.value.trim();
  for (var i = 0; i < createNames.length; i++) {
    if (createNames[i] === value) {
      setError(error);
      return true;
    }
  }
  hideError(error);
  return false;
}

function validateCreateForm() {
  let valid = true;

  for (const field of create_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateCreateField(input, error)) {
      valid = false;
    }

    if (input.value.trim() !== '' && field.inputId === "service_Create-servNameInput") {
      if (checkCreateServiceExists(input, document.getElementById('duplicateServiceError'))) {
        valid = false;
      }
    }
  }

  createBtn.disabled = !valid;
}

function resetCreateForm() {
  for (const field of create_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    hideError(error);
    hideError(document.getElementById('duplicateServiceError'));
  }
}

function initializeCreateForm() {
  resetCreateForm();
  createBtn.disabled = true;
}

function handleCreateInputChange(input, error) {
  validateCreateField(input, error);
  validateCreateForm();
}

/* Create Form */
for (const field of create_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    handleCreateInputChange(input, error);
  });
}

create_cancelBtn.addEventListener('click', function () {
  initializeCreateForm();
});

initializeCreateForm();

/* Update Service */

var update_fields = [
  { inputId: 'update-service_newNameInput', errorId: 'update_nameError' },
  { inputId: 'update-service_newDurationInput', errorId: 'update_durationError' },
  //{ inputId: 'update-service_newPriceInput', errorId: 'update_priceError' }
];

var updateBtn = document.querySelector('#update-service_UpdateBtn');
var update_cancelBtn = document.getElementById('update-service_CancelBtn');

var updateNames = [];

var tableContainer = document.getElementById('servicesTable');
    tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.service_updateOpenBtn')) {
            updateNames = [];
            var selectedOption = JSON.parse(document.querySelector('#department_NameSelect option:checked').value).s;
            selectedOption.forEach(function (option) {
                 updateNames.push(option.name);
             });
            initializeUpdateForm();
            updateBtn.disabled = true;
        }
    });

function validateUpdateField(input, error) {
  if (input.value.trim() === '') {
    setError(error);
    return false;
  } else {
    hideError(error);
    return true;
  }
}

function checkUpdateServiceExists(input, error) {
  var value = input.value.trim();
    for (var i = 0; i < updateNames.length; i++) {
        if (updateNames[i] === value) {
        setError(error);
        return true;
        }
    }
  hideError(error);
  return false;
}

function validateUpdateForm() {
  let valid = true;

  for (const field of update_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateUpdateField(input, error)) {
      valid = false;
    }

    if (field.inputId === "update-service_newNameInput" && input.value.trim() !== $('#update-service_servNameInput').val()) {
      if (checkUpdateServiceExists(input, document.getElementById('duplicateServiceUpdateError'))) {
        valid = false;
      }
    }
  }

  updateBtn.disabled = !valid;
}

function resetUpdateForm() {
  for (const field of update_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    hideError(error);
    hideError(document.getElementById('duplicateServiceUpdateError'));
  }
}

function initializeUpdateForm() {
  resetUpdateForm();
  updateBtn.disabled = true;
}

function handleUpdateInputChange(input, error) {
  validateUpdateField(input, error);
  validateUpdateForm();
}

/* Update Form */
for (const field of update_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    handleUpdateInputChange(input, error);
  });
}

update_cancelBtn.addEventListener('click', function () {
  initializeUpdateForm();
});

initializeUpdateForm();

/* Verify that new value not equals to prev.values*/
var prevUpdateNameValue = "";
var prevUpdateDurationValue = "";
//var prevUpdatePriceValue = "";

function waitUntil(conditionFn, interval = 100) {
    return new Promise((resolve) => {
        const intervalId = setInterval(() => {
            if (conditionFn()) {
                clearInterval(intervalId);
                resolve();
            }
        }, interval);
    });
}

async function verifyServiceFieldsValue() {
    await waitUntil(() => window.dataLoaded === true);

    const updateButtons = document.querySelectorAll('.service_updateOpenBtn');
    updateButtons.forEach(button => {
        button.addEventListener('click', async () => {
            await waitUntil(() => {
                const popup = document.getElementById('service_UpdateModal');
                const popupStyle = window.getComputedStyle(popup);
                return popupStyle.display === 'block';
            });
              prevUpdateNameValue = document.getElementById('update-service_newNameInput').value;
              prevUpdateDurationValue = document.getElementById('update-service_newDurationInput').value;
              //prevUpdatePriceValue = document.getElementById('update-service_newPriceInput').value;

            const update_fields = [
                { inputId: 'update-service_newNameInput', prevValue: prevUpdateNameValue },
                { inputId: 'update-service_newDurationInput', prevValue: prevUpdateDurationValue },
                //{ inputId: 'update-service_newPriceInput', prevValue: prevUpdatePriceValue }
            ];

            update_fields.forEach(field => {
                const input = document.getElementById(field.inputId);
                input.addEventListener('input', function () {
                    verifyServiceFields(field.inputId, field.prevValue);
                });
            });
        });
    });
}

verifyServiceFieldsValue();

function verifyServiceFields(inputId, prevValue) {
    const inputValue = document.getElementById(inputId).value;
    const updateBtn = document.querySelector('#update-service_UpdateBtn');
    const errorMessage = document.querySelector(`.${inputId}-error-message`);


    if (inputValue === prevValue) {
        updateBtn.disabled = true;
        errorMessage.textContent = langArr.prevValueError[localStorage.getItem('selectedLanguage')];
        errorMessage.style.display = 'block';
    } else if(inputValue === '') {
        updateBtn.disabled = true;
        errorMessage.style.display = 'none';
    } else {
        updateBtn.disabled = false;
        errorMessage.style.display = 'none';
    }
}

const service_errorMessages = document.querySelectorAll('.error-message');
document.getElementById('update-service_CancelBtn').addEventListener('click', function() {
    service_errorMessages.forEach(function(errorMessage) {
      errorMessage.style.display = 'none';
    });
  });