package pokerGame.Entities;

public class Chip extends Entity {
	private ChipValue chipValue;
	
	public enum ChipValue
	{
		ONE(1.00),
		TWO(2.00),
		FIVE(5.00),
		TEN(10.00),
		TWENTY(20.00),
		TWENTYFIVE(25.00),
		FIFTY(50.00);
		
		private double value;
		
		ChipValue(double value){
			this.value = value;
		}
		
		public double getValue(ChipValue chip){
			return chip.value;
		}
 
	}

	public ChipValue getChipValue() {
		return chipValue;
	}
	
	public double getChipDoubleValue(){
		return chipValue.getValue(chipValue);
	}

	public void setChipValue(ChipValue chipSize) {
		this.chipValue = chipSize;
	}
}
