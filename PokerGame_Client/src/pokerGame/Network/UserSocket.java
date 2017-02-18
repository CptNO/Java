package pokerGame.Network;

import java.net.Socket;

public class UserSocket extends Thread {
	private Socket socket;
	private Boolean isConnected;
	
	public UserSocket(Socket s){
		socket = s;
		isConnected = true;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	@Override
	public void run(){
		while(isConnected){
			try{
				System.out.println(socket.getInputStream());
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	
}
