import Ice

from generated_python.SmartHomeDevices import *
from client.handlers.basic_functions import test_connection


class SmartTvHandler:
    def __init__(self, proxy, communicator):
        self.proxy = proxy
        self.communicator = communicator
        self._obj = None
        self.device_type = "SmartTv"
        self.actions = ["changeChannel 1",
                        "changeChannel 8",
                        "changeChannel 30",
                        "getCurrentChannel",
                        "getName"]
        Ice.initialize()

    @property
    def obj(self):
        if not self._obj:
            base = self.communicator.stringToProxy(self.proxy)
            self._obj = SmartTVPrx.checkedCast(base)

        return self._obj

    def print_actions(self):
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "changeChannel 1":
                try:
                    test_connection(self)
                    print(self.obj.changeChannel(1))
                except RangeError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "changeChannel 8":
                try:
                    test_connection(self)
                    print(self.obj.changeChannel(8))
                except RangeError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "changeChannel 30":
                try:
                    test_connection(self)
                    print(self.obj.changeChannel(30))
                except RangeError as e:
                    print(f"Error: {e.reason}")
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "getCurrentChannel":
                try:
                    test_connection(self)
                    print(self.obj.getCurrentChannel())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "getName":
                try:
                    test_connection(self)
                    print(self.obj.getName())
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case other:
                print("???")

