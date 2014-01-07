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

	public String acceptQuestionAndAnswer(long acceptQuestionCount, long acceptAnswerCount);
	
	public String acceptQuestionAndAnswerExaminer(long acceptQuestionCount, long acceptAnswerCount);
	
	public String questionDigitCount(String actual,String total);

	public String imageUploadSize(int width, int height);

	public String answerTextMinMaxContinueAnyWay(long min, long max);
	
	public String questionAllowed(int msg);
	
	public String imgKeyError(String answerType, int noOfAns);
}
