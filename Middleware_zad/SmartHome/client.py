import Ice

from client.handlers.bulbulator.bulbulator_handle import BulbulatorHandler
from client.handlers.camera.camera_handler import CameraHandler
from client.handlers.camera.ptz_camera_handler import PTZCameraHandler
from client.handlers.smartTv.smart_tv_handler import SmartTvHandler

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

    communicator = None

    with Ice.initialize(config_file) as communicator:
        smart_devices = get_devices(communicator)

        if not smart_devices:
            print("No devices found in config file")
            exit(1)

        print("Entering processing loop...")

        while True:
            print_devices(smart_devices)

            print("Select device:")
            device = input("=>")

            if not device or device == "x":
                exit(0)

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


if __name__ == "__main__":
    run(None)

