package pokerGame.Entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import objects.Entity;
import objects.Card.State;

public class CustomButton extends Entity{
	private String Id;
	private String text;
	private ButtonState currentState;
	private Map<ButtonState, Image> sprites;
	private boolean isDisabled;
	private Color textFill;
	
	public CustomButton(){
		setSprites(new HashMap<>());
	}
	
	public enum ButtonState{
		BUTTON_NONE,
		BUTTON_MOUSE_OVER,
		BUTTON_PRESSED
	}

	public void setSprites(HashMap<ButtonState, Image> sprites) {
		this.sprites = sprites;
	}
	
	public void addSprite(ButtonState state, Image image, double width, double height){
		this.width = width;
		this.height = height;
		sprites.put(state, image);
	}
	
	public void changeCurrentState(ButtonState state){
		this.currentState = state;
		this.setImage(sprites.get(state), width, height);
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(sprites.get(currentState), getX(), getY(), this.width, this.height);
		
		if(text != null){
			gc.setFill(textFill);
			gc.setFontSmoothingType(FontSmoothingType.LCD);
			gc.fillText(text, positionX + 15, positionY + 15);
		}
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public ButtonState getCurrentState() {
		return currentState;
	}
	
	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Color getTextFill() {
		return textFill;
	}

	public void setTextFill(Color textFill) {
		this.textFill = textFill;
	}
}
