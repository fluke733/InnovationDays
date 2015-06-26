package dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Page - Data Transfer Object 
 * 
 * 
 * @author Jeffrey Walker (jwalker1)
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "page")
public class Page implements Serializable
{
	private static final long serialVersionUID = -5867436747690170868L;
	
	@XmlAttribute(name = "pageId")
	private String pageId;
	@XmlAttribute(name = "href")
	private String href;
	@XmlAttribute(name = "body")
	private PageBody body;
	@XmlAttribute(name = "numOfCodeSamples")
	private int numOfCodeSamples;
	@XmlAttribute(name = "numOfSubPages")
	private int numOfSubPages;
	@XmlAttribute(name = "dateModified")
	private String dateModified;
	
	public String getPageId() 
	{
		return pageId;
	}
	public String getHref() 
	{
		return href;
	}
	public PageBody getBody() 
	{
		return body;
	}
	public int getNumOfCodeSamples() 
	{
		return numOfCodeSamples;
	}
	public int getNumOfSubPages() 
	{
		return numOfSubPages;
	}
	public String getDateModified() 
	{
		return dateModified;
	}
	public void setPageId(String pageId) 
	{
		this.pageId = pageId;
	}
	public void setHref(String href) 
	{
		this.href = href;
	}
	public void setBody(PageBody body) 
	{
		this.body = body;
	}
	public void setNumOfCodeSamples(int numOfCodeSamples) 
	{
		this.numOfCodeSamples = numOfCodeSamples;
	}
	public void setNumOfSubPages(int numOfSubPages) 
	{
		this.numOfSubPages = numOfSubPages;
	}
	public void setDateModified(String dateModified) 
	{
		this.dateModified = dateModified;
	}
}
