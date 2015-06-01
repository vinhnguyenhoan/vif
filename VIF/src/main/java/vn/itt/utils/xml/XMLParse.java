package vn.itt.utils.xml;
import java.io.StringReader;
import java.io.StringWriter;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLParse {
	
	public static Res parse(String strXML ) {
		try {
			 
			StringReader reader = new StringReader(strXML);
			JAXBContext jaxbContext = JAXBContext.newInstance(Res.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Res res = (Res) jaxbUnmarshaller.unmarshal(reader);
			return res;
			
		  } catch (JAXBException e) {
			  e.printStackTrace();
			  return null;
		  }
	 }
	
	public static String create(Req req){
		
		  try {
	 
				 java.io.StringWriter sw = new StringWriter();
				 JAXBContext jaxbContext = JAXBContext.newInstance(Req.class);
				 Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
				 // output pretty printed
				 jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				 jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				 
				 jaxbMarshaller.marshal(req , sw);
				 
				 return sw.toString();
 	 
		 } catch (JAXBException e) {
		    	  e.printStackTrace();
		    	  return null;
		 }
	}
}
