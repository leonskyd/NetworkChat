package ChatServer;

public class ServerAPP {
    public static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length != 0) {   // вот эти 2 строки непонятно зачем
            port = Integer.parseInt(args[0]);
        }

        try {
            new MyServer(port).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error"); // shall be changed for logger
            System.exit(1);
        }
    }
}
