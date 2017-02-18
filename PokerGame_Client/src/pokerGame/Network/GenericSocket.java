package pokerGame.Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;

public abstract class GenericSocket implements SocketListener {
	
	public static int DEFAULT_PORT = 2010; //Ovo ce se lodat iz nekog options xml-a
	public int port;
    protected Socket socketConnection = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;
    private boolean ready = false;
    private Thread socketReaderThread;
    private Thread setupThread;

    // Flags
    public static final int DEBUG_NONE = 0x0;
    public static final int DEBUG_IO = 0x1;
    public static final int DEBUG_EXCEPTIONS = 0x2;
    public static final int DEBUG_STATUS = 0x4;
    public static final int DEBUG_ALL =
            DEBUG_IO | DEBUG_EXCEPTIONS | DEBUG_STATUS;
    protected int debugFlags = 0x0;

    public GenericSocket() {
        this(DEFAULT_PORT, DEBUG_NONE);
    }

    public GenericSocket(int port) {
        this(port, DEBUG_NONE);
    }

    public GenericSocket(int port, int debugFlags) {
        this.port = port;
        this.debugFlags = debugFlags;
    }
    
    public void connect() {
        try {
            setupThread = new SetupThread();
            setupThread.start();
            
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
        } catch (Exception e) {
            if (debugFlagIsSet(DEBUG_EXCEPTIONS)) {
                e.printStackTrace();
            }
        }  
    }
    
    public void shutdown() {
        close();
    }
    
    private void close() {
        try {
            if (socketConnection != null && !socketConnection.isClosed()) {
                socketConnection.close();
            }

            closeAdditionalSockets();
            if (debugFlagIsSet(DEBUG_STATUS)) {
                System.out.println("Connection closed");
            }

            onClose(Boolean.TRUE);
        } catch (Exception e) {
            if (debugFlagIsSet(DEBUG_EXCEPTIONS)) {
                e.printStackTrace();
            }
        }
    }
    
    public abstract void initSocketConnection() throws SocketException;
    public abstract void closeAdditionalSockets();
    
    private synchronized void waitForReady() {
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
    
    private synchronized void notifyReady() {
        ready = true;
        notifyAll();
    }
    
    public void sendPacket(Object msg) {
        //Send packet here
    }
    
    public boolean debugFlagIsSet(int flag) {
        return ((flag & debugFlags) != 0) ? true : false;
    }
    
    public void setDebugFlag(int flag) {
        debugFlags |= flag;
    }
    
    public void clearDebugFlag(int flag) {
        debugFlags &= flag;
    }
    
    
    class SetupThread extends Thread {
        @Override
        public void run() {
            try {
                initSocketConnection();
                if (socketConnection != null && !socketConnection.isClosed()) {
                    /*
                     * Get input and output streams
                     */
                    input = new BufferedReader(new InputStreamReader(
                            socketConnection.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(
                            socketConnection.getOutputStream()));
                    output.flush();
                }
                notifyReady();
            } catch (Exception e) {
                if (debugFlagIsSet(DEBUG_EXCEPTIONS)) {
                    e.printStackTrace();
                }
                notifyReady();
            }
        }
    }
    
    class SocketReaderThread extends Thread {

        @Override
        public void run() {
          
            waitForReady();
           
            if (socketConnection != null && socketConnection.isConnected()) {
            	onClose(Boolean.FALSE) ;
            }

            try {
            	System.out.println("Message Receved from server: ");
                if (input != null) {
					ObjectInputStream ois = new ObjectInputStream(socketConnection.getInputStream());
                    
					onPacketRecived(ois.readObject());

                }
            } catch (Exception e) {
                if (debugFlagIsSet(DEBUG_EXCEPTIONS)) {
                    e.printStackTrace();
                }
            } finally {
                close();
            }
        }
    }
        
	@Override
	public void onPacketRecived(Object packet) {
		
	}
	
	private Object CastToClass(Object packet){
		try{
			Method method = packet.getClass().getMethod("getType");
			
		
			
			return null;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public void onClose(Boolean isClosed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect(Boolean isClosed) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSend(Object packet){
		
	}
	 
}
