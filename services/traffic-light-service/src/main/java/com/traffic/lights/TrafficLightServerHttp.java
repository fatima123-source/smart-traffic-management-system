package trafficlight;

import com.sun.net.httpserver.HttpServer;
import javax.xml.ws.Endpoint;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.util.*;

public class TrafficLightServerHttp {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8889), 0);

        // ==========================
        // SOAP SERVICE
        // ==========================
        Endpoint endpoint = Endpoint.create(new TrafficLightServiceImpl());
        endpoint.publish(server.createContext("/trafficLightService"));

        // ==========================
        // JSON API POUR LE DASHBOARD
        // ==========================
        server.createContext("/trafficLights", exchange -> {

            TrafficLightServiceImpl service = new TrafficLightServiceImpl();
            String[] routes = {"centre","nord","sud","est","ouest"};
            List<String> jsonItems = new ArrayList<>();

            for(int i=1; i<=5; i++){
                // 🔹 Récupère les données depuis la base
                int vehicles = TrafficDataDAO.getVehicleCount(i);
                int noise = TrafficDataDAO.getNoiseLevel(i);
                int pollution = TrafficDataDAO.getPollutionLevel(i);

                // 🔹 Debug console
                System.out.println("Route " + i + " ("+routes[i-1]+") | Vehicles: " + vehicles
                        + ", Noise: " + noise + ", Pollution: " + pollution);

                // 🔹 Décision du feu
                String decision = DecisionEngine.makeDecision(vehicles, noise, pollution);

                // 🔹 Construction manuelle du JSON
                String jsonItem = String.format("{\"route\":\"%s\",\"light\":\"%s\"}",
                        routes[i-1], decision);
                jsonItems.add(jsonItem);
            }

            // 🔹 JSON final
            String response = "[" + String.join(",", jsonItems) + "]";

            exchange.getResponseHeaders().add("Content-Type","application/json");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200,response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();

        System.out.println("SOAP Service: http://localhost:8889/trafficLightService?wsdl");
        System.out.println("JSON API: http://localhost:8889/trafficLights");
    }
}