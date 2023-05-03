package sr.ice.server;


import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import sr.ice.server.SmartDevices.bulbulator.BulbulatorI;
import sr.ice.server.SmartDevices.camera.PTZCameraI;
import sr.ice.server.SmartDevices.smartTV.NormalSmartTV;
import sr.ice.server.SmartDevices.smartTV.SmartTVI;
import sr.ice.server.SmartDevices.smartTV.SuperSmartTV;

public class Server {

    public void t1(String[] args){
        int status = 0;
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

//            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter1");
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

            SmartTVI superSmartTv = new SuperSmartTV();
            SmartTVI normalSmartTv = new NormalSmartTV();


            BulbulatorI bulbulator = new BulbulatorI();

            PTZCameraI ptzCamera = new PTZCameraI();


            adapter.add(superSmartTv, new Identity("superSmartTv1", "smartTv"));
            adapter.add(normalSmartTv, new Identity("normalSmartTv1", "smartTv"));

            adapter.add(bulbulator, new Identity("bulbulator1", "bulbulator"));

            adapter.add(ptzCamera, new Identity("ptzCamera1", "camera"));


            adapter.activate();


            System.out.println("Entering event processing loop...");


            communicator.waitForShutdown();

        } catch (Exception e) {
            System.err.println(e);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);

    }

    public static void main(String[] args) {
        Server app = new Server();
        app.t1(args);
    }
}
