package tools.customDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;

/**
 * This class extends RemoteWebDriver class
 * @author Arpo Adhikari
 *
 */
public class AttachedWebDriver extends RemoteWebDriver{

	/**
	 * This method is overridden to attach a WebDriver object to an existing session
	 * @param sessionId
	 * @throws MalformedURLException
	 */
	public AttachedWebDriver(String sessionId) throws MalformedURLException {

		super();
		setSessionId(sessionId);
		URL url = new URL("http://localhost:4444/wd/hub");
		setCommandExecutor(new HttpCommandExecutor(url) {
			@Override
			public Response execute(Command command) throws IOException {
				if(command.getName()!="newSession") {
					return super.execute(command);
				}
				return super.execute(new Command(getSessionId(),"getCapabilities"));
			}
		});
		startSession(new DesiredCapabilities());
	}
}
