package pokerGame.Scenes;

public enum GameScenes {
	MAIN_MENU("/pokerGame/Scenes/MainMenu.fxml"),
	SPLASH_SCREEN("/pokerGame/Scenes/Main.fxml"),
	CHAT("/pokerGame/Scenes/ChatPane.fxml"),
	
	LOBBY("/pokerGame/Scenes/Lobby.fxml"),
	ROOMS_PANE("/pokerGame/Scenes/RoomsPane.fxml"),
	NEW_ROOM_PANE("/pokerGame/Scenes/NewRoomPane.fxml"),
	ROOM("/pokerGame/Scenes/RoomPane.fxml"),
	
	PLAY("/pokerGame/Scenes/Game.fxml"),
	REPLAY("/pokerGame/Scenes/Replay.fxml"),
	
	LOGIN("/pokerGame/Scenes/Login.fxml"),
	LOGIN_PANE("/pokerGame/Scenes/LoginPane.fxml"),
	REGISTER_PANE("/pokerGame/Scenes/RegisterPane.fxml"),
	STATISTICS("/pokerGame/Scenes/Statistics.fxml"),
	
	NONE("/pokerGame/Scenes/Main.fxml");
	
	String state;
	
	GameScenes(String state){
		this.state = state;
	}
	
	private String getsState(){
		return state;
	}
	
	public static String getStateClass(GameScenes state){
		for(GameScenes gstate : GameScenes.values()){
			if(gstate == state){
				return gstate.getsState();
			}
		}
		return null;
	}
	
	
}
