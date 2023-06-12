import topic.ConsoleColors;
import topic.Listener;
import topic.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class Admin {

    public static void main(String[] args) throws IOException, TimeoutException {
        Map<String, String> keymap = new HashMap<>();
        keymap.put("agencies", "admin.agencies");
        keymap.put("carriers", "admin.carriers");
        keymap.put("all", "admin.all");

        new Thread(new Listener("admin")).start();
        Writer writer = new Writer();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(ConsoleColors.CONFIG + "ADMIN READY" + ConsoleColors.RESET);

        while (true) {
            System.out.println(ConsoleColors.INFO + "Write your message using target:message format" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.INFO + "Available targets: agencies / carriers / all \n" + ConsoleColors.RESET);

            String msg = reader.readLine();
            if (msg.equals("exit")){
                System.out.println(ConsoleColors.EXIT + "EXIT" + ConsoleColors.RESET);
                break;
            }

            String[] msg_parts = msg.split(":");

            if (msg_parts.length != 2){
                System.out.println(ConsoleColors.INFO + "Incorrect message format" + ConsoleColors.RESET);
                continue;
            }

            Optional.ofNullable(keymap.get(msg_parts[0])).ifPresentOrElse(
                    key ->
                    {
                        String admin_msg = "admin:null:" + msg_parts[1];
                        try {
                            writer.send(key, admin_msg);
                            System.out.println("Sent: " + admin_msg + " to: " + msg_parts[0] + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    },
                    () ->
                    {
                        System.out.println("Incorrect target");
                    }
            );
        }
    }

}
