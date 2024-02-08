import {setColorScheme} from "./modules/helper.js";

function init() {
    handleEventListeners();
    setColorScheme();
    setCheckBox();
}

function handleEventListeners() {
    document.querySelector("input[type='checkbox']").addEventListener("change", function (e) {
        const $target = e.target;
        if ($target.checked) {
            localStorage.setItem("color-theme", "dark");
        } else {
            localStorage.setItem("color-theme", "light");
        }
        setColorScheme();
    });
}

function setCheckBox() {
    const $checkbox = document.querySelector("input[type='checkbox']");
    const theme = localStorage.getItem("color-theme");
    if (theme === "dark") {
        $checkbox.click();
    }
}

init();
