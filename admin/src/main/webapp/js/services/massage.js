$(window).on('load', function() {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });
    var email = localStorage.getItem('customer');

    var typeSelect = $("#update_depTypeSelect");
    var choose_depNameSelect = $("#department_NameSelect");

    var update_timeZoneSelect = $("#update_timeZoneSelect");

    choose_depNameSelect.append('<option value="Loading...">Loading...</option>');
    update_timeZoneSelect.append('<option value="Loading...">Loading...</option>');
    typeSelect.append('<option value="Loading...">Loading...</option>');

    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;
    window.dataLoaded;
    loadDepartmentData(url, choose_depNameSelect);

    const selectElement = document.getElementById('department_NameSelect');

    document.getElementById('department_NameSelect').addEventListener('change', function() {
        var checkedSelect = this.value;
        relocateToPage(JSON.parse(checkedSelect).tp);
        localStorage.setItem('lastSelectedOption', checkedSelect);
    });
});

function relocateToPage(depType) {
   switch(depType)
   {
    case 'GENERAL':
        if(!window.location.href.includes("dashboard")) {
             window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/dashboard.html#en';
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

function loadDataFromSelectedDep(url, choose_depNameSelect) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
      var selectedDepartmentData = JSON.parse($('option:checked', '#department_NameSelect').val());
      loadCommonData(selectedDepartmentData);
      window.dataLoaded = true;
    },
    error: function (data) {
        if (data.status === 0) {
            window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
        }
    }
  });
}

function loadDepartmentData(url, choose_depNameSelect) {
  $.ajax({
    url: url,
    type: 'get',
    dataType: 'json',
    success: function (data) {
      choose_depNameSelect.empty();

      console.log(data);
      var lastSelectedOption = JSON.parse(localStorage.getItem('lastSelectedOption'));

      $.each(data.customerDepartments, function (i, department) {
        var name = $("<option value='" + JSON.stringify(department) + "'></option>").text(this.n);

        if (lastSelectedOption && this.n === lastSelectedOption.n) {
          name.prop("selected", true);
        }
        choose_depNameSelect.append(name);
        window.dataLoaded = true;
      });
    },
    error: function (data) {
      if (data.status === 0) {
         window.location.href = 'https://' + uiBucket + '.s3.eu-central-1.amazonaws.com/html/login.html?buttonClicked=true';
      }
    }
  });
}