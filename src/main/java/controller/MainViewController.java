/**
 * 
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;

import business.ReachableHost;
import common.DataBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * @author Christian
 * Controller class for the FXML main view.
 */
public class MainViewController implements Initializable {
	
	// Data bean
	private DataBean dataBean = DataBean.getInstance();

	@FXML
    private Button btnStart;
	
	@FXML
    private Button btnStop;
	
	@FXML
	private Label lblAppStatus;
	 
	@FXML
    private Label lblHostName;
	
	@FXML
    private Label lblLocalHostIP;

    @FXML
    private Label lblLocalHostMac;
	    
	@FXML
	private Label lblUnknownHostValue;

	@FXML
    private Label lblInfo;

    @FXML
    private Label lblPercent;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableView<ReachableHost> tableView;
    
    @FXML
    private TableColumn<ReachableHost, String> tableColumnHostname;

    @FXML
    private TableColumn<ReachableHost, String> tableColumnIP;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// save this controller instance in data bean
		DataBean.getInstance().setMainViewController(this);
	}
    
    // EVENT-Handling
    //
    @FXML
    void btnStartMouseClicked(MouseEvent event) {
    	
    	// reset boolean property, which indicates the scan process was aborted by user
    	this.dataBean.getIsScanAborted().set(false);
    	
    	// make progress bar visible
    	if(! this.dataBean.getMainViewController().getProgressBar().isVisible()) {
    		this.dataBean.getMainViewController().getProgressBar().setVisible(true);
    	}
    	
    	// reset all necessary counters
    	this.dataBean.resetCounters();
		// set label for unknown hosts again to value 254
    	this.dataBean.getMainViewController().getLblUnknownHostValue().setText("" + DataBean.MAX_HOST_ADDRESS);
    	// set property of the percent label to show only text -> to hide 'done' image
    	this.dataBean.getMainViewController().getLblPercent().setContentDisplay(ContentDisplay.TEXT_ONLY);
    	// clear list for reachable hosts to get on each scan process a new result
    	this.dataBean.getReachableHosts().clear();
    	// clean up table view from last scan results
    	this.dataBean.getMainViewController().getTableView().getItems().clear();
    	// start Time line to update progress bar and start network scan
    	this.dataBean.getMainViewApp().startScan();
    	this.dataBean.getMainViewApp().getTimeLineProgressBar().play();    	
    	// disable start button until time line line stopped and scan is done
    	this.btnStart.setDisable(true);
    }
    
    @FXML
    void btnStopMouseClicked(MouseEvent event) {
    	
    	// First: set boolean property for aborted scan to true
    	this.dataBean.getIsScanAborted().set(true);
    	
    	// Second: stop time line to updates progress bar
    	this.dataBean.getMainViewApp().getTimeLineProgressBar().stop();  	
    }

	// GETTER and SETTER
    //
	public Button getBtnStart() {
		return this.btnStart;
	}

	public Button getBtnStop() {
		return this.btnStop;
	}

	public Label getLblHostName() {
		return this.lblHostName;
	}

	public Label getLblLocalHostIP() {
		return this.lblLocalHostIP;
	}

	public Label getLblLocalHostMac() {
		return this.lblLocalHostMac;
	}

	public Label getLblAppStatus() {
		return this.lblAppStatus;
	}

	public Label getLblInfo() {
		return this.lblInfo;
	}
	
	public Label getLblUnknownHostValue() {
		return this.lblUnknownHostValue;
	}

	public Label getLblPercent() {
		return this.lblPercent;
	}

	public ProgressBar getProgressBar() {
		return this.progressBar;
	}

	public TableView<ReachableHost> getTableView() {
		return this.tableView;
	}

	public TableColumn<ReachableHost, String> getTableColumnHostname() {
		return this.tableColumnHostname;
	}

	public TableColumn<ReachableHost, String> getTableColumnIP() {
		return this.tableColumnIP;
	}
}