package sr.ice.server.SmartDevices;

import SmartHomeDevices.AlreadyOffError;
import SmartHomeDevices.AlreadyOnError;
import SmartHomeDevices.SmartDevice;
import com.zeroc.Ice.Current;

import java.util.logging.Logger;

public class SmartDeviceI implements SmartDevice {
    private boolean turnedOn = false;
    private final String name;
    private final Logger logger;


    public SmartDeviceI(String name, Logger logger) {
        this.name = name;
        this.logger = logger;
    }


    @Override
    public String getName(Current current) {
        logger.info("Name: " + name);
        return name;
    }
}



