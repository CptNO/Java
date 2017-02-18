package server.rmi;

import java.io.File;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pokerGame.Entities.User;
import server.controllers.LoggerController.LoggerLevel;
import server.rmi.LoginMessage.Type;

public class LoginImpl extends BaseService implements ILogin {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String USER_DATA_LOCATION = "src\\UserData.xml";
	
	public LoginImpl() throws RemoteException {
		super();
		//Empty
	}

	@Override
	public LoginMessage authenticateUser(User user) throws RemoteException {
		LoginMessage msg = new LoginMessage();
		try{
			if(findUser(user, loadUsersdocument().getDocumentElement())){
				msg.setType(Type.LOGIN_PASSED);
			}else{
				msg.setType(Type.LOGIN_FAILED);
				msg.setAdditionalInfo("Login not found");
				throw new UserLoginException("User not found");
			}
			if(checkIfUserLoggedIn(user)){
				msg.setType(Type.LOGIN_FAILED);
				msg.setAdditionalInfo("User is aleready logged in");
				throw new UserLoginException("User alerady loged in");
			}
			mainController.getLogger().log("User logged in: " + user.getUsername(), LoggerLevel.INFO );
			addUser(user);
		}catch(Exception ex){
			mainController.getLogger().log(String.format("Login Error for User[%s] error: %s",user.getUsername(), ex.getMessage())
					, LoggerLevel.WARNING);
		}
		
		return msg;
	}
	
	private Document loadUsersdocument(){
		try{
			File settings = new File(USER_DATA_LOCATION);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(settings);
			return doc;
		}catch(Exception ex){
			mainController.getLogger().log("Could not find or parse " + USER_DATA_LOCATION, LoggerLevel.ERROR);
		}
		return null;
	}
	
 	private boolean findUser(User user, Node root){
		NodeList nodeList = root.getChildNodes(); 
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if(currentNode.getNodeName().equals("User")){
				if(checkMatch(user, currentNode))
					return true;
			}
		}
		return false;
	}
	
	private boolean checkMatch(User user, Node root){
		NodeList nodeList = root.getChildNodes();
		Integer id = 0;
		String username = "";
		String password = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if(currentNode.getNodeName().equals("Username")){
				username = currentNode.getTextContent();
			}else if(currentNode.getNodeName().equals("Password")){
				password = currentNode.getTextContent();
			}else if(currentNode.getNodeName().equals("Id")){
				id = Integer.parseInt(currentNode.getTextContent());
			}
		}
		if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
			user.setId(id);
			return true;
		}
		return false;
	}
	
	private boolean checkIfUserLoggedIn(User user){
		if(mainController.getUserList().stream().filter(_user ->
			_user.getUsername().equals(user.getUsername())).findFirst().orElse(null) == null){
			return false;
		}
		return true;
	}

	@Override
	public LoginMessage registerUser(User user) throws RemoteException {
		LoginMessage msg = new LoginMessage();
		try{
			if(findUser(user, loadUsersdocument().getDocumentElement())){
				msg.setType(Type.REGISTRATION_FAILED);
				msg.setAdditionalInfo("Username aleready Taken");
				throw new LoginException("Username alerady taken");
			}else{
				if(!createUserNode(user)){
					msg.setType(Type.REGISTRATION_FAILED);
					msg.setAdditionalInfo("Internal server Error");
				}else
					msg.setType(Type.REGISTRATION_PASSED);
				
				mainController.getLogger().log(String.format("User registered User[%s]", user.getUsername()), LoggerLevel.INFO);
			}
		}catch(Exception ex){
			mainController.getLogger().log(String.format("Registration Error for User[%s] error: %s",
					user.getUsername(), ex.getMessage()), LoggerLevel.WARNING);
		}
		return msg;
	}
	
	@Override
	public LoginMessage logOutUser(User user) throws RemoteException {
		for(User p : mainController.getUserList()){
			if(p.getUsername().equals(user.getUsername())){
				mainController.getUserList().remove(p);
				mainController.getLogger().log("User Loged out: " + user.getUsername(), LoggerLevel.INFO);
				break;
			}
		}
		return null;
	}
	
	private void addUser(User user){
		mainController.getUserList().add(user);
	}
	
	private boolean createUserNode(User user){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(USER_DATA_LOCATION);
			
			Node users = doc.getFirstChild();
			Node userNode = doc.createElement("User");
			
			Element username = doc.createElement("Username");
			username.appendChild(doc.createTextNode(user.getUsername()));
			userNode.appendChild(username);
			
			Element password = doc.createElement("Password");
			password.appendChild(doc.createTextNode(user.getPassword()));
			userNode.appendChild(password);
			
			users.appendChild(userNode);
			
			NodeList nodes = users.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				 if ("LastId".equals(node.getNodeName())) {
					 node.setTextContent(increaseId(Integer.parseInt(node.getTextContent())).toString());
					 Element id = doc.createElement("Id");
					 id.appendChild(doc.createTextNode(node.getTextContent()));
					 userNode.appendChild(id);
				 }
			}
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(USER_DATA_LOCATION));
			transformer.transform(source, result);
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	private Integer increaseId(Integer lastId){
		return lastId += 1;
	}

}
