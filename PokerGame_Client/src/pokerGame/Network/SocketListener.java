package pokerGame.Network;

public interface SocketListener {
	public void onPacketRecived(Object packet);
	public void onClose(Boolean isClosed);
	public void onConnect(Boolean isClosed);
	public void onSend(Object packet);
}
