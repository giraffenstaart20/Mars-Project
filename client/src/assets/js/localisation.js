import { eventListenerPopup, showPopup } from "./modules/popup.js";
import {setColorScheme} from "./modules/helper.js";

function init() {
    handleEventListeners();
    setColorScheme();
    eventListenerPopup();
}

function handleEventListeners() {
    document.querySelector("form").addEventListener("submit", function (e) {
        e.preventDefault();
        showPopup(e, "request");
        removeText();
    });
}

function removeText() {
    document.querySelectorAll("input, textarea").forEach($input => {
        $input.value = "";
    });
}

init();
