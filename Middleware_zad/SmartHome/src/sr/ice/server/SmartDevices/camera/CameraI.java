package sr.ice.server.SmartDevices.camera;

import SmartHomeDevices.AlreadyOffError;
import SmartHomeDevices.AlreadyOnError;
import SmartHomeDevices.Camera;
import com.zeroc.Ice.Current;
import sr.ice.server.SmartDevices.SmartDeviceI;

import java.util.logging.Logger;

public class CameraI extends SmartDeviceI implements Camera {
    private boolean isRecording = false;
    private final Logger logger;

    public CameraI(String name, Logger logger) {
        super(name, logger);
        this.logger = logger;
    }


    @Override
    public boolean takePicture(Current current) {
        System.out.println("Pstryk");
        logger.info("Picture was taken");
        return true;
    }

    @Override
    public boolean startRecording(Current current) throws AlreadyOnError {
        if (isRecording){
            logger.severe("Camera " + getName(current) + " is already recording");
            throw new AlreadyOnError();
        }

        logger.info(getName(current) + " started recording");
        isRecording = true;
        return true;
    }

    @Override
    public boolean stopRecording(Current current) throws AlreadyOffError {
        if (!isRecording){
            logger.severe("Camera " + getName(current) + " is already off");
            throw new AlreadyOffError();
        }

        logger.info(getName(current) + " stopped recording");
        isRecording = false;
        return true;
    }

    @Override
    public boolean isRecording(Current current) {
        logger.info("Recording: " + isRecording);
        return isRecording;
    }
}

