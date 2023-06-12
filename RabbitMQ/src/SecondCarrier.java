import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondCarrier {
    public static void main(String[] args) throws IOException {
        List<String> serv = new ArrayList<>();
        serv.add("satellite");
        serv.add("load");
//        serv.add("people");

        Carrier carrier = new Carrier(serv);
        carrier.run();
    }
}
