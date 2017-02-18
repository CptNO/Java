package pokerGame.Network;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SimpleSocket implements SocketListener {
	private Integer port;
	private String serverIp;
	private Socket socket;
	private String settingsLocation = "src\\ConnectionSettings.xml";
	
	public SimpleSocket(){
		loadSettings();
	}
	
	private void loadSettings(){
		try{
			File settings = new File(settingsLocation);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(settings);
			
			parseXML(doc);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void parseXML(Document xmlDoc){
		NodeList root = xmlDoc.getElementsByTagName("ConnectionSettings");
		
		for (int i = 0; i < root.getLength(); i++) {
			Element element = (Element)root.item(i);
			port = Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent());
			serverIp = element.getElementsByTagName("serverIp").item(0).getTextContent();
		}
	}

	//TEMP
	public Object lsten(){
		try{
			return socket.getInputStream();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	//TEMP
	public void connect(){
		try{
			socket = new Socket(serverIp, port);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//TEMP
	public OutputStream getObjectOutputStream() throws Exception{
		return socket.getOutputStream();
	}
	
	@Override
	public void onPacketRecived(Object packet) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClose(Boolean isClosed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect(Boolean isClosed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSend(Object packet) {
		// TODO Auto-generated method stub
		
	}
	
}
