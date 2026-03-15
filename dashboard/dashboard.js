function showSection(id){

let sections=document.querySelectorAll(".section");

sections.forEach(section=>{
section.classList.add("hidden");
});

document.getElementById(id).classList.remove("hidden");

}


const routes=["centre","nord","sud","est","ouest"];

function updateData(){

let totalVehicles=0;
let totalNoise=0;
let totalPollution=0;

routes.forEach(route=>{

let vehicles=Math.floor(Math.random()*200);
let noise=Math.floor(Math.random()*90);
let pollution=Math.floor(Math.random()*100);

document.getElementById("veh-"+route).innerText=vehicles;
document.getElementById("noise-"+route).innerText=noise;
document.getElementById("pollution-"+route).innerText=pollution;

totalVehicles+=vehicles;
totalNoise+=noise;
totalPollution+=pollution;

if(pollution>80){
let alerts=document.getElementById("alertsList");
alerts.innerHTML+="<li>⚠ Pollution élevée dans "+route+"</li>";
}

});

document.getElementById("vehicles").innerText=totalVehicles;
document.getElementById("noiseValue").innerText=Math.floor(totalNoise/5)+" dB";
document.getElementById("pollutionValue").innerText=Math.floor(totalPollution/5)+" AQI";

}

setInterval(updateData,4000);



const trafficCtx=document.getElementById("trafficChart");

new Chart(trafficCtx,{
type:"line",
data:{
labels:["10:00","10:05","10:10","10:15","10:20"],
datasets:[{
label:"Vehicles",
data:[30,45,40,60,70]
}]
}
});


const pollutionCtx=document.getElementById("pollutionChart");

new Chart(pollutionCtx,{
type:"bar",
data:{
labels:["CO2","PM10","PM2.5"],
datasets:[{
label:"Pollution Level",
data:[40,55,35]
}]
}
});


var map=L.map('map').setView([33.5731,-7.5898],16);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
maxZoom:19
}).addTo(map);

L.marker([33.5731,-7.5898]).addTo(map)
.bindPopup("Smart Intersection");


var car=L.marker([33.5730,-7.5895]).addTo(map);

function moveCar(){

let lat=33.5730+(Math.random()*0.001);
let lng=-7.5895+(Math.random()*0.001);

car.setLatLng([lat,lng]);

}

setInterval(moveCar,3000);
