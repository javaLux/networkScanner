/**
 * 
 */
package business;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Christian
 *
 * Class holds IP and host name for an reachable host on the current network.
 */
public class ReachableHost {

	private final SimpleStringProperty ipAddress;
	private final SimpleStringProperty hostName;
	
	/**
	 * Constructor initialize IP-address and host name for this reachable host
	 * @param ipAddress
	 * @param hostName
	 */
	public ReachableHost(String ipAddress, String hostName) {
		
		this.ipAddress = new SimpleStringProperty(ipAddress);
		this.hostName = new SimpleStringProperty(hostName);
	}

	public String getIpAddress() {
		return this.ipAddress.get();
	}

	public String getHostName() {
		return this.hostName.get();
	}
	
	
}
