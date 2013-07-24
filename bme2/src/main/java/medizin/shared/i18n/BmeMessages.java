package medizin.shared.i18n;

import com.google.gwt.i18n.client.Messages;

public interface BmeMessages extends Messages {
	
	public String mediaErrorMsg(int size);
	
	public String imageSizeError(int width, int height);
	
	public String answerTextMinMax(long min, long max);
	
	public String blockingWarning(int numQuestions);
	
	public String sumOfAnswer(int sum);
	
	public String sumOfTrueAnswer(int sum);
	
	public String sumOfFalseAnswer(int sum);
	
}