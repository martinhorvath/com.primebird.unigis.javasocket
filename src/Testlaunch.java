import java.io.Console;
import java.io.IOException;

public class Testlaunch {
	private static String updateurl = "http://unigis.primebird.com/demo/MapBs.php";
	private static int DEFAULT_PORT = 19999;

	/**
	 * Tested with
	 * GPRMC,090248.6,A,5023.012742,N,02202.143827,E,0.0,,310710,,,A*48$0221348#
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		UnigisSocket socket = new UnigisSocket();
		socket.initialize(DEFAULT_PORT, updateurl);
		try {
			socket.listen();
		}
		catch (Exception ex) {
			System.out.println("Listening failed... " + ex.getMessage());
		}
	}
}