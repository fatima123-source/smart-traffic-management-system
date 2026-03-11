/* pour kafka non socket tcp 
package com.traffic.noise;

import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class NoiseSimulator {

   public NoiseSimulator() {}

   public static void main(String[] args) {
      Properties props = new Properties();

      try {
         props.load(NoiseSimulator.class.getClassLoader()
               .getResourceAsStream("kafka/producer-config.properties"));
      } catch (Exception e) {
         e.printStackTrace();
         return;
      }

      KafkaProducer<String, String> producer = new KafkaProducer<>(props);
      Random random = new Random();

      // On définit les routes disponibles
      int[] routeIds = {1, 2, 3, 4, 5};

      while(true) {
         // Choisir une route aléatoirement
         int routeIndex = random.nextInt(routeIds.length);
         int routeId = routeIds[routeIndex];

         // Générer le bruit
         int noiseLevel = 50 + random.nextInt(51);

         // Créer le message avec la route
         String message = "Route " + routeId + ": Noise level " + noiseLevel + " dB";

         ProducerRecord<String, String> record = new ProducerRecord<>("noise-topic", message);
         producer.send(record);
         System.out.println("Sent: " + message);

         try {
            Thread.sleep(10000L); // 10 secondes
         } catch (InterruptedException e) {
            producer.close();
            return;
         }
      }
   }
}*/

/*package com.traffic.noise;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class NoiseSimulator {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 5000;

        Random random = new Random();

        try {

            Socket socket = new Socket(host, port);

            PrintWriter writer =
                    new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to Central System");

            int[] routes = {1,2,3,4,5};

            while (true) {

                int routeId = routes[random.nextInt(routes.length)];
                int noise = 50 + random.nextInt(51);

                String message =
                        "Route " + routeId + ": Noise level " + noise + " dB";

                writer.println(message);

                System.out.println("Sent: " + message);

                Thread.sleep(10000);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/


package com.traffic.noise;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class NoiseSimulator {

    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Random random = new Random();

            System.out.println("Connected to Central System");

            while (true) {

                int route = random.nextInt(5) + 1;
                int noise = random.nextInt(50) + 50;

                String message = "Route " + route + ": Noise level " + noise + " dB";

                out.println(message);

                System.out.println("Sent: " + message);

                Thread.sleep(3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}