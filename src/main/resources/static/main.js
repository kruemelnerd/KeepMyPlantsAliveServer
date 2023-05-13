// REST-API-URL
const apiUrl = "http://localhost:8080/api/data";

// Daten von der REST-API abrufen
fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
        console.log(data)
        // Daten für das Liniendiagramm vorbereiten
        const labels = data.map(item => item.dateTime);
        const soilMoistureData = data.map(item => item.soilMoisture);


        // Liniendiagramm erstellen
        const ctx = document.getElementById("myChart").getContext("2d");


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
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Chart.js Line Chart'
                    }
                }
            }
        });
/*
        const chart = {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: "Bodenfeuchtigkeit",
                        data: soilMoistureData,
                        borderColor: "rgba(75, 192, 192, 1)",
                        backgroundColor: "rgba(75, 192, 192, 0.2)"
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Chart.js Line Chart'
                    }
                }
            },
        };
*/
        /*const chart = new Chart(ctx, {
            type: "line",
            data: {
                labels: labels,
                datasets: [
                    {
                        label: "Bodenfeuchtigkeit",
                        data: soilMoistureData,
                        borderColor: "rgba(75, 192, 192, 1)",
                        backgroundColor: "rgba(75, 192, 192, 0.2)"
                    }
                ]
            },
            options: {
                scales: {
                    x: {
                        type: "time",
                        time: {
                            unit: "second"
                        }
                    }
                }
            }
        });*/
    })
    .catch(error => console.error("Fehler beim Abrufen der API-Daten:", error));
