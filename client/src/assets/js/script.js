import {createMap, center, setView} from "./modules/map.js";
import {makeHidden, removeHidden, makeSuggestions, setPosition, setColorScheme} from "./modules/helper.js";
import {get, setApi} from "./modules/api.js";

function loadConfig() {
    fetch("config.json")
        .then(resp => resp.json())
        .then(config => {
            const api = `${config.host ? config.host + '/': ''}${config.group ? config.group + '/' : ''}api/`;
            setApi(api);
            init();
        });
}

function init() {
    handleEventListeners();
    setColorScheme();
    setTimeout(startupAnimation, 2500);
    makeMap();
}

function makeMap() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(makeMapCurrentPosition, makeMapDefaultPosition);
    }
}

function makeMapCurrentPosition(currentPosition) {
    const coords = currentPosition.coords;
    const lat = coords.latitude;
    const lon = coords.longitude;
    const position = setPosition([lat, lon]);
    createMap(position, true);
}

function makeMapDefaultPosition() {
    createMap([-23, -69], false);
}

function handleEventListeners() {
    document.querySelector('.center').addEventListener("click", function() {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(center, handleError);
        }
    });

    document.querySelector('#searchbar input').addEventListener("keyup", function () {
        document.querySelector("#suggestions").classList.remove("hidden");
        makeSuggestions(addEventListenersSuggestions);
    });

    document.querySelector("body").addEventListener("click", function (e) {
        const targetId = e.target.id;
        const inMap = e.target.parentElement.closest('#map');

        if (inMap || targetId === "map") {
            document.querySelector("#suggestions").classList.add("hidden");
        }
    });

    document.querySelector(".close").addEventListener("click", () => {
        makeHidden("#side_navigation");
    });
    document.querySelector(".menu").addEventListener("click", () => {
        removeHidden("#side_navigation");
    });
}

function addEventListenersSuggestions() {
    document.querySelectorAll('#suggestions li').forEach(li => {
        li.addEventListener("click", function (ev) {
            const target = ev.currentTarget.id;
            changeView(target);
        });
    });
}

function changeView(search) {
    get("domes", succesHandler);

    function succesHandler(response) {
        response.json().then(data => {
            const domes = data.domes;
            for (const i in domes) {
                if (domes.hasOwnProperty(i)) {
                    const dome = domes[i];
                    if (dome.id === parseInt(search)) {
                        setView([dome.latitude, dome.longitude]);
                    }
                }
            }
        });
    }
}

function handleError() {
    console.log("Give permission to see your location");
}

function startupAnimation() {
    const loader = document.querySelector(".loading");
    const map = document.querySelector("#map");
    loader.classList.add("invisible");
    map.classList.remove("hidden");
}

loadConfig();
