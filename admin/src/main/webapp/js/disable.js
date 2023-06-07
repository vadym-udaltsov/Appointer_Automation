const depNameInput = document.getElementById('depName_create');
const typeSelect = document.getElementById('typeSelect_create');
const createButton = document.getElementById('create');
const cancelButton = document.getElementById('close_create');

function checkFields() {
    const depName = depNameInput.value.trim();
    const type = typeSelect.value.trim();


    if (depName !== '' && type !== '') {
            createButton.classList.remove('disabled');
    } else {
            createButton.classList.add('disabled');
    }
}

depNameInput.addEventListener('input', checkFields);
typeSelect.addEventListener('change', checkFields);

document.addEventListener('DOMContentLoaded', function() {
    depNameInput.value = '';
    typeSelect.selectedIndex = 0;
    createButton.classList.add('disabled');
});

/*Update Box */
    document.addEventListener("DOMContentLoaded", function() {
       var depNameSelect = document.getElementById("depNameSelect");
       var timeZoneSelect = document.getElementById("timeZoneSelect");
       var depTypeSelect = document.getElementById("depTypeSelect");
       var startWork = document.getElementById("startWork");
       var finishWork = document.getElementById("finishWork");
       var closeButton = document.getElementById("close_update");

       var saveButton = document.getElementById("update");

       var formInputs = [
         depNameSelect,
         timeZoneSelect,
         depTypeSelect,
         startWork,
         finishWork
       ];
       formInputs.forEach(function(input) {
         input.addEventListener("change",  updateButtonState);
       });

       updateButtonState();

     function updateButtonState() {
       var allInputsFilled = formInputs.every(function(input) {
         return input.value.trim() !== "" && input.value !== "" && input.value !== "" && input.value !== "";
       });

       if (allInputsFilled) {
         saveButton.classList.remove('disabled');
       } else {
         saveButton.classList.add('disabled');
       }
     }
});
