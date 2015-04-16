package org.kansasmission.m;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Advanced XML DOM Parser
 * Convert XML code to String Array
 * Last Updated 13.08.09
 * @version 1.2	Adding Time Conversion Function
 * @author Faney
 *
 */
public class AdvDomParser {
    Document doc = null;
    InputSource is = null;
    NodeList nodes = null;
    Element element = null;
    
    public AdvDomParser(String xmlRecords) throws ParserConfigurationException, SAXException, IOException{
    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        doc = db.parse(is);    	
    }

    public Document getDocData(){
    	return doc;
    }
    
    public String[] getStringByTagName(String parents, String tagName){
        nodes = doc.getElementsByTagName(parents);
        String[] data = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {
          Element element = (Element) nodes.item(i);

          NodeList name = element.getElementsByTagName(tagName);
          Element line = (Element) name.item(0);
          data[i] = getCharacterDataFromElement(line);
        }
		return data;
    }
    public String[] getStringByTagName(String parents, String tagName, boolean hasTag) throws TransformerException{
        nodes = doc.getElementsByTagName(parents);
        String[] data = new String[nodes.getLength()];
        if(hasTag){
            for (int i = 0; i < nodes.getLength(); i++) {
//                Element element = (Element) nodes.item(i);
//                NodeList name = element.getElementsByTagName(tagName);
//                CharacterData cd = (CharacterData) name.item(0).getFirstChild();
//                data[i] = cd.getData();
                //Element line = (Element) name.item(0).toString();
                //data[i] = line.getTextContent();
                Element element = (Element) nodes.item(i);
                NodeList name = element.getElementsByTagName(tagName);
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				StringWriter buffer = new StringWriter();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				    transformer.transform(new DOMSource(name.item(0).getFirstChild()),
				          new StreamResult(buffer));
			    data[i] = buffer.toString();
				}
    		return data;
        }
        else{
        	return getStringByTagName(parents, tagName);
        }
    }
    public String[] getAttributesByTagName(String parents, String tagName, int index, String attrName){
        nodes = doc.getElementsByTagName(parents);
        String[] data = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {
          Element element = (Element) nodes.item(i);

          NodeList name = element.getElementsByTagName(tagName);
          Element line = (Element) name.item(index);
          data[i] = line.getAttribute(attrName);
        }
		return data;	
    }

  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData();
    }
    return "";
  }
  public static String convertTimeFormat(String pFormat, String pSource){
	  String converted = null;
	  SimpleDateFormat sdf = new SimpleDateFormat(pFormat, new Locale("US"));
	  try 
	  {
	      Date date = (Date)sdf.parse(pSource);
	      System.out.println(date);
	      SimpleDateFormat day = new SimpleDateFormat("yyyy-MMM-dd", new Locale("US"));
	      SimpleDateFormat time = new SimpleDateFormat("HH:mm", new Locale("US"));
	      converted = day.format(date) + " " + time.format(date);
	  } 
	  catch (ParseException e) 
	  {
	      e.printStackTrace();
	  }
	return converted;
  }
  public static void convertTimeFormats(String pFormat, String[] pSource, String timeZone){
	  if (pSource != null){
		  SimpleDateFormat sdf = new SimpleDateFormat(pFormat, new Locale("US"));
		  sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		  for (int i = 0 ; i < pSource.length ; i++){
			  try 
			  {
			      Date date = (Date)sdf.parse(pSource[i]);
			      System.out.println(date);
			      SimpleDateFormat day = new SimpleDateFormat("yyyy-MMM-dd EEE", new Locale("US"));
			      SimpleDateFormat time = new SimpleDateFormat("HH:mm", new Locale("US"));
			      day.setTimeZone(TimeZone.getTimeZone(timeZone));
			      time.setTimeZone(TimeZone.getTimeZone(timeZone));
			      pSource[i] = day.format(date) + " " + time.format(date);
			  }
			  catch (ParseException e) 
			  {
			      e.printStackTrace();
			  }
		  }
	  }
  }
  public static void shiftTimes(String[] pSource, Date pTime){
	  if (pSource != null){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale("US"));
		  for (int i = 0 ; i < pSource.length ; i++){
			  try 
			  {
			      Date date = (Date)sdf.parse(pSource[i]);
			      System.out.println(date);
			      SimpleDateFormat day = new SimpleDateFormat("yyyy-MMM-dd EEE", new Locale("US"));
			      SimpleDateFormat time = new SimpleDateFormat("HH:mm", new Locale("US"));
			      pSource[i] = day.format(date) + " " + time.format(date);
			  } 
			  catch (ParseException e) 
			  {
			      e.printStackTrace();
			  }
		  }
	  }
  }
}
