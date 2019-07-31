import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Server main class */
/**
 * This class is the server that multiple clients can connect to.
 * It has a list to store clients messages.
 * It also executes threads to handle incoming clients.
 * @author 100404970
 *
 */
public class Server {
    public static void main(String[] args){ 

    	/*Method Variables*/
        int portNumber = 15882;
        ArrayList<ServerStorage> storage = new ArrayList<ServerStorage>();
        
        /* Try and catch to see if the server can create a socket */
        try{    
        	boolean valid = true;       	
            ServerSocket serverSoc = new ServerSocket(portNumber);
            
            /* Enable multiple clients to join at any point, executes the thread and passes in relevant info */
            while (valid){       
                Socket soc = serverSoc.accept();
                ServerCodeResponses respon = new ServerCodeResponses(soc);
                ConnectionManager connection = new ConnectionManager(soc);
                ServerConnetionHandler sch = new ServerConnetionHandler(connection, respon, storage);
                Thread schThread = new Thread(sch);
                schThread.start();
            }
            
        }
        catch (Exception except){
            System.out.println("Error --> " + except.getMessage());
        }
    }   
}

/* ServerConnetionHandler class */
/**
 * This is the thread that executes when a client joins the server.
 * @author 100404970
 */
class ServerConnetionHandler implements Runnable
{
	/* Class Private Variables */
	/* List instance for the server storage */
    private ArrayList<ServerStorage> serverStorage = null;
    
    /* Create list to store the current clients recipient(s) */
    private ArrayList<String> recipientStorage = new ArrayList<String>();
    
    /* Instances of the connection manager and server storage */
    private ConnectionManager user = null;
    private ServerCodeResponses response = null;
    
    /* Variables for the email */
    private String domainName = "";
    private String mailName = "";
    private String subject = "";
    private String dataMessage = "";
    private String messageIn = "";
    private Random rand = new Random();
    
    /* 'valid' used for loops within methods and 'reset' for when the client types in 'RSET' */
    private boolean valid = true;
    private boolean reset = false;

    /* For formating the domain and email addresses */
    private String emailFormat = "(?:[a-zA-Z0-9-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private String domainFormat = "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private Pattern emailPattern = Pattern.compile(emailFormat);
    private Pattern domainPattern = Pattern.compile(domainFormat);

    /* ServerConnectionHandler constructor */
    /**
     * This uses external values which can then be used within this class.
     * @param inSoc
     * @param respon
     * @param storage
     */
    public ServerConnetionHandler (ConnectionManager inSoc, ServerCodeResponses respon, ArrayList<ServerStorage> storage){
        user = inSoc;
        response = respon;
        serverStorage = storage;
    }
    
    /* run method */
    /**
     * This method is used to implement the private methods within this class.
     * This runs when the thread is executed.
     * The 'HELO()' method can only be executed once whereas the other can be executed multiple times.
     */
    public void run(){
        try{
        	HELO();
        	while (true) {
        		MAIL();
        		RCPT();
        		DATA();
        		store();
            }
        }
        catch (Exception except){
            /* Exception thrown (except) when something went wrong, variables get set to blank */
        }
    }
    
    /* HELO method */
    /**
     * This method implements the 'HELO', 'QUIT', 'HELP' and 'NOOP' commands.
     * It checks the clients input, runs certain functions and throws code responses according to what they type in.
     * The input is also checked to make sure it is in the correct sequence.
     * If the input starts with 'HELO' then it would check the format of the domain to make sure it is correct.
     * @throws IOException
     */
    private void HELO() throws IOException {
    	response.code220();
    	while(valid) {
    		messageIn = user.input();
            if(!messageIn.startsWith("HELO")) {
            	if(messageIn.equals("QUIT")) {
            		response.code221();
            		user.QUIT();
    			}
            	else if(messageIn.equals("HELP")) {
            		response.code214(1);
            	}
            	else if(messageIn.equals("NOOP")) {
            		response.code250();
            		user.NOOP();
            	}
            	else if(messageIn.startsWith("SEND") || messageIn.startsWith("SOML") || messageIn.startsWith("SAML") || messageIn.startsWith("VRFY") || messageIn.startsWith("EXPN")) {
            		response.code502();
            	}
            	else if(messageIn.startsWith("MAIL FROM:") || messageIn.startsWith("RCPT TO:") || messageIn.startsWith("DATA")){
    				response.code503();
    			}
            	else {
            		response.code500();
            	}
            }
            else {
    			messageIn = messageIn.replace("HELO ", "");
    			if(messageIn.startsWith("<") && messageIn.endsWith(">")) {
    				messageIn = messageIn.replace("<", "");
    				messageIn = messageIn.replace(">", "");
		    		Matcher matcher = domainPattern.matcher(messageIn);
		    		if(matcher.matches() == true) {
		    			domainName = messageIn;
		    			response.code250();
		            	valid = false;
		    		}
		    		else{
		    			response.code501();
		    		}
    			}
	    		else{
	    			response.code501();
	    		}
    		}
    	}
    	valid = true;
    }
    
    /* MAIL method */
    /**
     * This method implements the 'MAIL FROM', 'QUIT', 'HELP' and 'NOOP' commands. 
     * It checks the clients input, runs certain functions and throws code responses according to what they type in.
     * The input is also checked to make sure it is in the correct sequence.
     * If the input starts with 'MAIL FROM: ' then it would check the format of the email to make sure it is correct.
     * @throws IOException
     */
    private void MAIL() throws IOException {
		while(valid) {
			messageIn = user.input();
    		if(!messageIn.startsWith("MAIL FROM:")) {
    			if(messageIn.equals("QUIT")) {
    				response.code221();
    				user.QUIT();
    			}
    			else if(messageIn.equals("HELP")) {
            		response.code214(2);
            	}
    			else if(messageIn.equals("NOOP")) {
    				response.code250();
            		user.NOOP();
            	}
    			else if(messageIn.startsWith("SEND") || messageIn.startsWith("SOML") || messageIn.startsWith("SAML") || messageIn.startsWith("VRFY") || messageIn.startsWith("EXPN")) {
            		response.code502();
            	}
    			else if(messageIn.startsWith("HELO") || messageIn.startsWith("RCPT TO:") || messageIn.startsWith("DATA")){
    				response.code503();
    			}
    			else {
    				response.code500();
    			}
    		}
    		
    		else {
    			messageIn = messageIn.replace("MAIL FROM:", "");
    			if(messageIn.startsWith("<") && messageIn.endsWith(">")) {
    				messageIn = messageIn.replace("<", "");
    				messageIn = messageIn.replace(">", "");
		    		Matcher matcher = emailPattern.matcher(messageIn);
		    		if(matcher.matches() == true) {
		    			mailName = messageIn;
		    			response.code250();
		            	valid = false;
		    		}
		    		else{
		    			response.code501();
		    		}
    			}
	    		else{
	    			response.code501();
	    		}
    		}
		}
		valid = true;
	}
    
    /* RCPT method */
    /**
     * This method implements the 'RCPT TO', 'QUIT', 'HELP', 'NOOP', 'DATA' and 'RSET' commands. 
     * It checks the clients input, runs certain functions and throws code responses according to what they type in.
     * The input is also checked to make sure it is in the correct sequence.
     * If the input starts with 'RCPT TO:' then it would check the format of the email to make sure it is correct.
     * The user can keep adding to the list of recipients until they type in either 'DATA' or 'RSET'.
     * However when the client inputs 'DATA' there must at least be one person in the list to proceed.
     * @throws IOException
     */
    private void RCPT() throws IOException {
    	while(valid) {
    		messageIn = user.input();
    		if(!messageIn.startsWith("RCPT TO:")) {
    			if(messageIn.equals("QUIT")) {
    				response.code221();
    				user.QUIT();
    			}
    			else if(messageIn.equals("HELP")) {
            		response.code214(3);
            	}
    			else if(messageIn.equals("NOOP")) {
    				response.code250();
            		user.NOOP();
            	}
    			else if(messageIn.equals("DATA")) {
        			if(!recipientStorage.isEmpty()) {
    					valid = false;
    				}
        			else {
        				response.code503();
        			}
        		}
    			else if(messageIn.equals("RSET")) {
            		reset = true;
            		mailName = "";
            		recipientStorage.clear();
            		valid = false;
            	}
    			else if(messageIn.startsWith("SEND") || messageIn.startsWith("SOML") || messageIn.startsWith("SAML") || messageIn.startsWith("VRFY") || messageIn.startsWith("EXPN")) {
            		response.code502();
            	}
    			else if(messageIn.startsWith("HELO") || messageIn.startsWith("MAIL FROM:")){
    				response.code503();
    			}
    			else {
    				response.code500();
    			}
			}
    		else {
    			messageIn = messageIn.replace("RCPT TO:", "");
    			if(messageIn.startsWith("<") && messageIn.endsWith(">")) {
    				messageIn = messageIn.replace("<", "");
    				messageIn = messageIn.replace(">", "");
		    		Matcher matcher = emailPattern.matcher(messageIn);
		    		if(matcher.matches() == true) {
		    			recipientStorage.add(messageIn);
		    			response.code250();
		    		}
		    		else{
		    			response.code501();
		    		}
    			}
	    		else{
	    			response.code501();
	    		}
    		}
    	}
   	    valid = true;
    }
    
    /* DATA method */
    /**
     * This method implements the 'DATA', 'QUIT', 'HELP', 'NOOP' and '.' commands. 
     * It checks the clients input, runs certain functions and throws code responses according to what they type in.
     * This method is for concatting a message to send to the recipient(s).
     * The client can input 'SUBJECT: ' to add a subject header to the email.
     * You can only break out of the loop until the user inputs 'QUIT', 'RSET' or '.'.
     * '.' indicates that the user wishes to finish the message to send.
     * @throws IOException
     */
    private void DATA() throws IOException {
    	if(reset != true) {
        	response.code354();
        	while(valid) {
	        	messageIn = user.input();

	        	if(messageIn.equals("QUIT")) {
	        		response.code221();
	        		user.QUIT();
				}
	        	else if(messageIn.equals("HELP")) {
	        		response.code214(4);
	        	}
	        	else if(messageIn.equals("NOOP")) {
	        		response.code250();
	        		user.NOOP();
	        	}
	        	else if(messageIn.startsWith("SUBJECT: ")) {
	        		messageIn = messageIn.replace("SUBJECT: ", "");
	        		subject = messageIn;
	        		response.code250();
	        	}
	        	else if(messageIn.equals("RSET")) {
	        		valid = false;
	        		reset = true;
	        	}     
	        	else if(messageIn.equals(".")) {
        			response.code250();
        			dataMessage = dataMessage.replace("null", "");
        			valid = false;
	        	}
	        	else {
	        		/* The user can input '..' which the server reads and stores as '.' so the server doesnt send the unfinished message */
	        		if(messageIn.equals("..")) {
	        			messageIn = ".";
	        		}
	        		dataMessage = dataMessage + "\r\n" + messageIn;
	        	}
        	}
        	/* Add this string to the end of dataMessage to indicate the end of the message */
        	dataMessage = dataMessage + "\r\n\r\n-END OF MESSAGE-\r\n\r\n";
		}
        valid = true;
    }
    
    /* store method */
    /**
     * This method is for storing the email components on the server itself and on a file. 
     * It goes through the recipients entered in this current loop to produce an email for each of them.
     * Each email is given a time stamp and a unique id.
     * @throws IOException
     */
    private void store() throws IOException {
    	if(reset != true) {
    		
    		/* Get the current time */
    		Timestamp stamp = new Timestamp(System.currentTimeMillis());
            String dateTime = stamp.toString();
            
            /* Create a file writer if there isnt already a file */
            File file = new File("ServerStorageFile.txt");
            if (!file.exists()) 
            {
                 file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter output = null;
            output = new BufferedWriter(fw);
            
            /* For every recipient in the list print out to the file the email for that particular recipient */
            for(String recipient : recipientStorage) {
            	/* Create unique id using the mailName and uniqueID */
            	int uniqueID = rand.nextInt(10000-999) + 999;
            	output.write("Unique ID: " + mailName + uniqueID + "\r\n\r\n");
            	output.write("Domain name: " + domainName + "\r\n");
            	output.write("DATE TIME: " + dateTime + "\r\n");
            	output.write("TO: <" + recipient.toString() + ">\r\n");
            	output.write("FROM: <" + mailName + ">\r\n");
            	output.write("SUBJECT: " + subject);
            	output.write(dataMessage);
            	
            	/* Create temporary instance of MessageObject to store the details of the current email */
            	String ID = mailName + Integer.toString(uniqueID);
            	ServerStorage temp = new ServerStorage(ID, domainName, dateTime, mailName, recipient.toString(), subject, dataMessage);
            	serverStorage.add(temp);
            }
            
            output.close();
            
            /* Used to demonstrate that the server is storing information */
            System.out.println("Current Emails Stored Within Server:\r\n\r\n");
            for(ServerStorage store : serverStorage) {
            	System.out.println("Unique ID: " + store.getID());
            	System.out.println("Domain Name: " + store.getDomain());
            	System.out.println("DATE TIME: " + store.getDate());
            	System.out.println("To: <" + store.getReceiver() + ">");
            	System.out.println("From: <" + store.getSender() + ">");
            	System.out.println("Subject: " + store.getSubject());
            	System.out.println(store.getMessage());
            }
        }
    	/* This executes when the client enters 'RSET', makes all the variables blank */
        else {
        	mailName = "";
    		recipientStorage.clear();
    		subject = "";
    		dataMessage = "";
        	response.code250();
        }
        reset = false;
    }
}