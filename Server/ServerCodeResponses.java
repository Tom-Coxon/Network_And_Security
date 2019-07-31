import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* ServerCodeResponses Class */
/**
 * This class contains Server Code Responses.
 * It sends messages to the client.
 * @author 100404970
 */
public class ServerCodeResponses {
	
	/* Class Variables */
	private DataOutputStream output = null;
	
	/* ServerCodeResponses Constructor */
	/**
	 * This uses external values which can then be used within this class.
	 * @param socket
	 * @throws IOException
	 */
	public ServerCodeResponses(Socket socket) throws IOException {
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	/* code214 method */
	/**
	 * This is used to send the client all commands.
	 * @throws IOException
	 */
	public void code214(int number) throws IOException {
		output.writeUTF("214 - Help message");
		output.writeUTF("Possible commands at the current step");
		output.writeUTF("Commands:	Syntax:				Function:");

		if(number == 1) {
			output.writeUTF("Hello		HELO <sending-host>		Identify sending SMTP");
		}
		if(number == 2) {
			output.writeUTF("From		MAIL FROM: <from-address>	Sender address");
		}
		if(number == 3) {
			output.writeUTF("Recipient	RCPT TO: <to-address>		Recipient address");
			output.writeUTF("Data		DATA				Begin a message");
		}
		if(number == 3 || number == 4) {
			output.writeUTF("Reset		RSET				Restarts the mail path");
		}
		if(number == 1 || number == 2 || number == 3 || number == 4) {
			output.writeUTF("Help		HELP				Request online help");
			output.writeUTF("Connection	NOOP				Sends whether the connection status");
			output.writeUTF("Quit		QUIT				End the SMTP session");
		}
	}
	
	/* code220 method */
	/**
	 * This is used to send the client information once they connect to the server.
	 * @throws IOException 
	 */
	public void code220() throws IOException {
		output.writeUTF("220 - Successfully connected to SMTP Server");
	}
	
	/* code221 method */
	/**
	 * This is used to send the client information about if they have exited the server.
	 * @throws IOException 
	 */
	public void code221() throws IOException {
		output.writeUTF("221 - Terminated connection");
	}
	
	/* code250 method */
	/**
	 * This is used to send the client information if what they have entered is correct.
	 * @throws IOException 
	 */
	public void code250() throws IOException {
		output.writeUTF("250 - Requested mail action okay, completed");
	}
	
	/* code354 method */
	/**
	 * This is used to send the client information once they have entered the DATA section of the code.
	 * @throws IOException 
	 */
	public void code354() throws IOException {
		output.writeUTF("354 - Send message content; end with <CRLF>.<CRLF>");
		output.writeUTF("Enter 'SUBJECT: ' followed by a subject name if you'd like to add to include one");
	}
	
	/* code421 method */
	/**
	 * This is used to send the client information if the client to server connection has failed.
	 * @throws IOException 
	 */
	public void code421() throws IOException {
		output.writeUTF("421 - Connection failed");
	}
	
	/* code500 method */
	/**
	 * This is used to send the client information if what they have entered is not recognised.
	 * @throws IOException 
	 */
	public void code500() throws IOException {
		output.writeUTF("500 - Incorrect Syntax or Command Not Recognised");
	}
	
	/* code501 method */
	/**
	 * This is used to send the client information if the input has incorrect parameters.
	 * @throws IOException 
	 */
	public void code501() throws IOException {
		output.writeUTF("501 - Syntax error in parameters or arguments");
	}
	
	/* code502 method */
	/**
	 * This is used to send the client information if the command they have entered has not been implemented.
	 * @throws IOException 
	 */
	public void code502() throws IOException {
		output.writeUTF("502 - Command not implemented");
	}
	
	/* code503 method */
	/**
	 * This is used to send the client information if the command they have entered is not in sequence.
	 * @throws IOException 
	 */
	public void code503() throws IOException {
		output.writeUTF("503 - Bad sequence of commands");
	}
}
