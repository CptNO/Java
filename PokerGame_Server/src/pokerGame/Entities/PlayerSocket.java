package pokerGame.Entities;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerSocket extends Thread {
	private String username;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private List<GameUpdate> updateQuee;
	
	PlayerSocket(Socket socket){
		this.socket = socket;
		try{
			outputStream = new ObjectOutputStream(this.socket.getOutputStream());
			inputStream = new ObjectInputStream(this.socket.getInputStream());
			username = ((Player)inputStream.readObject()).getUsername();
			updateQuee = new ArrayList<>();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(!socket.isClosed()){
			try{
				GameUpdate update = (GameUpdate)inputStream.readObject();
				updateQuee.add(update);
			}catch(EOFException ex){
				close();
			}catch(Exception ex){
				close();
			}
		}
		close();
	}
	
	public void sendUpdate(GameUpdate update){
		if(!socket.isConnected()){
			close();
			return;
		}
	}
	
	private void close(){
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			socket = null;
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public ObjectInputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}
	public List<GameUpdate> getUpdateQuee() {
		return updateQuee;
	}
	public void setUpdateQuee(List<GameUpdate> updateQuee) {
		this.updateQuee = updateQuee;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
