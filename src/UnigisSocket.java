import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class UnigisSocket
{
  static ServerSocket socket1; 
  private Controller controller; 
  private Socket connection;
	
  public void initialize(int port, String updateurl)
  {
  	controller = new Controller(updateurl);
    try
	{
    	socket1 = new ServerSocket(19999);
	}
    catch (IOException ex)
	{
		return;
	} 
  }
  
  public void listen() throws Exception {
	  while (true) {
		  try {
			  connection = socket1.accept();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
		  StringBuffer isrData = new StringBuffer();
		  BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
		  InputStreamReader isr = new InputStreamReader(is);
		  int asciiVal;

		  while ((asciiVal = isr.read()) > 0) {
			  isrData.append((char) asciiVal);     /* to convert the ascii values into a string, readable by the controller */
		  }

		  System.out.println(controller.processStreamResult(isrData.toString()));
		  isr.close();
		  is.close();
		  connection.close();
	  }
  }
}