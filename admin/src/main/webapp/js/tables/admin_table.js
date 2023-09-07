$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    document.querySelector('#admin_CreateBtn').addEventListener('click', function() {
        var admin = new Object();
        admin.pn = $("#admin_CreatePhoneInput").val();

        var newAdminBlock = document.createElement('div');
        newAdminBlock.classList.add('admin');
        newAdminBlock.setAttribute('value', admin);

        var phoneDiv = document.createElement('div');
        phoneDiv.classList.add('admCol', 'centerPos');
        phoneDiv.textContent = admin.pn;

        var actionsDiv = document.createElement('div');
        actionsDiv.classList.add('admCol', 'centerPos', 'actions');

        var deleteButton = document.createElement('button');
        deleteButton.setAttribute("type", "button");
        deleteButton.className = "sub-button admin_deleteOpenBtn lng-deleteBtn";
        deleteButton.setAttribute("data-toggle", "modal");
        deleteButton.setAttribute("data-target", "#admin_DeleteModal");
        deleteButton.setAttribute("value", JSON.stringify(admin));
        deleteButton.textContent = 'Delete';

        actionsDiv.appendChild(deleteButton);

        newAdminBlock.appendChild(phoneDiv);
        newAdminBlock.appendChild(actionsDiv);

        var column = document.querySelector('.columns_titles_admin');
        var lastService = column.parentElement.querySelector('.admin:last-of-type');
        if (lastService) {
          lastService.insertAdjacentElement('afterend', newAdminBlock);
        } else {
          column.insertAdjacentElement('afterend', newAdminBlock);
        }

    });

    var deleteButtons = document.querySelectorAll('.delete');

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var column = this.closest('.admin');
            column.remove();
        });
    });

    var column = document.querySelector('.columns_titles_admin');

    column.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete')) {
            var service = event.target.closest('.admin');
            service.remove();
        }
    });

    async function applyStyleAfterDataLoad() {
        while (window.dataLoaded !== true) {
            await new Promise(resolve => setTimeout(resolve, 200)); // Ждем 100 миллисекунд перед следующей проверкой
        }
            var tableElements = document.querySelectorAll('.admin');
            var elementCount = tableElements.length;
            if (elementCount <= 4) {
                tableElements.forEach(function(element) {
                    element.style.borderBottom = '1px solid black';
                });
            } else {
                for (var i = 0; i < elementCount - 1; i++) {
                    tableElements[i].style.borderBottom = '1px solid black';
                }
           }
    }
    applyStyleAfterDataLoad();

    document.getElementById('department_NameSelect').addEventListener('change', function() {
        setTimeout(function() {
            applyStyleAfterDataLoad();
        }, 150);
    });
});
