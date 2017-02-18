package pokerGame.Events;

import java.math.BigDecimal;

public class Call implements IEvent{
	private boolean callAndRaise;
	private BigDecimal amount;
	
	public boolean isCallAndRaise() {
		return callAndRaise;
	}
	public void setCallAndRaise(boolean callAndRaise) {
		this.callAndRaise = callAndRaise;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
