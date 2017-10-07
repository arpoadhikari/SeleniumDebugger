package appWindow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import tools.BrowserFactory;
import tools.CheckForLock;
import tools.ServerStatus;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Control;

/**
 * This class is for Application UI
 * @author Arpo Adhikari
 *
 */
public class MainUI {

	protected Shell shlSessionGenerator;
	private Text winConsole;

	ServerStatus serverStatus = new ServerStatus();
	RemoteControlConfiguration serverConfig = new RemoteControlConfiguration();
	int portNo = 4444;
	int timeoutSec = 1800;
	SeleniumServer server;
	String browserName = "Firefox";
	String hostName = "localhost";
	String serverURL = "http://"+hostName+":"+portNo+"/wd/hub";
	BrowserFactory factory = new BrowserFactory();
	CheckForLock ua = new CheckForLock("SessionGenerator");
	private Combo browsers;
	private Button btnStart;
	private Button btnStop;

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		try {
			MainUI window = new MainUI();
			window.open();
		} catch (Exception e) {
			System.out.println("Unable to open the UI : "+e.getMessage());
		}
	}

	/**
	 * Method for starting server and browser session
	 */
	public void doStart() {
		try {

			if(serverStatus.get(serverURL).equalsIgnoreCase("OK")) {
				winConsole.append("Server is already RUNNING.\n");				
			}
			else {
				winConsole.append("Trying to start the server.\n");
				serverConfig.setPort(portNo);
				serverConfig.setTimeoutInSeconds(timeoutSec);
				server = new SeleniumServer(false, serverConfig);
				server.boot();
				winConsole.append("Server is STARTED Now.\n");
			}
			winConsole.append("Trying to launch the browser.\n");
			winConsole.append(factory.openBrowser(serverURL, browserName));
			winConsole.append("Session ID : {"+factory.getSessionId()+"}\n");

		} catch (Exception e1) {
			winConsole.append("ERROR while trying to start the server/browser : "+e1.getMessage()+"\n");
		}
	}

	/**
	 *  Method for closing server and browser session
	 */
	public void doStop() {	
		try {
			if(serverStatus.get(serverURL).equalsIgnoreCase("OK")) {	
				winConsole.append(factory.closeBrowser());
				server.stop();
				winConsole.append("Server is STOPPED now.\n");
			}
			else {
				winConsole.append("Server was already STOPPED.\n");
			}
		} catch (IOException e1) {
			winConsole.append("ERROR while trying to close the server/browser : "+e1.getMessage()+"\n");
		}
	}

	/**
	 * Open the window.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void open() throws IOException, InterruptedException {
		if (ua.isAppActive()) {
			System.out.println("Application is already running.");
			Thread.sleep(1000);
			System.exit(0);
		}
		Display display = Display.getDefault();
		createContents();
		//---
		Image small = new Image(display,getClass().getResourceAsStream("/resources/icon.png")); 
		shlSessionGenerator.setImage(small);
		shlSessionGenerator.setTabList(new Control[]{browsers, winConsole, btnStart, btnStop});
		//---
		shlSessionGenerator.open();
		shlSessionGenerator.layout();
		while (!shlSessionGenerator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.close();
		System.exit(0);
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlSessionGenerator = new Shell(SWT.DIALOG_TRIM | SWT.MIN);
		shlSessionGenerator.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		shlSessionGenerator.setToolTipText("Session Generator v1.0");
		shlSessionGenerator.setMinimumSize(new Point(400, 330));
		shlSessionGenerator.setSize(400, 350);
		shlSessionGenerator.setText("Session Generator v1.0");
		shlSessionGenerator.setLayout(null);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point frameSize = shlSessionGenerator.getSize();
		int x = (screenSize.width - frameSize.x) / 2;
		int y = (screenSize.height - frameSize.y) / 2;
		shlSessionGenerator.setLocation(x, y);

		btnStart = new Button(shlSessionGenerator, SWT.NONE);
		btnStart.setImage(SWTResourceManager.getImage(MainUI.class, "/resources/start.png"));
		btnStart.setToolTipText("Create a Session");
		btnStart.setBounds(122, 275, 64, 32);

		winConsole = new Text(shlSessionGenerator, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		winConsole.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		winConsole.setBounds(10, 79, 374, 180);
		winConsole.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		browsers = new Combo(shlSessionGenerator, SWT.READ_ONLY);
		browsers.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		browsers.setToolTipText("Select a Browser to Launch");
		browsers.setBounds(112, 39, 170, 31);
		browsers.setItems(new String[] {"Firefox", "Chrome", "Internet Explorer"});
		browsers.select(0);

		Label lblBrowser = new Label(shlSessionGenerator, SWT.NONE);
		lblBrowser.setAlignment(SWT.CENTER);
		lblBrowser.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblBrowser.setBounds(122, 10, 150, 23);
		lblBrowser.setText("Choose a Browser :");

		btnStop = new Button(shlSessionGenerator, SWT.NONE);
		btnStop.setImage(SWTResourceManager.getImage(MainUI.class, "/resources/stop.png"));
		btnStop.setToolTipText("Delete the Session");
		btnStop.setBounds(208, 275, 64, 32);

		// Event listener from Browser DropDown
		browsers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				browserName = browsers.getItem(browsers.getSelectionIndex());
			}
		});

		// Event listener for Start button
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doStart();
			}
		});

		// Event listener for Stop button
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doStop();
			}
		});

		// Event listener for Window Close
		shlSessionGenerator.addShellListener(new ShellListener()
		{
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void shellClosed(ShellEvent arg0) {	
				int style = SWT.ICON_WARNING | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shlSessionGenerator, style);
				messageBox.setText("Warning");
				messageBox.setMessage("Server and browser session will be terminated. Continue ?");
				if (messageBox.open() == SWT.YES) {
					doStop();
					arg0.doit = true;
				}
				else {
					arg0.doit = false;
				}
			}

			@Override
			public void shellDeactivated(ShellEvent arg0) {
				// TODO Auto-generated method stub		
			}

			@Override
			public void shellDeiconified(ShellEvent arg0) {
				// TODO Auto-generated method stub		
			}

			@Override
			public void shellIconified(ShellEvent arg0) {
				// TODO Auto-generated method stub		
			}
		});
	}
}
