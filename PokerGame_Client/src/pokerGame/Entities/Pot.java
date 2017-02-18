package pokerGame.Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import objects.Entity;

public class Pot extends Entity {
	private double potAmount;

	public double getPotAmount() {
		return potAmount;
	}

	public void setPotAmount(double potAmount) {
		this.potAmount = potAmount;
	}
	
	public void addToPot(Chip chip){
		potAmount += chip.getChipValue().getValue(chip.getChipValue());
	}
	
	public void addToPot(double amount){
		potAmount += amount;
	}
	
	public void renderText(GraphicsContext gc){
		gc.setFill(Color.DARKRED);
		gc.fillText(String.format("%s $$$", potAmount), 480, 380);
	}

}
