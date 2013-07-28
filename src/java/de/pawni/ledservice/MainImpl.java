/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice;

import java.awt.Color;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.pawni.ledservice.common.model.LEDStatus;
import de.pawni.ledservice.ledcontrol.LEDController;

/**
 * @author Nick Pawlowski
 *
 */
public class MainImpl implements Main{

	private static MainImpl self;
	private static String lookupURL = "rmi://localhost/ledservice";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		self = new MainImpl();

		if(args.length == 0) {
			// error message
			showHelp();
			return;
		}
		String command = args[0];
		System.out.println("Got command: " + command);
		try {
			switch(command.toLowerCase()) {
				case "start":
					startup();
					break;
				case "stop":
					shutdown();
					break;
				case "status":
					showStatus();
					break;
				case "setrgb":
					setRGB(args);
					break;
				default:
					showHelp();
					break;
			}
		} catch (AccessException e) {
			System.out.println("An AccessException occured: "+ e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("An NotBoundException occured: "+ e.getMessage());
			e.printStackTrace();
		} catch(RemoteException e) {
			System.out.println("An RemoteException occured: "+ e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static void startup() throws RemoteException {
		self.start();
	}
	
	public static void shutdown() throws AccessException, RemoteException, NotBoundException {
		MainImpl instance = connectToRMI();
		instance.stop();
	}
	
	public static void showStatus() throws AccessException, RemoteException, NotBoundException {
		MainImpl instance = connectToRMI();
		LEDStatus status = instance.status();
		System.out.println("Status is:\n"+status);
	}
	
	public static void setRGB(String[] args) throws IllegalArgumentException, AccessException, RemoteException, NotBoundException{
		if(args.length != 2) {
			throw new IllegalArgumentException("No color to set specified.");
		}
		LEDStatus status = new LEDStatus(args[1]);
		MainImpl instance = connectToRMI();
		instance.setLEDStatus(status);
	}
	
	public static void showHelp() {
		System.out.println("Usage: java ledservice.jar start|stop|help|status|setrgb");
		System.out.println("start - starts the service");
		System.out.println("stop - stops the service");
		System.out.println("help - shows this page");
		System.out.println("status - shows the status of the leds");
		System.out.println("setrgb #AABBCC - sets the color to #AABBCC");
	}
	
	public static MainImpl connectToRMI() throws AccessException, RemoteException, NotBoundException {
		MainImpl res = (MainImpl) getRegistry().lookup(lookupURL);
		return res;
	}
	
	public static Registry getRegistry() throws RemoteException {
		try {
			return LocateRegistry.createRegistry(1099);
		} catch(RemoteException e) {
			return LocateRegistry.getRegistry(1099);
		}
		
	}
	
	public MainImpl() {
		
	}
	
	
	
	public void start() throws RemoteException{
		System.out.println("Starting LEDService");
		UnicastRemoteObject.exportObject(this);
		
		Registry r = getRegistry();
		
		try {
			r.bind(lookupURL, this);
		} catch (AlreadyBoundException e) {
			throw new RemoteException("Already bound", e);
		}
		
		// start other things ..
		System.out.println("LEDService started");
	}
	
	public void stop() {
		System.out.println("Stopping LEDService");
		// stop servicethread
		LEDController.getInstance().setRGB(new LEDStatus(Color.BLACK));
		System.out.println("Stopped LEDService");
	}
	
	public LEDStatus status() {
		return LEDController.getInstance().getStatus();
	}


	public void setLEDStatus(LEDStatus status) {
		System.out.println("Setting LEDStatus: "+status);
		LEDController.getInstance().setRGB(status);
	}

}
