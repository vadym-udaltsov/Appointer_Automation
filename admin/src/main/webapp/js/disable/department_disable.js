var createDepartmentBtn = document.getElementById('create_depBtn');
var departmentCancelBtn = document.getElementById('close_depModalBtn');
var openCreateDepartmentModal = document.getElementById('department_CreatePopup');

const create_depNameInput = document.getElementById("depName_create");

openCreateDepartmentModal.addEventListener('click', function () {
    if($("#depName_create").val() == '') {
       createDepartmentBtn.disabled = true;
    }
});


function checkInputs() {
    if (create_depNameInput.value === '') {
        createDepartmentBtn.disabled = true;
    } else {
        createDepartmentBtn.disabled = false;
    }
}

create_depNameInput.addEventListener("input", checkInputs);


