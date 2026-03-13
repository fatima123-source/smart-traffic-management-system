function showSection(id){

let sections=document.querySelectorAll(".section");

sections.forEach(section=>{
section.classList.add("hidden");
});

document.getElementById(id).classList.remove("hidden");

}


// TRAFFIC CHART

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


// POLLUTION CHART

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


// TRAFFIC LIGHT ANIMATION

const lights=document.querySelectorAll(".light");

let current=2;

function changeLight(){

lights.forEach(light=>light.classList.remove("active"));

current++;

if(current>2) current=0;

lights[current].classList.add("active");

}

setInterval(changeLight,3000);


// SIMULATED DATA

function updateData(){

let vehicles=Math.floor(Math.random()*200);
let noise=Math.floor(Math.random()*90);
let pollution=Math.floor(Math.random()*100);

document.getElementById("vehicles").innerText=vehicles;
document.getElementById("noiseValue").innerText=noise+" dB";
document.getElementById("pollutionValue").innerText=pollution+" AQI";

}

setInterval(updateData,4000);


// MAP

var map=L.map('map').setView([33.5731,-7.5898],16);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
maxZoom:19
}).addTo(map);


// TRAFFIC LIGHT MARKER

L.marker([33.5731,-7.5898]).addTo(map)
.bindPopup("Traffic Light");


// CAR SIMULATION

var car=L.marker([33.5730,-7.5895]).addTo(map);

function moveCar(){

let lat=33.5730+(Math.random()*0.001);
let lng=-7.5895+(Math.random()*0.001);

car.setLatLng([lat,lng]);

}

setInterval(moveCar,3000);
