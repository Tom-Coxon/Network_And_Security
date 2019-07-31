import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* ConnectionManager class */
/**
 * This class is used for each client to allow communication between the server and client.
 * It also contains methods relating to the connection between the server and client; 'QUIT' and 'NOOP'.
 * @author 100404970
 */
public class ConnectionManager {
	
	/* Class variables */
	private DataInputStream input = null;
	private DataOutputStream output = null;
	
	/* ConnectionManager constructor */
	/**
	 * This uses external values which can then be used within this class.
	 * @param socket
	 * @throws IOException
	 */
	public ConnectionManager(Socket socket) throws IOException {
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	/* input method */
	/**
	 * This method returns the user input to the server.
	 * @throws IOException 
	 */
	public String input() throws IOException {
		return input.readUTF().toString();
	}
    
	/* QUIT method */
	/**
	 * This method sends a string back to the client to close the client reader down.
	 * @throws IOException 
	 */
    public void QUIT() throws IOException {
    	output.writeUTF("QUIT");
    }
    
    /* NOOP method */
    /**
     * This method sends a message to the client to let them know if the server is running.
     * @throws IOException 
     */
    public void NOOP() throws IOException {
		output.writeUTF("-Still connected-");
    }  
}