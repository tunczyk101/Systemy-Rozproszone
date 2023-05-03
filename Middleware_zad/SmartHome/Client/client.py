import traceback

import Ice

from handlers.bulbulator.bulbulator_handle import BulbulatorHandler
from handlers.camera.camera_handler import CameraHandler
from handlers.camera.ptz_camera_handler import PTZCameraHandler
from handlers.smartTv.smart_tv_handler import SmartTvHandler

config_file = "config.client"


def get_devices(communicator):
    smart_devices = dict()

    with open(config_file) as file:
        for line in file:
            if line == "\n":
                continue
            if line.startswith("# END DEVICE DEFINITIONS"):
                break

            name = line.split("=")[0]

            if "bulbulator" in name:
                smart_device = BulbulatorHandler(name, communicator)
            elif "ptz" in name:
                smart_device = PTZCameraHandler(name, communicator)
            elif "camera" in name:
                smart_device = CameraHandler(name, communicator)
            elif "SmartTv" in name:
                smart_device = SmartTvHandler(name, communicator)
            else:
                raise ValueError(f"Device {name} not recognized")

            smart_devices[name] = smart_device

        return smart_devices


def print_devices(smart_devices):
    print("Available smart devices:")
    for device in smart_devices:
        print(f"- {device}")


def run(args):
    status = 0

    with Ice.initialize(config_file) as communicator:
        smart_devices = get_devices(communicator)
        # smart_devices = {"kkk": "lll"}

        if not smart_devices:
            print("No devices found in config file")
            exit(1)

        print("Entering processing loop...")

        while True:
            print_devices(smart_devices)

            print("Select device:")
            device = input("=>")

            if not device or device == "x":
                break

            if device not in smart_devices:
                print("???")
                continue

            device = smart_devices[device]
            device.print_actions()

            action = input("Select action\n===>")

            if not action:
                continue

            if action not in device.actions:
                print(f"Unknown action: {action}")

            try:
                device.handle_action(action)
            except Ice.EndpointParseException:
                print(f"Incorrect port for device {device.name}, removing it from available devices.")
                del smart_devices[device.name]
        print("ending...")
        # smart_devices.get("bulbulator1").destroy()
        for device in smart_devices.values():
            if device.communicator is not None:
                print('destroy')
                try:
                    device.communicator.destroy()
                except Exception as e:
                    print(f"Error: {e.args}")
                    traceback.print_exc()
                    status = 1
        if communicator is not None:
            print('destroy')
            try:
                communicator.destroy()
            except Exception:
                traceback.print_exc()
                status = 1

        exit(status)


if __name__ == "__main__":
    run(None)
