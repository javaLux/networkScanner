package execute;

import business.MainViewApp;
import business.NetworkScan;
import common.DataBean;
import common.FxmlUtil;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class FxApp extends Application {
	
	// Data bean
	private DataBean dataBean = DataBean.getInstance();
	
	// root layout manager
	private Parent root = null;
	
	
	@Override
	public void init() {
		// before application start, check the availability of the local host IP/MAC address
		try {
			
			NetworkScan.getInstance().initLocalHostData();
			
		} catch (Exception e) {
			
			// TODO Log the exception and display a error information
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FxmlUtil fxmlUtil = new FxmlUtil(DataBean.APP_NAME);
		
    	// load FXML file to root layout container with self made
    	// FxmlUtil class 
    	this.root = fxmlUtil.loadFxmlFile("/fxml/mainView.fxml");
    	
    	if(this.root != null) {
    		
    		// initialize MainViewApp and store object in data bean
    		new MainViewApp();
    		
    		// prepare all necessary things for the application
    		this.dataBean.getMainViewApp().prepareListener();
    		this.dataBean.getMainViewApp().prepareTLineProgressBar();
    		this.dataBean.getMainViewApp().prepareTableColumns();
    		
    		Scene scene = new Scene(this.root);
    		
    		// load specific Style sheet file
    		scene.getStylesheets().add(this.getClass().getResource("/css/mainView.css").toExternalForm());
    		
    		primaryStage.setScene(scene);
    		
    		// set minimum size of the main view
    		primaryStage.setMinHeight(200.0);
    		primaryStage.setMinWidth(350.0);
    		
    		// set main view icon
        	primaryStage.getIcons().add(new Image("images/network.png"));
    		
    		primaryStage.setTitle(DataBean.APP_NAME);
    		
    		primaryStage.show();
    	
    	}
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
