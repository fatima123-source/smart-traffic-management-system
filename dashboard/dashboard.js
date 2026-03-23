// =========================
// Smart Intersection Dashboard JS
// =========================

// =========================
// Section management
// =========================
function showSection(id){
    let sections = document.querySelectorAll(".section");
    sections.forEach(section => section.classList.add("hidden"));
    document.getElementById(id).classList.remove("hidden");
}

// =========================
// Routes & data
// =========================
const routes = ["centre","nord","sud","est","ouest"];

function updateData(){
    let totalVehicles = 0;
    let totalNoise = 0;
    let totalPollution = 0;
    const alerts = document.getElementById("alertsList");
    alerts.innerHTML = ""; // reset alerts

    routes.forEach(route => {
        // Simule des données pour le dashboard (ou lire depuis ton serveur si tu veux)
        let vehicles = Math.floor(Math.random()*200);
        let noise = Math.floor(Math.random()*90);
        let pollution = Math.floor(Math.random()*100);

        document.getElementById("veh-"+route).innerText = vehicles;
        document.getElementById("noise-"+route).innerText = noise;
        document.getElementById("pollution-"+route).innerText = pollution;

        totalVehicles += vehicles;
        totalNoise += noise;
        totalPollution += pollution;

        // Alertes pollution
        if(pollution > 80){
            alerts.innerHTML += `<li>⚠ Pollution élevée dans ${route}</li>`;
        }
    });

    document.getElementById("vehicles").innerText = totalVehicles;
    document.getElementById("noiseValue").innerText = Math.floor(totalNoise/5) + " dB";
    document.getElementById("pollutionValue").innerText = Math.floor(totalPollution/5) + " AQI";
}

// Rafraîchit les données toutes les 4 secondes
setInterval(updateData, 4000);
updateData();

// =========================
// Charts
// =========================
const trafficCtx = document.getElementById("trafficChart");
new Chart(trafficCtx, {
    type: "line",
    data: {
        labels: ["10:00","10:05","10:10","10:15","10:20"],
        datasets: [{
            label: "Vehicles",
            data: [30,45,40,60,70],
            borderColor: "blue",
            tension: 0.3
        }]
    }
});

const pollutionCtx = document.getElementById("pollutionChart");
new Chart(pollutionCtx, {
    type: "bar",
    data: {
        labels: ["CO2","PM10","PM2.5"],
        datasets: [{
            label: "Pollution Level",
            data: [40,55,35],
            backgroundColor: ["red","orange","green"]
        }]
    }
});

// =========================
// Map + vehicles
// =========================
var map = L.map('map').setView([33.5731,-7.5898],16);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
    maxZoom:19,
    attribution:'&copy; OpenStreetMap contributors'
}).addTo(map);

// Intersection marker
var intersectionMarker = L.marker([33.5731,-7.5898],{
    title: "Smart Intersection",
    riseOnHover:true
}).addTo(map).bindPopup("<b>Smart Intersection</b><br>Traffic Control Center");

// Zone de surveillance
L.circle([33.5731,-7.5898],{
    color:'blue',
    fillColor:'#3399ff',
    fillOpacity:0.1,
    radius:100
}).addTo(map);

// Crée plusieurs véhicules
var vehicles = [];
for(let i=0;i<5;i++){
    let lat = 33.5730 + (Math.random()*0.001-0.0005);
    let lng = -7.5895 + (Math.random()*0.001-0.0005);
    let car = L.marker([lat,lng],{
        icon: L.icon({
            iconUrl:'https://cdn-icons-png.flaticon.com/512/743/743920.png',
            iconSize:[32,32],
            iconAnchor:[16,16]
        }),
        title:`Vehicle ${i+1}`
    }).addTo(map).bindPopup(`Vehicle ${i+1}`);
    vehicles.push(car);
}

// Déplacement aléatoire des véhicules
function moveVehicles(){
    vehicles.forEach(car=>{
        let lat = 33.5730 + (Math.random()*0.001-0.0005);
        let lng = -7.5895 + (Math.random()*0.001-0.0005);
        car.setLatLng([lat,lng]);
    });
}
setInterval(moveVehicles,2000);

// =========================
// Feux dynamiques  (DOHA)
// =========================
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
