package server.network;

import java.net.Socket;

import server.controllers.DashboardController;

public class UserSocket extends Thread {
	private Socket socket;
	private Boolean isConnected;
	private DashboardController controller;
	
	public UserSocket(Socket s){
		socket = s;
		isConnected = true;
		this.setDaemon(true);
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	@Override
	public void run(){
		while(isConnected){
			try{
				socket.getInputStream();
			}catch(Exception ex){
				if(socket.isClosed()){
					
				}
			}
		}
	}
	
	public void injectController(DashboardController controller){
		this.controller = controller;
	}
	
	
	
}
