package br.com.pedidovenda.recursos;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

public class JsonXmlConverter {

 public static final String base = "/home/cubicrace/workspace";
 
 static String srcJsonFile = base + "/json2xml/samples/dbInventory.json";
 static String srcXmlFile = base +  "/json2xml/samples/dbInventory.xml";    
 static String convertedJsonFile = base + "/json2xml/samples/convertedFile.json";
 static String convertedXmlFile = base + "/json2xml/samples/convertedFile.xml";
 
 public boolean createFile(String filePath) {
	  File file = new File(filePath);
	  file.delete();
	  try {
	   return file.createNewFile();
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   return false;
	  }
 }
 
 private void jsonToXml(String jsonFile) throws Exception{
	 
		  File jsonObj = new File(jsonFile);
		  JAXBContext jc = JAXBContext.newInstance(Frase.class);
		  Unmarshaller unmarshaller = jc.createUnmarshaller();
		  unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
		  Frase activity = (Frase) unmarshaller.unmarshal(jsonObj);
		  
		  Marshaller xmlM = jc.createMarshaller();
		  xmlM.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		  FileOutputStream fos = new FileOutputStream(new File(convertedXmlFile));
		  xmlM.marshal(activity, fos);
		  fos.close();
		  
 }
 
 private void xmlToJson(String xmlFile) throws Exception{
	 
	  File xml = new File(xmlFile);
	  JAXBContext jc = JAXBContext.newInstance(Frase.class);
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Frase activity = (Frase) unmarshaller.unmarshal(xml);
	
	  Marshaller marshaller = jc.createMarshaller();
	  marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true);
	  marshaller.setProperty( MarshallerProperties.MEDIA_TYPE, "application/json");
	  marshaller.setProperty( MarshallerProperties.JSON_INCLUDE_ROOT, true);
	  FileOutputStream fos = new FileOutputStream(new File(convertedJsonFile));
	  marshaller.marshal(activity, fos);
	  fos.close();
	  
 }
 
 	public static void main(String[] args) {

 	JsonXmlConverter jxc = new JsonXmlConverter();
    
	  try {
	   jxc.createFile(convertedJsonFile);
	   System.out.println("Source XML: " +  srcXmlFile);
	   jxc.xmlToJson(srcXmlFile);
	   System.out.println("Converted Output JSON: " + convertedJsonFile);
	   
	   jxc.createFile(convertedXmlFile);
	   System.out.println("Source JSON: " + srcJsonFile);
	   jxc.jsonToXml(srcJsonFile);
	   System.out.println("Converted Output XML: " + convertedXmlFile);
	  } catch (Exception e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	 }

}