package org.kansasmission.m;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import android.util.SparseArray;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Advanced XML Parser
 * Convert XML code to Listview
 * Last Updated 12.11.07
 * @version 1.3
 * @author Faney
 *
 */
public class AdvXMLParser {
	static final int TYPE_P = 0x00000001;
	static final int TYPE_S = 0x00000010;
	static final int TYPE_C = 0x00000100;
	static final int TYPE_F = 0x00001000;
	static final int TYPE_Q = 0x00010000;
	static final int TYPE_M = 0x00100000;
	static final int TYPE_0 = 0x01000000;
	
	private HashMap<String, String> keyMap = null;
	private HashMap<String, String> keyFormat = null;
	private SparseArray<String> codeMap = null;
	private String xmlRawData = null;
	private XmlPullParserFactory factory;
	private XmlPullParser parser;
	private int xmlLineCount = 0;
	private String msgFlag = "";
	/**
	 * Constructor
	 * 1. saves original URL
	 * 2. Initialize keyMapper and codeMapper
	 * 3. calculates Line size
	 * @param strXML XML Raw data
	 */
	public AdvXMLParser(String strXML){
		xmlRawData = new String(strXML);
		keyMap = new HashMap<String, String>();
		keyFormat = new HashMap<String, String>();
		codeMap = new SparseArray<String>();
		xmlLineCount = countSubstring(xmlRawData.toString(), "<");
	}
	/**
	 * Gets XML Line Size
	 * @return Number of XML Lines
	 * 
	 */
	public int getXmlSize(){
		return xmlLineCount;
	}
	/**
	 * Initialize XML Parser
	 * @throws XmlPullParserException
	 */
	public void init() throws XmlPullParserException{
		if(xmlRawData != null){
			factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			StringReader xmlBuffer = new StringReader(xmlRawData); 
			parser.setInput(xmlBuffer);
		}
	}
	/**
	 * Set Key Name with special flag
	 * @see TYPE_P : Primary Key 
	 * @see TYPE_S : Secondary Key
	 * @see TYPE_M : Message Text
	 * @see TYPE_C : Convert Status Code to Status Text
	 * @see TYPE_F : Convert SAP Number form to Integer String
	 * @see TYPE_Q : Convert SAP Number form to Integer String and stick after key
	 * @see TYPE_0 : Normal 
	 * 
	 * @author Faney
	 * @param keyName field name. almost DB field name
	 * @param keyValue saves field text.
	 * @param formFlag determine format
	 * @return void
	 * */	
	public void setKeyName(String keyName, String keyValue, int formFlag){
		keyMap.put(keyName, keyValue);
		if((formFlag ^ TYPE_C) == 0){
			keyFormat.put(keyName, "CODE");
		}
		if((formFlag ^ TYPE_F) == 0){
			keyFormat.put(keyName, "FLOAT");
		}
		if((formFlag ^ TYPE_Q) == 0){
			keyFormat.put(keyName, "QUANTITY");
		}
		if((formFlag ^ TYPE_P) == 0){
			keyFormat.put(keyName, "PRIMARY");
		}
		if((formFlag ^ TYPE_S) == 0){
			keyFormat.put(keyName, "SECONDRY");
		}
		if((formFlag ^ TYPE_M) == 0){
			keyFormat.put(keyName, "MESSAGE");
		}
	}
	/**
	 * Set Code Name with code number
	 * @param codeNum
	 * @param codeValue
	 */
	public void setCodeName(Integer codeNum, String codeValue){
		codeMap.put(codeNum, codeValue);
	}
	/**
	 * Converts XML data to String Array Combinations
	 * @param keyArray key Array
	 * @param valueArray value Array of key data
	 * @throws XmlPullParserException Problems with XML data
	 * @throws IOException Memory Problems
	 */
	public void setXMLtoTableList(String[] keyArray, String[] valueArray)
									throws XmlPullParserException, IOException{
		StringBuffer apndMsg = new StringBuffer();
		int eventType = parser.getEventType();
		int itemCount = 0;
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			case XmlPullParser.END_TAG:
				break;
			case XmlPullParser.START_TAG:
				msgFlag = parser.getName();
				if(keyFormat.get(msgFlag) != null){
					if(keyFormat.get(msgFlag).equals("PRIMARY")){
					}
					else if (keyFormat.get(msgFlag).equals("MESSAGE")){
						// TODO 차후 추가
					}
					else if (keyFormat.get(msgFlag).equals("SECONDRY")){
						// TODO 차후 추가
					}
				}
				else if(parser.isEmptyElementTag()){
					//TODO Executes if parser meets empty tag
				}
				break;
			case XmlPullParser.TEXT:
				if(msgFlag.length() == 0){
				}
				else if(keyFormat.get(msgFlag) != null){
					if(keyFormat.get(msgFlag).equals("MESSAGE")){
						if (parser.getText().equals("200")){
//							textShipData.setText("200 OK!!");
						}
						else{
							//Toast.makeText(ShipmentReader.this, parser.getText(), Toast.LENGTH_SHORT).show();
						}
					}
					else if(keyFormat.get(msgFlag).equals("CODE")){
						keyArray[itemCount] = getKeyName(msgFlag.toString());
						valueArray[itemCount] =  getCodeName(Integer.parseInt(parser.getText()));
						itemCount++;
					}
					else if(keyFormat.get(msgFlag).equals("FLOAT")){
						String formSAP = parser.getText();
						formSAP = removeChar(formSAP, ",");
						float floatRAW =  Float.parseFloat(formSAP);
						keyArray[itemCount] = getKeyName(msgFlag.toString());
						valueArray[itemCount] = Float.toString(floatRAW);
						itemCount++;
					}
					else if(keyFormat.get(msgFlag).equals("QUANTITY")){
						String formSAP = parser.getText();
						formSAP = removeChar(formSAP, ",");
						float floatRAW =  Float.parseFloat(formSAP);
						keyArray[itemCount] = getKeyName(msgFlag.toString());
						apndMsg.append(Float.toString(floatRAW));
					}
					else if(apndMsg.length() > 0){
						apndMsg.append(" ").append(parser.getText());
						valueArray[itemCount] = apndMsg.toString();
						apndMsg.setLength(0);
						itemCount++;
					}
				}
				else if(apndMsg.length() > 0){
					apndMsg.append(" ").append(parser.getText());
					valueArray[itemCount] = apndMsg.toString();
					apndMsg.setLength(0);
					itemCount++;
				}
				else{
					keyArray[itemCount] = getKeyName(msgFlag.toString());
					valueArray[itemCount] = parser.getText();
					itemCount++;
				}
				msgFlag = "";
			}
			eventType = parser.next();
		}
	}
	/**
	 * Get Shipment Status and Converts Description
	 * @see SAP http://help.sap.com
	 * @author Faney
	 * @param statusNum status code
	 * @return Shipment Status Description
	 * */
	@SuppressWarnings("unused")
	private static String getStatus(String statusNum){
		if(statusNum.equals("1"))
			return "Partially Scheduled\n";
			//rtnMsg = rtnMsg + "Status : Partially Scheduled\n";
		else if(statusNum.equals("2"))
			return "Scheduled\n";
			//rtnMsg = rtnMsg + "Status : Scheduled\n";
		else if(statusNum.equals("3"))
			return "Partially Loaded\n";
			//rtnMsg = rtnMsg + "Status : Partially Loaded\n";
		else if(statusNum.equals("4"))
			return "Loading Confirmed\n";
			//rtnMsg = rtnMsg + "Status : Loading Confirmed\n";
		else if(statusNum.equals("5"))
			return "Partially Deliveried\n";
			//rtnMsg = rtnMsg + "Status : Partially Deliveried\n";
		else if(statusNum.equals("6"))
			return "Delivery Confirmed\n";
			//rtnMsg = rtnMsg + "Status : Delivery Confirmed\n";
		else
			return "Exception Status\n";
	}
	/**
	 * Gets key value with key name
	 * @param keyName Table Key
	 * @return Data of key name
	 */
	private String getKeyName(String keyName){
		if(keyMap.get(keyName) != null){
			return keyMap.get(keyName);
		}
		else{
			return keyName;
		}
	}
	/**
	 * Gets code value with key code
	 * @param codeNum Code Number
	 * @return Text of Code
	 */
	private String getCodeName(Integer codeNum){
		if(codeMap.get(codeNum) != null){
			return codeMap.get(codeNum);
		}
		else{
			return "Invalid Code";
		}
	}
	/**
	 * Removes substring from the source string
	 * @author Faney
	 * @param 
	 * @return Removed String
	 * */
	private static String removeChar(String str, String ch){
		StringBuffer buff = new StringBuffer();
		StringTokenizer token = new StringTokenizer(str, ch);
		while(token.hasMoreTokens()){
			buff.append(token.nextToken());
		}
		return buff.toString();
	}
	/**
	 * Counts the number of prefix in the source String
	 * @param src Source String
	 * @param sub Prefix
	 * @return number of prefix in the source String
	 */
	public static int countSubstring(String src, String sub) {
		Pattern p = Pattern.compile(sub);
		Matcher m = p.matcher(src);
		int count = 0;
		for( int i = 0; m.find(i); i = m.end()){
			count++;
		}
		return count;
	}
	/**
	 * Trims space, null, or unnecessary characters in the starts of XML data 
	 * @param str XML Raw Data
	 * @return The Pure XML Data without space, null, or unnecessary characters
	 */
	private static String trimXMLStart(String str){
		if(str !=null){
			while(!str.startsWith("<")){
				str = str.substring(1);
			}
			return str;
		}
	return null;
	}
}
