package vn.vif.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailUtils {
	 
	 
	public static void main(String[] a){
		 
		 final String username ="bhkppc2@vms.com.vn";

        final String password ="bhkppc2345";

        Properties props =new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.host", "mail.vms.com.vn");

        String to = "tai.le@itt.vn";
        String subject = "abc";
        String body = createBody("aaaaaaa");
          /*Doan nay ben em dang dung IP*/ 

        Session session = Session.getInstance(props,

       		 new javax.mail.Authenticator(){
            		protected PasswordAuthentication getPasswordAuthentication(){
            			return new PasswordAuthentication(username, password);
            		}
        		});

        		try{
        			
        			MimeMessage mimeMessage = new MimeMessage(session);
           	    mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
           	    mimeMessage.setFrom(new InternetAddress(username));
           	    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
           	    mimeMessage.setSubject(subject, "utf-8");
           	    mimeMessage.setContent(body , "text/html; charset=UTF-8");
           	    
           	    Transport.send(mimeMessage);

        			System.out.println("Done");

        		}catch(MessagingException e){

        			throw new RuntimeException(e);
        		}
	 	}
	
	 public static void sendMail(String to, String subject, String body){
		 
		 final String username ="bhkppc2@vms.com.vn";

         final String password ="bhkppc2345";

         Properties props =new Properties();

         props.put("mail.smtp.auth", "true");

         props.put("mail.smtp.host", "mail.vms.com.vn");

         /*Doan nay ben em dang dung IP*/ 

         Session session = Session.getInstance(props,

        		 new javax.mail.Authenticator(){
             		protected PasswordAuthentication getPasswordAuthentication(){
             			return new PasswordAuthentication(username, password);
             		}
         		});

         		try{
         			String content = createBody(body);
         			MimeMessage mimeMessage = new MimeMessage(session);
            	    mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
            	    mimeMessage.setFrom(new InternetAddress(username));
            	    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            	    mimeMessage.setSubject(subject, "utf-8");
            	    mimeMessage.setContent(content , "text/html; charset=UTF-8");
            	    
            	    Transport.send(mimeMessage);

         			//System.out.println("Done");

         		}catch(MessagingException e){

         			throw new RuntimeException(e);
         		}
	 	}
	 
	 
	 private static String createBody(String body){
		 String content = "";
		 
		 content = "<table width='100%' border='0'>";
		 content += "<tr>";
		 content += "<td width='100%' align='center' style='color:#000'>";
        
		 content = "<table width='100%' border='0' style='background-color:#1A5CBA'>";
		 content += "<tr>";
		 content += "<td width='188px'>";
		 content += "<img src='http://cskpp.hcm.vms.com.vn/GPS/images/upload/logo.png' width='188px' height='43px' /> ";
		 content += "</td>";
		 content += "<td width='100%' align='center' style='color:#FFFFFF'>";
		 content += "<b>CHƯƠNG TRÌNH BÁN HÀNG MOBIFONE TRUNG TÂM 2</b> ";
		 content += "</td>";
		 content += "</tr>";
		 content += "</table>";
		 content += "</td>";
		 content += "</tr>";
        
		 content += "<tr>";
		 content += "<td>";
		 content += "<p style='padding-top:20px;padding-lef:15px'>";
		 content += body;
		 content += "</p>";
		 content += "</td>";
		 content += "</tr>";
		 
		 content += "<tr>";
		 content += "<td>";
		 content += "<p style='padding-top:20px;padding-lef:15px'>";
		 content += " Trân trọng.<br/> Nhóm quản trị chương trình bán hàng trung tâm 2.";
		 content += "</p>";
		 content += "</td>";
		 content += "</tr>";
		 content += "</table>";
        
		 return content;
	 }
}
