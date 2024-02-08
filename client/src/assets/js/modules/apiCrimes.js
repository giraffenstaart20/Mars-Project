const api = "https://project-ii.ti.howest.be/mars-11/api/";

export function getIncidents(successHandler = logJson, failureHandler = logError) {
    const request = new Request(api + "incidents?active=false", {
        method: 'GET',
    });
    call(request, successHandler, failureHandler);
}

function logJson(response) {
    response.json().then(console.log);
}

function logError(error) {
    console.log(error);
}

function call(request, successHandler, errorHandler) {
    fetch(request).then(successHandler).catch(errorHandler);
}
