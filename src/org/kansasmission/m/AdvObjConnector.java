package org.kansasmission.m;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
/**
 * Advanced Object Connector
 * Connects HTTP and get data easily
 * What's new : Corrects the first character "<" missing / Aug. 8. 2013.
 * @version 1.6
 * @author Faney
 *
 */
public class AdvObjConnector {
	String reqURL;
	StringBuffer strURLbuffer;
	StringBuffer parmToken;
	String authID;
	String authPW;
	int timeOut;
	/** 
	 * Set URL and Timeout seconds. after that you can set parameters
	 * @param reqURL
	 * @param timeOut
	 */
	public AdvObjConnector(String reqURL, int timeOut){
		strURLbuffer = new StringBuffer(reqURL);
		this.timeOut = timeOut;  
	}
	/**
	 * set HTTP Authentication with HTTP ID and Password
	 * @param userID
	 * @param userPW
	 */
	protected void setHttpAuth(String userID, String userPW){
		authID = userID;
		authPW = userPW;
	}
	/**
	 * set parameters by HTTP get method. you can use multiple parameters
	 * @param pName
	 * @param pValue
	 */
	protected void appendParm(String pName, String pValue){
		strURLbuffer.append(pName);
		strURLbuffer.append(pValue);
	}
    /**
     * Send HTTP URL using Get Parameter and Receive String Data
     * @author Faney
     * @param
     * @return Received String
     * */
	protected String retrievePage() {
		reqURL = strURLbuffer.toString();
		String respData = null;
		HttpURLConnection conn = null;
	    try {
	        URL url = new URL(String.format(reqURL));
	        conn = (HttpURLConnection) url.openConnection();
	
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-length", "0");
	        conn.setUseCaches(false);
	        conn.setAllowUserInteraction(false);
	        conn.setConnectTimeout(timeOut);
	        conn.setReadTimeout(timeOut);
	
	        // NOTE: Below is used to perform the http authentication. It is working in JAVA but not
	        // always working in Android platform.
	
	        // Set to use the default HTTP authentication. Working in JAVA, but not working in
	        // Android.
	        Authenticator.setDefault(new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(authID, authPW.toCharArray());
	            }
	        });
	        conn.connect();
	
	        System.out.println(conn.getResponseCode()); // <-- The request will stop here if running
	                                                    // in Android.
	        System.out.println(conn.getResponseMessage());
	
	        respData = printOutput(conn);
	
	    } catch (MalformedURLException e) {
	        // TODO: Exceptions with Illegal Server Address.
	        respData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<MESSAGE>Illegal Server Address.</MESSAGE>";
	        e.printStackTrace();
	    } catch (IOException e) {
	        // Operation timed out.
	        System.out.println(e.getStackTrace());
	        e.printStackTrace();
	        respData="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<MESSAGE>Server Time Out.</MESSAGE>";
	    } catch(Exception e){
	    	respData="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<MESSAGE>" + e.getStackTrace() + "</MESSAGE>";
	    }
		finally {
	        if (conn != null) {
	            conn.disconnect();
	            conn = null;
	        }
	    }
		return respData;
    }
	/**
	 * @brief print the output of the HTTP connection.
	 * @param conn [in] Http connection.
	 */
	private String printOutput(HttpURLConnection conn) throws IOException {
	    InputStream is = conn.getInputStream();
	    BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    StringBuffer buffer = new StringBuffer();
	    int rcvChar = 0;
	    boolean startApp = false;

	    while ((rcvChar = in.read()) != -1) {
//	    	char rcvChar =(char)rcvRtn ;
	    	if(startApp){
		    	buffer.append((char)rcvChar);
	    	}else{
	    		if((char)rcvChar == '<'){
			    	buffer.append((char)rcvChar);
	    			startApp = true;
	    		}
	    	}
        }
	    return buffer.toString();
	}

}
