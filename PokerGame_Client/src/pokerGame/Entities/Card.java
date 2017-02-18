package pokerGame.Entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import javafx.scene.image.Image;

public class Card extends Entity implements Serializable {

	public enum Suit{
		CLUBS,
		DIAMONDS,
		HEARTS,
		SAPDES,
		DECK
	}
	
	public enum Rank{
		TWO,
		TREE,
		FOUR,
		FIVE,
		SIX,
		SVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE,
		RED
	}
	
	public enum State{
		SHOWN,
		BACK,
		NONE
	}
	
	private Suit suit;
	private Rank rank;
	private HashMap<State, Image> sprites;
	
	public Card(Rank rank, Suit suit){
		this.rank = rank;
		this.suit = suit;
		setSprites(new HashMap<>());
	}
	
	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public HashMap<State, Image> getSprites() {
		return sprites;
	}

	public void setSprites(HashMap<State, Image> sprites) {
		this.sprites = sprites;
	}
	
	public void addSprite(State state, Image image, double width, double height){
		this.width = width;
		this.height = height;
		sprites.put(state, image);
	}
	
	public Image getSprite(State state){
		return sprites.get(state);
	}
	
	public void changeCurrentState(State state){
		this.setImage(sprites.get(state), this.width, this.height);
	}
}
