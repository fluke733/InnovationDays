package dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PageEval - Data Transfer Object 
 * 
 * 
 * @author Jeffrey Walker (jwalker1)
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "page")
public class PageEval implements Serializable
{
	private static final long serialVersionUID = 6563755305326641319L;
	
	@XmlAttribute(name = "shortName")
	private String shortName;
	@XmlAttribute(name = "serviceName")
	private String serviceName;
	@XmlAttribute(name = "numOfTopLevelSubpages")
	private int numOfTopLevelSubpages;
	@XmlAttribute(name = "listOfPages")
	private ArrayList<Page> listOfPages;
	@XmlAttribute(name = "numOfInvalidLinks")
	int numOfInvalidLinks=0;
	@XmlAttribute(name = "numOfCodeSamples")
	private int numOfCodeSamples=0;
	@XmlAttribute(name = "freshScore")
	private int freshScore =0;
	@XmlAttribute(name = "numOfSyllables")
	int numOfSyllables=0;
	@XmlAttribute(name = "numOfWords")
	int numOfWords=0;
	@XmlAttribute(name = "numOfSentences")
	int numOfSentences=0;
	@XmlAttribute(name = "numOfMisSpellings")
	int numOfMisSpellings=0;
	
	
	
	public int getNumOfCodeSamples() {
		return numOfCodeSamples;
	}
	public void setNumOfCodeSamples(int numOfCodeSamples) {
		this.numOfCodeSamples = numOfCodeSamples;
	}
	
	public int getFreshScore() {
		return freshScore;
	}
	public void setFreshScore(int freshScore) {
		this.freshScore = freshScore;
	}
	public int getNumOfSyllables() {
		return numOfSyllables;
	}
	public void setNumOfSyllables(int numOfSyllables) {
		this.numOfSyllables = numOfSyllables;
	}
	public int getNumOfWords() {
		return numOfWords;
	}
	public void setNumOfWords(int numOfWords) {
		this.numOfWords = numOfWords;
	}
	public int getNumOfSentences() {
		return numOfSentences;
	}
	public void setNumOfSentences(int numOfSentences) {
		this.numOfSentences = numOfSentences;
	}
	public int getNumOfMisSpellings() {
		return numOfMisSpellings;
	}
	public void setNumOfMisSpellings(int numOfMisSpellings) {
		this.numOfMisSpellings = numOfMisSpellings;
	}
	public int getNumOfInvalidLinks() {
		return numOfInvalidLinks;
	}
	public void setNumOfInvalidLinks(int numOfInvalidLinks) {
		this.numOfInvalidLinks = numOfInvalidLinks;
	}
	
	public String getShortName() 
	{
		return shortName;
	}
	public String getServiceName() 
	{
		return serviceName;
	}
	public int getNumOfTopLevelSubpages() 
	{
		return numOfTopLevelSubpages;
	}
	public ArrayList<Page> getListOfPages() 
	{
		return listOfPages;
	}
	public void setShortName(String shortName) 
	{
		this.shortName = shortName;
	}
	public void setServiceName(String serviceName) 
	{
		this.serviceName = serviceName;
	}
	public void setNumOfTopLevelSubpages(int numOfTopLevelSubpages) 
	{
		this.numOfTopLevelSubpages = numOfTopLevelSubpages;
	}
	public void setListOfPages(ArrayList<Page> listOfPages) 
	{
		this.listOfPages = listOfPages;
	} 
}
