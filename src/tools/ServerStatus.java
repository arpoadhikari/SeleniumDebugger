package tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is written to get the server respone
 * @author Arpo Adhikari
 *
 */
public class ServerStatus {
	
	/**
	 * Return the server response message
	 * @param serverAddress
	 * @return Server response message
	 * @throws IOException
	 */
	public String get(String serverAddress) throws IOException {
		
		URL link = new URL(serverAddress);
		String status = null;
		HttpURLConnection connection = (HttpURLConnection) link.openConnection();
		try {
			connection.connect();
			status = connection.getResponseMessage();
			return status;
		} catch (Exception e) {
			return e.getMessage();
		}
		finally {
			connection.disconnect();
		}
	}
}
