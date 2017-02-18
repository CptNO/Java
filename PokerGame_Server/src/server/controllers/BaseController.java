package server.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {

	protected MainController mainController;
	
	@Override
	public abstract void initialize(URL location, ResourceBundle resources);

	public void injectController(Object controller) {
		mainController = (MainController)controller;
	}
}
