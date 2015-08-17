package vn.vif.utils;

public class VIFException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5844921246484215900L;
	private String errorCode;
	
    public VIFException(String message) {
        super(message);
    }

    public VIFException(String message, Throwable cause) {
        super(message, cause);
    }

	public VIFException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
