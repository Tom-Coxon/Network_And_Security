import java.io.*;
import java.net.*;

/* Client class */
/**
 * This class is the client that connects to a server.
 * It also executes threads to listen and write to the server.
 * @author 100404970
 */
public class Client {
    public static void main(String[] args){ 
        
    	/*Method Variables*/
        int portNumber = 15882;
        String serverIP = "localhost";
        
        /* Try and catch to see if the client can connect to the server and execute writer and reader threads */
        try{
            Socket soc = new Socket(serverIP,portNumber);
            ClientReader clientRead = new ClientReader(soc);
            Thread clientReadThread = new Thread(clientRead);
            clientReadThread.start();
            ClientWriter clientWrite = new ClientWriter(soc);
            Thread clientWriteThread = new Thread(clientWrite);
            clientWriteThread.start();        
        }
        catch (Exception except){
        	/* Response code if the client can't join the server */
            System.out.println("421 - Connection failed");
        }
    }
}

/* ClientWriter class */
/**
 * This is the class that contains the writer thread that executes when the client joins the server.
 * @author 100404970
 */
class ClientWriter implements Runnable
{
	/* Class variables */
    private Socket cwSocket = null;
    private String messageOut = "";
    
    /* ClientWriter constructor */
    /**
     * This uses external values which can then be used within this class.
     * @param outputSoc
     */
    public ClientWriter (Socket outputSoc){
        cwSocket = outputSoc;
    }
    
    /* run method */
    /**
     * This method is used to create an output stream to the server enabling the client to communicate with the server.
     */
    public void run(){
        try{
            DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());
           	BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            
           	/* Write until the client inputs 'QUIT' */
           	while(!messageOut.toUpperCase().equals("QUIT")) {
               	messageOut = buffer.readLine();
           		dataOut.writeUTF(messageOut);
            }
           	dataOut.flush();
           	System.exit(0);
        }
        catch (Exception except){
            System.out.println("Error in Writer--> " + except.getMessage());
        }
    }
}

/* ClientReader class */
/**
 * This is the class that contains the reader thread that executes when the client joins the server.
 * @author 100404970
 */
class ClientReader implements Runnable
{
	/* Class variables */
	Socket cwSocket = null;
    String messageIn = "";

	/* ClientReader constructor */
    /**
     * This uses external values which can then be used within this class.
     * @param inputSoc
     */
	public ClientReader (Socket inputSoc){
		cwSocket = inputSoc;
	}
	  
	/* run method */
    /**
     * This method is used to create an input stream to the server enabling the client to listen to the server.
     */
	public void run(){
		try{
	        DataInputStream dataIn = new DataInputStream(cwSocket.getInputStream());

	        /* Read until the client receives 'QUIT' */
	        while (!messageIn.toUpperCase().equals("QUIT")) {
	        	messageIn = dataIn.readUTF();
	        	if(!messageIn.equals("QUIT")) {
	        		System.out.println(messageIn);
	        	}
	        }
	    }
	    catch (Exception except){
	        System.out.println("Error in Writer--> " + except.getMessage());
	    }
	}
}