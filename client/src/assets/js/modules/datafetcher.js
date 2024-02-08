import {companyData} from "../../../data/company-data.js";
import {get} from "./api.js";
import {getIncidents} from "./apiCrimes.js";
import {setPosition} from "./helper.js";

const APICALLS = {
    crimes: "crimes",
    oxygen_leaks: "oxygenLeaks",
    population: "population",
    medical_dispaches: "medicalDispatches",
    dust_storms: "dustStorms",
    meteor_showers: "meteorShowers"
};

const CATEGORYTYPES = {
    crimes: "type",
    oxygen_leaks: "dangerLevel",
    population: "colony",
    medical_dispaches: "dispachType",
    dust_storms: "damageLevel",
    meteor_showers: "damageLevel"
};

const HEATMAPS = [{
    title: "Oxygen Leaks",
    dataApiCall: APICALLS.oxygen_leaks
},{
    title: "Population",
    dataApiCall: APICALLS.population
},{
    title: "Medical Dispaches",
    dataApiCall: APICALLS.medical_dispaches,
},{
    title: "Dust Storms",
    dataApiCall: APICALLS.dust_storms,
},{
    title: "Meteor Showers",
    dataApiCall: APICALLS.meteor_showers,
}];


// MAP
export function getHeatMapData(func) {
    getCrimes(succesHandler);

    function succesHandler(data) {
        const res = [{title: "Crimes",
                        data: data.crimes}];
        createDataHeatmap(res, 0, nextFunction);

        function nextFunction(res, i) {
            if (i + 1 < HEATMAPS.length) {
                createDataHeatmap(res, i + 1, nextFunction);
            } else {
                func(res);
            }
        }
    }
}


function createDataHeatmap(res, i, func) {
    get(HEATMAPS[i].dataApiCall, succesHandler);

    function succesHandler(response) {
        response.json().then(data => {
            const obj = {
                title: HEATMAPS[i].title,
                data: makeDataInPosition(data[HEATMAPS[i].dataApiCall])
            };
            res.push(obj);
            func(res, i);
        });
    }
}

function makeDataInPosition(data) {
    const res = [];
    data.forEach(obj =>{
        const position = [obj.longitude, obj.latitude, 1];
        for (let i = 0; i < 0.001; i = i + 0.0001) {
            res.push([...position]);
            position[0] = position[0] + i;
            res.push([...position]);
            position[1] = position[1] + i;
            res.push([...position]);
            position[1] = position[1] - i;
            res.push([...position]);
            position[1] = position[1] - i;
            res.push([...position]);
        }
    });
    return res;
}

// BAR CHART
export function getBarChartData(category, period, func) {
    category = category.replace("-","_");
    period = parseInt(period);
    const apiCall = APICALLS[category];
    if (category === "crimes") {
        getCrimes(succesHandler);
    } else {
        get(apiCall, succesHandler);
    }
    function succesHandler(res) {
        if (res["crimes"]) {
            createData(res);
        } else {
            res.json().then(data => {
                createData(data, apiCall, period, func);
            });
        }
    }
}

function createData(data, apiCall, period, func) {
    data = data[apiCall].sort((a, b) => a.dome.id - b.dome.id);
    let dataPerDome = createDomeNameLabels(data);
    data = data
        .filter(obj => filterOnPeriod(obj, period));
    dataPerDome = getDataPerDome(dataPerDome, data);
    func(dataPerDome);
}

function createDomeNameLabels(data) {
    const res = {};
    data.forEach(el => {
        res[el.dome.domeName] = 0;
    });
    return res;
}

function filterOnPeriod(obj, period) {
    if (period === 0) {
        return true;
    }
    const now = new Date();
    const currentMonth = now.getMonth();
    const beginMonth = currentMonth - period;
    now.setMonth(beginMonth);

    const objDate = new Date(obj.date);
    return objDate > now;
}

function getDataPerDome(dataPerDome, data) {
    data.forEach(obj => {
        dataPerDome[obj.dome.domeName] += 1;
    });
    return dataPerDome;
}

// PIE CHART
export function getPieChartData(category, period, domeId, func) {
    category = category.replace("-","_");
    period = parseInt(period);
    domeId = parseInt(domeId);
    const apiCall = APICALLS[category];
    if (category === "crimes") {
        getCrimes(succesHandler);
    } else {
        get(apiCall, succesHandler);
    }

    function succesHandler(res) {
        if (res["crimes"]) {
            createData(res);
        } else {
            res.json().then(data => {
                createPieChartData(data, apiCall, category, domeId, period, func);
            });
        }
    }
}

function createPieChartData(data, apiCall, category, domeId, period, func) {
    let dataPerType = createLabels(data[apiCall], CATEGORYTYPES[category]);
    data = data[apiCall]
        .filter(obj => obj.dome.id === domeId)
        .filter(obj => filterOnPeriod(obj, period));
    dataPerType = getDataPerType(dataPerType, data, category);
    dataPerType = addPercentage(dataPerType);
    dataPerType = sortObject(dataPerType);
    func(dataPerType);
}

function sortObject(obj) {
    const keys = Object.keys(obj);
    const sortedKeys = sortKeys(obj, keys);
    const sortedObj = {};
    sortedKeys.forEach(key => {
        sortedObj[key] = obj[key];
    });
    return sortedObj;
}

function sortKeys(obj, keys) {
    return keys.sort((a, b) => obj[b] - obj[a]);
}

function getDataPerType(dataPerType, data, category) {
    data.forEach(obj => {
        dataPerType[obj[CATEGORYTYPES[category]]] += 1;
    });
    return dataPerType;
}

function addPercentage(dataPerType) {
    const total = Object.values(dataPerType).reduce((a, b) => a + b);
    for (const dataPerTypeKey in dataPerType) {
        if (dataPerType.hasOwnProperty(dataPerTypeKey)) {
            const value = dataPerType[dataPerTypeKey];
            const percentage = (value / total * 100).toFixed(0);
            const label = dataPerTypeKey.charAt(0).toUpperCase() + dataPerTypeKey.slice(1).toLowerCase();
            delete Object.assign(dataPerType, {[` (${percentage}%) ${label}`]: dataPerType[dataPerTypeKey] })[dataPerTypeKey];
        }
    }
    return dataPerType;
}

function createLabels(data, key) {
    const res = {};
    data.forEach(el => {
        res[el[key]] = 0;
    });
    return res;
}

// LINE CHART
export function getLineChartData(category, years) {
    const res = [];
    years.forEach(year => {
        res.push(companyData[category][year]);
        // normally the company itself should prepare this data, so we do it with the data of out company we made in the business case
    });
    return res;
}

// GLOBAL

function getCrimes(func) {
    getIncidents(succesHandler);

    function succesHandler(res) {
        res.json().then(data => {
            makeCrimes(data, func);
        });
    }
}

function makeCrimes(data, func) {
    let res = [];
    for (let i = 0; i < data.length; i++) {
        const incident = data[i];
        const position = setPosition([incident.latitude, incident.longitude]);
        getClosestDome(position, succesHandler);

        async function succesHandler(closestDome) {
            const crime = {
                id: incident.id,
                lat: position[0],
                lon: position[1],
                dome: closestDome,
                type: incident.type
            };
            res.push(crime);
            if (i === data.length - 1) {
                res = {"crimes": res};
                func(res);
            }
        }
    }
}

function getClosestDome(position, func) {
    get("domes", succesHandler);

    function succesHandler(response) {
        response.json().then(data => {
            const domes = data.domes;
            let closestDome = domes[0];
            let minDistance = getDistance(position, domes[0]);
            domes.forEach(dome => {
                const distance = getDistance(position, dome);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestDome = dome;
                }
            });
            func(closestDome);
        });
    }
}

function getDistance(position, dome) {
    const distanceLat = Math.abs(Math.round(position[0] - dome.latitude));
    const distanceLong = Math.abs(Math.round(position[1] - dome.longitude));
    return distanceLong - distanceLat;
}


