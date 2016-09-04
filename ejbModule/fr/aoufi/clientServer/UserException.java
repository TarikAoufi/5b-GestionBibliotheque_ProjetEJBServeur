package fr.aoufi.clientServer;



public class UserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	// la classe mere possède le message
	// code d'erreur
	private int code;

	public UserException() {
		super();
	}
	
	public UserException(int code) {
		super();
		this.code = code;
	}

	public UserException(String message, int code) {
		super(message);
		this.code = code;
	}

	public UserException(Throwable cause, int code) {
		super(cause);
		this.code = code;
	}

	public UserException(String message, Throwable cause, int code) {
		super(message, cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return super.getMessage();
	}

}
