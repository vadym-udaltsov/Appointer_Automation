const depNameInput = document.getElementById('depName');
const typeSelect = document.getElementById('typeSelect');
const createButton = document.getElementById('create');
const cancelButton = document.getElementById('close_create');

function checkFields() {
    const depName = depNameInput.value.trim();
    const type = typeSelect.value.trim();


    if (depName !== '' && type !== 'Choose Type') {
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

cancelButton.addEventListener('click', function() {
     depNameInput.value = '';
     typeSelect.selectedIndex = 0;
     createButton.classList.add('disabled');
});

/*Update Box */
    document.addEventListener("DOMContentLoaded", function() {
       var depNameSelect = document.getElementById("depNameSelect");
       var timeZoneSelect = document.getElementById("timeZoneSelect");
       var depTypeSelect = document.getElementById("depTypeSelect");
       var depNameUpdate = document.getElementById("depNameUpdate");
       var startWork = document.getElementById("startWork");
       var finishWork = document.getElementById("finishWork");
       var closeButton = document.getElementById("close_update");

       var saveButton = document.getElementById("update");

       var formInputs = [
         depNameSelect,
         timeZoneSelect,
         depTypeSelect,
         depNameUpdate,
         startWork,
         finishWork
       ];
       formInputs.forEach(function(input) {
         input.addEventListener("change",  updateButtonState);
       });

       updateButtonState();

     function updateButtonState() {
       var allInputsFilled = formInputs.every(function(input) {
         return input.value.trim() !== "" && input.value !== "Choose Department" && input.value !== "Choose Time Zone" && input.value !== "Choose Type";
       });

       if (allInputsFilled) {
         saveButton.classList.remove('disabled');
       } else {
         saveButton.classList.add('disabled');
       }
     }


          closeButton.addEventListener("click", resetForm);
          function resetForm() {
            depNameSelect.value = "Choose Department";
            timeZoneSelect.value = "Choose Time Zone";
            depTypeSelect.value = "Choose Type";
            depNameUpdate.value = "";
            startWork.value = "";
            finishWork.value = "";
          }
     });