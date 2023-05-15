// REST-API-URL
const apiUrl = "http://192.168.178.33:8080/api/data";

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
                animation : true,
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
                    }
                }
            }
        });
    })
    .catch(error => console.error("Fehler beim Abrufen der API-Daten:", error));
