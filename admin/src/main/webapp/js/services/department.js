$(window).on('load', function() {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var typeSelect = $("#update_depTypeSelect");
    var choose_depNameSelect = $("#department_NameSelect");
    var update_timeZoneSelect = $("#update_timeZoneSelect");

    hideElements();

    choose_depNameSelect.append('<option value="Loading...">Loading...</option>');
    update_timeZoneSelect.append('<option value="Loading...">Loading...</option>');
    typeSelect.append('<option value="Loading...">Loading...</option>');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    window.dataLoaded;
    loadDepartmentData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect);

    const selectElement = document.getElementById('department_NameSelect');

    document.getElementById('department_NameSelect').addEventListener('change', function() {
        var checkedSelect = this.value;
        relocateToPage(JSON.parse(checkedSelect).tp);
        localStorage.setItem('lastSelectedOption', checkedSelect);
    });

    $("#department_UpdatePopup").click(function() {
        loadDepUpdateData(url);
    });

    $("#update_DepBtn").click(function() {
    var updateButton = document.getElementById('update_DepBtn');
        if (updateButton.classList.contains('disabled')) {
            return false;
        }
        var department = new Object();
        var id = JSON.parse($("#department_NameSelect").val()).id;
        var name = $("#department_NameSelect option:selected").text().trim();

        department.id = id;
        department.n = name;
        department.c = email;

        department.zone = $("#update_timeZoneSelect").val();
        department.sw = $("#startWork").val();
        department.ew = $("#finishWork").val();
        var checkboxes = document.getElementsByName("dayCheck");
        const days = [];
        for (var i = 0; i < checkboxes.length; i++) {
            if(checkboxes[i].checked) {
                days.push(checkboxes[i].value);
            }
        }
        department.nwd = days;
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department');
        $("#department_UpdateModal").modal("hide");
        location.reload();
        return false;
    });

     $("#create_depBtn").click(function() {
        const createButton = document.getElementById('create_depBtn');
        if (createButton.classList.contains('disabled')) {
            return false;
        }
        var department = new Object();
        department.name = $("#depName_create").val();
        department.email = localStorage.getItem('customer');
        department.tp = $("#update_depTypeSelect").val();
        executePost(JSON.stringify(department), 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/create');
        $("#department_CreateModal").modal("hide");
        document.getElementById('notRegisteredContainer').style.display = 'none';
        document.getElementById('waitMessageContainer').style.display = 'block';
        document.getElementById('department_CreatePopup').disabled = true;
        localStorage.setItem('createBtnClicked', 'true');
        return false;
     });

     function hideElements() {
         document.getElementById('department_UpdatePopup').style.display = 'none';
         document.getElementById('department_CreatePopup').style.display = 'none';
         document.getElementById('service_createOpenBtn').disabled = true;
         document.getElementById('specialist_createOpenBtn').disabled = true;
         document.getElementById('admin_createOpenBtn').disabled = true;
     }
});

var email = localStorage.getItem('customer');

function relocateToPage(depType) {
   switch(depType)
   {
    case 'MASSAGE':
        if(!window.location.href.includes("massage")) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/massage.html';
        }
        break;
    case 'HAIR_DRESS':
        if(!window.location.href.includes("hair")) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/hair.html';
        }
        break;
    default:
        console.log('404 - Page is not found.');
   }
}

function loadDataForTables(selectedDepartmentData) {
  var serviceTable = $("#servicesTable");
  serviceTable.find('.service').remove();
  $.each(selectedDepartmentData.s, function (j, item) {
    var row = $("<div class='service'></div>");
    var nameCell = $("<div class='service_name cell'></div>").text(item.name);
    var durationCell = $("<div class='service_duration cell'></div>").text(item.duration);
    var priceCell = $("<div class='service_price cell'></div>").text(item.price);
    var actionsCell = $("<div class='cell actions'></div>");

    var updateButton = $("<button type='button' class='sub-button service_updateOpenBtn lng-updateBtn' data-toggle='modal' data-target='#service_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button service_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#service_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(updateButton, deleteButton);

    row.append(nameCell, durationCell, priceCell, actionsCell);

    serviceTable.append(row);
  });

  var specialistTable = $("#specialistTable");
  specialistTable.find('.specialist').remove();
  $.each(selectedDepartmentData.as, function (j, item) {
    var row = $("<div class='specialist'></div>");
    var nameCell = $("<div class='specialist_name specCol'></div>").text(item.name);
    var durationCell = $("<div class='specialist_phone specCol'></div>").text(item.pn);
    var actionsCell = $("<div class='specCol actions'></div>");

    var updateButton = $("<button type='button' class='sub-button specialist_updateOpenBtn lng-updateBtn' data-toggle='modal' data-target='#specialist_UpdateModal'>")
      .text("Update")
      .val(JSON.stringify(item));
    var deleteButton = $("<button type='button' class='sub-button specialist_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#specialist_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(updateButton, deleteButton);

    row.append(nameCell, durationCell, actionsCell);

    specialistTable.append(row);
  });

  var adminTable = $("#adminTable");
  adminTable.find('.admin').remove();
  $.each(selectedDepartmentData.adm, function (j, item) {
    var row = $("<div class='admin'></div>");
    var phoneCell = $("<div class='admin_phone admCol centerPos'></div>").text(item);
    var actionsCell = $("<div class='admCol actions  centerPos'></div>");

    var deleteButton = $("<button type='button' class='sub-button admin_deleteOpenBtn lng-deleteBtn' data-toggle='modal' data-target='#admin_DeleteModal'>")
      .text("Delete")
      .val(JSON.stringify(item));

    actionsCell.append(deleteButton);

    row.append(phoneCell, actionsCell);

    adminTable.append(row);
  });

  window.dataLoaded = true;
}

function loadDepartmentData(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
        document.getElementById('spinner_loading').style.display = 'flex';
        choose_depNameSelect.empty();
        update_timeZoneSelect.empty();
        typeSelect.empty();

        var createServiceBtn  = document.getElementById('service_createOpenBtn');
        var createSpecialistBtn  = document.getElementById('specialist_createOpenBtn');
        var createAdminBtn  = document.getElementById('admin_createOpenBtn');


        if(data.customerDepartments.length === 0) {
            if (data.registered == false) {
                localStorage.setItem('createBtnClicked', 'false');
                document.getElementById('waitMessageContainer').style.display = 'none';
                document.getElementById('notRegisteredContainer').style.display = 'block';
                document.getElementById('department_CreatePopup').disabled = false;
                if(data.admin) {
                    $.each(data.availableTypes, function () {
                        var type = $("<option value='" + this + "'></option>").text(this);
                        typeSelect.append(type);
                    });
                } else {
                    if (data.availableTypes.length > 0) {
                        var firstType = $("<option value='" + data.availableTypes[0] + "'></option>").text(data.availableTypes[0]);
                        typeSelect.append(firstType);
                    }
                }
            } else {
                document.getElementById('waitMessageContainer').style.display = 'block';
                document.getElementById('department_CreatePopup').disabled = true;
                document.getElementById('notRegisteredContainer').style.display = 'none';
            }
            document.getElementById('department_CreatePopup').style.display = 'block';
            document.getElementById('department_UpdatePopup').style.display = 'none';
            document.getElementById('bot_name_section').style.display = 'none';

            createServiceBtn.disabled = true;
            createSpecialistBtn.disabled = true;
            createAdminBtn.disabled = true;
            window.dataLoaded = true;
            changeLanguage();
            document.getElementById('spinner_loading').style.display = 'none';
        } else {
            document.getElementById('department_UpdatePopup').style.display = 'block';
            document.getElementById('department_CreatePopup').style.display = 'none';
            document.getElementById('notRegisteredContainer').style.display = 'none';
            createServiceBtn.disabled = false;
            createSpecialistBtn.disabled = false;
            createAdminBtn.disabled = false;
            console.log(data);
            changeLanguage();
            localStorage.setItem('lastSelectedOption', data.customerDepartments[0].n);
            var lastSelectedOption = localStorage.getItem('lastSelectedOption');
            var botNameSpan = document.getElementById('bot_name_value');
            botNameSpan.textContent = data.customerDepartments[0].bun;

            $.each(data.customerDepartments, function (i, department) {
                var name = $("<option value='" + JSON.stringify(department) + "'></option>").text(this.n);

                if (lastSelectedOption && this.n === lastSelectedOption) {
                  name.prop("selected", true);
                }

                choose_depNameSelect.append(name);
            });

            $.each(data.availableZones, function () {
                var timeZone = $("<option value='" + this.id + "'></option>").text(this.title);
                update_timeZoneSelect.append(timeZone);
            });

            var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
            loadDataForTables(selectedDepartmentData);
            document.getElementById('spinner_loading').style.display = 'none';
        }
    },
    error: function (data) {
      if (data.status === 0) {
         window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}

function loadDepUpdateData(url) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
        var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
        document.getElementById("update_selectedDepartment").value = selectedDepartmentData.n;
        document.getElementById("update_timeZoneSelect").value = selectedDepartmentData.zone;
        document.getElementById("update_depTypeInput").value = selectedDepartmentData.tp;
        document.getElementById("startWork").value = selectedDepartmentData.sw;
        document.getElementById("finishWork").value = selectedDepartmentData.ew;

        var checkedDateFromData = selectedDepartmentData.nwd;
        var daysCheckboxes = document.querySelectorAll('input[name="dayCheck"]');
        daysCheckboxes.forEach(function (checkbox) {
          if (checkedDateFromData.includes(parseInt(checkbox.value))) {
              checkbox.checked = true;
          } else {
              checkbox.checked = false;
          }
        });
    },
    error: function (data) {
        if (data.status === 0) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
        }
    }
  });
}

function loadDataFromSelectedDep(url, typeSelect, choose_depNameSelect, update_timeZoneSelect, dataLoaded) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadDataForTables(selectedDepartmentData);
    },
    error: function (data) {
        if (data.status === 0) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
        }
    }
  });
}

function activateButton() {
  $('#department_link').addClass('btn-active');
}

document.getElementById('close_updateDepBtn').addEventListener('click', function () {
    var updateDepBtn = document.getElementById('update_DepBtn');
    updateDepBtn.disabled = false;
});

function validateInput(input) {
  input.value = input.value.replace(/\D/g, '');
}

function validateUpdateDepInput(input, inputType) {
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

function updateCharCount() {
  const depNameInput = document.getElementById("depName_create");
  const charCount = depNameInput.value.length;
  const charCounter = document.getElementById("charCounter");

  charCounter.textContent = `${charCount}/30`;
}







