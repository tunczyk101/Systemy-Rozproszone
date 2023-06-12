import queue.QueueOut;
import topic.ConsoleColors;
import topic.Listener;
import topic.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class Carrier implements Runnable{
    private List<String> availableServices = new ArrayList<>();
    private String id = null;
    private static final Set<String> allServices = new HashSet<>() {{
        add("people");
        add("load");
        add("satellite");
    }};

    public Carrier(List<String> availableServices) throws IOException {

        if (availableServices.size() == 2
                && allServices.contains(availableServices.get(0))
                && allServices.contains(availableServices.get(1))
                && !availableServices.get(0).equals(availableServices.get(1)))
            this.availableServices = availableServices;
        else {
            addAvailableServices(new BufferedReader(new InputStreamReader(System.in)));
        }
    }

    public Carrier() {

    }
//    private String secondService;
    
    private void addAvailableServices(BufferedReader reader) throws IOException {
        String firstS;
        String secondS;

        while (availableServices.size() != 2){
            System.out.println(ConsoleColors.CONFIG + "Enter two chosen services: (from: people / load / satellite)" + ConsoleColors.RESET);
            firstS = reader.readLine();
            secondS = reader.readLine();

            if (allServices.contains(firstS) && allServices.contains(secondS) && !firstS.equals(secondS)){
                availableServices = new ArrayList<>();
                availableServices.add(firstS);
                availableServices.add(secondS);
            }
        }
    }

    private void configure() throws IOException {
        System.out.println(ConsoleColors.CONFIG + "CONFIGURE CARRIER");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter agency ID:");
        id = reader.readLine();
        
        if (availableServices.size() != 2)
            addAvailableServices(reader);

        System.out.println("CARRIER READY" + ConsoleColors.RESET);
    }

    private void processData(String data){

    }

    @Override
    public void run() {
        try {
            configure();

            Writer writer = new Writer();

            Consumer<String> consumer = msg ->
            {
                String[] service_parts = msg.split(":");
                if (service_parts.length != 3)
                    System.out.println(ConsoleColors.INFO + "Incorrect form from sender" + ConsoleColors.RESET);
                else {
                    String sender = service_parts[0];
                    String serviceId = service_parts[1];
                    String data = service_parts[2];

                    processData(data);

                    String response = id + ":" + serviceId + ":" + "service_finished";
                    String senderKey = "spacex.agencies." + sender;

                    try {
                        writer.send(senderKey, response);
                        writer.send("admin", response);

                        System.out.println("Sent: " + response + " to: " + senderKey + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            new Thread(new QueueOut("spacex.carriers." + availableServices.get(0), consumer)).start();
            new Thread(new QueueOut("spacex.carriers." + availableServices.get(1), consumer)).start();
            new Thread(new Listener("admin.carriers")).start();
            new Thread(new Listener("admin.all")).start();

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        List<String> serv = new ArrayList<>();
//        serv.add("satellite");
        serv.add("load");
        serv.add("people");

        Carrier carrier = new Carrier(serv);
        carrier.run();
    }

}
