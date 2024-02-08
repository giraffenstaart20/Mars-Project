import {
    getBarChartData, getPieChartData, getLineChartData
} from "./datafetcher.js";
import {makeHidden, removeHidden, searchDome} from "./helper.js";

const FONTCOLOR = "hsl(142deg, 10%, 75%)";
const PIECHARTCOLORSET = {
    light: ["#663926", "#6b4c26", "#724b2f", "#734c30", "#8b5742", "#8c5843", "#8c5d44", "#8d5e45", "#9b6e3e", "#9b7a5c", "#9c7a5a", "#9d6f3f", "#a5876d", "#a67c52", "#a69071", "#a78c53", "#a7987b", "#a89a7c", "#a67c52", "#b38f6b", "#bc9f6f"],
    dark:   ["#2e2e2e", "#3f3f3f", "#4d4d4d", "#4e4e4e", "#5d5d5d", "#5e5e5e", "#6d6d6d", "#6e6e6e", "#7d7d7d", "#7e7e7e", "#8d8d8d", "#8e8e8e", "#9d9d9d", "#9e9e9e", "#aeaeae", "#bebebe", "#cecece", "#dfdfdf", "#efefef", "#ffffff"]
};
const LINECHARTCOLORSET = ["blue", "red", "orange", "green", "pink"];
const PRIMARYCOLOR = {
    light: "#3d2117",
    dark: "#1f1f1f"
};
const SECONDARYCOLOR = {
    light: "#C67B5C",
    dark: "#8a8a8a"
};
const SIDEVALUE = {
    revenue: "Revenue",
    profit: "Profit",
    costs: "Costs",
    employees: "Amount of employees",
    sales: "Sales",
    crimes: "Amount of crimes",
    oxygen_leaks: "Amount of oxygen leaks",
    population: "population",
    medical_dispaches : "Amount of medical dispaches",
    meteor_showers: "Amount of of meteor showers",
    dust_storms: "Amount of dust storms"
};

export function createBarChart() {
    removeHidden("#bar-chart-container .spinner-wave-in");
    deleteOldChart("bar-chart", "bar-chart-container");
    const period = document.querySelector("#period").value;
    const category = document.querySelector("aside .selected").id;
    getBarChartData(category, period, makeBarChart);
}

function makeBarChart(data) {
    const sideValue = getSideValue();
    const theme = getTheme();

    const ctx = document.querySelector("#bar-chart").getContext('2d');
    const configuration = {
        type: 'bar',
        data: {
            datasets: [{
                data: data,
            }]
        },
        options: {
            backgroundColor: PRIMARYCOLOR[theme],
            hoverBackgroundColor: SECONDARYCOLOR[theme],
            borderRadius: 10,
            scales: {
                y: {
                    title: {
                        display: true,
                        text: sideValue,
                        font: {
                            weight: "bold",
                        },
                        color: FONTCOLOR
                    },
                    ticks: {
                        color: FONTCOLOR
                    },
                    suggestedMin: 0
                },
                x: {
                    title: {
                        display: true,
                        text: "Domes",
                        font: {
                            weight: "bold",
                        },
                        color: FONTCOLOR,
                        padding: {
                            bottom: 20
                        }
                    },
                    ticks: {
                        color: FONTCOLOR
                    }
                }
            },
            plugins: {
                legend: { display: false, }
            },
            onClick: changePieChart,
            maintainAspectRatio: false
        }
    };
    makeHidden("#bar-chart-container .spinner-wave-in");
    new Chart(ctx, configuration);
    return Chart;
}

export function createPieChart() {
    removeHidden("#pie-chart-container .spinner-wave-in");
    deleteOldChart("pie-chart", "pie-chart-container");
    const domeId = document.querySelector("#dome-choice h3").id;
    const category = document.querySelector("aside .selected").id;
    const period = document.querySelector("#period").value;
    getPieChartData(category, period, domeId, makePieChart);
}

function makePieChart(data) {
    const theme = getTheme();

    const ctx = document.querySelector('#pie-chart').getContext('2d');
    const configuration = {
        type: 'doughnut',
        data: {
            labels: Object.keys(data),
            datasets: [{
                data: Object.values(data),
                backgroundColor: PIECHARTCOLORSET[theme]
            }]
        },
        options: {

            plugins: {
                legend: {
                    maxWidth: 270,
                    position: "left",
                    labels: {
                        boxHeight: 20,
                        font: {
                            size: 15
                        },
                        padding: 15,
                        color: FONTCOLOR
                    }

                }
            },
            maintainAspectRatio: false
        },
    };
    makeHidden("#pie-chart-container .spinner-wave-in");
    new Chart(ctx, configuration);
}

export function createLineChart() {
    const category = document.querySelector("aside .selected").id;
    const years = getYears();
    const data =  getLineChartData(category, years);
    makeLineChart(data);
}

function makeLineChart(data) {
    deleteOldChart("line-chart", "line-chart-container");

    const sideValue = getSideValue();
    const datasets = getDataSets(data);

    const ctx = document.querySelector('#line-chart').getContext('2d');
    const configuration = {
        type: "line",
        data: {
            labels: Object.keys(data[0]),
            datasets: datasets
        },
        options: {
            pointRadius: 4,
            plugins: {
                legend: {
                    labels: {
                        title: {
                            text: 2022
                        },
                        font: {
                            weight: "bold"
                        },
                        color: FONTCOLOR,
                        padding: 10
                    }
                }
            },
            scales: {
                y: {
                    title: {
                        display: true,
                        text: sideValue,
                        font: {
                            weight: "bold"
                        },
                        color: FONTCOLOR
                    },
                    ticks: {
                        color: FONTCOLOR
                    },
                    suggestedMin: 0
                },
                x: {
                    title: {
                        display: true,
                        text: "Months",
                        font: {
                            weight: "bold"
                        },
                        color: FONTCOLOR
                    },
                    ticks: {
                        color: FONTCOLOR
                    }
                }
            },

        },
        maintainAspectRatio: false
    };
    new Chart(ctx, configuration);
}

function getYears() {
    const res = [];
    document.querySelectorAll("#years input").forEach(year => {
        if (year.checked) {
            res.push(year.id);
        }
    });
    return res;
}


function getSideValue() {
    let category = document.querySelector("aside .selected").id;
    category = category.replace("-", "_");
    return SIDEVALUE[category];
}

function getDataSets(data) {
    const res = [];
    const theme = localStorage.getItem("color-theme");
    for (let i = 0; i < data.length; i++) {
        const chart = {
            backgroundColor: LINECHARTCOLORSET[i],
            borderColor: LINECHARTCOLORSET[i],
            borderDash: [5, 5],
            pointBackgroundColor: PRIMARYCOLOR[theme],
            pointBorderColor: PRIMARYCOLOR[theme],
            pointHoverBackgroundColor: SECONDARYCOLOR[theme],
            pointHoverBorderColor: SECONDARYCOLOR[theme],
            label: getYears()[i],
            data: data[i]
        };
        res.push(chart);
    }
    return res;
}

function changePieChart(event, elements) {
    if (elements.length > 0) {
        const clickedElement = this.getElementsAtEventForMode(event, "nearest", {intersect: true}, true);
        const index = clickedElement[0].index;

        searchDome(index, succesHandler);
        function succesHandler(dome) {
            const $target = document.querySelector("#types_chart h3");
            $target.innerText = dome.domeName;
            $target.id = index;
            createPieChart();
        }
    }
}

function deleteOldChart(chartId, parentId) {
    const $oldChart = document.querySelector("#" + chartId);
    if ($oldChart) {
        $oldChart.remove();
    }
    const html = `<canvas id="${chartId}"></canvas>`;
    const $target = document.querySelector("#" + parentId);
    $target.innerHTML += html;
}

function getTheme() {
    return localStorage.getItem("color-theme");
}
