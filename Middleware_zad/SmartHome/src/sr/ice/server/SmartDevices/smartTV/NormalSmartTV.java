package sr.ice.server.SmartDevices.smartTV;

public class NormalSmartTV extends SmartTVI{
    private static final String NAME = "Normal TV";
    private static final int MAXCHANNEL = 10;

    public NormalSmartTV() {
        super(NAME, MAXCHANNEL);
    }

}
