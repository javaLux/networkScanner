package business;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class to manage the network scan operations.
 * 
 * @author Christian
 *
 */
public class NetworkScan {
	
	// Singleton instance
	private static NetworkScan instance = null;
	
	// Time out in milliseconds for ping response
	final int timeOut = 5000;
	
	// byte array for the IPv4 address of the local host
	private byte[] ip = null;
	
	
	// private constructor
	private NetworkScan() {}
	
	public static NetworkScan getInstance() {
		
		if(instance == null) {
			synchronized (NetworkScan.class) {
				if(instance == null) {
					instance = new NetworkScan();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Method to get IP address from local host.
	 */
	public void getLocalHostIp() {
		try {
			this.ip = Inet4Address.getLocalHost().getAddress();
		} catch (UnknownHostException e) {
			
			// TODO log exception and info at user, abort program
			e.printStackTrace();
		}
	}
	
	/**
	 * Method send a PING to a IP address (dependent on the given host) in the current network.
	 * @param hostAddress	->	[Integer]	the current host address of a IP
	 * @throws Exception	->	throws a Exception if the IP address has a illegal length
	 * @return	->	if IP is reachable than the InetAddress object, otherwise null
	 */
	public InetAddress scanHostOnNetwork(int hostAddress) throws Exception {
		
		// replace the host address from local host with given host address
		ip[3] = (byte) hostAddress;
		
        InetAddress address = Inet4Address.getByAddress(ip);
        
        if (address.isReachable(timeOut)) {
        	
        	return address;
        }
        
        return null;
	}
}
