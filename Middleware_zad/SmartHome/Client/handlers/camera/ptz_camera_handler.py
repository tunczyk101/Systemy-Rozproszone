import Ice

from SmartHomeDevices import *
from handlers.basic_functions import test_connection
from handlers.camera.camera_handler import CameraHandler
# from smarthome_ice import PTZCameraPrx, PTZ, RangeError


class PTZCameraHandler(CameraHandler):
    def __init__(self, proxy, communicator):
        self.proxy = proxy
        self.communicator = communicator
        self._obj = None
        self.device_type = "SmartTv"
        self.actions = ["getPTZ",
                        "setPTZ",
                        "resetPTZ",
                        "emptyPTZ",
                        "takePicture",
                        "startRecording",
                        "stopRecording",
                        "isRecording",
                        "getName"]
        Ice.initialize()

    @property
    def obj(self):
        if not self._obj:
            base = self.communicator.propertyToProxy(self.proxy)
            self._obj = PTZCameraPrx.checkedCast(base)

        return self._obj

    def handle_action(self, action):
        match action:
            case "getPTZ":
                try:
                    test_connection(self)
                    print(f"PTZ settings: \n{self.obj.getPTZ()}")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "setPTZ":
                try:
                    test_connection(self)
                    ptz = PTZ(pan=20, tilt=88)
                    if self.obj.setPTZ(ptz):
                        print(f"Changed values: {ptz}\n")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "resetPTZ":
                try:
                    test_connection(self)
                    ptz = PTZ(pan=0, tilt=0, zoom=0)
                    if self.obj.setPTZ(ptz):
                        print(f"Changed values: {ptz}\n")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "emptyPTZ":
                try:
                    test_connection(self)
                    ptz = PTZ()
                    if self.obj.setPTZ(ptz):
                        print(f"Changed values: {ptz}\n")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")

            case other:
                super().handle_action(action)

