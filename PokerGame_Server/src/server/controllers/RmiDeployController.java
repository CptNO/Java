package server.controllers;

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.Remote;

import application.Settings;
import server.controllers.LoggerController.LoggerLevel;

public class RmiDeployController {

	private MainController mainController;
	
	public void initRmi(){
		try{
		    getLogger().log("Initializing RMI", LoggerLevel.INFO);
	    	java.rmi.registry.LocateRegistry.createRegistry(1099);
			deployObjects();
		}catch(Exception ex){
			getLogger().log("RMI initalization problem \n " + ex.getMessage(), LoggerLevel.ERROR);
			deployObjects();
		}
	}

	private void deployObjects(){
		getSettings().getRmiObjects().forEach((key, value) -> {
			try{
				bindObjectToMainController(value);
				Naming.rebind(String.format("rmi://%s/%s", getSettings().getServerLocation(), key), (Remote)value);
				getLogger().log("Deployed RMI object: " + value.getClass().getName().toString() , LoggerLevel.INFO);
			}catch(Exception ex){
				getLogger().log("Error Deploying Object: \n" + ex.getMessage() , LoggerLevel.WARNING);
			}
		});
	}
	
	private void bindObjectToMainController(Object value){
		try{
			Method[] methodsSet = value.getClass().getMethods();
			for (int i = 0; i < methodsSet.length; i++) {
				Method objectMethod = methodsSet[i];
				if(objectMethod.getName().equals("setMainController")){
					objectMethod.invoke(value, mainController);
				}
			}
			
		}catch(Exception ex){
			mainController.getLogger().log("Error while setting mainController" + ex.getMessage(), LoggerLevel.WARNING);
		}
	}
	
	private LoggerController getLogger(){
		return mainController.getLogger();
	}
	
	private Settings getSettings(){
		return mainController.getSettings();
	}
	
	public void setMainController(MainController controller){
		this.mainController = controller;
	}
}
