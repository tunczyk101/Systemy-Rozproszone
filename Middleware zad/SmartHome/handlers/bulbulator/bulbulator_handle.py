import Ice

from generated_python.smarthome_ice import BulbulatorPrx
from handlers.basic_functions import test_connection


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
            base = self.communicator.stringToProxy(self.proxy)
            self._obj = BulbulatorPrx.checkedCast(base)

        return self._obj

    def print_actions(self):
        print("Actions:")
        for action in self.actions:
            print(f"- {action}")

    def handle_action(self, action):
        match action:
            case "bulbul":
                try:
                    test_connection(self)
                    print(self.obj.bulbul())
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

