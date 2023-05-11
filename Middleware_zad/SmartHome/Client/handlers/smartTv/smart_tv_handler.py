from SmartHomeDevices import *
# from SmartHomeDevices_ice import RangeError
from handlers.basic_functions import test_connection
# from smarthome_ice import SmartTVPrx, RangeError


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
            base = self.communicator.propertyToProxy(self.proxy)
            self._obj = SmartTVPrx.checkedCast(base)

        return self._obj

    def print_actions(self):
        print("\n=================================\n")
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "changeChannel 1":
                try:
                    test_connection(self)
                    if self.obj.changeChannel(1):
                        print(f"Channel changed to 1")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "changeChannel 8":
                try:
                    test_connection(self)
                    if self.obj.changeChannel(8):
                        print(f"Channel changed to 8")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "changeChannel 30":
                try:
                    test_connection(self)
                    if self.obj.changeChannel(30):
                        print(f"Channel changed to 30")
                except RangeError as e:
                    print(f"Error: RangeError")
                    print(e)
                    return
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "getCurrentChannel":
                try:
                    test_connection(self)
                    print(f"Current channel: {self.obj.getCurrentChannel()}")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case "getName":
                try:
                    test_connection(self)
                    print(f"Name: {self.obj.getName()}")
                except Ice.ObjectNotExistException:
                    print("Servant object wasn't found")
                    return
            case other:
                print("???")

