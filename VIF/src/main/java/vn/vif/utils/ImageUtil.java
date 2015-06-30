package vn.vif.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

public class ImageUtil {
	private static final Logger logger = Logger
			.getLogger(ImageUtil.class);
	
	public static String generateHashedPath(Date imgCreatedDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(imgCreatedDate);
		int imgYear = cal.get(Calendar.YEAR);
		int imgMonth = cal.get(Calendar.MONTH) + 1;
		int imgDay = cal.get(Calendar.DAY_OF_MONTH);
		String separator = System.getProperty("file.separator");
		StringBuffer hashedPath = new StringBuffer(separator);
		hashedPath.append(imgYear).append(separator).append(imgMonth)
				.append(separator).append(imgDay);

		return hashedPath.toString();
	}
	
	public static String writeBase64BinaryAsImageFile(String base64Binary, Date imgCreatedDate, String fileName, String imagesRootDir) {
		String hashedPath  = ImageUtil.generateHashedPath(imgCreatedDate);
		String imagePath = imagesRootDir + System.getProperty("file.separator") + hashedPath;
		File imageDir = (new File(imagePath));
		if (!imageDir.exists()) {
			boolean dirsCreated = imageDir.mkdirs();
			if (!dirsCreated) {
				logger.error("Cannot create directories to store image files");
				return "";
			}
		}
		File imageFile = new File(imagePath + System.getProperty("file.separator") + fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imageFile);
			byte[] imgData = DatatypeConverter.parseBase64Binary(base64Binary);
			fos.write(imgData);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			logger.error("An error occurs while writing image data of Point of Sale to file system: " + e.getMessage());
			return "";
		}
		
		return hashedPath + System.getProperty("file.separator") + fileName;
	}
	
	public static void writeImage(String path, InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		byte[] bf = new byte[1024];
		int len;
		while ((len = is.read(bf)) != -1) {
			fos.write(bf, 0, len);
		}
		fos.flush();
		fos.close();
	}
}
