import Ice

from SmartHomeDevices import *
from handlers.basic_functions import test_connection


class CameraHandler:
    def __init__(self, proxy, communicator):
        self.proxy = proxy
        self.communicator = communicator
        self._obj = None
        self.device_type = "SmartTv"
        self.actions = ["takePicture",
                        "startRecording",
                        "stopRecording",
                        "isRecording",
                        "getName"]
        Ice.initialize()

    @property
    def obj(self):
        if not self._obj:
            base = self.communicator.propertyToProxy(self.proxy)
            self._obj = CameraPrx.checkedCast(base)

        return self._obj

    def print_actions(self):
        print("\n=================================\n")
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "takePicture":
                try:
                    test_connection(self)
                    if self.obj.takePicture():
                        print("Picture was taken")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "startRecording":
                try:
                    test_connection(self)
                    if self.obj.startRecording():
                        print("Started Recording")
                except AlreadyOnError as e:
                    print(f"Error: AlreadyON")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")

            case "stopRecording":
                try:
                    test_connection(self)
                    if self.obj.stopRecording():
                        print("Stoped Recording")
                except AlreadyOffError as e:
                    print(f"Error: Already OFF")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "isRecording":
                try:
                    test_connection(self)
                    print(f"Recording: {self.obj.isRecording()}")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "getName":
                try:
                    test_connection(self)
                    print(f"Name: {self.obj.getName()}")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case other:
                print("???")

