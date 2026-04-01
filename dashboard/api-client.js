const API_URL_EVENTS = "http://localhost:8080/api/events";
const API_URL_NOISE = "http://localhost:8080/api/noise";
const API_URL_ALERTES = "http://localhost:8080/api/alertes"; // 🔹 nouvel API
const API_URL_POLLUTION = "http://localhost:8080/api/pollution";
const API_URL_TRAFFIC = "http://localhost:8080/api/traffic";


async function getEvents() {
    try {
        const res = await fetch(API_URL_EVENTS);
        return await res.json();
    } catch (err) {
        console.error("Erreur récupération événements:", err);
        return [];
    }
}

async function getNoiseData() {
    try {
        const res = await fetch(API_URL_NOISE);
        return await res.json();
    } catch (err) {
        console.error("Erreur récupération bruit:", err);
        return [];
    }
}

async function getAlertes() {
    try {
        const res = await fetch(API_URL_ALERTES);
        return await res.json();
    } catch (err) {
        console.error("Erreur récupération alertes:", err);
        return [];
    }
}
async function getPollutionData() {
    try {
        const res = await fetch(API_URL_POLLUTION);
        return await res.json();
    } catch (err) {
        console.error("Erreur récupération pollution:", err);
        return [];
    }
}

async function getTrafficData() {
    try {
        const res = await fetch(API_URL_TRAFFIC);
        return await res.json();
    } catch (err) {
        console.error("Erreur récupération traffic:", err);
        return [];
    }
}