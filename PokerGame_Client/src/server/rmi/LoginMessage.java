package server.rmi;

import java.io.Serializable;

public class LoginMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum Type{
		LOGIN_PASSED,
		LOGIN_FAILED,
		REGISTRATION_PASSED,
		REGISTRATION_FAILED,
		SERVER_ERROR,
	}
	
	private Type type;
	private String additionalInfo;
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
