import {get} from "./api.js";
import {MAPBOUNDS} from "./map.js";

export function makeHidden(selector) {
    const $element = document.querySelector(selector);
    if ($element) {
        $element.classList.add("hidden");
    }
}

export function removeHidden(selector) {
    const $element = document.querySelector(selector);
    if ($element) {
        $element.classList.remove("hidden");
    }
}

export function removeClass(selector, className) {
    document.querySelectorAll(selector).forEach($el => {
        $el.classList.remove(className);
    });
}

export function removeClassAfterClick(selector, className) {
    document.querySelector("body").addEventListener("click", function (e) {
        const targetClassList = e.target.classList;
        if (!targetClassList.contains(className)) {
            removeClass(selector, className);
        }
    });
}

export function setColorScheme() {
    let theme = getTheme();
    if (!theme) {
        localStorage.setItem("color-theme", "light");
        theme = "light";
    }
    document.documentElement.setAttribute("color-theme", theme);
}

export function searchDome(searchDome, func) {
    get("domes", succesHandler);

    function succesHandler(res) {
        res.json().then(data => {
            const domes = data.domes;
            let result;
            if (Number.isInteger(searchDome)) {
                result = domes.find(dome => dome.id === searchDome);
            } else {
                result = domes.find(dome => dome.domeName === searchDome);
            }
            func(result);
        });
    }
}

export function setPosition(position) {
    const res = [];
    const differenceLatitude = Math.abs(MAPBOUNDS.south - MAPBOUNDS.north);
    const differenceLongitude = Math.abs(MAPBOUNDS.west - MAPBOUNDS.east);
    for (let i = 0; i < position.length; i++) {
        let n = position[i];
        if (i === 0) {
            const modulo = Math.abs(n % differenceLatitude);
            n = MAPBOUNDS.south + modulo;
        } else {
            const modulo = Math.abs(n % differenceLongitude);
            n = MAPBOUNDS.west + modulo;
        }
        res.push(n);
    }
    return res;
}

export function selectClickedCategory(selector, func) {
    document.querySelectorAll("aside li").forEach($li => {
        $li.addEventListener("click", function (ev) {

            document.querySelectorAll("aside li").forEach(li => {
                li.classList.remove("selected");
            });
            const $currentTarget = ev.currentTarget;
            $currentTarget.classList.add("selected");

            const $oldTitle = document.querySelector(selector);
            const newTitle = $currentTarget.querySelector("p").innerText;
            $oldTitle.innerText = newTitle;

            func();
        });
    });
}

export function makeSuggestions(addEventListeners) {
    get("domes", succesHandler);

    function succesHandler(response) {
        response.json().then(data => {
            let domes = data.domes;
            if (domes) {
                domes = domes
                    .filter(dome => filterDomes(dome.domeName))
                    .sort((a, b) => a.domeName.localeCompare(b.domeName));
                showSuggestions(domes);
                addEventListeners();
            }
        });
    }
}

function filterDomes(domeName) {
    const search = document.querySelector("#searchbar input").value;
    const searchLength = search.length;
    for (let i = 0; i < searchLength; i++) {
        if (domeName.charAt(i).toLowerCase() !== search.charAt(i).toLowerCase()) {
            return false;
        }
    }
    return true;
}

function showSuggestions(domes) {
    const $target = document.querySelector('#suggestions');
    const maxSuggestions = 6;
    $target.innerHTML = '';
    for (let i = 0; i < domes.length; i++) {
        if (i < maxSuggestions) {
            const dome = domes[i];
            const html = `<li id="${dome.id}"><p class="hover-underline-animation">${dome.domeName}</p></li>`;
            $target.innerHTML += html;
        }
    }
}

export function eventListenerFullscreen(iconsSelector, parentSelector) {
    document.querySelectorAll(iconsSelector).forEach($icon => {
        $icon.addEventListener("click", fullscreenToggle);
    });

    document.querySelector(parentSelector).addEventListener("fullscreenchange", ev => {
        ev.currentTarget.querySelectorAll(iconsSelector).forEach(icon => {
            icon.classList.toggle('hidden');
        });
    });

    function fullscreenToggle(e) {
        const target = e.currentTarget;
        const parent = target.parentElement;

        if (target.innerText === "fullscreen") {
            parent.requestFullscreen();
        }
        else {
            document.exitFullscreen();
        }
    }
}

function getTheme() {
    return localStorage.getItem("color-theme");
}
