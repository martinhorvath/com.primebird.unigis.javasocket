import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class UnigisSocket {  /* Class for socket processing of GPS data to a controller  */
  static ServerSocket socket1; 
  private Controller controller; 
  private Socket connection; /* connection endpoint for mobile devices */
	
  public void initialize(int port, String updateurl) /* to initialize the server; create an endpoint and make it available */
    { controller = new Controller(updateurl);
	    try {                               /* try function in case no port is available */
	    	socket1 = new ServerSocket(port);
	    } /* any port that is available */
	    catch (IOException ex) {
			  return;                   /* no further exception statement in a void method */
		     } 
    }
  
  public void listen() throws IOException
  {  /* listen to the socket: method to make the program listen whether a device tries to connect in order to accept the connection and process the data */
  	  while (true) {              /* infinite while loop as we needs this all the time */
  		  try
  		  {
  			  connection = socket1.accept();  /* accept a connection to the socket */
		  }
	  	  catch (IOException e)
  		  {     /* do not stop the program if connection failed */
	  		  e.printStackTrace();
  		  }
  		  StringBuffer isrData =new StringBuffer(); /* to store data  */
  		  BufferedInputStream is = new BufferedInputStream(connection.getInputStream());  /* to get the data from our connection */
  		  InputStreamReader isr = new InputStreamReader (is); /* to read the input data */
  		  int asciiVal;
	 
  		  while ((asciiVal = isr.read())>0) {  /* "While" is used to read the data stream until the end is reached instead of just the first character; the method "read" returns ascii values; the method returns "-1" when the end of the stream is reached. This condition allows to stop the while loop instead of an infinite reading process (until all memory is full) */
  			  isrData.append((char)asciiVal);	 /* to convert the ascii values into a string, readable by the controller */
  		  }
   
  		  System.out.println(controller.processStreamResult(isrData.toString())); /* the data needs to be passed on to the controller (new class) */
	
  		  /* to close readers and connections:  */
  		  isr.close();      
  		  is.close();
  		  connection.close();
  	  }
  }
}