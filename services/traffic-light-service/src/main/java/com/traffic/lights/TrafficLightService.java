package trafficlight;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface TrafficLightService {

    @WebMethod
    String getTrafficDecision(int intersectionId);

}