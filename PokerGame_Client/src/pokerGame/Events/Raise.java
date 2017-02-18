package pokerGame.Events;

import java.math.BigDecimal;

public class Raise implements IEvent {
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
