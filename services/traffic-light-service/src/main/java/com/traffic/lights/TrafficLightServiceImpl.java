package trafficlight;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class TrafficLightServiceImpl {

    @WebMethod
    public String getTrafficDecision(int route_id) {

        int vehicles = TrafficDataDAO.getVehicleCount(route_id);
        int noise = TrafficDataDAO.getNoiseLevel(route_id);
        int pollution = TrafficDataDAO.getPollutionLevel(route_id);

        // Afficher dans la console pour vérifier
        System.out.println("Intersection " + route_id +
                           " | Vehicles: " + vehicles +
                           ", Noise: " + noise +
                           ", Pollution: " + pollution);


        String decision = DecisionEngine.makeDecision(
                vehicles,
                noise,
                pollution
        );

        return decision;
    }
}