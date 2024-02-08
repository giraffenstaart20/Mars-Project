export function eventListenerPopup() {
    document.querySelectorAll(".help").forEach(help => {
        help.addEventListener("click", showPopup);
    });

    document.querySelectorAll(".popup .close").forEach(close => {
        close.addEventListener("click", hidePopup);
    });
}

export function showPopup(e, id) {
    let popupId;
    if (id) {
        popupId = id;
    }
    else {
        popupId = e.target.dataset.help;
    }
    const $popup = document.querySelector(`#popup-${popupId}`);
    $popup.classList.remove("hidden");
    $popup.classList.remove("slide-out");
    $popup.classList.add("slide-in");
    addBlur();
}

export function hidePopup(e) {
    const $target = e.target;
    const $popup = $target.closest(".popup");
    $popup.classList.remove("slide-in");
    $popup.classList.add("slide-out");
    removeBlur();
}

function addBlur() {
    const $body = document.querySelector("body");
    $body.classList.add("blur");
}

function removeBlur() {
    const $body = document.querySelector("body");
    $body.classList.remove("blur");
}
