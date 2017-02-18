package pokerGame.Entities;

import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class AssetHandler {
	
	protected final double CHIP_WIDTH = 50.00;
	protected final double CHIP_HEIGHT = 50.00;
	protected final double CARD_WIDTH = 200.00;
	protected final double CARD_HEIGHT = 100.00;
	protected final double BUTTON_WIDTH = 100.00;
	protected final double BUTTON_HEIGHT = 200.00;
	
	private HashMap<String, Image> imageList;
	private HashMap<String, Font> fontList;

	public AssetHandler() {
		imageList = new HashMap<>();
		fontList = new HashMap<>();
	}

	public void loadImages() {
		loadChips();
		loadCards();
		loadButtons();
		loadFotns();
		loadOther();
	}
	
	private void loadChips(){
		imageList.put("ONE", new Image("assets/PNG/Chips/1.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("TWO", new Image("assets/PNG/Chips/2.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("FIVE", new Image("assets/PNG/Chips/5.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("TEN", new Image("assets/PNG/Chips/10.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("TWENTY", new Image("assets/PNG/Chips/20.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("TWENTYFIVE", new Image("assets/PNG/Chips/25.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
		imageList.put("FIFTY", new Image("assets/PNG/Chips/50.png", CHIP_WIDTH, CHIP_HEIGHT, true, true));
	}
	
	private void loadCards(){
		imageList.put("TWO_CLUBS", new Image("assets/PNG/Cards/club_2.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TREE_CLUBS", new Image("assets/PNG/Cards/club_3.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FOUR_CLUBS", new Image("assets/PNG/Cards/club_4.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FIVE_CLUBS", new Image("assets/PNG/Cards/club_5.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SIX_CLUBS", new Image("assets/PNG/Cards/club_6.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SVEN_CLUBS", new Image("assets/PNG/Cards/club_7.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("EIGHT_CLUBS", new Image("assets/PNG/Cards/club_8.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("NINE_CLUBS", new Image("assets/PNG/Cards/club_9.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TEN_CLUBS", new Image("assets/PNG/Cards/club_10.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("JACK_CLUBS", new Image("assets/PNG/Cards/club_J.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("QUEEN_CLUBS", new Image("assets/PNG/Cards/club_Q.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("KING_CLUBS", new Image("assets/PNG/Cards/club_K.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("ACE_CLUBS", new Image("assets/PNG/Cards/club_A.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		
		imageList.put("TWO_DIAMONDS", new Image("assets/PNG/Cards/diamond_2.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TREE_DIAMONDS", new Image("assets/PNG/Cards/diamond_3.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FOUR_DIAMONDS", new Image("assets/PNG/Cards/diamond_4.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FIVE_DIAMONDS", new Image("assets/PNG/Cards/diamond_5.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SIX_DIAMONDS", new Image("assets/PNG/Cards/diamond_6.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SVEN_DIAMONDS", new Image("assets/PNG/Cards/diamond_7.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("EIGHT_DIAMONDS", new Image("assets/PNG/Cards/diamond_8.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("NINE_DIAMONDS", new Image("assets/PNG/Cards/diamond_9.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TEN_DIAMONDS", new Image("assets/PNG/Cards/diamond_10.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("JACK_DIAMONDS", new Image("assets/PNG/Cards/diamond_J.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("QUEEN_DIAMONDS", new Image("assets/PNG/Cards/diamond_Q.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("KING_DIAMONDS", new Image("assets/PNG/Cards/diamond_K.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("ACE_DIAMONDS", new Image("assets/PNG/Cards/diamond_A.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		
		imageList.put("TWO_HEARTS", new Image("assets/PNG/Cards/heart_2.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TREE_HEARTS", new Image("assets/PNG/Cards/heart_3.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FOUR_HEARTS", new Image("assets/PNG/Cards/heart_4.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FIVE_HEARTS", new Image("assets/PNG/Cards/heart_5.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SIX_HEARTS", new Image("assets/PNG/Cards/heart_6.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SVEN_HEARTS", new Image("assets/PNG/Cards/heart_7.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("EIGHT_HEARTS", new Image("assets/PNG/Cards/heart_8.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("NINE_HEARTS", new Image("assets/PNG/Cards/heart_9.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TEN_HEARTS", new Image("assets/PNG/Cards/heart_10.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("JACK_HEARTS", new Image("assets/PNG/Cards/heart_J.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("QUEEN_HEARTS", new Image("assets/PNG/Cards/heart_Q.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("KING_HEARTS", new Image("assets/PNG/Cards/heart_K.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("ACE_HEARTS", new Image("assets/PNG/Cards/heart_A.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		
		imageList.put("TWO_SAPDES", new Image("assets/PNG/Cards/spade_2.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TREE_SAPDES", new Image("assets/PNG/Cards/spade_3.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FOUR_SAPDES", new Image("assets/PNG/Cards/spade_4.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("FIVE_SAPDES", new Image("assets/PNG/Cards/spade_5.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SIX_SAPDES", new Image("assets/PNG/Cards/spade_6.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("SVEN_SAPDES", new Image("assets/PNG/Cards/spade_7.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("EIGHT_SAPDES", new Image("assets/PNG/Cards/spade_8.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("NINE_SAPDES", new Image("assets/PNG/Cards/spade_9.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("TEN_SAPDES", new Image("assets/PNG/Cards/spade_10.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("JACK_SAPDES", new Image("assets/PNG/Cards/spade_J.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("QUEEN_SAPDES", new Image("assets/PNG/Cards/spade_Q.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("KING_SAPDES", new Image("assets/PNG/Cards/spade_K.png", CARD_WIDTH, CARD_HEIGHT, true, true));
		imageList.put("ACE_SAPDES", new Image("assets/PNG/Cards/spade_A.png", CARD_WIDTH, CARD_HEIGHT, true, true));
	
		imageList.put("RED_DECK", new Image("assets/PNG/Cards/BackgroundRed.png", CARD_WIDTH, CARD_HEIGHT, true, true));
	}
	
	private void loadButtons(){
		imageList.put("BUTTON_NONE", new Image("assets/PNG/UI/red_button11.png", 0, 0, true, true));
		imageList.put("BUTTON_MOUSE_OVER", new Image("assets/PNG/UI/red_button12.png", 0, 0, true, true));
		imageList.put("BUTTON_PRESSED", new Image("assets/PNG/UI/red_button13.png", 0, 0, true, true));
	}
	
	private void loadOther(){
		imageList.put("TABLE", new Image("assets/PNG/table.png", 1025.0, 600.0, true, true));
		imageList.put("POT", new Image("assets/PNG/frame.png", 200, 200, true, true));
		imageList.put("CARD_NONE", new Image("assets/PNG/frame_fliped.png", 150, 100, true, true));
	}
	
	private void loadFotns(){
		fontList.put("Stoehr_Numbers", new Font("assets/Fonts/Stoehr Numbers.ttf",18));
	}

	public Image getIamge(String key) {
		return imageList.get(key);
	}
	
	public Font getFont(String key){
		return fontList.get(key);
	}
	
	
}
