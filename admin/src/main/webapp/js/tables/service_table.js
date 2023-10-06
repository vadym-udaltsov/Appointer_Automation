$(window).ready(function () {
    $.ajaxSetup({
        headers: { 'token': localStorage.getItem('token')}
    });

    var email = localStorage.getItem('customer');
    var url = 'https://' + apiGatewayId + '.execute-api.eu-central-1.amazonaws.com/dev/admin/department/data/' + email;

    document.querySelector('#service_CreateBtn').addEventListener('click', function() {
        var service = new Object();
        service.name = $("#service_Create-servNameInput").val();
        service.duration = $("#service_Create-servDurationInput").val();
        service.price = $("#service_Create-servPriceInput").val();
        if(service.price == "") {
            service.price = 0;
        }

        var newServiceBlock = document.createElement('div');
        newServiceBlock.classList.add('service');
        newServiceBlock.setAttribute('value', service);

        var nameDiv = document.createElement('div');
        nameDiv.classList.add('cell', 'mobile_row', 'service_name');
        nameDiv.textContent = service.name;

        var durationDiv = document.createElement('div');
        durationDiv.classList.add('cell', 'service_duration', 'mobile_row');
        durationDiv.textContent = service.duration;

        var priceDiv = document.createElement('div');
        priceDiv.classList.add('cell', 'service_price', 'mobile_row');
        priceDiv.textContent = service.price;

        var actionsDiv = document.createElement('div');
        actionsDiv.classList.add('cell', 'actions', 'mobile_row_actions');

        var updateButton = document.createElement('button');
        updateButton.setAttribute("type", "button");
        updateButton.className = "sub-button service_updateOpenBtn lng-updateBtn mobile_action_btn";
        updateButton.setAttribute("data-toggle", "modal");
        updateButton.setAttribute("data-target", "#service_UpdateModal");
        updateButton.setAttribute("value", JSON.stringify(service));
        updateButton.textContent = 'Update';

        var deleteButton = document.createElement('button');
        deleteButton.setAttribute("type", "button");
        deleteButton.className = "sub-button service_deleteOpenBtn lng-deleteBtn mobile_action_btn";
        deleteButton.setAttribute("data-toggle", "modal");
        deleteButton.setAttribute("data-target", "#service_DeleteModal");
        deleteButton.setAttribute("value", JSON.stringify(service));
        deleteButton.textContent = 'Delete';

        actionsDiv.appendChild(updateButton);
        actionsDiv.appendChild(deleteButton);

        newServiceBlock.appendChild(nameDiv);
        newServiceBlock.appendChild(durationDiv);
        newServiceBlock.appendChild(priceDiv);
        newServiceBlock.appendChild(actionsDiv);

        var column = document.querySelector('.columns_titles');
        var lastService = column.parentElement.querySelector('.service:last-of-type');
            if (lastService) {
                lastService.insertAdjacentElement('afterend', newServiceBlock);
            } else {
                column.insertAdjacentElement('afterend', newServiceBlock);
            }

    });

    var deleteButtons = document.querySelectorAll('.delete');

    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var column = this.closest('.service');
            column.remove();
        });
    });

    var column = document.querySelector('.columns_titles');

    column.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete')) {
            var service = event.target.closest('.service');
            service.remove();
        }
    });

    async function applyStyleAfterDataLoad() {
        while (window.dataLoaded !== true) {
            await new Promise(resolve => setTimeout(resolve, 200));
         }
            var tableElements = document.querySelectorAll('.service');
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
