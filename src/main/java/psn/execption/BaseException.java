package psn.execption;

public class BaseException extends Exception{
	
	String message = null;
	public BaseException(String message){
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
