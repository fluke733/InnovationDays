package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Counts number of sentences on a page 
 * 
 * 
 * @author Jeffrey Walker (jwalker1)
 */
public class SentenceCounter 
{

	public int countSentences(String s)
	{
		String regex = "[?|!|.]+[ ]";
		  Pattern p = Pattern.compile(regex);
		  int count = 0;
		  Matcher m = p.matcher(s);       
		  while (m.find()) {
		     count++;
		  }
		  if (count == 0){
		     return 1;
		  }
		  else {
		     return count + 1;
		  }
	}
}
