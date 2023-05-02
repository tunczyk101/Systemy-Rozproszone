package sr.ice.server.SmartDevices.bulbulator;

import SmartHomeDevices.Bulbulator;
import com.zeroc.Ice.Current;
import sr.ice.server.SmartDevices.SmartDeviceI;

import java.util.logging.Logger;

public class BulbulatorI extends SmartDeviceI implements Bulbulator {
    private static final Logger logger = Logger.getLogger(BulbulatorI.class.getName());

    public BulbulatorI() {
        super("Bululator", logger);
    }

    @Override
    public void bulbul(Current current) {
        logger.info("Bulbulator: bulul");
        System.out.println("bulul");
    }
}
