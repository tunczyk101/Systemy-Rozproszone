import Ice

from generated_python import *
from client.handlers.basic_functions import test_connection


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
            base = self.communicator.stringToProxy(self.proxy)
            self._obj = CameraPrx.checkedCast(base)

        return self._obj

    def print_actions(self):
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "takePicture":
                try:
                    test_connection(self)
                    print(self.obj.takePicture())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "startRecording":
                try:
                    test_connection(self)
                    print(self.obj.startRecording())
                except AlreadyOnError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")

            case "stopRecording":
                try:
                    test_connection(self)
                    print(self.obj.stopRecording())
                except AlreadyOffError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "isRecording":
                try:
                    test_connection(self)
                    print(self.obj.isRecording())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case "getName":
                try:
                    test_connection(self)
                    print(self.obj.getName())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
            case other:
                print("???")

