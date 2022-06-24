/**
 * 
 */
package business;

import java.net.InetAddress;

import common.DataBean;
import common.OwnSortHelper;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * @author Christian
 *
 */
public class MainViewApp {

	// Data bean
	private DataBean dataBean = DataBean.getInstance();
	
	// Done image
	private ImageView imgDone = new ImageView(new Image("/images/done.png"));

	// Time line to update progress bar
	Timeline tLineProgressBar = null;

	/**
	 * Constructor store an instance of this class in data bean and initialize
	 * members.
	 */
	public MainViewApp() {

		dataBean.setMainViewApp(this);
		this.tLineProgressBar = new Timeline();
	}
	
	/**
	 * Method define which values we will store in each table column.
	 */
	public void prepareTableColumns() {
		this.dataBean.getMainViewController().getTableColumnIP().setCellValueFactory(new PropertyValueFactory<ReachableHost, String>("ipAddress"));
		this.dataBean.getMainViewController().getTableColumnHostname().setCellValueFactory(new PropertyValueFactory<ReachableHost, String>("hostName"));
			
	}

	public void prepareListener() {

		// add listener to observe the current thread counter
		this.dataBean.getThreadCounter().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				// calculate current percent value ( 100% = 254, 1% = 2.54)
				double percent = newValue.intValue() / 2.54;
				// build string to display percent value with one decimal place
				String currentDone = String.format("%.1f %s", percent, "%");
				
				// set current percent value to label
				dataBean.getMainViewController().getLblPercent().setText(currentDone);
			}

		});
		
		// add listener to observe if list with reachable hosts changed
		this.dataBean.getReachableHosts().addListener(new ListChangeListener<ReachableHost>() {

			@Override
			public void onChanged(Change<? extends ReachableHost> c) {
				
				// First:	sort the reachable host list by IP address
				ObservableList<ReachableHost> sortedList = OwnSortHelper.getInstance().sortReachableHosts(dataBean.getReachableHosts());
				
				// Second:	update table view with sorted list
				dataBean.getMainViewController().getTableView().setItems(sortedList);
			}
		});
		
		// add listener to observe on which time the time line was stopped
		this.tLineProgressBar.statusProperty().addListener(new ChangeListener<Status>() {

			@Override
			public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
				
				// if time line for update progress bar stopped -> then reset thread counter
				if(newValue == Status.STOPPED) {
					
					// set property to show only a image on the percent label and add 'done' image to it
					dataBean.getMainViewController().getLblPercent().setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					dataBean.getMainViewController().getLblPercent().setGraphic(imgDone);
					// reactivate button start
					dataBean.getMainViewController().getBtnStart().setDisable(false);				
				}
			}
		});
		
		// add listener to observe changes on the unknown host counter
		this.dataBean.getUnknownHostsCounter().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// update label for unknown hosts value
				dataBean.getMainViewController().getLblUnknownHostValue().setText(newValue.toString());
			}
		});
	}
	
	public void prepareTLineProgressBar() {
		
		this.tLineProgressBar.setCycleCount(Timeline.INDEFINITE);
		
		this.tLineProgressBar.getKeyFrames().add(new KeyFrame(Duration.millis(100.0), event -> {
			
			// calculate percent value to update progress bar (value must divided by 100 because only values between 0.0 - 1.0 allowed)
			double valueProgressBar = dataBean.getThreadCounter().get() / 2.54 / 100;
			// update progress bar value
			dataBean.getMainViewController().getProgressBar().setProgress(valueProgressBar);
			
			
			if(dataBean.getThreadCounter().get() == DataBean.MAX_HOST_ADDRESS) {
				
				// stop time line for update progress bar
				this.tLineProgressBar.stop();
			}
		}));
	}

	public void startScan() {
		
		// iterate over all possible host ID's in the current network
		for (int i = 1; i < DataBean.MAX_HOST_ADDRESS + 1; i++) {

			final int j = i;

			// IMPORTANT: initialize for each host a new background thread (Service)
			// to make network scan faster.
			new Service<Void>() {

				/**
				 * Constructor of anonymous class 'Service'
				 */
				{
					// add Event handling for the succeeded event of this service
					this.setOnSucceeded(event -> {

						// increment the thread counter if the current running background thread is done
						dataBean.getThreadCounter().set(dataBean.getThreadCounter().getValue() + 1);
					
						// calculate the current unknown hosts and set to the unknown host counter
						dataBean.getUnknownHostsCounter().set(DataBean.MAX_HOST_ADDRESS - dataBean.getReachableHosts().size());
					});
					
					// add Event handling for the failed event of this service
					this.setOnFailed(event -> {
						// in case of that service failed, we must also increment the thread counter,
						// because the time line to update the progress bar will never ending without this step
						dataBean.getThreadCounter().set(dataBean.getThreadCounter().getValue() + 1);			
						
					});
				}

				@Override
				protected Task<Void> createTask() {

					Task<Void> task = new Task<Void>() {

						@Override
						protected Void call() throws Exception {

							try {
								// check IP address
								InetAddress ipAddress = NetworkScan.getInstance().scanHostOnNetwork(j);
								
								if(ipAddress != null) {
						        	// when host reachable on the network, put them in the observable list
						        	dataBean.getReachableHosts().add(new ReachableHost(ipAddress.getHostAddress(), ipAddress.getHostName()));
								}
								
							} catch (Exception e) {
								// if exception was thrown -> this current service will be automatically set to state FAILED
								// TODO: log the exception (log4j)
								e.printStackTrace();
								
							}

							return null;
						}
					};

					return task;
				}
			}.start();
		}
	}
	
	// GETTER and SETTER
	//
	public Timeline getTimeLineProgressBar() {
		return this.tLineProgressBar;
	}
}
