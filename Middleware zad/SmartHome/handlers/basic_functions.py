from ice import Ice
from time import sleep


def get_device_data(handler):
    pass


def test_connection(handler):
    status = 1

    try:
        status = handler.obj.ice_ping()
    except (Ice.ConnectionRefusedException, Ice.InvocationTimeoutException):
        pass

    if status == 1:
        print("Connection problem")


def _get_sleep_time(requests):
    if requests <= 2:
        return 1
    elif requests <= 7:
        return 0.5
    elif requests <= 15:
        return 0.25
    return 0.125


def reconnect(handler):
    requests = 1

    print("Reconnecting...")
    while requests < 30:
        status = 1

        try:
            status = handler.obj.ice_ping()
        except (Ice.ConnectionRefusedException, Ice.InvocationTimeoutException):
            pass

        if status == 1:
            requests += 1
            sleep_time = _get_sleep_time(requests)
            sleep(sleep_time)
        else:
            print("Connected")
            return

    print("Problem with network. \nAbandoning work...")
    exit(1)



