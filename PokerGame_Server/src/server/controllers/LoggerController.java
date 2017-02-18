package server.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;


public class LoggerController extends BaseController {

	@FXML
    private ListView<Label> loggerList;

    private ObservableList<Label> logList;
    
    private ListProperty<Label> listProperty;
    

	public enum LoggerLevel{
		INFO,
		ERROR,
		WARNING
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        logList = FXCollections.observableArrayList();
        listProperty = new SimpleListProperty<>();
        listProperty.set(logList);
        loggerList.itemsProperty().bind(listProperty);
	}
	
	public void log(final String message, LoggerLevel level) {
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	Label label = createLabel(level);
	            	label.setText(level + "[" + Calendar.getInstance().getTime() + "]" + message);
	                logList.add(label);
	                if(level == LoggerLevel.ERROR){
	                	mainController.shutDownServer();
	                }
	            }
	        });
	    }
	
	private Label createLabel(LoggerLevel level){
		Label label = new Label();
		switch (level) {
		case INFO:
			label.setTextFill(Color.web("#00529B"));
			return label;
		case ERROR:
			label.setTextFill(Color.web("#D8000C"));
			return label;
		case WARNING:
			label.setTextFill(Color.web("#9F6000"));
			return label;
		default:
			return label;
		}
	}
}
