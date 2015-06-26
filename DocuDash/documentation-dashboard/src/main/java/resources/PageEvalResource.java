package resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.CommonUtil;
import util.JazzySpellChecker;
import util.LinkChecker;
import util.SentenceCounter;
import util.SyllableCounter;
import util.WordCounter;
import dto.Page;
import dto.PageBody;
import dto.PageEval;

/**
 * Document Dashboard Web Service. 
 * 
 * 
 * @author Jeffrey Walker (jwalker1)
 */
@Path("/services")
public class PageEvalResource 
{
	//TODO: Code currently works only with this uri, thus will return only services that are playground-enabled. 
	//Need to make it work for all services
	private String portalServicesURL = "https://devinternal.intuit.com/apip/as/v1.0/core/applications?serviceEnabled=true&offset=0&limit=20&status=1&lifeCycles=1&privateEnabled=0";
	
	private String mindtouchURL = "http://authoring.intuit.com/@api/deki/pages";
	private String serviceName;
	private PageEval pageEval;
	private int numOfMisSpellings;
	private int numOfInvalidLinks;
	private int numOfSyllables;
	private int numOfWords;
	private int numOfSentences;
	private ArrayList<Page> listOfPages;
	private ArrayList<PageEval> listOfPageEvaluations;
	private int numOfCodeSamples; 
	private String lastModified;
	private CommonUtil commonUtil;
	private JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
	private LinkChecker linkChecker = new LinkChecker();
	
	/**
	 * Example calls to the web service:
	 * 
	 * http://localhost:8080/DocumentDashboard/services/evaluate/all
	 * http://localhost:8080/DocumentDashboard/services/evaluate/zjz7
	 */
	
	
	public static void main(String [] args)
	{
		PageEvalResource pageEvalMain = new PageEvalResource();
		pageEvalMain.evaluate("mer");
		//pageEvalMain.evaluate();
	}
	
	@GET
	@Path("/evaluate/{shortName}")
	@Produces("application/json")
	public ArrayList<PageEval> evaluate(@PathParam("shortName") String shortName) 
	{
		commonUtil = new CommonUtil();
		
		//Step 1: Call the Portal API to get the short names for all of the services
		JSONArray servicesJsonArray = getServices(shortName);//TODO: remove this call
		
		//Step 2: For each of the short names, contact Mindtouch to get further details
		processMindtouchPageDetails(servicesJsonArray, shortName);	
		
		return listOfPageEvaluations;
	}
	
	
	@GET
	@Path("/evaluate/all")
	@Produces("application/json")
	public ArrayList<PageEval> evaluate() 
	{
		commonUtil = new CommonUtil();
		
		//Step 1: Call the Portal API to get the short names for all of the services
		JSONArray servicesJsonArray = getServices(null);
		
		//Step 2: For each of the short names, contact Mindtouch to get further details
		processMindtouchPageDetails(servicesJsonArray, null);	
		
		return listOfPageEvaluations;
	}
	
	
	private JSONArray getServices(String shortName)
	{
		JSONArray jsonArray = null;
		
		try
		{
			String responseStr = commonUtil.httpGetRequest(portalServicesURL, "application/json");
			JSONParser parser = new JSONParser();
			jsonArray = (JSONArray) parser.parse(responseStr);
			return jsonArray;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return jsonArray;
	}
	
	
	private void processMindtouchPageDetails(JSONArray servicesJsonArray, String shortName)
	{
		
		try
		{
			listOfPageEvaluations = new ArrayList<PageEval>();
			int numOfIterations = servicesJsonArray.size();
			
			
			if(shortName!=null)
			{
				numOfIterations = 1;
			}	
			
				
			for(int i=0; i<numOfIterations; i++)
			{
				System.out.println("--------------------------------------------"); 
				listOfPages = new ArrayList<Page>();
				pageEval = new PageEval();
				JSONObject jsonObj = (JSONObject) servicesJsonArray.get(i);
				if(shortName==null)
				{
					shortName = jsonObj.get("shortName").toString();
					serviceName = jsonObj.get("name").toString();
					System.out.println("Service Name: " + serviceName);
				}
				
				
				pageEval.setServiceName(serviceName);
				pageEval.setShortName(shortName);
				pageEval.setNumOfInvalidLinks(linkChecker.countInvalidLink(shortName));
					
					
				
				System.out.println("Short Name: " + shortName);
				
				process(shortName, true);//First call is calling the top level overview page
				//process("ius", true);
				
				//consolidate
				for ( Page page:listOfPages){
					pageEval.setNumOfMisSpellings(pageEval.getNumOfMisSpellings()+page.getBody().getNumOfMisSpellings());
					pageEval.setNumOfSentences(pageEval.getNumOfSentences()+page.getBody().getNumOfSentences());
					pageEval.setNumOfSyllables(pageEval.getNumOfSyllables()+page.getBody().getNumOfSyllables());
					pageEval.setNumOfWords(pageEval.getNumOfWords()+page.getBody().getNumOfWords());
					pageEval.setNumOfCodeSamples(pageEval.getNumOfCodeSamples()+page.getNumOfCodeSamples());
					
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					Date pageDate = sdf.parse(page.getDateModified());
					int pageScore = getScore(pageDate) * 1/listOfPages.size();
					pageEval.setFreshScore(pageEval.getFreshScore()+pageScore);	
				}
				
				listOfPageEvaluations.add(pageEval);
				shortName=null;
				System.out.println("--------------------------------------------"); 
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	private int getScore(Date pageDate) {
		// TODO Auto-generated method stub
		Date now = new Date();
		
		int m1 = pageDate.getYear() * 12 + pageDate.getMonth();
	    int m2 = now.getYear() * 12 + now.getMonth();
	    int diff = m2 - m1 + 1;
	    
	    int score = 0;
	    
	    if (diff<4){
	    	score =100;
	    }else if (diff<7){
	    	score = 85;
	    }else if (diff <10){
	    	score = 50;
	    }else if (diff <12){
	    	score =25;
	    }
	    
	    return score;
	}

	public void process(String shortnameOrPageId, boolean isFirstIteration) throws ParserConfigurationException, SAXException, IOException
	{
		String responseStr = "";
		Document doc = null;
		//http://authoring.intuit.com/@api/deki/pages/=ius
		//- get the page id for the top level page [this is not an actual page -- this is just used to get the subpages]
		//- one time activity per service
		if(isFirstIteration)
		{
			//Get the pageId for the top level MT page [this is not an actual page -- this is just used to get the subpages]
			//This is a one time activity per service
			responseStr = commonUtil.httpGetRequest(mindtouchURL + "/="+ shortnameOrPageId, "application/xml");
			//System.out.println(responseStr);
		    doc = commonUtil.parseXml(responseStr);
		    if(!responseStr.equals(""))//Means that they have mindtouch documentation
		    {
		    	String pageId = doc.getDocumentElement().getAttribute("id");
			    
				
			    //Get number of top level subpages
				responseStr = commonUtil.httpGetRequest(mindtouchURL + "/"+ pageId + "/subpages", "application/xml");   
				doc = commonUtil.parseXml(responseStr);
			    NodeList nList = doc.getElementsByTagName("page.subpage");
			    int numOfTopLevelSubpages = nList.getLength();
			    System.out.println("nTopLevelSubPages: " + numOfTopLevelSubpages);   
			    pageEval.setNumOfTopLevelSubpages(numOfTopLevelSubpages);
					    
			    //Begin the recursive process (Note: even if there's one page on the site, it will be the only subpage)
			    process(pageId, false);
		    }
		    
		}
		
		//http://authoring.intuit.com/@api/deki/pages/1255/subpages
		//- get all the path nodes [this is the path to the actual html for each of the subpages]
		//- process all the subpages to get all the data for each page
		responseStr = commonUtil.httpGetRequest(mindtouchURL + "/"+ shortnameOrPageId + "/subpages", "application/xml");   
		
		doc = commonUtil.parseXml(responseStr);
		
		int numOfSubPages = 0;
		if(doc!=null)
		{
			numOfSubPages = Integer.parseInt(doc.getDocumentElement().getAttribute("totalcount"));
		}
		
		
		if(numOfSubPages!=0)
		{
			NodeList nList = null;
			
	    	nList = doc.getElementsByTagName("page.subpage");
	    
			
		    for (int i = 0; i < numOfSubPages; i++) 
		    {	 
		    	Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element eElement = (Element) nNode;
			 
					//Get uri.ui and process html and fill the page object
					String pageId = eElement.getAttribute("id");
					String htmlContentUrl = eElement.getElementsByTagName("uri.ui").item(0).getTextContent();
					URL htmlContents = new URL(htmlContentUrl);
			        BufferedReader in = new BufferedReader(new InputStreamReader(htmlContents.openStream()));
			        String htmlBody = "", inputLine = "";
			        while ((inputLine = in.readLine()) != null)
			        {
			        	htmlBody = htmlBody + inputLine;
			        }
			        in.close();
			        processHtmlBody(htmlBody);
			        
			        
			        //Get subpages for the current page
			        responseStr = commonUtil.httpGetRequest(mindtouchURL + "/"+ pageId + "/subpages", "application/xml");  
					doc = commonUtil.parseXml(responseStr);
					int nSubpages = Integer.parseInt(doc.getDocumentElement().getAttribute("totalcount"));
			        
			        
			        System.out.println("pageId: " + pageId + ", dateLastModified: " + lastModified + ", numOfInvalidLinks: "+numOfInvalidLinks +", numOfMisSpellings: " + numOfMisSpellings + ", numOfSyllables: " + numOfSyllables + ", numOfWords: " + numOfWords + ", numOfSentences: " +  numOfSentences + 
			        				   ", numOfCodeSamples: " +   numOfCodeSamples + ", nSubpages: " +nSubpages + ", htmlContentUrl: " +  htmlContentUrl);
			        
			        Page page = new Page();
					page.setPageId(pageId);
					page.setHref(htmlContentUrl);
					page.setNumOfCodeSamples(numOfCodeSamples);
					page.setNumOfSubPages(nSubpages);
					page.setDateModified(lastModified);
					PageBody pageBody = new PageBody();
					pageBody.setNumOfSentences(numOfSentences);
					pageBody.setNumOfSyllables(numOfSyllables);
					pageBody.setNumOfMisSpellings(numOfMisSpellings);
					pageBody.setNumOfWords(numOfWords);
					page.setBody(pageBody);
					listOfPages.add(page);
			        
			        
			        
					//Get Page id and call the recursive method with false for all subpages
					process(pageId, false);
				}
	    	}
		}
	}
	
	
	
	private void processHtmlBody(String html)
	{
		try
		{
			//Strip html tags
			String plainText = new HtmlToPlainText().getPlainText(Jsoup.parse(html));
			plainText = plainText.substring(plainText.indexOf("Table of Contents"));
			
			//Count number of syllables
			SyllableCounter syllableCounter = new SyllableCounter();
			numOfSyllables = syllableCounter.countSyllables(plainText);
			numOfMisSpellings = jazzySpellChecker.getMisspelledWordsCount(plainText);
			
			//Count number of words
			WordCounter wordCounter = new WordCounter();
			numOfWords = wordCounter.countWords(plainText);
			
			//Count number of sentences
			SentenceCounter sentenceCounter = new SentenceCounter();
			numOfSentences = sentenceCounter.countSentences(plainText);
			
			//Count number of code samples
			numOfCodeSamples = commonUtil.getSubstringCount(html, "<code>") + commonUtil.getSubstringCount(html, "class=\"code\"");
			
			//Date Modified
			lastModified = html.substring(html.indexOf("page-version-tags"));
			lastModified = lastModified.substring(lastModified.indexOf("modified\">") + "modified\">".length());
			lastModified = lastModified.substring(lastModified.indexOf(",") + 1, lastModified.indexOf("</div>"));
			lastModified = lastModified.trim();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
