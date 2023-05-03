
#ifndef SMART_HOME_ICE
#define SMART_HOME_ICE

module SmartHomeDevices
{
    exception AlreadyOffError {}
    exception AlreadyOnError {}

    interface SmartDevice {
        bool turnOn() throws AlreadyOnError;
        bool turnOff() throws AlreadyOffError;
        bool isOn();
        string getName();
    };

    interface Bulbulator extends SmartDevice
      {
    	  void bulbul();
      }

    interface Camera extends SmartDevice {
        bool takePicture();

        bool startRecording() throws AlreadyOnError;
        bool stopRecording() throws AlreadyOffError;
        idempotent bool isRecording();
    };


    class PTZ{
        optional(0) short pan;
        optional(1) short tilt;
        optional(2) short zoom;
    };

    exception RangeError {
          int minValue;
          int maxValue;
    };

    interface PTZCamera extends Camera{
        idempotent PTZ getPTZ();
        idempotent bool setPTZ(PTZ ptz) throws RangeError;
    };


    interface SmartTV extends SmartDevice {
        bool changeChannel(int channelNumber) throws RangeError;
        idempotent int getCurrentChannel();
    };

};

#endif