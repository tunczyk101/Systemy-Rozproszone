import traceback

from handlers.basic_functions import test_connection
from SmartHomeDevices import *
import Ice


class BulbulatorHandler:
    def __init__(self, proxy, communicator):
        self.proxy = proxy
        self.communicator = communicator
        self._obj = None
        self.device_type = "bulbulator"
        self.actions = ["bulbul",
                        "getName"]
        Ice.initialize()

    @property
    def obj(self):
        if not self._obj:
            base = self.communicator.propertyToProxy(self.proxy)
            self._obj = BulbulatorPrx.checkedCast(base)

        return self._obj

    def destroy(self):
        try:
            print(f"destroying {self.proxy}")
            self.communicator.destroy()
        except Exception:
            traceback.print_exc()
            status = 1

    def print_actions(self):
        print("\n=================================\n")
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "bulbul":
                try:
                    test_connection(self)
                    if self.obj.bulbul():
                        print("bulul")
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
