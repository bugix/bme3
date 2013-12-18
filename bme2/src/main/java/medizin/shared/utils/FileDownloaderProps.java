package medizin.shared.utils;

public interface FileDownloaderProps {

	String METHOD_KEY = "1";
	String ASSIGNMENT = "2";
	String VERSION = "3";
	String A_VERSION = "4";
	String B_VERSION = "5";
	String DISALLOW_SORTING = "6";
	
	public enum Method {
		DOCX_PAPER, XML_PAPER, DOCX_PAPER_ALL, SOLUTION_KEY
	}
}
