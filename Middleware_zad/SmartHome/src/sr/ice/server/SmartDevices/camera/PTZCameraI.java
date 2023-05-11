package sr.ice.server.SmartDevices.camera;

import SmartHomeDevices.PTZ;
import SmartHomeDevices.PTZCamera;
import SmartHomeDevices.RangeError;
import com.zeroc.Ice.Current;

import java.util.logging.Logger;

public class PTZCameraI extends CameraI implements PTZCamera {
    private static final Logger logger = Logger.getLogger(PTZCameraI.class.getName());

    private short pan = 0;
    private short tilt = 0;
    private short zoom = 0;

    public PTZCameraI() {
        super("PTZ Camera", logger);
    }

    public PTZCameraI(short pan, short tilt, short zoom) {
        super("PTZ Camera", logger);

        this.pan = pan;
        this.tilt = tilt;
        this.zoom = zoom;
    }


    @Override
    public PTZ getPTZ(Current current) {
        return new PTZ(pan, tilt, zoom);
    }

    @Override
    public boolean setPTZ(PTZ ptz, Current current) throws RangeError {
        short temp;
        if (ptz.hasPan()) {
            temp = ptz.getPan();
            if (temp < -180 || temp > 180) {
                logger.severe("Incorrect Pan value");
                throw new RangeError(-180, 180);
            }
            this.pan = temp;
        }
        if (ptz.hasTilt()) {
            temp = ptz.getTilt();
            if (temp < -90 || temp > 90) {
                logger.severe("Incorrect Tilt value");
                throw new RangeError(-90, 90);
            }
            this.tilt = temp;
        }
        if (ptz.hasZoom()) {
            temp = ptz.getZoom();
            if (temp < 0 || temp > 10) {
                logger.severe("Incorrect Zoom value");
                throw new RangeError(0, 10);
            }
            this.zoom = temp;
        }
        logger.info("Camera: PTZ adjusted");
        return true;
    }
}

