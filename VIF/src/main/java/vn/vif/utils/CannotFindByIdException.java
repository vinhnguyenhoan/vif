package vn.vif.utils;

public class CannotFindByIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2522049376062891942L;
	
	public CannotFindByIdException() {
        super();
    }
	
    public CannotFindByIdException(String message) {
        super(message);
    }

    public CannotFindByIdException(String message, Throwable cause) {
        super(message, cause);
    }

}
