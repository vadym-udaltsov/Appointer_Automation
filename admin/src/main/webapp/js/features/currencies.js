var currenciesDropdown = $('#update_currencySelect');

function fetchCurrencies(countryCode) {
    var apiUrl = 'https://openexchangerates.org/api/currencies.json';
    var currenciesRequest = new XMLHttpRequest();
    currenciesRequest.open('GET', apiUrl, true);

    currenciesRequest.onreadystatechange = function () {
        var data = currenciesRequest.response;
        if (data == "" ) {
            console.log("Empty")
        } else {
            var validData = JSON.parse(data);
                    for (var code in validData) {
                        var country = validData[code];
                        if (country.length > 20) {
                            country = country.slice(0, 20) + '...';
                        }
                        currenciesDropdown.append($('<option>', {
                            value: code,
                            text: code + ' - ' + country
                        }));
                    }
        }
    };
    currenciesRequest.onerror = function () {
        currenciesDropdown.append('<option value="Loading...">Loading...</option>');
    };
    currenciesRequest.send();
}
