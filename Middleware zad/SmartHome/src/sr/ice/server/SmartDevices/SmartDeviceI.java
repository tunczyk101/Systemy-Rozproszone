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
    public boolean turnOn(Current current) throws AlreadyOnError {
        if (turnedOn){
            logger.warning("Device was already turned on");
            throw new AlreadyOnError();
        }
        logger.info("Device " + name + "turned on");
        turnedOn = true;
        return true;
    }

    @Override
    public boolean turnOff(Current current) throws AlreadyOffError {
        if (!turnedOn){
            logger.warning("Device was already turned off");
            throw new AlreadyOffError();
        }
        logger.info("Device " + name + "turned off");
        turnedOn = false;
        return true;
    }

    @Override
    public boolean isOn(Current current) {
        logger.info("Current status (" + name + "): " + turnedOn);
        return turnedOn;
    }


    @Override
    public String getName(Current current) {
        logger.info("Name: " + name);
        return name;
    }
}



