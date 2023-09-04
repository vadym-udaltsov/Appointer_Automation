const countryInput = document.getElementById('countryInput');
const countryList = document.getElementById('countryList');
const cityInput = document.getElementById('cityInput');
const cityList = document.getElementById('cityList');

function fetchCountriesAndCities() {
    $.ajax({
        type: 'GET',
        url: 'https://restcountries.com/v3.1/all',
        dataType: 'JSON',
        success: function (data) {
            countryList.innerHTML = '';
            data.sort(function (a, b) {
                return a.name.common.localeCompare(b.name.common);
            });

            data.forEach(function (country) {
                const li = document.createElement('li');
                var currentLanguage = localStorage.getItem('selectedLanguage');
                switch (currentLanguage) {
                   case 'en':
                        li.textContent = country.name.common;
                        countryList.appendChild(li);

                        li.addEventListener('click', function () {
                            countryInput.value = country.name.common;
                            countryList.style.display = 'none';
                            cityInput.value = '';
                            const selectedCountryCode = country.cca2;
                            fetchCities(selectedCountryCode);
                        });
                       break;
                   case 'ua':
                        li.textContent = country.translations.rus.common;
                        countryList.appendChild(li);

                        li.addEventListener('click', function () {
                            countryInput.value = country.translations.rus.common;
                            countryList.style.display = 'none';
                            cityInput.value = '';
                            const selectedCountryCode = country.cca2;
                            fetchCities(selectedCountryCode);
                        });
                       break;
                   case 'ru':
                        li.textContent = country.translations.rus.common;
                        countryList.appendChild(li);

                        li.addEventListener('click', function () {
                            countryInput.value = country.translations.rus.common;
                            countryList.style.display = 'none';
                            cityInput.value = '';
                            const selectedCountryCode = country.cca2;
                            fetchCities(selectedCountryCode);
                        });
                       break;
                   case 'pl':
                        li.textContent = country.translations.pol.common;
                        countryList.appendChild(li);

                        li.addEventListener('click', function () {
                            countryInput.value = country.translations.pol.common;
                            countryList.style.display = 'none';
                            cityInput.value = '';
                            const selectedCountryCode = country.cca2;
                            fetchCities(selectedCountryCode);
                        });
                       break;
                   case 'cnr':
                        li.textContent = country.translations.srp.common;
                        countryList.appendChild(li);

                        li.addEventListener('click', function () {
                            countryInput.value = country.translations.srp.common;
                            countryList.style.display = 'none';
                            cityInput.value = '';
                            const selectedCountryCode = country.cca2;
                            fetchCities(selectedCountryCode);
                        });
                       break;
                   default:
                }
            });
        },
        error: function () {
            console.error('Ошибка при загрузке стран');
        }
    });
}

function filterCountriesByInput() {
    const searchText = countryInput.value.toLowerCase();
    const countries = countryList.getElementsByTagName('li');

    Array.from(countries).forEach(function (country) {
        const countryName = country.textContent.toLowerCase();
        const isMatch = countryName.includes(searchText);

        country.style.display = isMatch ? 'block' : 'none';
    });

    cityList.innerHTML = '';
}

countryInput.addEventListener('input', function () {
    filterCountriesByInput();
    countryList.style.display = 'block';
});

document.addEventListener('click', function (event) {
    const target = event.target;
    if (target !== countryInput && !countryList.contains(target)) {
        countryList.style.display = 'none';
    }
});

countryInput.addEventListener('click', function () {
    countryList.style.display = 'block';
});

function fetchCities(countryCode) {
    const apiUrl = `https://secure.geonames.org/searchJSON?country=${countryCode}&maxRows=10&username=vadym_udaltsov`;
    const citiesRequest = new XMLHttpRequest();
    citiesRequest.open('GET', apiUrl, true);

    citiesRequest.onreadystatechange = function () {
        if (citiesRequest.readyState === 4) {
            if (citiesRequest.status === 200) {
                const data = JSON.parse(citiesRequest.responseText);
                const cities = data.geonames;

                cities.sort(function (a, b) {
                    return a.name.localeCompare(b.name);
                });

                cityList.innerHTML = '';

                const selectedCountryText = document.getElementById('countryInput').value;

                cities.forEach((city) => {
                    if (city.name.toLowerCase() !== selectedCountryText.toLowerCase()) {
                        const li = document.createElement('li');
                        li.textContent = city.name;
                        cityList.appendChild(li);

                        li.addEventListener('click', function () {
                            cityInput.value = city.name;
                            cityList.style.display = 'none';
                        });
                    }
                });
            } else {
                console.error('Ошибка при загрузке городов:', citiesRequest.statusText);
            }
        }
    };

    citiesRequest.onerror = function () {
        console.error('Ошибка при выполнении запроса к GeoNames API');
    };

    citiesRequest.send();
}

function filterCitiesByInput() {
    const searchText = cityInput.value.toLowerCase();
    const cities = cityList.getElementsByTagName('li');

    Array.from(cities).forEach(function (city) {
        const cityName = city.textContent.toLowerCase();
        const isMatch = cityName.includes(searchText);

        city.style.display = isMatch ? 'block' : 'none';
    });
}

cityInput.addEventListener('input', function () {
    filterCitiesByInput();
    cityList.style.display = 'block';
});

document.addEventListener('click', function (event) {
    const target = event.target;
    if (target !== cityInput && !cityList.contains(target)) {
        cityList.style.display = 'none';
    }
});

cityInput.addEventListener('click', function () {
    cityList.style.display = 'block';
});