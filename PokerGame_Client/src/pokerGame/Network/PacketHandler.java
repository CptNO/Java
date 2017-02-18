package pokerGame.Network;

import pokerGame.Controllers.GameController;
import pokerGame.Controllers.MainController;

public class PacketHandler {
	
	private GameController gameController;
	private MainController mainController;
	
	public void handlePacket(Packet packet){
		switch (packet.getAction()) {
		case RAISE:
			
			break;
		case CALL:
			break;
		case FOLD:
			break;
		case CHECK:
			break;
		default:
			break;
		}
	}
	
}
