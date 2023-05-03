package sr.ice.client;

import SmartHomeDevices.BulbulatorPrx;
import SmartHomeDevices.PTZCameraPrx;
import SmartHomeDevices.SmartTVPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.LocalException;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import java.util.Objects;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.run(args);
    }

    public void run(String[] args) {
        int status = 0;
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectPrx obj;
            SmartTVPrx tvobj;
            String name;

            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            do {
                System.out.print("==> ");
                line = in.readLine();

                switch (line) {
                    case "superSmartTv":
                        obj = communicator.propertyToProxy("superSmartTv1.Proxy");

                        tvobj = SmartTVPrx.checkedCast(obj);
                        if (tvobj == null) {
                            throw new Error("Invalid proxy");
                        }

                        name = tvobj.getName();
                        System.out.println("SmartTv: " + name);
                        break;
                    case "normalSmartTv":
                        obj = communicator.propertyToProxy("normalSmartTv1.Proxy");

                        tvobj = SmartTVPrx.checkedCast(obj);
                        if (tvobj == null) {
                            throw new Error("Invalid proxy");
                        }

                        name = tvobj.getName();
                        System.out.println("SmartTv: " + name);
                        break;
                    case "PTZCamera":
                        obj = communicator.propertyToProxy("PTZCamera1.Proxy");

                        PTZCameraPrx cameraObj = PTZCameraPrx.checkedCast(obj);
                        if (cameraObj == null) {
                            throw new Error("Invalid proxy");
                        }

                        name = cameraObj.getName();
                        System.out.println("Name: " + name);
                        break;
                    case "bulbulator":
                        obj = communicator.propertyToProxy("Bulbulator1.Proxy");

                        BulbulatorPrx bulObj = BulbulatorPrx.checkedCast(obj);
                        if (bulObj == null) {
                            throw new Error("Invalid proxy");
                        }

                        name = bulObj.getName();
                        System.out.println("Name: " + name);
                        break;
                    case "x":
                    case "":
                        break;
                    default:
                        System.out.println("???");

                }

            }
            while (!Objects.equals(line, "x"));
        } catch (LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        if (communicator != null) { //clean
            try {
                communicator.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);
    }
}
