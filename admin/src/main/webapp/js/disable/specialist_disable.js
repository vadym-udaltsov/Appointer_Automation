var specialist_create_fields = [
  { inputId: 'specialist_CreateNameInput', errorId: 'specialist_nameError' },
  { inputId: 'specialist_CreatePhoneInput', errorId: 'specialist_phoneError' }
];

var specialist_createBtn = document.getElementById('specialist_CreateBtn');
var specialist_create_cancelBtn = document.getElementById('specialist_Create-CancelBtn');
var specialist_openCreateModal = document.getElementById('specialist_createOpenBtn');

var specialist_createNames = [];

specialist_openCreateModal.addEventListener('click', function () {
  specialist_createNames = [];
  var selectedOption = document.querySelector('#department_NameSelect option:checked');
  JSON.parse(selectedOption.value).as.forEach(function (option) {
    specialist_createNames.push(option.name);
  });
  initializeSpecialistCreateForm();
  specialist_createBtn.disabled = true;
});

function specialistHideError(error) {
  error.style.display = 'none';
}

function specialistSetError(error, message) {
  error.textContent = message;
  error.style.display = 'block';
}

function validateSpecialistCreateField(input, error) {
  if (input.value.trim() === '') {
    specialistSetError(error, 'Field is required');
    return false;
  } else {
    specialistHideError(error);
    return true;
  }
}

function checkCreateSpecialistExists(input, error) {
  var value = input.value.trim();
  for (var i = 0; i < specialist_createNames.length; i++) {
    if (specialist_createNames[i] === value) {
      specialistSetError(error, 'Specialist already exists');
      return true;
    }
  }
  specialistHideError(error);
  return false;
}

function validateSpecialistCreateForm() {
  let valid = true;

  for (const field of specialist_create_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateSpecialistCreateField(input, error)) {
      valid = false;
    }

    if (input.value.trim() !== '') {
      if (checkCreateSpecialistExists(input, error)) {
        valid = false;
      }
    }
    if(input.id == "specialist_CreatePhoneInput") {
        if (input.value.length < 11) {
            specialistSetError(error, 'Number is not valid. Minimum length must be 8 number');
            return true;
        }
    }

  }

  specialist_createBtn.disabled = !valid;
}

function resetSpecialistCreateForm() {
  for (const field of specialist_create_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    specialistHideError(error);
  }
}

function initializeSpecialistCreateForm() {
  resetSpecialistCreateForm();
  specialist_createBtn.disabled = true;
}

function handleSpecialistCreateInputChange(input, error) {
  validateSpecialistCreateField(input, error);
  validateSpecialistCreateForm();
}

/* Create Form */
for (const field of specialist_create_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    handleSpecialistCreateInputChange(input, error);
  });
}

specialist_create_cancelBtn.addEventListener('click', function () {
  initializeSpecialistCreateForm();
});

initializeSpecialistCreateForm();

/* Update Specialist */

var specialist_update_fields = [
  { inputId: 'update-specialist_newNameInput', errorId: 'specialist_update_nameError' },
  { inputId: 'update-specialist_newPhoneInput', errorId: 'specialist_update_phoneError' },
];

var specialist_updateBtn = document.querySelector('#update-specialist_UpdateBtn');
var specialist_update_cancelBtn = document.getElementById('update-specialist_CancelBtn');

var specialist_updateNames = [];

var specialist_tableContainer = document.getElementById('specialistTable');
    specialist_tableContainer.addEventListener('click', (event) => {
        if (event.target.matches('.sub-button.specialist_updateOpenBtn')) {
            specialist_updateNames = [];
            var selectedOption = JSON.parse(document.querySelector('#department_NameSelect option:checked').value).as;
            selectedOption.forEach(function (option) {
                 specialist_updateNames.push(option.name);
             });
            initializeSpecialistUpdateForm();
            specialist_updateBtn.disabled = true;
        }
    });

function validateSpecialistUpdateField(input, error) {
  if (input.value.trim() === '') {
    specialistSetError(error, 'Field is required');
    return false;
  } else {
    specialistHideError(error);
    return true;
  }
}

function checkUpdateSpecialistExists(input, error) {
  var value = input.value.trim();
    for (var i = 0; i < specialist_updateNames.length; i++) {
        if (specialist_updateNames[i] === value) {
        specialistSetError(error, 'Specialist already exists');
        return true;
        }
    }
    if(value === '') {
        specialistSetError(error, 'Field is required');
        return true;
    }

  specialistHideError(error);
  return false;
}

function validateSpecialistUpdateForm() {
  let valid = true;

  for (const field of specialist_update_fields) {
    const input = document.getElementById(field.inputId);
    const error = document.getElementById(field.errorId);

    if (!validateSpecialistUpdateField(input, error)) {
      valid = false;
    }

    if (field.inputId === "update-specialist_newNameInput" && input.value.trim() !== $('#update-specialist_servNameInput').val()) {
      if (checkUpdateSpecialistExists(input, error)) {
        valid = false;
      }
    }

    if(input.id == "update-specialist_newPhoneInput") {
        if (input.value.length < 11) {
            specialistSetError(error, 'Number is not valid.Minimum length must be 8 number');
            return true;
        }
    }
  }

  specialist_updateBtn.disabled = !valid;
}

function resetSpecialistUpdateForm() {
  for (const field of specialist_update_fields) {
    const input = document.getElementById(field.inputId);
    input.value = '';
    const error = document.getElementById(field.errorId);
    specialistHideError(error);
  }
}

function initializeSpecialistUpdateForm() {
  resetSpecialistUpdateForm();
  specialist_updateBtn.disabled = true;
}

function handleSpecialistUpdateInputChange(input, error) {
  validateSpecialistUpdateField(input, error);
  validateSpecialistUpdateForm();
}

/* Update Form */
for (const field of specialist_update_fields) {
  const input = document.getElementById(field.inputId);
  const error = document.getElementById(field.errorId);

  input.addEventListener('input', function () {
    handleSpecialistUpdateInputChange(input, error);
  });
}

specialist_update_cancelBtn.addEventListener('click', function () {
  initializeSpecialistUpdateForm();
});

initializeSpecialistUpdateForm();