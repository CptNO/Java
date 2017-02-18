package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import server.controllers.LoggerController;
import server.controllers.LoggerController.LoggerLevel;

public class Settings {
	private int port;
	private String serverLocation;
	private String styleSheetLocation;
	private String codeBaseProperty;
	private HashMap<String,Object> rmiObjects;
	private final String SETTINGS_LOCATION; //= "src\\Settings.xml";
	private LoggerController logger;
	
	public Settings(LoggerController logger, String settingsLocation){
		SETTINGS_LOCATION = settingsLocation;
		this.logger = logger;
		setRmiObjects(new HashMap<>());
		loadSettings();
	}
	
	private void loadSettings(){
		try{
			loadSettings(loadXml());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private Document loadXml(){
		try{
			File settings = new File(SETTINGS_LOCATION);
			if(!settings.exists()){
				throw new IOException("File Sttings.xml not found");
			}else{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setIgnoringElementContentWhitespace(true);
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(settings);
				return doc;
			}
		}catch(IOException ex){
			logger.log(ex.getMessage(), LoggerLevel.ERROR);
		}catch(Exception ex){
			logger.log(ex.getMessage(), LoggerLevel.WARNING);
		}
		return null;
	}
	
	private void loadSettings(Document doc){
		searchForSettings(doc.getDocumentElement());
	}
	
	private void searchForSettings(Node root){
		NodeList nodeList = root.getChildNodes(); 
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeName().equals("Port")){
				logger.log("Option set: " + currentNode.getNodeName(), LoggerLevel.INFO);
				 setPort(Integer.parseInt(currentNode.getTextContent()));
			}else if(currentNode.getNodeName().equals("ServerIp")){
				logger.log("Option set: " + currentNode.getNodeName(), LoggerLevel.INFO);
				setServerLocation(currentNode.getTextContent());
			}else if(currentNode.getNodeName().equals("StyleLocation")){
				logger.log("Option set: " + currentNode.getNodeName(), LoggerLevel.INFO);
				setStyleSheetLocation(currentNode.getTextContent());
			}else if(currentNode.getNodeName().equals("RmiObjects")){
				logger.log("Option set: " + currentNode.getNodeName(), LoggerLevel.INFO);
				setRmiObjectsFromXml(currentNode);
			}else{
				searchForSettings(currentNode);
			}
		}
	}
	
	private void setRmiObjectsFromXml(Node parentNode){
		NodeList childNodes = parentNode.getChildNodes();
		Object item = null;
		String bindingName = null;
		for(int i = 0; i < childNodes.getLength(); i++){
			Node child = childNodes.item(i);
			if(child.getNodeName().equals("RmiObject")){
				setRmiObjectsFromXml(child);
			}			
			if(child.getNodeName().equals("ClassName")){
				item = loadObject(child.getTextContent());
			}else if(child.getNodeName().equals("BindingName")){
				bindingName = child.getTextContent();
			}else if(child.getNodeName().equals("CodebaseProperty")){
				//Empty
			}else{
				//Empty
			}
			if(bindingName != null && rmiObjects.get(bindingName) == null){
				logger.log("Adding Rmi Object to settings Id:" + bindingName, LoggerLevel.INFO);
				rmiObjects.put(bindingName, item);
			}
		}
	}
	
	private Object loadObject(String className){
		try{
			logger.log("Loading class " + className, LoggerLevel.INFO);
			Class<?> newClass = Class.forName(className);
			Object newObject = newClass.newInstance();
			return newObject;
		}catch(Exception ex){
			logger.log(ex.getMessage(), LoggerLevel.WARNING);
			ex.printStackTrace();
			return null;
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServerLocation() {
		return serverLocation;
	}

	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}

	public String getStyleSheetLocation() {
		return styleSheetLocation;
	}

	public void setStyleSheetLocation(String styleSheetLocation) {
		this.styleSheetLocation = styleSheetLocation;
	}

	public String getCodeBaseProperty() {
		return codeBaseProperty;
	}

	public void setCodeBaseProperty(String codeBaseProperty) {
		this.codeBaseProperty = codeBaseProperty;
	}

	public HashMap<String,Object> getRmiObjects() {
		return rmiObjects;
	}

	public void setRmiObjects(HashMap<String,Object> rmiObjects) {
		this.rmiObjects = rmiObjects;
	}
	
	public void addRmiObject(String bindingName, Object object){
		this.rmiObjects.put(bindingName, object);
	}
}
