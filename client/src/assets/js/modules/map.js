import {get} from "./api.js";
import {getHeatMapData} from "./datafetcher.js";
import {removeClass, removeClassAfterClick, setPosition, searchDome} from "./helper.js";

const MBURL = 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}';
const mbAttr = '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> ';
let map;
export const MAPBOUNDS = {
    north: -18,
    east: -66,
    south: -27,
    west: -70
};

export function createMap(position, isCurrentPosition) {
    const southWest = L.latLng(MAPBOUNDS.south, MAPBOUNDS.west);
    const northEast = L.latLng(MAPBOUNDS.north, MAPBOUNDS.east);
    const bounds = L.latLngBounds(southWest, northEast);

    const osm = L.tileLayer(MBURL, {
        id: "map",
        attribution: mbAttr,
        maxZoom: 20
    });
    map = L.map('map', {
        maxBounds: bounds,
        maxZoom: 17,
        minZoom: 10,
        layers: [osm],
        zoomControl: false
    }).setView(position, 11);

    const baseLayers = {
        "OpenStreetMap": osm
    };
    const overlays = {};
    const layerControl = L.control.layers(baseLayers, overlays).addTo(map);
    makeLayers(isCurrentPosition, layerControl, position);
}

function makeLayers(isCurrentPosition, layerControl, position) {
    createDomes();
    if (isCurrentPosition) {
        createPersonPin(layerControl, position);
    }
    createHeatMaps(layerControl);
    setZoomTopRight();
}

function createHeatMaps(layerControl) {
    getHeatMapData(succesHandler);

    function succesHandler(data) {
        data.forEach(heatmap => {
            const heatLayer = L.heatLayer(heatmap.data, {
                radius: 25,
                minOpacity: 0.4,
                gradient: {
                    '0.00': 'rgb(255,0,255)',
                    '0.25': 'rgb(0,0,255)',
                    '0.50': 'rgb(0,255,0)',
                    '0.75': 'rgb(255,255,0)',
                    '1.00': 'rgb(255,0,0)'
                }
            });
            layerControl.addOverlay(heatLayer, heatmap.title);
        });
    }
}

function createPersonPin(layerControl, location) {
    const personPinIcon = L.icon({
        iconUrl: 'assets/media/person_pin.png',
        iconSize: [50, 50]
    });
    const personPinMarker = L.marker(location, { icon: personPinIcon }).bindPopup('You are here' + location);
    personPinMarker.addTo(map);
}

function setZoomTopRight() {
    L.control.zoom({
        position: "topright"
    }).addTo(map);
}

function createDomes() {
    get("domes", function(response) {
        response.json().then(data => createDome(data.domes));
    });
}

function createDome(data) {
    data.forEach(dome => {
        const icon = L.icon({
            iconUrl: "assets/media/blackdome.png",
            iconSize: [50, 50],
            className: "dome"
        });
        const domeMarker = L.marker([dome.latitude, dome.longitude], { icon: icon }).bindPopup(`this is ${dome.domeName}`);
        domeMarker.addTo(map);
        domeMarker._icon.id = dome.domeName;
    });
    selectedOnClick();
}

function selectedOnClick() {
    document.querySelectorAll(".dome").forEach(dome =>{
        dome.addEventListener("click", function (e) {
            removeClass(".selected", "selected");
            const target = e.target;
            target.classList.add("selected");
            removeClassAfterClick(".selected", "selected");
            createCircle(target.id);
        });
    });
}

function createCircle(domeName) {
    const $oldCircle = document.querySelector(".circle");
    if ($oldCircle) {
        $oldCircle.remove();
    }

    searchDome(domeName, succesHandler);
    function succesHandler(dome) {
        const radius = Math.sqrt(dome.surface / Math.PI);
        const circle = L.circle([dome.latitude, dome.longitude], {
            color: "grey",
            fillColor: "grey",
            fillOpacity: 0.3,
            radius: radius,
            className: "circle"
        }).addTo(map);
    }
}

export function setView(position) {
    map.setView(position, 10);
}

export function center(currentPosition) {
    const coords = currentPosition.coords;
    const lat = coords.latitude;
    const lon = coords.longitude;
    const position = setPosition([lat, lon]);
    map.setView(position, 10);
}
