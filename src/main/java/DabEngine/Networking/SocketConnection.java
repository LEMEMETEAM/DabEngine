package DabEngine.Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnection {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static SocketConnection connect(String ip, int port) {
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            socket = new Socket(ip, port);
            LOGGER.log(Level.INFO, "Connected to " + ip + " on port " + port);

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException uhe) {
            LOGGER.log(Level.WARNING, "Host '" + ip + "' not found or there is no service on port '" + port + "'.",
                    uhe);
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new SocketConnection(socket, in, out);
    }

    private SocketConnection(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    public void sendToServer(Object obj) {
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Object receiveFromServer() {
        Object obj = null;
        try {
            obj = in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }
}