import Ice

# from generated_python import *
from client.handlers.basic_functions import test_connection
from client.handlers.camera.camera_handler import CameraHandler


class PTZCameraHandler(CameraHandler):
    def __init__(self, proxy, communicator):
        self.proxy = proxy
        self.communicator = communicator
        self._obj = None
        self.device_type = "SmartTv"
        self.actions = ["getPTZ",
                        "setPTZ",
                        "takePicture",
                        "startRecording",
                        "stopRecording",
                        "isRecording",
                        "getName"]
        Ice.initialize()

    @property
    def obj(self):
        if not self._obj:
            base = self.communicator.stringToProxy(self.proxy)
            self._obj = PTZCameraPrx.checkedCast(base)

        return self._obj

    def handle_action(self, action):
        match action:
            case "getPTZ":
                try:
                    test_connection(self)
                    print(self.obj.getPTZ())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "setPTZ":
                try:
                    test_connection(self)
                    ptz = PTZ(pan=20, tilt=88)
                    print(self.obj.setPTZ(ptz))
                except RangeError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case other:
                super().handle_action()

