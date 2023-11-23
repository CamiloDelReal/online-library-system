package utils;

public class BooleanWrapper {
	private boolean value;

	public BooleanWrapper(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public void setFalse(){
		value = false;
	}
	
	public void setTrue(){
		value = true;
	}
}
