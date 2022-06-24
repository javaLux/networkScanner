/**
 * 
 */
package common;

import business.MainViewApp;
import business.ReachableHost;
import controller.MainViewController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Christian
 *
 */
public class DataBean {

	// Singleton instance
	private static DataBean instance = null;
	
	// static fields
	//
	// Application name
	public static final String APP_NAME = "Simple-Network-Scanner";
	
	// highest host address (24 bit Suffix) -> class c network
	public static final int MAX_HOST_ADDRESS = 254;
	
	// Observable list for the reachable hosts
	private ObservableList<ReachableHost> reachableHosts = FXCollections.observableArrayList();
	
	// Thread counter for the background threads to scan the network
	private IntegerProperty threadCounter = new SimpleIntegerProperty(0);
	
	// Counter for the UNKNOWN hosts
	private IntegerProperty unknownHostsCounter = new SimpleIntegerProperty(0);
	
	// instance of MainView Controller class
	private MainViewController mainViewController = null;
	
	// instance of the main view application class
	private MainViewApp mainViewApp = null;
	
	// private constructor
	private DataBean() {}
	
	// GETTER for Thread safe Singleton instance
	public static DataBean getInstance() {
		
		if(instance == null) {
			synchronized (DataBean.class) {
				if(instance == null) {
					instance = new DataBean();
				}
			}
		}
		
		return instance;
	}
	
	
	/**
	 * Method reset all counters
	 */
	public void resetCounters() {
		this.threadCounter.set(0);
		this.unknownHostsCounter.set(0);
	}

	// GETTER and SETTER
	//
	public MainViewController getMainViewController() {
		return this.mainViewController;
	}
	
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}
	
	public MainViewApp getMainViewApp() {
		return this.mainViewApp;
	}
	
	public void setMainViewApp(MainViewApp mainViewApp) {
		this.mainViewApp = mainViewApp;
	}

	public ObservableList<ReachableHost> getReachableHosts() {
		return this.reachableHosts;
	}

	public IntegerProperty getUnknownHostsCounter() {
		return this.unknownHostsCounter;
	}

	public IntegerProperty getThreadCounter() {
		return this.threadCounter;
	}
}
