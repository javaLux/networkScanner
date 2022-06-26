package business;

import java.net.InetAddress;
import java.net.NetworkInterface;

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
	private byte[] rawIpLocalHost = null;
	
	// String representation of the local host IP address
	private String ipLocalHost = "";
	
	// MAC address local host
	private String macAddressLocalHost = "";
	
	
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
	 * Method to get basic network information from the local host.
	 * 
	 * @throws Exception 
	 */
	public void initLocalHostData() throws Exception {
		
			InetAddress iNetAd = InetAddress.getLocalHost();
			
			// get raw data for the local host IP address
			this.rawIpLocalHost = iNetAd.getAddress();
			
			// get the string format of the local host IP address
			this.ipLocalHost = iNetAd.getHostAddress();
			
			NetworkInterface netInterface = NetworkInterface.getByInetAddress(iNetAd);
			
			if(netInterface != null) {
				// get MAC from local host
				byte[] hardwareAddress = netInterface.getHardwareAddress();
				
				// format the result in a string representation of an MAC address
				String[] hexaDecimal = new String[hardwareAddress.length];
				for (int i = 0; i < hardwareAddress.length; i++) {
					hexaDecimal[i] = String.format("%02X", hardwareAddress[i]);
				}
				this.macAddressLocalHost = String.join("-", hexaDecimal);
			}
			else {
				this.macAddressLocalHost = "Unknown";
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
		rawIpLocalHost[3] = (byte) hostAddress;
		
        InetAddress localIp = InetAddress.getByAddress(rawIpLocalHost);
        
        if (localIp.isReachable(timeOut)) {
        	
        	return localIp;
        }
        
        return null;
	}

	public String getIpLocalHost() {
		return this.ipLocalHost;
	}

	public String getMacAddressLocalHost() {
		return this.macAddressLocalHost;
	}
}
