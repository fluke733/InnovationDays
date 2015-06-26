package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LinkChecker {
	
	private Map<String, Integer> brokenLinkCountMap; 
	private static String mtHostToken = "authoring.intuit.com/";
	
	/**
	 * 
	 */
	public LinkChecker() {
		super();
		// TODO Auto-generated constructor stub
		
		brokenLinkCountMap = new HashMap<String, Integer>();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("/Users/xzhang1/Desktop/InnovationDays/invalid_link.csv"));
			String line;
	        while((line = br.readLine())!=null){
	        	
	           int i = line.indexOf(mtHostToken);
	           if (i>0){
	        	   int j = line.indexOf("/", i+mtHostToken.length());
	        	   if (j>i){
	        		   String shortName = line.substring(i+mtHostToken.length(),j);
	        		   if (shortName.indexOf(",")>0){
	        			   shortName= shortName.substring(0, shortName.indexOf(","));
	        		   }
	        		   Integer count = brokenLinkCountMap.get(shortName);
	        		   if (null==count){
	        			   brokenLinkCountMap.put(shortName, 1);
	        		   }else{
	        			   brokenLinkCountMap.put(shortName, ++count);
	        		   }
	        	   }
	           }
	        }
	        br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 
	 * @param shortName
	 * @return
	 */
	public int countInvalidLink(String shortName){
		Integer count =  brokenLinkCountMap.get(shortName);
		if (null==count){
			count = 0;
		}
		
		return count;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String [] args)
	{
		LinkChecker linkChecer = new LinkChecker();
		System.out.println(linkChecer.countInvalidLink("cookbook"));
	}
	
}
