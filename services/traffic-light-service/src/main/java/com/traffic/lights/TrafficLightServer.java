package trafficlight;

import com.sun.net.httpserver.HttpServer;
import javax.xml.ws.Endpoint;
import java.net.InetSocketAddress;

public class TrafficLightServer {
    public static void main(String[] args) throws Exception {
        // Crée le serveur HTTP intégré sur le port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Crée ton service JAX-WS
        Endpoint endpoint = Endpoint.create(new TrafficLightServiceImpl());

        // Publie le service sur le contexte "/trafficLightService"
        endpoint.publish(server.createContext("/trafficLightService"));

        // Démarre le serveur
        server.start();
        System.out.println("Traffic Light Service Running at http://localhost:8080/trafficLightService?wsdl");
    }
}
