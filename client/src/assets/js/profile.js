import {makeHidden, removeHidden, setColorScheme} from "./modules/helper.js";
import {eventListenerPopup, hidePopup, showPopup} from "./modules/popup.js";
import {get, post, setApi, remove} from "./modules/api.js";

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
  defaultDate();
  displayAppointments();
  setColorScheme();
  eventListenerPopup();
}

function handleEventListeners() {
  document.querySelector("form").addEventListener("submit", function (e) {
    e.preventDefault();
    submitAppointment();
  });
}

function defaultDate() {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  document.querySelector("#date").valueAsDate = tomorrow;
}

function displayAppointments() {
  removeHidden(".spinner-wave-in");
  const $target = document.querySelector("#appointments");
  if ($target) {
    $target.innerHTML = "";
  }
  get("appointments", succesHandler);
  function succesHandler(res) {
    res.json().then(data => {
      const appointments = data.appointments;
      appointments.forEach(appointment => {
        const clone = document.querySelector("#clone-appointment").cloneNode(true);
        clone.classList.remove("hidden");
        clone.id = appointment.id;
        clone.querySelector(".subject").innerText = appointment.topic;
        clone.querySelector(".date").innerText = appointment.date;
        clone.querySelector(".time").innerText = appointment.time;
        clone.querySelector(".expertise").innerText = appointment.expertise;
        clone.querySelector(".employee").innerText = appointment.employeeName;
        clone.querySelector(".delete").addEventListener("click", deleteAppointment);
        $target.appendChild(clone);
      });
      makeHidden(".spinner-wave-in");
    });
  }
}

function deleteAppointment(e) {
  e.preventDefault();
  const $appointment = e.target.closest(".appointment");
  const id = $appointment.id;

  showPopup(e, "delete");
  document.querySelector("#popup-delete #no").addEventListener("click", hidePopup);
  document.querySelector("#popup-delete #yes").addEventListener("click", function (e) {
    e.preventDefault();
    hidePopup(e);
    showPopup(e, "deleted");
    remove(`appointment/${id}`, displayAppointments);
  });
}

function submitAppointment(e) {
  const date = document.querySelector("#date").value;
  const time = document.querySelector("#time").value;
  const expertise = document.querySelector("#expertise").value;
  const employee = document.querySelector("#employee").value;
  const subject = document.querySelector("#subject").value;

  const body = {
    "date": date,
    "time": time,
    "topic": subject,
    "employee_name": employee,
    "expertise": expertise
  };

  const valid = checkBodyValid(body);
  if (valid) {
    changeValuesPopup(body);
    post("appointment", body, displayAppointments);
    showPopup(e, "submit");
  } else {
    showPopup(e, "failed");
  }
}

function checkBodyValid(body) {
  let res = true;
  let message = "<p>";

  if (new Date(body.date) <= new Date()) {
    message += "- You can't book an appointment in the past or today.<br/>";
    res = false;
  }
  if (!body.topic) {
    message += "- Please fill in the subject.<br/>";
    res = false;
  }
  if (!/^([0-1]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$/.test(body.time)) {
    message += "- Please fill in the time in the correct form HH:MM (ex: 12:00).<br/>";
    res = false;
  }

  message += "</p>";
  const $errorMessage = document.querySelector("#popup-failed .content");
  $errorMessage.innerHTML =  message;
  return res;
}

function changeValuesPopup(body) {
  const $target = document.querySelector("#popup-submit");
  $target.querySelector(".date").innerText = body.date;
  $target.querySelector(".time").innerText = body.time;
  $target.querySelector(".subject").innerText = body.topic;
  $target.querySelector(".employee").innerText = body.employee_name;
  $target.querySelector(".expertise").innerText = body.expertise;
}

loadConfig();
