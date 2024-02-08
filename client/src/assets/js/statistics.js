import {get, setApi } from "./modules/api.js";
import { eventListenerPopup } from "./modules/popup.js";
import {
    eventListenerFullscreen,
    makeSuggestions,
    selectClickedCategory,
    setColorScheme,
} from "./modules/helper.js";
import { createBarChart, createPieChart } from "./modules/graphs.js";

const PIECHARTTITLES = {
    crimes: "Types (crimes)",
    oxygen_leaks: "Danger level (oxygen leaks)",
    population: "Colony (population)",
    medical_dispaches: "Danger level (medical dispaches)",
    dust_storms: "Damage level (dust storms)",
    meteor_showers: "Damage level (meteor showers)"
};

function loadConfig() {
    fetch("config.json")
        .then((resp) => resp.json())
        .then((config) => {
            const api = `${config.host ? config.host + "/" : ""}${config.group ? config.group + "/" : ""}api/`;
            setApi(api);
            init();
        });
}

function init() {
    handleEventListeners();
    setColorScheme();
    defaultDome();
    defaultSuggestions();
    createBarChart();
    createPieChart();
    eventListenerPopup();
}

function handleEventListeners() {
    selectClickedCategory("#category_chart h2", function() {
        makeCharts();
        const $target = document.querySelector("#types_chart h2");
        let category = document.querySelector("aside .selected").id;
        category = category.replace("-", "_");
        $target.innerText = PIECHARTTITLES[category];
    });

    document.querySelector("#searchbar input").addEventListener("keyup", function() {
        makeSuggestions(addEventListenersSuggestions);
    });

    document.querySelector("#period").addEventListener("change", makeCharts);

    document.querySelector("#period").addEventListener("change", makeCharts);

    function makeCharts() {
        createBarChart();
        createPieChart();
    }

    eventListenerFullscreen("#category_chart .material-icons", "#category_chart");
}


function addEventListenersSuggestions() {
    document.querySelectorAll("#suggestions li").forEach((li) => {
        li.addEventListener("click", function(ev) {
            const $clicked = ev.currentTarget;
            const id = $clicked.id;
            const domeName = $clicked.querySelector("p").innerText;
            const $target = document.querySelector("#dome-choice h3");
            $target.innerText = domeName;
            $target.id = id;

            createPieChart();
        });
    });
}

function defaultDome() {
    get("domes", succesHandler);

    function succesHandler(res) {
        res.json().then((data) => {
            const $target = document.querySelector("#dome-choice h3");
            const domeName = data.domes[0].domeName;
            $target.innerText = domeName;
        });
    }
}

function defaultSuggestions() {
    makeSuggestions(addEventListenersSuggestions);
}

loadConfig();
