// =========================
// Menu et Sections
// =========================
function showSection(id) {
    document.querySelectorAll(".section").forEach(s => s.classList.add("hidden"));
    const section = document.getElementById(id);
    if(section) section.classList.remove("hidden");

    if(id === "alerts") showAlertes();

    // 🔥 AJOUT
    if(id === "traffic") loadTraffic();
    if(id === "pollution") loadPollution();
}
showSection('dashboard'); // section par défaut

// =========================
// ALERTES DATA
// =========================
const alertesData = [
    { type_alerte: "Bruit eleve", route_id: 1, timestamp: "2026-03-10 04:54:42", action_recommandee: "Rduire trafic ou avertir conducteurs" },
    { type_alerte: "Bruit eleve", route_id: 3, timestamp: "2026-03-10 04:55:02", action_recommandee: "Rduire trafic ou avertir conducteurs" },
    { type_alerte: "Accident", route_id: 5, timestamp: "2026-03-11 11:01:47", action_recommandee: "Devier le trafic vers une autre route" },
    { type_alerte: "Pollution", route_id: 1, timestamp: "2026-03-14 23:12:53", action_recommandee: "Limiter la circulation sur cette route" }
];

// =========================
// Cards aléatoires
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
setInterval(updateCards, 20000);

// =========================
// Tableau et Graphiques Bruit
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
        console.log("Charts aléatoires OK");

        // Générer heures (ex: 10 points)
        function generateLabels() {
            let labels = [];
            for(let i = 0; i < 10; i++){
                labels.push("T" + (i+1));
            }
            return labels;
        }

        function generateData() {
            let data = [];
            for(let i = 0; i < 10; i++){
                data.push(Math.floor(Math.random() * 100) + 20);
            }
            return data;
        }

        function createChart(canvasId, label) {
            const ctx = document.getElementById(canvasId).getContext("2d");

            return new Chart(ctx, {
                type: "line",
                data: {
                    labels: generateLabels(),
                    datasets: [{
                        label: label,
                        data: generateData(),
                        borderColor: "#2563eb",
                        backgroundColor: "rgba(37, 99, 235, 0.3)",
                        fill: true,
                        tension: 0.4
                    }]
                }
            });
        }
        let charts = [];

        function loadCharts(){
            charts.push(createChart("noiseChart1", "Route Nord"));
            charts.push(createChart("noiseChart2", "Route Sud"));
            charts.push(createChart("noiseChart3", "Route Est"));
            charts.push(createChart("noiseChart4", "Route Ouest"));
        }

        function updateCharts(){
            charts.forEach(chart => {
                chart.data.datasets[0].data = generateData();
                chart.update();
            });
        }

        loadCharts();
        setInterval(updateCharts, 20000); // toutes les 5 secondes
}
updateDashboard();

// =========================
// Dashboard (events + noise)
// =========================

async function updateDashboard() {
    const events = await getEvents();
    const filter = document.getElementById("filterType").value;
    const noiseData = await getNoiseData();

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

    if(noiseCharts.length === 0){ 
        const routes = [1,2,3,4];
        let grouped = {};
        routes.forEach(r => grouped[r] = []);

        noiseData.forEach(n => {
            if(grouped[n.route_id]){
                grouped[n.route_id].push(n);
            }
        });

        routes.forEach((routeId, i) => {
            const ctx = document.getElementById("noiseChart" + (i+1)).getContext("2d");
            const labels = grouped[routeId].map(d => d.timestamp.substring(11,16));
            const values = grouped[routeId].map(d => d.niveau_db);

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
                }
            });
        });
    }
}
updateDashboard();

// =========================
// ALERTES
// =========================
function showAlertes() {
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

// =========================
// 🔥 TRAFFIC (FAKE DATA)
// =========================
let trafficChart = null;

function loadTraffic() {

    const data = [
        { route_id: 1, nb_vehicules: 120 },
        { route_id: 2, nb_vehicules: 80 },
        { route_id: 3, nb_vehicules: 200 },
        { route_id: 4, nb_vehicules: 150 }
    ];

    const ctx = document.getElementById("trafficChart").getContext("2d");

    const labels = data.map(d => "Route " + d.route_id);
    const values = data.map(d => d.nb_vehicules);

    const total = values.reduce((a,b)=>a+b,0);

    document.getElementById("trafficTotal").innerHTML =
        `<h3>Total véhicules</h3><p>${total}</p>`;

    if(trafficChart) trafficChart.destroy();

    trafficChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Nombre de véhicules",
                data: values,
                backgroundColor: "#2563eb"
            }]
        }
    });
}

// =========================
// 🔥 POLLUTION (FAKE DATA)
// =========================
let pollutionChart = null;

function loadPollution() {

    const data = [
        { timestamp: "10:00", valeur: 50 },
        { timestamp: "10:10", valeur: 60 },
        { timestamp: "10:20", valeur: 45 },
        { timestamp: "10:30", valeur: 70 },
        { timestamp: "10:40", valeur: 55 }
    ];

    const ctx = document.getElementById("pollutionChart").getContext("2d");

    const labels = data.map(d => d.timestamp);
    const values = data.map(d => d.valeur);

    const avg = values.reduce((a,b)=>a+b,0) / values.length;

    document.getElementById("pollutionAvg").innerHTML =
        `<h3>Pollution moyenne</h3><p>${avg.toFixed(2)} AQI</p>`;

    if(pollutionChart) pollutionChart.destroy();

    pollutionChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                label: "Pollution",
                data: values,
                borderColor: "#dc2626",
                backgroundColor: "rgba(220,38,38,0.2)",
                fill: true,
                tension: 0.4
            }]
        }
    });
    function updateLights(lightDiv, color){
    if(!lightDiv || !color) return;
    let lights = lightDiv.querySelectorAll(".light");
    lights.forEach(l => l.classList.remove("active","red","yellow","green"));

    const c = color.toUpperCase();
    if(c==="RED") lights[0].classList.add("active","red");
    else if(c==="YELLOW") lights[1].classList.add("active","yellow");
    else if(c==="GREEN") lights[2].classList.add("active","green");
}

function loadTrafficLights(){
    fetch("http://localhost:8889/trafficLights?time="+Date.now())
    .then(res=>res.json())
    .then(data=>{
        data.forEach(item=>{
            const div = document.getElementById("light-"+item.route.toLowerCase());
            updateLights(div,item.light);
        });
    })
    .catch(err=>{
        console.error("API ERROR:",err);
        // fallback statique
        const fallback = [
            {"route":"centre","light":"RED"},
            {"route":"nord","light":"RED"},
            {"route":"sud","light":"RED"},
            {"route":"est","light":"GREEN"},
            {"route":"ouest","light":"GREEN"}
        ];
        fallback.forEach(item=>{
            const div = document.getElementById("light-"+item.route.toLowerCase());
            updateLights(div,item.light);
        });
    });
}

setInterval(loadTrafficLights,5000);
loadTrafficLights();
}