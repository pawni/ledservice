/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;

import de.pawni.ledservice.common.model.LEDColor;
import de.pawni.ledservice.common.model.LEDStatus;
import de.pawni.ledservice.ledcontrol.LEDController;
import de.pawni.ledservice.service.CalenderScheduler;

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
		String command = args[0].split("=")[0];
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
				case "list":
					showRegistry();
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
		System.out.println("Command " + command + " executed");
	}
	
	public static void startup() throws RemoteException {
		self.start();
	}
	
	public static void shutdown() throws AccessException, RemoteException, NotBoundException {
		Main instance = connectToRMI();
		instance.stop();
	}
	
	public static void showStatus() throws AccessException, RemoteException, NotBoundException {
		Main instance = connectToRMI();
		LEDStatus status = instance.status();
		System.out.println("Status is:\n"+status);
	}
	
	public static void setRGB(String[] args) throws IllegalArgumentException, AccessException, RemoteException, NotBoundException{
		if(args[0].split("=").length != 2) {
			throw new IllegalArgumentException("No color to set specified.");
		}
		System.out.println("Got color "+args[0].split("=")[1]);
		LEDStatus status = new LEDStatus(args[0].split("=")[1]);
		System.out.println("Set Status to "+status.toString());
		Main instance = connectToRMI();
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
	
	public static Main connectToRMI() throws AccessException, RemoteException, NotBoundException {
		Remote r = getRegistry().lookup(lookupURL);
		Main res = (Main) r;
		return res;
	}
	
	public static Registry getRegistry() throws RemoteException {
		try {
			return LocateRegistry.createRegistry(1099);
		} catch(RemoteException e) {
			return LocateRegistry.getRegistry(1099);
		}
		
	}
	
	public static void showRegistry() throws RemoteException, NotBoundException{
		Registry r = getRegistry();
		System.out.println("Found in this Registry: ");
		for (String s : r.list()) {
			System.out.println(s + " - type: " + r.lookup(s).getClass());
		}
	}
	
	public MainImpl() {
		
	}
	
	private Timer timer;
	
	public void start() throws RemoteException{
		System.out.println("Starting LEDService");
		UnicastRemoteObject.exportObject(this, 1099);
		
		Registry r = getRegistry();
		
		try {
			r.bind(lookupURL, this);
			
		} catch (AlreadyBoundException e) {
			throw new RemoteException("Already bound", e);
		}
		
		String[] list = r.list();
		System.out.println("In this registry: "+list[0]);
		
		//CoreService.start();
		// start other things ..
		
		timer = new Timer(true);
		int recurrentRate = 6 * 60; // minutes
		CalenderScheduler cs = new CalenderScheduler(recurrentRate);
		timer.scheduleAtFixedRate(cs, 0, recurrentRate * 60 * 1000);
		
		System.out.println("LEDService started");
	}
	
	public void stop() {
		System.out.println("Stopping LEDService");
		// stop servicethread	
		LEDController.getInstance().setRGB(new LEDStatus(new LEDColor(0, 0, 0)));
		//CoreService.stop();
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			getRegistry().unbind(lookupURL);
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Stopped LEDService");
	}
	
	public LEDStatus status() {
		return LEDController.getInstance().getStatus();
	}


	public void setLEDStatus(LEDStatus status) {
		System.out.println("Setting LEDStatus: "+status.toString());
		LEDController.getInstance().fadeToRGB(status);
	}

}
