import java.io.IOException;

/* ServerStorage class */
/**
 * This class is used to contain the message details produced by the clients in the server.
 * It uses methods to access the variables in the class.
 * @author 100404970
 */
public class ServerStorage {
	
	/* Class variables */
	private String ID = "";
	private String domainName = "";
	private String dateOfMes = "";
	private String sender = "";
	private String receiver = "";
	private String subHeader = "";
	private String data = "";
	
	/* ServerStorage constructor */
	/**
	 * This uses external values which can then be used within this class.
	 * @param inID
	 * @param domain
	 * @param dateTime
	 * @param to
	 * @param from
	 * @param subject
	 * @param message
	 * @throws IOException
	 */
	public ServerStorage(String inID, String domain, String dateTime, String to, String from, String subject, String message){
		ID = inID;
		domainName = domain;
		dateOfMes = dateTime;
		sender = to;
		receiver = from;
		subHeader = subject;
		data = message;
	}
	
	/* getID method */
	/**
	 * This returns the string ID.
	 * @return
	 */
	public String getID() {
		return ID;
	}
	
	/* getDomain method */
	/**
	 * This returns the string domainName.
	 * @return
	 */
	public String getDomain() {
		return domainName;
	}
	
	/* getDate method */
	/**
	 * This returns the string dateOfMes.
	 * @return
	 */
	public String getDate() {
		return dateOfMes;
	}
	
	/* getSender method */
	/**
	 * This returns the string sender.
	 * @return
	 */
	public String getSender() {
		return sender;
	}
	
	/* getReceiver method */
	/**
	 * This returns the string receiver.
	 * @return
	 */
	public String getReceiver() {
		return receiver;
	}
	
	/* getSubject method */
	/**
	 * This returns the string subHeader.
	 * @return
	 */
	public String getSubject() {
		return subHeader;
	}
	
	/* getMessage method */
	/**
	 * This returns the string data.
	 * @return
	 */
	public String getMessage() {
		return data;
	}
}
