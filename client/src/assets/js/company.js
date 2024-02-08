import { createLineChart } from "./modules/graphs.js";
import { selectClickedCategory, eventListenerFullscreen, setColorScheme } from "./modules/helper.js";
import { eventListenerPopup } from "./modules/popup.js";

function init() {
    handleEventListeners();
    setColorScheme();
    createLineChart();
    eventListenerPopup();
}

function handleEventListeners() {
    selectClickedCategory("#company-chart h4", createLineChart);

    document.querySelectorAll("#years input").forEach(year => {
        year.addEventListener("change", createLineChart);
    });

    eventListenerFullscreen("#company-chart .material-icons", "#company-chart");
}

init();
