/**
 * 
 * Nick Pawlowski
 * pawlowski.nick@live.com
 * 
 */
package de.pawni.ledservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.pawni.ledservice.common.model.LEDStatus;

/**
 * @author Nick Pawlowski
 *
 */
public interface Main extends Remote{
	public void start() throws RemoteException;
	
	public void stop() throws RemoteException;
	
	public LEDStatus status() throws RemoteException;
	
	public void setLEDStatus(LEDStatus status) throws RemoteException;
}
