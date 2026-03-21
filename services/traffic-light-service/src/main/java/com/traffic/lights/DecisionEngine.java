package trafficlight;
public class DecisionEngine {

    public static String makeDecision(int vehicles, int noise, int pollution) {

        // priorité : sécurité environnementale
        if (noise > 70 || pollution > 100) {
            return "RED"; // trop dangereux
        }

        // trafic élevé
        if (vehicles > 100) {
            return "GREEN";
        }

        // trafic moyen
        if (vehicles > 50) {
            return "YELLOW";
        }

        // trafic faible
        return "RED";
    }
}