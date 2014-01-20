package medizin.shared.utils;

public interface FileDownloaderProps {

	String METHOD_KEY = "1";
	String ASSIGNMENT = "2";
	String VERSION = "3";
	String A_VERSION = "4";
	String B_VERSION = "5";
	String DISALLOW_SORTING = "6";
	String ID = "7";
	String QUESTION_DETAIL = "8";
	String KEYWORD = "9";
	String LEARNING_OBJECTIVE = "10";
	String USED_IN_MC = "11";
	String ANSWER = "12";
	String LOCALE = "13";
	
	public enum Method {
		DOCX_PAPER, XML_PAPER, DOCX_PAPER_ALL, SOLUTION_KEY, PRINT_QUESTION_PDF
	}
}
