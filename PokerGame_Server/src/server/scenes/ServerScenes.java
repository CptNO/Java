package server.scenes;

public enum ServerScenes {
	DASHBOARD("/server/scenes/Dashboard.fxml"),
	LOGGER("/server/scenes/Logger.fxml"),
	SERVER_CONTROL("/server/scenes/ServerControl.fxml"),
	NONE("null");
	
	
	String state;
	
	ServerScenes(String state){
		this.state = state;
	}
	
	private String getsState(){
		return state;
	}
	
	public static String getStateClass(ServerScenes state){
		for(ServerScenes gstate : ServerScenes.values()){
			if(gstate == state){
				return gstate.getsState();
			}
		}
		return null;
	}
	
	
}
