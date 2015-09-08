package vn.vif.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionException {

	private final Log logger;

	public FunctionException(Class<?> clazz, Exception e) {
		logger = LogFactory.getLog(clazz.getName());
		logger.error(e.getMessage(), e);
		/*for (StackTraceElement st : e.getStackTrace()) {
			Method[] list = clazz.getMethods();
			for (Method method : list) {
				if (st.getMethodName().equalsIgnoreCase(method.getName()))
					logger.error(e.getMessage() + " \n " + st.getClassName() + "." + st.getMethodName() 
							+ "(" + st.getFileName() + ":" + st.getLineNumber() + ")");
			}
		}*/
	}

	public FunctionException(String message) {
		logger = LogFactory.getLog("");
		logger.info(message);
	}

}
