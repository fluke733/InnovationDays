package dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PageBody - Data Transfer Object 
 * 
 * 
 * @author Jeffrey Walker (jwalker1)
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "pagebody")
public class PageBody implements Serializable
{
	private static final long serialVersionUID = -6398625974392332004L;
	
	@XmlAttribute(name = "numOfSyllables")
	int numOfSyllables;
	@XmlAttribute(name = "numOfWords")
	int numOfWords;
	@XmlAttribute(name = "numOfSentences")
	int numOfSentences;
	@XmlAttribute(name = "numOfMisSpellings")
	int numOfMisSpellings;
	
	public int getNumOfMisSpellings() {
		return numOfMisSpellings;
	}
	public void setNumOfMisSpellings(int numOfMisSpellings) {
		this.numOfMisSpellings = numOfMisSpellings;
	}
	public int getNumOfSyllables() 
	{
		return numOfSyllables;
	}
	public int getNumOfWords() 
	{
		return numOfWords;
	}
	public int getNumOfSentences() 
	{
		return numOfSentences;
	}
	public void setNumOfSyllables(int numOfSyllables) 
	{
		this.numOfSyllables = numOfSyllables;
	}
	public void setNumOfWords(int numOfWords) 
	{
		this.numOfWords = numOfWords;
	}
	public void setNumOfSentences(int numOfSentences) 
	{
		this.numOfSentences = numOfSentences;
	}
}
