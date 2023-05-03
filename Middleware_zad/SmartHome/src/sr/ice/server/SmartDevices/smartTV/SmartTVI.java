package sr.ice.server.SmartDevices.smartTV;

import SmartHomeDevices.RangeError;
import SmartHomeDevices.SmartTV;
import com.zeroc.Ice.Current;
import sr.ice.server.SmartDevices.SmartDeviceI;

import java.util.logging.Logger;

public class SmartTVI extends SmartDeviceI implements SmartTV {
    private static final Logger logger = Logger.getLogger(SmartTVI.class.getName());

    private final int maxChannel;

    private int currentChannel;

    public SmartTVI(String name, int maxChannel) {
        super(name, logger);
        this.maxChannel = maxChannel;
    }

    @Override
    public boolean changeChannel(int channelNumber, Current current) throws RangeError {
        if (channelNumber <= 0 || channelNumber > maxChannel){
            logger.severe("Incorrect channel number: " + channelNumber);
            throw new RangeError(0, maxChannel);
        }
        currentChannel = channelNumber;
        logger.info("Changed channel to: " + channelNumber);
        return true;
    }

    @Override
    public int getCurrentChannel(Current current) {
        logger.info("Current channel (" + getName(current) + ") : " + currentChannel);
        return currentChannel;
    }
}
