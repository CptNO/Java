package pokerGame.Entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import objects.Entity;


public class Mouse extends Entity implements EventHandler<MouseEvent> {
	private MouseEvent mouseEvent;
	private boolean isMousePressed;

	public Mouse(Canvas playground){
		//Empty
	}
	
	public boolean isMousePressed(){
		return isMousePressed;
	}
	

	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}

	public void setMouseEvent(MouseEvent mouseEvent) {
		this.mouseEvent = mouseEvent;
	}
	
	@Override
	public void handle(MouseEvent event) {
		positionX = event.getSceneX();
		positionY = event.getSceneY();
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
			isMousePressed = true;
		}else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
			isMousePressed = false;
		}
	}
}
