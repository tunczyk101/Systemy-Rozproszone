import queue.QueueIn;
import topic.ConsoleColors;
import topic.Listener;
import topic.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

public class Agency{

    private static Map<String, QueueIn> mapService() throws IOException, TimeoutException {
        Map<String, QueueIn> serviceMap = new HashMap<>();
        // services queues
        serviceMap.put("people", new QueueIn("spacex.carriers.people"));
        serviceMap.put("load", new QueueIn("spacex.carriers.load"));
        serviceMap.put("satellite", new QueueIn("spacex.carriers.satellite"));

        return serviceMap;
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println(ConsoleColors.CONFIG + "CONFIGURE AGENCY");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter agency ID:");
        String agencyID = reader.readLine();

        new Thread(new Listener("spacex.agencies." + agencyID)).start(); // info from carriers about finished service
        new Thread(new Listener("admin.agencies")).start(); // info for agencies from admin
        new Thread(new Listener("admin.all")).start(); // info for everyone from admin

        Writer adminWriter = new Writer();

        Map<String, QueueIn> serviceMap = mapService();

        System.out.println("AGENCY READY" + ConsoleColors.RESET);

        while (true){
            System.out.println(ConsoleColors.INFO + "Choose service: people / load / satellite");
            System.out.println("Format: service.data\n" + ConsoleColors.RESET);

            String chosenService = reader.readLine();

            if (chosenService.equals("exit")){
                System.out.println(ConsoleColors.EXIT + "EXIT" + ConsoleColors.RESET);
                break;
            }

            String[] service_parts = chosenService.split("\\.");

            if (service_parts.length != 2){
                System.out.println(ConsoleColors.INFO + "Incorrect msg format");
                System.out.println("Enter data after '.' or don't use '.' as part of data\n" + ConsoleColors.RESET);
                continue;
            }

            String service = service_parts[0];
            String data = service_parts[1];

            Optional.ofNullable(serviceMap.get(service)).ifPresentOrElse(
                    (serviceWriter)
                        ->
                        {
                            try {
                                String id = Integer.toString(ThreadLocalRandom.current().nextInt(0, 1000));
                                String msg = agencyID + "." + id + "." + data;

                                serviceWriter.send(msg);

                                adminWriter.send("admin", msg);

                                System.out.println("Sent: " + msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                    () ->
                            System.out.println(ConsoleColors.INFO + "Incorrect service name" + ConsoleColors.RESET));

        }
        System.out.println();
    }
}
