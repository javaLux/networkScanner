/**
 * 
 */
package common;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import business.ReachableHost;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Christian
 * 
 *         Class provides various sorting methods.
 */
public class OwnSortHelper {

	// Singleton instance
	private static OwnSortHelper instance = null;

	// private constructor
	private OwnSortHelper() {}

	public static OwnSortHelper getInstance() {

		if (instance == null) {
			synchronized (OwnSortHelper.class) {
				if (instance == null) {
					instance = new OwnSortHelper();
				}
			}
		}
		return instance;
	}

	/**
	 * Method sort the given map with the reachable hosts by the IP address.
	 * 
	 * @param mapToSort -> [Map<String, String>] map to be sorted by IP address
	 * @return -> the sorted map
	 */
	public Map<String, String> sortMapByIpAddress(Map<String, String> mapToSort) {

		Map<String, String> sortedMap = new LinkedHashMap<>();

		// specific Comparator to compare two IP addresses and sort them in a right way
		Comparator<String> ipComparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String[] ip1 = o1.split("\\.");
				String[] ip2 = o2.split("\\.");

				if (ip1.length == 4 && ip2.length == 4) {
					// only if string array of split IP address is correct, compares the IP
					// addresses
					String ipFormatted1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3]);
					String ipFormatted2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3]);

					return ipFormatted1.compareTo(ipFormatted2);
				}
				// if string array not correct return 0
				return 0;
			}
		};

		// use STREAM API
		mapToSort.entrySet() 											// get an entry set to create stream object
				.stream() 												// create a new stream object
				.sorted(Map.Entry.comparingByKey(ipComparator)) 		// sort all entries in the stream by the key (IP) with a specific ipComparator 
				.forEachOrdered(entry -> { 								// put the sorted entries in the new sorted map
					sortedMap.put(entry.getKey(), entry.getValue());
				});

		return sortedMap;
	}

	/**
	 * Method sort the given map with the reachable hosts by the IP address.
	 * 
	 * @param hostList -> [ObservableList<ReachableHost>] list to be sorted by IP address
	 * @return -> the sorted list
	 */
	public ObservableList<ReachableHost> sortReachableHosts(ObservableList<ReachableHost> hostList) {
		
		ObservableList<ReachableHost> sortedHostList = FXCollections.observableArrayList();

		// specific Comparator to compare two IP addresses and sort them in a right way
		Comparator<ReachableHost> ipComparator = new Comparator<ReachableHost>() {

			@Override
			public int compare(ReachableHost o1, ReachableHost o2) {

				String[] ip1 = o1.getIpAddress().split("\\.");
				String[] ip2 = o2.getIpAddress().split("\\.");

				if (ip1.length == 4 && ip2.length == 4) {
					// only if string array of split IP address is correct, compares the IP
					// addresses
					String ipFormatted1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3]);
					String ipFormatted2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3]);

					return ipFormatted1.compareTo(ipFormatted2);
				}
				// if string array not correct return 0
				return 0;
			}
		};

		// use STREAM API		
		hostList.stream() 						// create a new stream object
				.sorted(ipComparator)			// sort all entries in the stream by the IP with a specific ipComparator
				.forEach( host -> {				// add sorted hosts to the sorted list
					sortedHostList.add(host);
				});; 	
				
		return sortedHostList;
	}
}
