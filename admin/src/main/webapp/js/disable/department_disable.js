var createDepartmentBtn = document.getElementById('create_depBtn');
var departmentCancelBtn = document.getElementById('close_depModalBtn');
var openCreateDepartmentModal = document.getElementById('department_CreatePopup');

var addTokenInput = document.getElementById('add_depTokenInput');
var addTokenNameError = document.getElementById('token_addTokenError');

const create_depNameInput = document.getElementById("depName_create");

function validateInputs() {
    if (create_depNameInput.value === '' || addTokenInput.value === '') {
        createDepartmentBtn.disabled = true;
    } else {
        createDepartmentBtn.disabled = false;
    }
}

function showTokenError() {
    if (addTokenInput.value === '') {
        addTokenNameError.style.display = 'block';
    } else {
        addTokenNameError.style.display = 'none';
    }
}

openCreateDepartmentModal.addEventListener("click", validateInputs);

create_depNameInput.addEventListener("input", validateInputs);

addTokenInput.addEventListener("input", validateInputs);
addTokenInput.addEventListener("input", showTokenError);
addTokenInput.addEventListener("input", showTokenError);

function updateCharCount() {
  const depNameInput = document.getElementById("depName_create");
  const charCount = depNameInput.value.length;
  const charCounter = document.getElementById("charCounter");

  charCounter.textContent = `${charCount}/30`;
}

addTokenInput.addEventListener("input", function() {
    var inputValue = addTokenInput.value.replace(/\s/g, '');
    addTokenInput.value = inputValue;
});

/* Update Department */
document.getElementById('department_UpdatePopup').addEventListener('click', function () {
    var updateDepBtn = document.getElementById('update_DepBtn');
    updateDepBtn.disabled = true;
});

function validateInput(input) {
  input.value = input.value.replace(/\D/g, '');
}

function validateUpdateDepInput(input) {
  var value = parseInt(input.value, 10);

  if (value < 1) {
    input.value = 1;
  } else if (value > 24) {
    input.value = 24;
  }

  var startWork = parseInt(document.getElementById('startWork').value, 10);
  var finishWork = parseInt(document.getElementById('finishWork').value, 10);
  var updateDepBtn = document.getElementById('update_DepBtn');

  if (isNaN(startWork) || isNaN(finishWork) || startWork > finishWork) {
    updateDepBtn.disabled = true;
  } else if(startWork == '' || finishWork == '') {
    updateDepBtn.disabled = true;
  } else {
    updateDepBtn.disabled = false;
  }
}


/* Verify that new value not equals to prev.values*/
var prevUpdateTimeZoneValue = "";
var prevUpdateStartHour = "";
var prevUpdateFinishHour = "";
var prevUpdateWorkDays = [];
var selectedDays = [];

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

async function verifyDepartmentFieldsValue() {
    await waitUntil(() => window.dataLoaded === true);

    const updateDepartment = document.getElementById('department_UpdatePopup');
    updateDepartment.addEventListener('click', async () => {
        await waitUntil(() => {
            const popup = document.getElementById('department_UpdateModal');
            const popupStyle = window.getComputedStyle(popup);
            return popupStyle.display === 'block';
        });
        prevUpdateTimeZoneValue = document.getElementById('update_timeZoneSelect').value;
        prevUpdateStartHour = document.getElementById('startWork').value;
        prevUpdateFinishHour = document.getElementById('finishWork').value;

        prevUpdateWorkDays = Array.from(document.querySelectorAll('.custom-checkbox:checked')).map(checkbox => checkbox.value);

        const department_update_fields = [
            { inputId: 'update_timeZoneSelect', prevValue: prevUpdateTimeZoneValue },
            { inputId: 'startWork', prevValue: prevUpdateStartHour },
            { inputId: 'finishWork', prevValue: prevUpdateFinishHour },
        ];

        department_update_fields.forEach(field => {
            const input = document.getElementById(field.inputId);
            input.addEventListener('input', function () {
                updateButtonStatus(field.inputId, field.prevValue);
            });
        });

        const checkboxes = document.querySelectorAll('.custom-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function () {
                updateButtonStatus('checkboxes', null);
            });
        });

        updateButtonStatus();
    });
}

verifyDepartmentFieldsValue();

function updateButtonStatus(changedInputId, prevValue) {
    const updateBtn = document.querySelector('#update_DepBtn');
    const timeZoneValue = document.getElementById('update_timeZoneSelect').value;
    const startHour = document.getElementById('startWork').value;
    const finishHour = document.getElementById('finishWork').value;
    const selectedDays = Array.from(document.querySelectorAll('.custom-checkbox:checked')).map(checkbox => checkbox.value);

    const timeChanged =
        timeZoneValue !== prevUpdateTimeZoneValue ||
        startHour !== prevUpdateStartHour ||
        finishHour !== prevUpdateFinishHour ||
        JSON.stringify(selectedDays) !== JSON.stringify(prevUpdateWorkDays);

    if (timeChanged) {
        updateBtn.disabled = false;
    } else {
        updateBtn.disabled = true;
    }

    if (timeChanged && (changedInputId === 'startWork' || changedInputId === 'finishWork') && changedInputId !== 'checkboxes') {
        validateAndUpdate(changedInputId, changedInputId === 'startWork');
    }
}

function validateAndUpdate(inputId, isStart) {
    const inputElement = document.getElementById(inputId);
    if (validateUpdateDepInput(inputElement)) {
        if (isStart) {
            prevUpdateStartHour = inputElement.value;
        } else {
            prevUpdateFinishHour = inputElement.value;
        }
        updateButtonStatus(inputId, inputElement.value);
    }
}

