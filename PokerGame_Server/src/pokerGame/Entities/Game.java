package pokerGame.Entities;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import objects.Deck;
import objects.GameUpdate;
import objects.GameUpdate.PlayerAction;
import objects.GameUpdate.UpdateType;
import objects.Player;
import objects.Room;
import objects.User;
import server.controllers.LoggerController.LoggerLevel;
import server.controllers.MainController;

public class Game extends Thread {

	private Room room;

	private Deck deck;

	private Boolean isOver;
	
	private boolean isNewGame;

	private MainController mainController;

	private ServerSocket serverSocket;

	private boolean isGameReady;

	private List<GameUpdate> gameUpdates;

	private List<PlayerSocket> playerSockets;
	
	//private List<Player> playerList;
	
	private int nextPlayerIndex;
	
	private int numberOfCardsShown = 0;
	
	private double potSize = 0.00;

	@Override
	public void run() {
		mainController.getLogger().log(String.format("Game STARTING on Thread: %s %s",
				this.getName(), this.getId()), LoggerLevel.INFO);
		waitForPlayersToConnect();
		isOver = false;
		isNewGame = true;
		nextPlayerIndex = 0; 
		while (!isOver) {
			checkPlayerConnection();
			if(playerSockets.size() == 0){
				isOver = true;
				mainController.setOnGameEnd(room);
				break;
			}
			update();
		}
		mainController.getLogger().log(String.format("Game STOPPING on Thread: %s %s",
				this.getName(), this.getId()), LoggerLevel.INFO);
	}
	
	private void checkPlayerConnection(){
		Iterator<PlayerSocket> itr = playerSockets.iterator();
		while(itr.hasNext()){
			PlayerSocket playerSocket =(PlayerSocket)itr.next();
			if(playerSocket.getSocket() == null){
				itr.remove();
			}
		}
		
	}
	
	private void update() {
		if(checkIsNewRound() || isNewGame){
			if(!isNewGame){
				checkWinner();
			}
			setupNewRound();
			if(isNewGame){
				sendNewPlayerTurn();
			}
			isNewGame = false;
		}
		if(checkIfPlayerResponded()){
			if(checkIsNewCard()){
				dealNewCard();
				gameUpdates.clear();
				nextPlayerIndex = 0;
			}
			
			/*
			if((nextPlayerIndex == playerSockets.size() - 1 && isPlayesFinished())){
				nextPlayerIndex = 0;
			}
			*/
			sendNewPlayerTurn();
		}
	}
	
	private boolean checkIsNewRound(){
		if(numberOfCardsShown == 4 && isPlayesFinished()){
			numberOfCardsShown = 0;
			return true;
		}
		return false;
	}
	
	private void checkWinner(){
		Random rand = new Random();
		Integer index = 0;
		try{
			index = rand.nextInt(playerSockets.size() - 1);
		}catch(Exception ex){
			index = 0;
		}
		String username = playerSockets.get(index).getUsername();
		for(GameUpdate update : gameUpdates){
			if(update.getPlayer().getUsername().equals(username)){
				GameUpdate gameUpdate = new GameUpdate();
				gameUpdate.setPlayer(update.getPlayer());
				gameUpdate.setPotSize(potSize);
				gameUpdate.setUpdateType(UpdateType.PLAYER_WON);
				gameUpdate.setRaiseAmount(0.00);
				sendUpdateToAll(gameUpdate, true);
				break;
			}
		}
		potSize = 0.00;
	}
	
	private boolean checkIsNewCard(){
		if(numberOfCardsShown == 4){
			return false;
		}else if(isPlayesFinished()){
			numberOfCardsShown++;
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isPlayesFinished(){
		if(gameUpdates.size() == 0){
			return false;
		}else if(gameUpdates.size() == playerSockets.size()){
			for(GameUpdate updateMember : gameUpdates){
				if(updateMember.getActionType() == PlayerAction.RAISE || updateMember.getActionType() == PlayerAction.CALL){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean checkIfPlayerResponded(){
		if(playerSockets.get(nextPlayerIndex).getUpdateQuee().size() > 0){
			handlePlayerResponse(playerSockets.get(nextPlayerIndex).getUpdateQuee());
			
			playerSockets.get(nextPlayerIndex).getUpdateQuee().clear();
			nextPlayerIndex++;
			if(nextPlayerIndex > playerSockets.size() - 1){
				nextPlayerIndex = 0;
			}
			return true;
		}
		return false;
	}
	
	private void handlePlayerResponse(List<GameUpdate> updateList){
		if (updateList.get(0).getUpdateType().equals(UpdateType.PLAYER_ACTION)) {
			for (GameUpdate update : gameUpdates) {
				if (update.getPlayer().getUsername().equals(updateList.get(0).getPlayer().getUsername())) {
					gameUpdates.remove(update);
					break;
				}
			}
			
			GameUpdate newUpdate = new GameUpdate();
			newUpdate.setActionType(updateList.get(0).getActionType());
			newUpdate.setUpdateType(updateList.get(0).getUpdateType());
			newUpdate.setPlayer(updateList.get(0).getPlayer());
			potSize = updateList.get(0).getPotSize();
			newUpdate.setPotSize(potSize);
			gameUpdates.add(newUpdate);

			System.out.println("\n-----> Updates on que<------");
			for (GameUpdate update : gameUpdates) {
				System.out.println("Update for: " + update.getPlayer().getUsername());
				System.out.println("Update: " + update.getUpdateType());
				System.out.println("\t" + update.getActionType());
				System.out.println("-----------------------");
			}
			System.out.println("-----> Updates on que END<------\n");
		}
		sendUpdateToAll(updateList.get(0), false);
	}
	
	private void sendNewPlayerTurn(){
		GameUpdate update = new GameUpdate();
		Player player = new Player();
		player.setUsername(playerSockets.get(nextPlayerIndex).getUsername());
		update.setPlayer(player);
		update.setUpdateType(UpdateType.PLAYER_TURN);
		sendUpdateToAll(update, true);
	}

	public int setupServerPort() {
		try {
			serverSocket = new ServerSocket(0);
			return serverSocket.getLocalPort();
		} catch (Exception ex) {
			mainController.getLogger().log(
					String.format("Problem while setting port for game: %s \n", room.getRoomName(), ex.getMessage()),
					LoggerLevel.WARNING);
			return 0;
		}
	}

	private void waitForPlayersToConnect() {
		playerSockets = new ArrayList<>();
		isGameReady = false;
		while (!isGameReady) {
			try {
				Socket channel = serverSocket.accept();
				connectPlayer(channel);
				isGameReady = checkIfGameReady();
			} catch (Exception ex) {
				mainController.getLogger().log(String.format("Problem while accepting user on game: %s \n",
						room.getRoomName(), ex.getMessage()), LoggerLevel.WARNING);
			}
		}
		mainController.getLogger().log(String.format("Game Started for room " + room.getRoomName()), LoggerLevel.INFO);
	}

	private void connectPlayer(Socket channel) {
		PlayerSocket newPlayer = new PlayerSocket(channel);
		playerSockets.add(newPlayer);
		newPlayer.start();
		mainController.getLogger().log(String.format("Player[%s] CONNECTED to game %s \n-->IP:%s \n -->Port:. "
				, newPlayer.getUsername(), room.getRoomName(), channel.getRemoteSocketAddress(), channel.getPort()), LoggerLevel.INFO);
	}

	private boolean checkIfGameReady() {
		for(User userMember : room.getUserList()){
			boolean isPlayerFound = false;
			for(PlayerSocket playerSocketMember : playerSockets){
				if (userMember.getUsername().equals(playerSocketMember.getUsername())) {
					isPlayerFound = true;
					break;
				}
			}
			if (!isPlayerFound) {
				return false;
			}
		}
		return true;
	}
	
	private void dealNewCard(){
		GameUpdate update = new GameUpdate();
		update.addCard(deck.getCard());
		update.setUpdateType(UpdateType.NEW_CARD);
		sendUpdateToAll(update, true);
	}
	
	private void setupNewRound() {
		gameUpdates = new ArrayList<>();
		deck = new Deck();
		deck.generateDeck();
		gameUpdates.clear();
		dealCards();
		nextPlayerIndex = 0;
	}

	public void dealCards() {
		for(PlayerSocket playerMember : playerSockets){
			Player player = new Player();
			player.setUsername(playerMember.getUsername());
			if(isNewGame){
				player.setMonney(100.00);
			}
			GameUpdate update = new GameUpdate();
			update.setPlayer(player);
			update.setPotSize(0.00);
			update.addCard(deck.getCard());
			update.addCard(deck.getCard());
			update.setUpdateType(UpdateType.NEW_ROUND);
			
			sendUpdateToAll(update, true);
		}	
	}
	
	private void sendUpdateToAll(GameUpdate update, boolean toSender){
		for(PlayerSocket playerMember : playerSockets){
			try{
				if(!toSender){
					
					String username = "Server";
					if(update.getPlayer() != null){
						if(update.getPlayer().getUsername() != null){
							username = update.getPlayer().getUsername();
						}
					}
					System.out.println("Sending udpate TO: " + playerMember.getUsername() + " FROM: " + username);
					System.out.println("Update: " + update.getUpdateType());
					System.out.println("\t" + update.getActionType());
					System.out.println("-----------------------");
					
					if(!playerMember.getUsername().equals(update.getPlayer().getUsername())){
						playerMember.getOutputStream().writeObject(update);
						playerMember.getOutputStream().flush();
					}
				}else{
					playerMember.getOutputStream().writeObject(update);
					playerMember.getOutputStream().flush();
				}
			}catch(Exception ex){
				mainController.getLogger().log(String.format("Problem while sending[%s] to Player[%s] \n %s",
						GameUpdate.class.getName(), playerMember.getUsername(), ex.getMessage()), LoggerLevel.WARNING);
				ex.printStackTrace();
			}
		}
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}
