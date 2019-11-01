package DabEngine.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

public class Utils {

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = Utils.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    public static ByteBuffer colorToByteBuffer(float r, float g, float b, float a){
        ByteBuffer buf =  BufferUtils.createByteBuffer(1 * 1 *4);
        int rByte = Math.round(r*255);
        int gByte = Math.round(g*255);
        int bByte = Math.round(b*255);
        int aByte = Math.round(a*255);

        for(int y = 0; y < 1; y++){
            for(int x = 0; x < 1; x++){
                buf.put((byte) ((0xFFFF>> 16)&0xFF));
                buf.put((byte) ((0xFFFF >> 8)&0xFF));
                buf.put((byte) (0xFFFF & 0xFF));
                buf.put((byte) ((0xFFFF >> 24)&0xFF));
            }
        }

        return buf;
    }

    private static int toHex(Color col) {
        String as = pad(Integer.toHexString(col.getAlpha()));
        String rs = pad(Integer.toHexString(col.getRed()));
        String gs = pad(Integer.toHexString(col.getGreen()));
        String bs = pad(Integer.toHexString(col.getBlue()));
        String hex = "0x" + as + rs + gs + bs;
        return Integer.parseInt(hex, 16);
    }
    
    private static final String pad(String s) {
        return (s.length() == 1) ? "0" + s : s;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static Object byteArrayToObject(byte[] b){
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInputStream in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject(); 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close(); 
                    bis.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }

    public static byte[] objectToByteArray(Object o){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] b = null;
        try {
            out = new ObjectOutputStream(bos);   
            out.writeObject(o);
            out.flush();
            b = bos.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                if(out !=  null)
                    out.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return b;
    }

    public static float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    public static <K, V> List<K> getAllKeysForValue(Map<K, V> mapOfWords, V value) 
    {
        List<K> listOfKeys = null;
         
        //Check if Map contains the given value
        if(mapOfWords.containsValue(value))
        {
            // Create an Empty List
            listOfKeys = new ArrayList<>();
                    
            // Iterate over each entry of map using entrySet
            for (Map.Entry<K, V> entry : mapOfWords.entrySet()) 
            {
                // Check if value matches with given value
                if (entry.getValue().equals(value))
                {
                    // Store the key from entry to the list
                    listOfKeys.add(entry.getKey());
                }
            }
        }
        // Return the list of keys whose value matches with given value.
        return listOfKeys;	
    }

    public static boolean isWindows() {
        String operSys = System.getProperty("os.name").toLowerCase();
        if (operSys.contains("win")) {
            return true;
        }
        return false;
    }

    public static boolean isLinux() {
        String operSys = System.getProperty("os.name").toLowerCase();
        if (operSys.contains("nix") || operSys.contains("nux")
        || operSys.contains("aix")) {
            return true;
        }
        return false;
    }

    public static boolean isMac() {
        String operSys = System.getProperty("os.name").toLowerCase();
        if (operSys.contains("mac")) {
            return true;
        }
        return false;
    }

    public static DabEngine.Core.Monitor toMonitorClass(long monitor)
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer xpos = stack.mallocInt(1);
            IntBuffer ypos = stack.mallocInt(1);
            
            GLFW.glfwGetMonitorPos(monitor, xpos, ypos);
            String name = GLFW.glfwGetMonitorName(monitor);
            return new DabEngine.Core.Monitor(monitor, xpos.get(0), ypos.get(0), name);
        }
    }
}
