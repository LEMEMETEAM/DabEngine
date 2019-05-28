package DabEngine.Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {
    private ServerSocket server;
    private ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public SocketServer() {

        new Thread(() -> {
            try {
                server = new ServerSocket(Networking.DABENGINE_PORT);

                while (true) {
                    Socket s = server.accept();
                    LOGGER.log(Level.INFO, "Connection: " + s);

                    clients.put(s.getInetAddress().getHostAddress(), s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void receiveFromClientThenSend() {
        for (Entry<String, Socket> e : clients.entrySet()) {
            Object obj = null;
            try {
                obj = ((ObjectInputStream) e.getValue().getInputStream()).readObject();
            } catch (ClassNotFoundException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            sendToAllExceptOriginal(e.getValue(), obj);
        }
    }

    public void sendToAllExceptOriginal(Socket original, Object obj){
        for(Entry<String, Socket> e : clients.entrySet()){
            if(e.getValue() == original){
                continue;
            }
            try{
                ((ObjectOutputStream)e.getValue().getOutputStream()).writeObject(obj);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

    }
}