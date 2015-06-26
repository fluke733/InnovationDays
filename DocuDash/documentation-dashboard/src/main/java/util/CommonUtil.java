package util;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class CommonUtil 
{
	public String httpGetRequest(String url, String acceptValue)
	{
		String responseStr = "";
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", acceptValue);
			HttpResponse httpResponse = httpClient.execute(getRequest);
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) 
				{
					responseStr = EntityUtils.toString(entity);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public Document parseXml(String xml)
	{
		Document doc = null;
		try
		{
			if(!xml.trim().equals("") && !xml.equals(null) )
			{
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			    InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(xml));
			    doc = db.parse(is);
			    doc.getDocumentElement().normalize();
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		
		return doc;
	}
	
	public int getSubstringCount(String string, String search)
	{
		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1){

		       lastIndex = string.indexOf(search,lastIndex);

		       if( lastIndex != -1){
		             count ++;
		             lastIndex+=search.length();
		      }
		}
		return count;
	}
}
