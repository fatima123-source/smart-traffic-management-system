// =========================
// Menu et Sections
// =========================
function showSection(id) {
    document.querySelectorAll(".section").forEach(s => s.classList.add("hidden"));
    const section = document.getElementById(id);
    if(section) section.classList.remove("hidden");

    if(id === "alerts") showAlertes();
}
showSection('dashboard'); // section par défaut
const alertesData = [
    { type_alerte: "Bruit eleve", route_id: 1, timestamp: "2026-03-10 04:54:42", action_recommandee: "Rduire trafic ou avertir conducteurs" },
    { type_alerte: "Bruit eleve", route_id: 3, timestamp: "2026-03-10 04:55:02", action_recommandee: "Rduire trafic ou avertir conducteurs" },
    { type_alerte: "Accident", route_id: 5, timestamp: "2026-03-11 11:01:47", action_recommandee: "Devier le trafic vers une autre route" },
    { type_alerte: "Accident", route_id: 5, timestamp: "2026-03-11 11:01:57", action_recommandee: "Devier le trafic vers une autre route" },
    { type_alerte: "Pollution", route_id: 1, timestamp: "2026-03-14 23:12:53", action_recommandee: "Limiter la circulation sur cette route" },
    { type_alerte: "Traffic", route_id: 1, timestamp: "2026-03-15 00:40:09", action_recommandee: "Optimiser les feux de circulation" },
    { type_alerte: "Traffic", route_id: 2, timestamp: "2026-03-15 00:40:13", action_recommandee: "Optimiser les feux de circulation" }
    // ... tu peux rajouter le reste
];
// =========================
// Cards aléatoires (MAJ toutes les 20s)
// =========================
function updateCards() {
    document.getElementById("cardVehicles").innerHTML =
        "<h3>Total Vehicles</h3><p>" + Math.floor(Math.random()*500) + "</p>";

    document.getElementById("cardPollution").innerHTML =
        "<h3>Pollution</h3><p>" + Math.floor(Math.random()*100) + " AQI</p>";

    document.getElementById("cardNoise").innerHTML =
        "<h3>Noise</h3><p>" + Math.floor(Math.random()*120) + " dB</p>";
}
updateCards();
setInterval(updateCards, 20000); // 🔹 toutes les 20 secondes

// =========================
// Tableau et Graphiques Bruit (DATA RÉELLE)
// =========================
let noiseCharts = [];

async function updateDashboard() {
    const events = await getEvents();
    const filter = document.getElementById("filterType").value;
    const noiseData = await getNoiseData();

    // ========== Tableau ========== 
    const tbody = document.querySelector("#eventsTable tbody");
    tbody.innerHTML = "";
    events.filter(e => filter==="All" || e.type_evenement===filter)
          .slice(-10).reverse()
          .forEach(e => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${e.type_evenement}</td>
            <td>${e.route_id}</td>
            <td>${e.description}</td>
            <td>${e.timestamp}</td>
        `;
        tbody.appendChild(tr);
    });

    // =========================
    // Graphiques Bruit (chargement unique)
    // =========================
    if(noiseCharts.length === 0){ 
        const routes = [1,2,3,4];
        let grouped = {};
        routes.forEach(r => grouped[r] = []);

        const now = new Date();
        const oneHourAgo = new Date(now.getTime() - 60*60*1000);
        noiseData.forEach(n => {
            const ts = new Date(n.timestamp);
            if(ts >= oneHourAgo && grouped[n.route_id]){
                grouped[n.route_id].push(n);
            }
        });

        for (let r in grouped){
            grouped[r].sort((a,b) => new Date(a.timestamp) - new Date(b.timestamp));
        }

        routes.forEach((routeId, i) => {
            const ctx = document.getElementById("noiseChart" + (i+1)).getContext("2d");
            const dataRoute = grouped[routeId];
            const labels = dataRoute.map(d => d.timestamp.substring(11,16));
            const values = dataRoute.map(d => d.niveau_db);

            noiseCharts[i] = new Chart(ctx, {
                type: "line",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "Route " + routeId,
                        data: values,
                        borderColor: "#2563eb",
                        backgroundColor: "rgba(37, 99, 235, 0.3)",
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        x: { title: { display: true, text: "Temps" } },
                        y: { beginAtZero: true, title: { display: true, text: "dB" } }
                    }
                }
            });
        });
    }
}
updateDashboard();

// =========================
// Section ALERTES
function showAlertes() {
    console.log("Affichage manuel des alertes"); // Vérifie que ça s'exécute
    const container = document.getElementById("alertesContainer");
    container.innerHTML = "";

    alertesData.forEach(a => {
        const div = document.createElement("div");
        div.classList.add("alert-card");
        div.innerHTML = `
            <h3>Alerte: ${a.type_alerte} (Route ${a.route_id})</h3>
            <p>Heure: ${a.timestamp}</p>
            <p class="recommandation">Recommandation: ${a.action_recommandee}</p>
        `;
        container.appendChild(div);
    });
}