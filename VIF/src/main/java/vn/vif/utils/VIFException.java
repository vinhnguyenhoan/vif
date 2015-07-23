package vn.vif.utils;

public class VIFException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5844921246484215900L;
	
    public VIFException(String message) {
        super(message);
    }

    public VIFException(String message, Throwable cause) {
        super(message, cause);
    }

}
