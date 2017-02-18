package objects;

import java.io.Serializable;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity implements Serializable {
	protected Image image;
	protected double positionX;
	protected double positionY;
	protected double velocityX;
	protected double velocityY;
	protected double width;
	protected double height;

	public Entity()
    {
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
    }

	public void setImage(Image i, double width, double height) {
		image = i;
		this.width = width;
		this.height = height;
	}

	public void setImage(String filename) {
		Image i = new Image(filename);
		setImage(i, 0, 0);
	}
	
	public void setWidth(double width){
		this.width = width;
	}
	
	public void setHeight(double height){
		this.height = height;
	}
	
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}

	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}
	
	public double getX(){
		return positionX;
	}
	
	public double getY(){
		return positionY;
	}

	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY, width, height);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Entity s) {
		return s.getBoundary().intersects(this.getBoundary());
	}
}
