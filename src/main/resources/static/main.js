// REST-API-URL
const apiUrl = "http://192.168.178.33:8080/api/data";

Chart.register(ChartDataLabels);

fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
        console.log(data);

        //const deviceName = data[data.length - 1].deviceName;
        const labels = data[data.length - 1].dateTime
        const soilMoistureData = data[data.length - 1].soilMoisture;

        console.log("labels" + labels + " soilMoistureData: " + soilMoistureData);

        var chartProgress = document.getElementById("chartProgress");
        if (chartProgress) {
            new Chart(chartProgress, {
                type: 'doughnut',
                width: chartProgress.width,
                height: chartProgress.height,
                data: {
                    labels: ["Bodenfeuchtigkeit", "-"],
                    datasets: [
                        {
                            data: [soilMoistureData, 100 - soilMoistureData],
                            fill: true,
                            borderColor: ["rgba(75, 192, 192, 1)", 'rgba(208,208,208,0.22)'],
                            backgroundColor: ["rgba(75, 192, 192, 0.2)", 'rgba(208,208,208,0.22)'],
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                },
                plugins: {
                    datalabels: {
                        formatter: (value) => {
                            return value + '%';
                        },
                    },
                },
            });
        }
    })
    .catch(error => console.error("Fehler beim Abrufen der API-Daten:", error));


// Daten von der REST-API abrufen
fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
        console.log(data)
        // Daten für das Liniendiagramm vorbereiten
        const labels = data.map(item => item.dateTime);
        const soilMoistureData = data.map(item => item.soilMoisture);

        // Liniendiagramm erstellen
//        const ctx = document.getElementById("myChart").getContext("2d");
        const ctx = document.getElementById("myChart");
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: "Bodenfeuchtigkeit",
                        data: soilMoistureData,
                        fill: true,
                        borderColor: "rgba(75, 192, 192, 1)",
                        backgroundColor: "rgba(75, 192, 192, 0.2)"
                    }
                ]
            },
            options: {
                animation: true,
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Prozent'
                        }
                    },
                    x: {
                        type: 'time',
                        time: {
                            unit: 'minute',
                            displayFormats: {
                                quarter: 'MMM YYYY'
                            }
                        },
                        title: {
                            display: true,
                            text: 'Datum'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Übersicht über alle Pflanzen im Hause'
                    },
                    datalabels: {
                        labels: {
                            title: null,
                        }
                    }
                }
            }
        });
    })
    .catch(error => console.error("Fehler beim Abrufen der API-Daten:", error));
