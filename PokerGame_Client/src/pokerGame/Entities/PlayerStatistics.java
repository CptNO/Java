package pokerGame.Entities;

import java.io.Serializable;

public class PlayerStatistics implements Serializable {
	
	private double monneyWon;
	private double moneyLost;
	private double currentMoney;
	private double hoursPlayed;
	public double getMonneyWon() {
		return monneyWon;
	}
	public void setMonneyWon(double monneyWon) {
		this.monneyWon = monneyWon;
	}
	public double getMoneyLost() {
		return moneyLost;
	}
	public void setMoneyLost(double moneyLost) {
		this.moneyLost = moneyLost;
	}
	public double getCurrentMoney() {
		return currentMoney;
	}
	public void setCurrentMoney(double currentMoney) {
		this.currentMoney = currentMoney;
	}
	public double getHoursPlayed() {
		return hoursPlayed;
	}
	public void setHoursPlayed(double hoursPlayed) {
		this.hoursPlayed = hoursPlayed;
	}
	
	

}
