package pokerGame.Application;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Settings {
	private int port;
	private String serverLocation;
	private String styleSheetLocation;
	private final String SETTINGS_LOCATION = "src\\Settings.xml";
	
	public Settings(){
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
			System.out.println(ex.getMessage());
		}catch(Exception ex){
			ex.printStackTrace();
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
			if (currentNode.getNodeName().equals("port")){
				 setPort(Integer.parseInt(currentNode.getTextContent()));
				 System.out.println("Option set: " + currentNode.getNodeName());
			}else if(currentNode.getNodeName().equals("serverIp")){
				setServerLocation(currentNode.getTextContent());
				System.out.println("Option set: " + currentNode.getNodeName());
			}else if(currentNode.getNodeName().equals("StyleLocation")){
				setStyleSheetLocation(currentNode.getTextContent());
				System.out.println("Option set: " + currentNode.getNodeName());
			}else{
				searchForSettings(currentNode);
			}
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
}
