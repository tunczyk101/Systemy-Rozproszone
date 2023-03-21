package text;

public class TextColoring {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String CLIENT_DISCONNECTED = "\u001B[33m";
    public static final String NEW_CLIENT = "\u001B[35m";
    public static final String NEW_MSG = "\u001B[36m";

    private static void printReset(String str){
        System.out.println(str);
        System.out.print(ANSI_RESET);
    }

    public static void printMsg(String str){
        System.out.print(NEW_MSG);
        printReset(str);
    }

    public static void printNewClient(String str){
        System.out.print(NEW_CLIENT);
        printReset(str);
    }

    public static void printClientDisconnected(String str){
        System.out.print(CLIENT_DISCONNECTED);
        printReset(str);
    }
}

