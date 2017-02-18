package pokerGame.Controllers;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import objects.Card;
import objects.Entity;
import objects.GameUpdate;
import objects.GameUpdate.PlayerAction;
import objects.GameUpdate.UpdateType;
import objects.Player;
import objects.Room;
import objects.User;
import objects.Card.Rank;
import objects.Card.State;
import objects.Card.Suit;
import pokerGame.Entities.AssetHandler;
import pokerGame.Entities.Chip;
import pokerGame.Entities.Chip.ChipValue;
import pokerGame.Entities.CustomButton;
import pokerGame.Entities.CustomButton.ButtonState;
import pokerGame.Scenes.GameScenes;
import pokerGame.Entities.LongValue;
import pokerGame.Entities.Mouse;
import pokerGame.Entities.PlayerStatistics;
import pokerGame.Entities.Pot;
import pokerGame.Entities.Table;

public class GameController extends BaseController {

	@FXML
	private Canvas gameCanavas;

	private List<Chip> chipList;
	private List<Chip> potChips;
	
	private List<Object> updateQuee;
	private AssetHandler assetHandler;
	private Table table;
	private Chip dragChip;

	// Debug
	private Object hoveringOver;
	private Font font;
	private AnimationTimer gameLoop;
	private String infoText = "";
	private Thread listeningThread;
	private boolean play;
	private Socket clientSocket;	
	private List<Player> playerList;
	private List<CustomButton> buttonList;
	private Mouse mouse;
	
	private boolean isChipAnimation;
	private double chipAnimationTimmer;
	
	private boolean isCardTableAnimationStart = false;
	private boolean isCardTableAnimationMove = false;
	private boolean isCardTableAnimationEnd = false;
	private int lastTableCardShownIndex = 0;
	
	private boolean isCardPlayerAnimationStart = false;
	private boolean isCardPlayerAnimationMove = false;
	private boolean isCardPlayerAnimationEnd = false;
	private int lastPlayerCardShownIndex = 0;
	
	private double cardAnimationTimmer = 1.00;
	
	private final double CARD_WIDTH = 80.00;
	private final double CARD_HEIGHT = 100.00;
	
	private final double TABLE_WIDTH = 1025.0;
	private final double TABLE_HEIGHT = 600.0;
	
	private final double POT_WIDTH = 200.00;
	private final double POT_HEIGHT = 150.00;
	
	private final double CHIP_WIDTH = 50.00;
	private final double CHIP_HEIGHT = 50.00;
	
	private final double PLAYER_CARD_START_LOCATION_X = 540.00;
	private final double PLAYER_CARD_START_LOCATION_Y = 480.00;
	private final double PLAYER_CARD_OFFSET = 45.00;
	
	private final double DECK_LOCATION_X = 730.00;
	private final double DECK_LOCATION_Y = 150.00;
	
	private final double BUTTON_WIDTH = 100.00;
	private final double BUTTON_HEIGHT = 25.00;
	
	private double infoOpacity = 1.00;
	
	private List<String> eventMessagesList;
	private boolean isRaiseOccured = false;
	private double raiseAmount = 0.00;
	private PlayerAction playerLastAction = PlayerAction.NONE;
	
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	PlayerStatistics statistics;
	
	private enum RegistratedButtons{
		RAISE,
		CALL,
		FOLD,
		CHECK,
		LEAVE_GAME
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void startGameLoop() {
		loadAssets();
		LongValue lastTime = new LongValue(System.nanoTime());
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long currentTime) {
				double deltaTime = (currentTime - lastTime.value) / 1000000000.0;
				lastTime.value = currentTime;
				update(deltaTime);
				render(gameCanavas.getGraphicsContext2D());
			}
		};
		gameLoop.start();
	}
	

	private void stopGameLoop() {
		gameLoop.stop();
		freeObjects();
		((RoomsPaneController)mainController.getController(GameScenes.ROOMS_PANE)).removeTableRoom(mainController.getRoom());
		mainController.setRoom(null);
		((LobbyController)mainController.getController(GameScenes.LOBBY)).switchRoomsPane(mainController.getScene(GameScenes.ROOMS_PANE));
		mainController.setScene(GameScenes.MAIN_MENU);
	}
	

	private void freeObjects() {
		serilizeStatistics();
		assetHandler = null;
		chipList = null;
		playerList = null;
		updateQuee = null;
		try{
			outputStream.close();
			inputStream.close();
			clientSocket.close();
			clientSocket = null;
			play = false;
			listeningThread.interrupt();
			listeningThread.join();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void serilizeStatistics(){
		try {
			statistics = new PlayerStatistics();
			statistics.setCurrentMoney(getPlayer().getMonney());
			statistics.setHoursPlayed(0.00);
			statistics.setMonneyWon(table.getPot().getPotAmount());
			FileOutputStream outputFileStream = new FileOutputStream(getPlayer().getUsername() + ".ser");
			ObjectOutputStream outputStream = new ObjectOutputStream(outputFileStream);
			outputStream.writeObject(statistics);
			outputStream.close();
			outputFileStream.close();
		} catch (Exception ex) {

		}
	}
	

	private void update(double delta) {
		hoveringOver = null;
		updateChips(delta);
		updateCards(delta);
		updateOther(delta);
		updateButtons(delta);
		handleGameUpdate();
	}
	
	private void updateChips(double delta){
		for (Chip chip : chipList) {
			if (chip.intersects(mouse) && mouse.isMousePressed() && getPlayer().isCurrentTurn()) {
				dragChip = new Chip();
				dragChip.setChipValue(chip.getChipValue());
				dragChip.setImage(assetHandler.getIamge(chip.getChipValue().toString()), 55, 55);
				dragChip.setPosition(chip.getX() - chip.getBoundary().getWidth() / 2, chip.getY() - chip.getBoundary().getHeight() / 2);
				dragChip.update(delta);
			} else if (chip.intersects(mouse)) {
				hoveringOver = chip;
			}
			chip.update(delta);
		}
		if (dragChip != null && !isChipAnimation) {
			dragChip.setPosition(mouse.getX() - 25, mouse.getY() - 25);
			if (dragChip.intersects(table.getPot()) && !mouse.isMousePressed()) {
				if (isActionValid(dragChip.getChipDoubleValue())) {
					getPlayer().setMonney(getPlayer().getMonney() - dragChip.getChipDoubleValue());
					table.getPot().addToPot(dragChip);
					isChipAnimation = true;
					chipAnimationTimmer = 1.0;
				}
			}
		}
		if(isChipAnimation){
			chipAnimationTimmer -= 0.1;
			dragChip.setWidth(dragChip.getBoundary().getWidth() - 1.0);
			dragChip.setHeight(dragChip.getBoundary().getHeight() - 1.0);
			if(chipAnimationTimmer <= 0.00){
				dragChip.setWidth((CHIP_WIDTH / 2) + 10);
				dragChip.setHeight((CHIP_HEIGHT / 2) + 10);
				potChips.add(dragChip);
				isChipAnimation = false;
				dragChip = null;
			}
		}
		if(dragChip != null && !mouse.isMousePressed() && !isChipAnimation){
			dragChip = null;
		}
	}
	
	private void updateCards(double delta){
		for (Card card : table.getDeck()) {
			if (card.intersects(mouse)) {
				hoveringOver = card;
			}
			card.update(delta);
		}
		animateTableCards(delta);
		animatePlayerCards(delta);

	}
	
	private void animateTableCards(double delta){
		if(isCardTableAnimationStart){
			lockButtons(true);
			Card cardToAnimate = table.getTableCards().get(lastTableCardShownIndex);
			if(cardAnimationTimmer == 1.00){
				cardToAnimate.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
				cardToAnimate.changeCurrentState(State.BACK);
				cardAnimationTimmer -= 0.1;
			}else{
				cardToAnimate.setWidth(cardToAnimate.getBoundary().getWidth() + 0.5);
				cardToAnimate.setHeight(cardToAnimate.getBoundary().getHeight() + 0.5);
				cardAnimationTimmer -= 0.1;
				if(cardAnimationTimmer <= 0.00){
					cardAnimationTimmer = 1.00;
					isCardTableAnimationStart = false;
					isCardTableAnimationMove = true;
				}
			}
		}
		if(isCardTableAnimationMove){
			Card cardToAnimate = table.getTableCards().get(lastTableCardShownIndex);
			cardToAnimate.setVelocity(-60.0 * (lastTableCardShownIndex + 1), 0.0);
			cardToAnimate.update(delta);
			cardAnimationTimmer -= 0.01;
			if(cardAnimationTimmer <= 0.00){
				cardAnimationTimmer = 1.00;
				isCardTableAnimationMove = false;
				isCardTableAnimationEnd = true;
			}
			
		}
		if(isCardTableAnimationEnd){
			Card cardToAnimate = table.getTableCards().get(lastTableCardShownIndex);
			cardToAnimate.setWidth(cardToAnimate.getBoundary().getWidth() - 0.5);
			cardToAnimate.setHeight(cardToAnimate.getBoundary().getHeight() - 0.5);
			cardAnimationTimmer -= 0.1;
			if(cardAnimationTimmer <= 0.00){
				cardToAnimate.changeCurrentState(State.SHOWN);
				cardAnimationTimmer = 1.00;
				isCardTableAnimationEnd = false;
				lastTableCardShownIndex ++;
				lockButtons(false);
			}
		}
	}
	
	private void animatePlayerCards(double delta){
		if(isCardPlayerAnimationStart){
			lockButtons(true);
			Card cardToAnimate = getPlayer().getPlayerCards().get(lastPlayerCardShownIndex);
			if(cardAnimationTimmer == 1.00){
				if(lastPlayerCardShownIndex == 1){
					cardToAnimate.setPosition(DECK_LOCATION_X - 45, DECK_LOCATION_Y);
				}else
					cardToAnimate.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
				cardToAnimate.changeCurrentState(State.BACK);
				cardAnimationTimmer -= 0.1;
			}else{
				cardToAnimate.setWidth(cardToAnimate.getBoundary().getWidth() + 0.5);
				cardToAnimate.setHeight(cardToAnimate.getBoundary().getHeight() + 0.5);
				cardAnimationTimmer -= 0.1;
				if(cardAnimationTimmer <= 0.00){
					cardAnimationTimmer = 1.00;
					isCardPlayerAnimationStart = false;
					isCardPlayerAnimationMove = true;
				}
			}
		}
		if(isCardPlayerAnimationMove){
			Card cardToAnimate = getPlayer().getPlayerCards().get(lastPlayerCardShownIndex);
			Entity pos = new Entity();
			pos.setPosition(PLAYER_CARD_START_LOCATION_X, PLAYER_CARD_START_LOCATION_Y);
			pos.setWidth(CARD_WIDTH);
			pos.setHeight(CARD_HEIGHT);
			if(cardToAnimate.intersects(pos)){
				pos.setPosition(PLAYER_CARD_START_LOCATION_X + ((CARD_WIDTH / 2) * lastPlayerCardShownIndex), 
						PLAYER_CARD_START_LOCATION_Y);
				cardAnimationTimmer = 1.00;
				isCardPlayerAnimationMove = false;
				isCardPlayerAnimationEnd = true;
			}else{
				cardToAnimate.setVelocity(-135.0, 135.0);
				cardToAnimate.update(delta);
			}
			
			if(cardToAnimate.getX() < 0){
				pos.setPosition(PLAYER_CARD_START_LOCATION_X + ((CARD_WIDTH / 2) * lastPlayerCardShownIndex), 
						PLAYER_CARD_START_LOCATION_Y);
				cardAnimationTimmer = 1.00;
				isCardPlayerAnimationMove = false;
				isCardPlayerAnimationEnd = true;
			}
		}
		if(isCardPlayerAnimationEnd){
			Card cardToAnimate = getPlayer().getPlayerCards().get(lastPlayerCardShownIndex);
			cardToAnimate.setWidth(cardToAnimate.getBoundary().getWidth() - 0.5);
			cardToAnimate.setHeight(cardToAnimate.getBoundary().getHeight() - 0.5);
			cardAnimationTimmer -= 0.1;
			if(cardAnimationTimmer <= 0.00){
				cardToAnimate.changeCurrentState(State.SHOWN);
				cardAnimationTimmer = 1.00;
				isCardPlayerAnimationEnd = false;
				if(lastPlayerCardShownIndex == 0){
					isCardPlayerAnimationStart = true;
					lastPlayerCardShownIndex++;
				}else{
					isCardPlayerAnimationStart = false;
					lastPlayerCardShownIndex = 0;
					lockButtons(false);
				}
			}
		}
	}
	
	private void lockButtons(boolean lock){
		if(lock){
			for(CustomButton button : buttonList){
				button.setDisabled(true);
			}
		}else{
			for(CustomButton button : buttonList){
				button.setDisabled(false);
			}
		}
	}
	
	private void updateButtons(double delta){
		for(CustomButton buttonMember : buttonList){
			if(!getPlayer().isCurrentTurn()){
				buttonMember.setDisabled(true);
			}
			
			if(buttonMember.intersects(mouse) && mouse.isMousePressed() && !buttonMember.isDisabled()){
				buttonMember.changeCurrentState(ButtonState.BUTTON_PRESSED);
				for(RegistratedButtons button : RegistratedButtons.values()){
					if(buttonMember.getId().equals(button.toString())){
						buttonAction(button);
					}
				}
			}else if(buttonMember.intersects(mouse) && !buttonMember.isDisabled()){
				buttonMember.changeCurrentState(ButtonState.BUTTON_MOUSE_OVER);
				hoveringOver = buttonMember;
			}else{
				buttonMember.changeCurrentState(ButtonState.BUTTON_NONE);
			}
			
			if(buttonMember.isDisabled()){
				buttonMember.setTextFill(Color.GREY);
			}else{
				buttonMember.setTextFill(Color.WHITESMOKE);
			}
			
			if(buttonMember.getCurrentState() == ButtonState.BUTTON_PRESSED){
				getPlayer().setCurrentTurn(false);
				switch (buttonMember.getId()) {
				case "RAISE":
					createPlayerActionUpdate(PlayerAction.RAISE);
					eventMessagesList.add(String.format("% Raised for: %s $$",getPlayer().getUsername(), "0.00"));
					break;
				case "CALL":
					createPlayerActionUpdate(PlayerAction.CALL);
					eventMessagesList.add(String.format("%s Called for: %s $$",getPlayer().getUsername(), "0.00"));
					break;
				case "FOLD":
					createPlayerActionUpdate(PlayerAction.FOLD);
					eventMessagesList.add(String.format("%s Folded",getPlayer().getUsername()));
					break;
				case "CHECK":
					createPlayerActionUpdate(PlayerAction.CHECK);
					eventMessagesList.add(String.format("%s's Checkd",getPlayer().getUsername()));
					break;
				case "LEAVE_GAME":
					createGameActionUpdate(UpdateType.PLAYER_LEFT);
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void buttonAction(RegistratedButtons button){
		GameUpdate update = new GameUpdate();
		switch (button) {
		case RAISE:
			if(raiseAmount == 0.00){
				infoText = "Didnt put monney to raise";
			}else{
				update.setPlayer(getPlayer());
				update.setRaiseAmount(raiseAmount);
				update.setUpdateType(UpdateType.PLAYER_ACTION);
				update.setActionType(PlayerAction.RAISE);
				raiseAmount = 0.00;
			}
			break;
		case CALL:
			getPlayer().setMonney(getPlayer().getMonney() - raiseAmount);
			update.setPlayer(getPlayer());
			update.setRaiseAmount(raiseAmount);
			update.setUpdateType(UpdateType.PLAYER_ACTION);
			update.setActionType(PlayerAction.CALL);
			raiseAmount = 0.00;
			break;
		case FOLD:
			update.setPlayer(getPlayer());
			update.setUpdateType(UpdateType.PLAYER_ACTION);
			update.setActionType(PlayerAction.FOLD);
			break;
		case CHECK:
			update.setPlayer(getPlayer());
			update.setUpdateType(UpdateType.PLAYER_ACTION);
			update.setActionType(PlayerAction.CHECK);
			break;
		case LEAVE_GAME:
			update.setPlayer(getPlayer());
			update.setUpdateType(UpdateType.PLAYER_LEFT);
			sendUpdate(update);
			stopGameLoop();
			break;
		default:
			break;
		}
		getPlayer().setCurrentTurn(false);
		sendUpdate(update);
	}
	
	private void updateOther(double delta){

		if(table.getPot().intersects(mouse)){
			hoveringOver = table.getPot();
		}

		if(eventMessagesList.size() > 5){
			int index = 0;
			while(eventMessagesList.size() > 5){
				eventMessagesList.remove(index);
				index++;
			}
		}
	}

	private void handleGameUpdate(){
		Iterator<Object> itr = updateQuee.iterator();
		while(itr.hasNext()){
			GameUpdate gameUpdate =(GameUpdate)itr.next();
			switch (gameUpdate.getUpdateType()) {
			case PLAYER_TURN:
				handlePlayerTurnUpdate(gameUpdate);
				break;
			case NEW_ROUND:
				handleNewRoundUpdate(gameUpdate);
				break;
			case NEW_CARD:
				handleNewCardUpdate(gameUpdate);
				break;
			case PLAYER_WON:
				handlePlayerWonUpdate(gameUpdate);
				break;
			case PLAYER_LEFT:
				handlePlayerLeftUpdate(gameUpdate);
				break;
			case PLAYER_ACTION:
				handlePlayerActionUpdate(gameUpdate);
				break;
			case NONE:
				break;
			default:
				break;
			}
			itr.remove();
		}
	}
	
	private void handlePlayerWonUpdate(GameUpdate gameUpdate){
		eventMessagesList.add(String.format("%s WON %s$$$", gameUpdate.getPlayer().getUsername(), gameUpdate.getPotSize()));
		for(Player player : playerList){
			if(player.getUsername().equals(gameUpdate.getPlayer().getUsername())){
				player.setMonney(player.getMonney() + gameUpdate.getPotSize());
			}
		}
		table.getPot().setPotAmount(0.00);
		potChips.clear();
	}
	
	private void handleNewRoundUpdate(GameUpdate gameUpdate){
		table.getTableCards().clear();
		lastTableCardShownIndex = 0;
		playerLastAction = PlayerAction.NONE;
		for(Player playerMember : playerList){
			if(gameUpdate.getPlayer().getUsername().equals(getPlayer().getUsername())){
				List<Card> newPlayerCards = new ArrayList<>();
				playerMember.setMonney(gameUpdate.getPlayer().getMonney());
				double offset = 0;
				for(Card card : gameUpdate.getCards()){
					card.setPosition(DECK_LOCATION_X + offset, DECK_LOCATION_Y);
					card.addSprite(State.SHOWN, assetHandler.getIamge(card.getRank().toString() + "_" + card.getSuit().toString()), 
							CARD_WIDTH, CARD_HEIGHT);
					card.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), CARD_WIDTH, CARD_HEIGHT);
					newPlayerCards.add(card);
					offset += PLAYER_CARD_OFFSET;
				}
				getPlayer().setPlayerCards(newPlayerCards);
				isCardPlayerAnimationStart = true;
			}
			else if(playerMember.getUsername().equals(gameUpdate.getPlayer().getUsername())){
				playerMember = gameUpdate.getPlayer();
			}
		}
	}
	
	private void handleNewCardUpdate(GameUpdate gameUpdate){
		table.addCardToTable(createNewCard(gameUpdate.getCards().get(0)));
		playerLastAction = PlayerAction.NONE;
		isCardTableAnimationStart = true;
	}
	
	private Card createNewCard(Card card){
		card.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), 0,0);
		card.addSprite(State.SHOWN, assetHandler.getIamge(card.getRank().toString() + "_" + card.getSuit().toString()), 0,0);
		card.addSprite(State.NONE, assetHandler.getIamge("CARD_NONE"), 0,0);
		
		card.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
		card.setWidth(CARD_WIDTH);
		card.setHeight(CARD_HEIGHT);
		
		return card;
	}

	private void handlePlayerTurnUpdate(GameUpdate gameUpdate){
		for(Player playerMember : playerList){
			if(playerMember.getUsername().equals(gameUpdate.getPlayer().getUsername())){
				playerMember.setCurrentTurn(true);
			}else{
				playerMember.setCurrentTurn(false);
			}
		}
		
		eventMessagesList.add(String.format("%s's turn", gameUpdate.getPlayer().getUsername()));
	}
	
	private void handlePlayerActionUpdate(GameUpdate gameUpdate){
		if(!gameUpdate.getPlayer().getUsername().equals(getPlayer().getUsername())){
			updatePlayer(gameUpdate.getPlayer());
		}
		switch(gameUpdate.getActionType()){
		case RAISE:
			eventMessagesList.add(String.format("%s raised for: %s $$"
					,gameUpdate.getPlayer().getUsername(), gameUpdate.getRaiseAmount()));
			table.getPot().addToPot(gameUpdate.getRaiseAmount());;
			isRaiseOccured = true;
			raiseAmount = gameUpdate.getRaiseAmount();
			break;
		case CALL:
			eventMessagesList.add(String.format("%s Payed for: %s $$", 
					gameUpdate.getPlayer().getUsername(), gameUpdate.getRaiseAmount()));
			table.getPot().addToPot(gameUpdate.getRaiseAmount());
			break;
		case FOLD:
			eventMessagesList.add(String.format("%s Folded", gameUpdate.getPlayer().getUsername()));
			break;
		case CHECK:
			eventMessagesList.add(String.format("%s Cheked", gameUpdate.getPlayer().getUsername()));
			break;
		case NONE:
			break;
		}
		table.getPot().setPotAmount(gameUpdate.getPotSize());
	}

	private void handlePlayerLeftUpdate(GameUpdate gameUpdate){
		for(Player player : playerList){
			if(player.getUsername().equals(gameUpdate.getPlayer().getUsername())){
				playerList.remove(player);
				break;
			}
		}
		
		eventMessagesList.add(String.format("%s Left the game ", gameUpdate.getPlayer().getUsername()));
	}
	
	private void updatePlayer(Player player){
		for(Player playerMember : playerList){
			if(playerMember.getUsername().equals(player.getUsername())){
				playerMember.setMonney(player.getMonney());
				playerMember.setPlayerCards(player.getPlayerCards());
			}
		}
	}
	
	private boolean isActionValid(double chipValue) {
		if (chipValue > getPlayer().getMonney()) {
			infoText = "!YOU DONT HAVE ENOUGH MONEY!";
			return false;
		}
		return true;
	}

	private void render(GraphicsContext gc) {
		gc.clearRect(0, 0, gameCanavas.getWidth(), gameCanavas.getHeight());
		table.render(gc);
		renderOther(gc);
		renderButtons(gc);
		renderCards(gc);
		renderChips(gc);
	}

	private void renderCards(GraphicsContext gc) {
		for (Card card : getPlayer().getPlayerCards()) {
			card.render(gc);
		}
		for (Card card : table.getDeck()) {
			card.render(gc);
		}
		for (Card card : table.getTableCards()) {
			card.render(gc);
		}
	}
	 
	private void renderChips(GraphicsContext gc) {
		for (Chip chip : chipList) {
			chip.render(gc);
		}
		for(Chip chipMember : potChips){
			chipMember.render(gc);
		}
		table.getPot().render(gc);
		if (dragChip != null) {
			dragChip.render(gc);
		}
		
	}
	
	private void renderButtons(GraphicsContext gc){
		for(CustomButton buttonMember : buttonList){
			buttonMember.render(gc);
		}
	}
	
	private void renderOther(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillText(String.format("Mouse X: %s  Mouse Y: %s", mouse.getX(), mouse.getY()), 40, 30);
		if (hoveringOver != null) {
			gc.fillText(String.format("Object: %s", hoveringOver.getClass().getName()), 40, 44);
		}
		if(!infoText.equals("")){
			gc.setLineWidth(1);
			gc.setFill(Color.RED);
			gc.fillText(infoText, mouse.getX(), mouse.getY());
			if(infoOpacity >= 0.00){
				infoOpacity -= 0.01;
			}else{
				infoText = "";
				infoOpacity = 1.00;
			}
		}
		
		gc.setFill( Color.GREENYELLOW);
		gc.fillText("YOUR MONEY: " + getPlayer().getMonney(), 630, 460);
		
		table.getPot().renderText(gc);
		table.getPot().render(gc);
		
		double textOffset = 0.00;
		gc.setFill( Color.BLUE);
		for(String text : eventMessagesList){
			gc.fillText(text, 410.0, 0.0 + textOffset);
			textOffset += 25.00;
		}
		
		
		double offset = 20.00;
		gc.setFill( Color.BLUEVIOLET );
		gc.fillText("***PLAYERS***", 900, 35);
		
		gc.setFill( Color.BLUE );
		for(Player playerMember : playerList){
			if(playerMember.isCurrentTurn()){
				gc.fillText("->" + playerMember.getUsername() + " " + playerMember.getMonney() + "$", 900, 35+offset);
			}else
				gc.fillText(playerMember.getUsername() + " " + playerMember.getMonney() + "$", 900, 35+offset);
			
			offset += 15.00;
		}
	}

	private void loadAssets() {

		assetHandler = new AssetHandler();
		assetHandler.loadImages();

		//MOCK_DEBUG();
		
		
		table = new Table();
		table.setImage(assetHandler.getIamge("TABLE"), TABLE_WIDTH, TABLE_HEIGHT);
		Pot pot = new Pot();
		pot.setImage(assetHandler.getIamge("POT"), POT_WIDTH, POT_HEIGHT);
		pot.setPosition(400, 260);
		table.setPot(pot);
		
		eventMessagesList = new ArrayList<>();
		eventMessagesList.add("Game Started");
		generateChips();
		generateDeck();
		generateButtons();
		

		mouse = new Mouse(gameCanavas);
		gameCanavas.setOnMousePressed(mouse);
		gameCanavas.setOnMouseReleased(mouse);
		gameCanavas.setOnMouseMoved(mouse);
		
		font = assetHandler.getFont("Stoehr_Numbers");
		gameCanavas.getGraphicsContext2D().setFont(font);
		
		gameCanavas.setOnDragDetected(mouse);
		gameCanavas.setOnMouseDragged(mouse);
		
	}
	
	private void generateButtons(){
		double offset = 0.00;
		buttonList = new ArrayList<>();
		for(RegistratedButtons ids : RegistratedButtons.values()){
			CustomButton newButton = new CustomButton();
			newButton.setId(ids.toString());
			newButton.addSprite(ButtonState.BUTTON_NONE, assetHandler.getIamge("BUTTON_NONE"), BUTTON_WIDTH, BUTTON_HEIGHT);
			newButton.addSprite(ButtonState.BUTTON_MOUSE_OVER, assetHandler.getIamge("BUTTON_MOUSE_OVER"), BUTTON_WIDTH, BUTTON_HEIGHT);
			newButton.addSprite(ButtonState.BUTTON_PRESSED, assetHandler.getIamge("BUTTON_PRESSED"), BUTTON_WIDTH, BUTTON_HEIGHT);
			newButton.setPosition(300, 260 + offset);
			newButton.setText(ids.toString().toLowerCase().replaceAll("_", " "));
			newButton.setTextFill(Color.WHITESMOKE);
			newButton.setDisabled(true);
			offset += 25.00;
			newButton.changeCurrentState(ButtonState.BUTTON_NONE);
			buttonList.add(newButton);
		}
	}
	
	private void generateChips(){
		double offset = 0;
		chipList = new ArrayList<>();
		potChips = new ArrayList<>();
		for (ChipValue value : ChipValue.values()) {
			Chip chip = new Chip();
			chip.setChipValue(value);
			chip.setImage(assetHandler.getIamge(value.toString()), CHIP_WIDTH, CHIP_HEIGHT);
			chip.setPosition(350 + offset, 500);
			chip.setVelocity(0.0, 0.0);
			offset += 53;
			chipList.add(chip);
		}
	}	
	
	private void generateDeck(){
		double offset = 0;
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				try {
					Card card = new Card(rank, suit);
					card.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), 0,0);
					card.addSprite(State.SHOWN, assetHandler.getIamge(rank.toString() + "_" + suit.toString()), 0,0);
					card.addSprite(State.NONE, assetHandler.getIamge("CARD_NONE"), 0,0);
					
					card.setPosition(DECK_LOCATION_X + offset, DECK_LOCATION_Y);
					card.setWidth(CARD_WIDTH);
					card.setHeight(CARD_HEIGHT);
					offset += 0.1;
					card.changeCurrentState(State.SHOWN);
					table.getDeck().add(card);
				} catch (Exception ex) {
					
				}
			}
		}
	}
	
	private void MOCK_DEBUG(){
		Room room = new Room("This Room");
		User user = new User();
		user.setUsername("Player");
		room.addUser(user);
		mainController.setUser(user);
		mainController.setRoom(room);
		
		Player me = new Player();
		me.setUsername("Player");
		me.setMonney(100.00);
		me.setCurrentTurn(true);
		me.setPlayerCards(new ArrayList<>());
		
		playerList = new ArrayList<>();
		playerList.add(me);
		
		Card c = new Card(Rank.EIGHT, Suit.SAPDES);
		c.setImage(assetHandler.getIamge("EIGHT_SAPDES"), CARD_WIDTH, CARD_HEIGHT);
		c.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
		c.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), 0,0);
		c.addSprite(State.SHOWN, assetHandler.getIamge("EIGHT_SAPDES"), 0,0);
		c.addSprite(State.NONE, assetHandler.getIamge("CARD_NONE"), 0,0);
		c.setWidth(CARD_WIDTH);
		c.setHeight(CARD_HEIGHT);
		c.changeCurrentState(State.BACK);
		
		Card e	= new Card(Rank.NINE, Suit.HEARTS);
		e.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
		e.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), 0,0);
		e.addSprite(State.SHOWN, assetHandler.getIamge("NINE_HEARTS"), 0,0);
		e.addSprite(State.NONE, assetHandler.getIamge("CARD_NONE"), 0,0);
		e.setWidth(CARD_WIDTH);
		e.setHeight(CARD_HEIGHT);
		e.changeCurrentState(State.BACK);
		
		me.getPlayerCards().add(c);
		me.getPlayerCards().add(e);
		//X-560 Y-390
		
		table = new Table();
		table.setImage(assetHandler.getIamge("TABLE"), TABLE_WIDTH, TABLE_HEIGHT);
		Pot pot = new Pot();
		pot.setImage(assetHandler.getIamge("POT"), POT_WIDTH, POT_HEIGHT);
		pot.setPosition(400, 260);
		table.setPot(pot);
		
		DEBUG_TABLE_CARD_GEN();
		
		updateQuee = new ArrayList<>();
		isCardPlayerAnimationStart = true;
	}
	
	private void DEBUG_TABLE_CARD_GEN(){
		for(int i = 0; i<5; i++){
			Card d = new Card(Rank.ACE, Suit.SAPDES);
			d.addSprite(State.BACK, assetHandler.getIamge("RED_DECK"), 0,0);
			d.addSprite(State.SHOWN, assetHandler.getIamge("ACE_SAPDES"), 0,0);
			d.addSprite(State.NONE, assetHandler.getIamge("CARD_NONE"), 0,0);
			d.setWidth(CARD_WIDTH);
			d.setHeight(CARD_HEIGHT);
			d.changeCurrentState(State.BACK);
			d.setPosition(DECK_LOCATION_X, DECK_LOCATION_Y);
			table.addCardToTable(d);
		}
	}

	public void freeReasurces() {
		try{
			if(clientSocket != null){
				clientSocket.close();
				clientSocket = null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			clientSocket = null;
		}
	}

	public void setupGame(int port) {
		updateQuee = new ArrayList<>();
		createPlayerList();
		listenSerever(port);
		startGameLoop();
		mainController.setScene(GameScenes.PLAY);
	}
	
	private void createPlayerList(){
		playerList = new ArrayList<>();
		for(User userMember : mainController.getRoom().getUserList()){
			Player newPlayer = new Player();
			newPlayer.setUsername(userMember.getUsername());
			newPlayer.setPlayerCards(new ArrayList<>());
			newPlayer.setMonney(0.00);
			playerList.add(newPlayer);
		}
	}
	
	public void listenSerever(int port){
		try{
			clientSocket = new Socket(mainController.getSettings().getServerLocation(), port);
			
			inputStream = new ObjectInputStream(clientSocket.getInputStream());
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			startListeningServer();
			play = true;
			
			outputStream.writeObject(getPlayer());
			outputStream.flush();
			
			//((LobbyController)mainController.getController(GameScenes.LOBBY)).switchRoomsPane(mainController.getScene(GameScenes.ROOMS_PANE));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void startListeningServer(){
		Runnable target = new Runnable() {
		    @Override
			public void run() {
				while (play) {
					try {
						GameUpdate update = (GameUpdate)inputStream.readObject();
						updateQuee.add(update);
					} catch (Exception ex) {
						ex.printStackTrace();
						play = false;
					}
				}
			}
		};
		listeningThread = new Thread(target);
		listeningThread.setDaemon(true);
		listeningThread.start();
	}
	
	private void createPlayerActionUpdate(PlayerAction action){
		playerLastAction = action;
		GameUpdate update = new GameUpdate();
		update.setPlayer(getPlayer());
		update.setUpdateType(UpdateType.PLAYER_ACTION);
		update.setActionType(action);
		update.setPotSize(table.getPot().getPotAmount());
		update.setRaiseAmount(raiseAmount);
		sendUpdate(update);
	}
	
	private void createGameActionUpdate(UpdateType type){
		playerLastAction = PlayerAction.NONE;
		GameUpdate update = new GameUpdate();
		update.setPlayer(getPlayer());
		update.setUpdateType(type);
		update.setActionType(PlayerAction.NONE);
		update.setPotSize(table.getPot().getPotAmount());
		update.setRaiseAmount(raiseAmount);
		sendUpdate(update);
	}
	
	private void sendUpdate(GameUpdate update){
		try{
			if(playerLastAction != update.getActionType() || playerLastAction == PlayerAction.NONE){
				update.setPlayer(getPlayer());
	    		outputStream.writeObject(update);
	    		outputStream.flush();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns Player that is registered on this account
	 * @return
	 */
	public Player getPlayer(){
		for(Player playerMember : playerList){
			if(playerMember.getUsername().equals(mainController.getUser().getUsername())){
				return playerMember;
			}
		}
		return null;
	}
}
