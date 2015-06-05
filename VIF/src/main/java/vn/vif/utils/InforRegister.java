package vn.vif.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InforRegister {
	public static final int SIZE_120KB = 120 * 1024; // 120KB
	public static final int SIZE_600KB = 600 * 1024; // 600KB
	private static String LOGIN_PAGE = "http://dktt.mobifone.com.vn/web/vn/";
	private static String LOGIN_ACTION = "http://dktt.mobifone.com.vn/web/vn/users/authenticate.jsp";
	private static String REGISTER_PAGE = "http://dktt.mobifone.com.vn/web/vn/prepaid1/index.jsp";
	private static String UPLOAD_IMAGES_PAGE = "http://dktt.mobifone.com.vn/web/vn/prepaid1/upload_anh.jsp";
	private static String UPLOAD_IMAGES_ACTION = "http://dktt.mobifone.com.vn/web/vn/prepaid1/upload_anh_act.jsp";
	private static String LOG_OUT_ACTION = "http://dktt.mobifone.com.vn/web/vn/users/logout.jsp";
	private static String CHECK_PERSONID_ACTION = "http://dktt.mobifone.com.vn/web/vn/prepaid1/check_cmt.jsp?cmt=";
	private static String REGISTER_ACTION = "http://dktt.mobifone.com.vn/web/vn/prepaid1/prepaid_reg_act.jsp";
	private static final String DEFAULT_HOST = "dktt.mobifone.com.vn";
	private static final String CrLf = "\r\n";
	private static final String START_PARAM = "--p";
	private static final String END_PARAM = START_PARAM + "--";
	private static final int MAX_RE_LOGIN = 5;
	
	private CookieManager cm = new CookieManager();
	
	private String userName;
	private String passWord;
	private String host = "dktt.mobifone.com.vn";
	
	//private static InforRegister instance;

	public InforRegister(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}
	
	public InforRegister(String userName, String passWord, String host) {
		this.userName = userName;
		this.passWord = passWord;
		this.host = host;
		initURL();
	}
	
	private void initURL() {
		LOGIN_PAGE = "http://" + host + "/web/vn/";
		LOGIN_ACTION = "http://" + host + "/web/vn/users/authenticate.jsp";
		REGISTER_PAGE = "http://" + host + "/web/vn/prepaid1/index.jsp";
		UPLOAD_IMAGES_PAGE = "http://" + host + "/web/vn/prepaid1/upload_anh.jsp";
		UPLOAD_IMAGES_ACTION = "http://" + host + "/web/vn/prepaid1/upload_anh_act.jsp";
		LOG_OUT_ACTION = "http://" + host + "/web/vn/users/logout.jsp";
		CHECK_PERSONID_ACTION = "http://" + host + "/web/vn/prepaid1/check_cmt.jsp?cmt=";
		REGISTER_ACTION = "http://" + host + "/web/vn/prepaid1/prepaid_reg_act.jsp";
	}

	/*public static InforRegister getInstance(String userName, String password) {
		if (instance == null) {
			return instance = new InforRegister(userName, password); 
		}
		return instance;
	}*/
	
	private void getCookie() throws IOException {
		URL url = new URL(LOGIN_PAGE);
		URLConnection con = url.openConnection();
		con.connect();
		//**** get cookies
		cm.storeCookies(con);
	}
	
	public boolean checkLogin() {
		try {
			StringBuffer sb = login();
			return !sb.toString().contains("loginForm");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	private StringBuffer login() throws IOException {
		return login(1);
	}
	
	private StringBuffer login(int reLoginCount) throws IOException {
		// check do login if not login
		StringBuffer br = accessRegisterPage();
		if (!br.toString().contains("loginForm")) {
			return br;
		}
		
		// get cookie
		getCookie();
		
		// do login
		try {
			URL url = new URL(LOGIN_ACTION);
			URLConnection con = url.openConnection();
			cm.addCookies(con, "username", userName);
			cm.setCookies(con);
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
			String parameters = "password="+passWord+"&username="+userName+"&Submit.x=0&Submit.y=0";
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();
			InputStream in = con.getInputStream();
			in.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try {
				if (reLoginCount >= MAX_RE_LOGIN) {
					throw e;
				}
				System.out.println("Connection lost. Relogin...");
				Thread.sleep(500);
				return login(reLoginCount ++);
			} catch (InterruptedException e1) {
			}
		}
		System.out.println("Do login.");
		
		//**** access register form
		br = accessRegisterPage();
		return br;
	}

	//**** access register form
	public StringBuffer accessRegisterPage() throws IOException {
		StringBuffer br;
		HttpGet get = new HttpGet(REGISTER_PAGE);
		cm.setCookies(get);
		CloseableHttpResponse response = client.execute(get);
		try {
			br = getContent(response.getEntity().getContent());
		} finally {
			response.close();
		}
		return br;
	}
	
	private CloseableHttpClient client = HttpClients.createDefault();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public RegisterResult register(String name, String mobile, String personalId, Date issueDate, Date birthDay, 
			String city, String seriNumber, String file1, String file2, String file3) throws IOException {
		
		StringBuffer br = login();
		if (br.toString().contains("loginForm")) {
			return new RegisterResult(false, "Không thể đăng nhập vào server.");
		}
		Document doc = Jsoup.parse(br.toString());
		
		//**** access upload pictures form
	    HttpPost post = new HttpPost(UPLOAD_IMAGES_PAGE);
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    Element frm = doc.getElementById("frmMain");
	    Elements inputs = frm.getElementsByAttributeValueContaining("type", "hidden");
		for (Element el : inputs) {
			nameValuePairs.add(new BasicNameValuePair(el.attr("name"), el.attr("value")));
		}
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("cmt", personalId));
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("ngaycap", dateFormat.format(issueDate)));
		nameValuePairs.add(new BasicNameValuePair("ngaysinh", dateFormat.format(birthDay)));
		nameValuePairs.add(new BasicNameValuePair("seri", seriNumber));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
		cm.setCookies(post);
		CloseableHttpResponse response = client.execute(post);
		try {
			br = getContent(response.getEntity().getContent());
		} finally {
			response.close();
		}
		doc = Jsoup.parse(br.toString());
		
		
		//**** post image to server
		URL url = new URL(UPLOAD_IMAGES_ACTION);
		URLConnection con = url.openConnection();
		cm.setCookies(con);
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=p"); 
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		String extension = file1.substring(file1.lastIndexOf(".") + 1); 
		String fieldData = START_PARAM + CrLf +  
		"Content-Disposition: form-data; name=\"file1\"; filename=\"file1."+extension+"\";" + CrLf +
		"Content-Type: image/"+extension+";" + CrLf + CrLf;
		wr.writeBytes(fieldData);
		DataInputStream dis;
		int length;
		byte[] bs = new byte[1024 * 10];
		try {
			dis = new DataInputStream(new FileInputStream(file1));
			while ((length = dis.read(bs)) != -1) {
				wr.write(bs, 0, length);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			return new RegisterResult(false, "Không tìm thấy file scan CMT mặt trước.");
		}
		extension = file2.substring(file2.lastIndexOf(".") + 1); 
		fieldData = START_PARAM + CrLf +  
		"Content-Disposition: form-data; name=\"file2\"; filename=\"file2."+extension+"\";" + CrLf +
		"Content-Type: image/"+extension+";" + CrLf + CrLf;
		wr.writeBytes(fieldData);
		try {
			dis = new DataInputStream(new FileInputStream(file2));
			while ((length = dis.read(bs)) != -1) {
				wr.write(bs, 0, length);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			return new RegisterResult(false, "Không tìm thấy file scan CMT mặt sau.");
		}	
		extension = file3.substring(file3.lastIndexOf(".") + 1);
		fieldData = START_PARAM + CrLf +  
		"Content-Disposition: form-data; name=\"file3\"; filename=\"file3."+extension+"\";" + CrLf +
		"Content-Type: image/"+extension+";" + CrLf + CrLf;
		wr.writeBytes(fieldData);
		try {
			dis = new DataInputStream(new FileInputStream(file3));
			while ((length = dis.read(bs)) != -1) {
				wr.write(bs, 0, length);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			return new RegisterResult(false, "Không tìm thấy file scan mẫu đăng ký.");
		}
		frm = doc.getElementById("frmUpload");
		inputs = frm.getElementsByAttributeValueContaining("type", "hidden");
		for (Element el : inputs) {
			fieldData = START_PARAM + CrLf +  
			"Content-Disposition: form-data; name=\""+el.attr("name")+"\"" + CrLf + CrLf +
			el.attr("value") + CrLf;
			wr.writeBytes(fieldData);
		}
		wr.writeBytes(END_PARAM);
		wr.flush();
		wr.close();
		InputStream in = con.getInputStream();
		doc = Jsoup.parse(in, "windows-1252", url.getHost());
		in.close();
		System.out.println("Upload images successful.");
		
		//***register information
	    post = new HttpPost(REGISTER_ACTION);
		
	    nameValuePairs = new ArrayList<NameValuePair>();
	    frm = doc.getElementById("frmMain");
	    inputs = frm.getElementsByAttributeValueContaining("type", "hidden");
		for (Element el : inputs) {
			nameValuePairs.add(new BasicNameValuePair(el.attr("name"), el.attr("value")));
		}
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("cmt", personalId));
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("ngaycap", dateFormat.format(issueDate)));
		nameValuePairs.add(new BasicNameValuePair("ngaysinh", dateFormat.format(birthDay)));
		nameValuePairs.add(new BasicNameValuePair("seri", seriNumber));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
		cm.setCookies(post);
		response = client.execute(post);
		try {
			br = getContent(response.getEntity().getContent());
		} finally {
			response.close();
		}
		
		//System.out.println(br);
		String content = br.toString();
		if (content.contains("Redirect to")) {
			content = content.replaceAll(DEFAULT_HOST, host);
			String link = LOGIN_PAGE;
			doc = Jsoup.parse(content);
			Elements els = doc.getElementsByAttributeValueContaining("href", post.getURI().getHost());
			if (!els.isEmpty()) {
				link = els.first().attr("href");
			}
			// check error message on redirect link 
			if (link.contains("rs=")) {
				return new RegisterResult(false, link.substring(link.indexOf("rs=") + 3));
			}
			// check if success
			if (link.endsWith("?ec=1000")) {
				return new RegisterResult(true, "Quá trình đăng ký thông tin thành công.");
			}
			url = new URL(link);
			con = url.openConnection();
			cm.setCookies(con);
			in = con.getInputStream();
			doc = Jsoup.parse(in, "windows-1252", url.getHost());
			in.close();
			// get message
			els = doc.getElementsByAttributeValueContaining("background", "blue_header_bg.gif");
			if (!els.isEmpty()) {
				String mgs = els.first().parent().parent().children().last().text();
				
				return new RegisterResult(false, mgs);
			}
			
		}
		
		return new RegisterResult(false, "Chưa có phản hồi từ server.");
	}
	
	public RegisterResult checkPersonId(String personId) throws IOException {
		
		login();
		
		URL url = new URL(CHECK_PERSONID_ACTION + personId);
		URLConnection con = url.openConnection();
		cm.setCookies(con);
		InputStream in = con.getInputStream();
		Document doc = Jsoup.parse(in, "windows-1252", url.getHost());
		in.close();
		Elements els = doc.getElementsByAttributeValueContaining("style", "height:75px; width:500px; font-family: Arial, Helvetica, sans-serif;font-size:15px;font-weight:bold;color:#0062bd; readonly=");
		if (!els.isEmpty()) {
			return new RegisterResult(true, els.first().text());
		}
		
		return new RegisterResult(false, "Không kiểm tra được.");
	}

	public void logout() throws IOException {
		//***log out
		URL url = new URL(LOG_OUT_ACTION);
		URLConnection con = url.openConnection();
		cm.setCookies(con);
		InputStream in = con.getInputStream();
		in.close();
		System.out.println("Logout successful.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InforRegister ir = new InforRegister("01284722888", "mayman275");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			RegisterResult re = ir.register("Nguyễn Thành Nhân", "0909037997", "285856743", dateFormat.parse("09/12/2009"), 
					dateFormat.parse("09/12/1979"), "BinhDuong", "7954", "C:\\Untitled.png", "C:\\Untitled.png", "C:\\Untitled.png");
			System.out.println(re.getMessage());
			//System.out.println(ir.checkPersonId("285856743").getMessage());
			ir.logout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StringBuffer getContent(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(in));
		String inputLine;
		StringBuffer re = new StringBuffer();
		
		while ((inputLine = br.readLine()) != null) {
			re.append(inputLine + "\n");
		}
		br.close();
		return re;
	}

}
