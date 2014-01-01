package medizin.shared.i18n;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface BmeEnumConstants extends ConstantsWithLookup {
	//BlockingTypes
	public String NON_BLOCLING();
	
	public String PERSONAL_BLOCKING();
	
	public String GLOBAL_BLOCKING();

	//Gender
	public String MALE();
	
	public String FEMALE();

	//MultimediaType
	public String Video();
	
	public String Image();
	
	public String Sound();

	// QuestionTypes
	public String Textual();
	
	public String Imgkey();
	
	public String Matrix();
	
	public String ShowInImage();
	
	public String MCQ();
	
	public String LongText();
	
	public String Sort();
	
	public String Drawing();

	//SelectionType
	public String SEL_PICK();
	
	public String SEL_CHOOSE();
	
	public String SEL_SORT();

	//Status
	public String NEW();
	
	public String CORRECTION_FROM_ADMIN();
	
	public String CORRECTION_FROM_REVIEWER();
	
	public String CORRECTED();
	
	public String ACCEPTED_ADMIN();
	
	public String ACCEPTED_REVIEWER();
	
	public String ACTIVE();
	
	public String DEACTIVATED();
	
	public String EDITED_BY_ADMIN();
	
	public String EDITED_BY_REVIEWER();
	
	public String CREATIVE_WORK();

	//UserType
	public String ADMIN();
	public String USER();
	
	//Keyword Advance Search
	public String KEYWORD_EQUALS();
	
	public String KEYWORD_NOT_EQUALS();
	
	//bind type
	public String AND();
	
	public String OR();
	
	//Question Event Advanced Search
	public String QUESTION_EVENT_EQUALS();
	
	public String QUESTION_EVENT_NOT_EQUALS();
	
	//Text search advanced search
	public String TEXT_SEARCH_EQUALS();
	
	public String TEXT_SEARCH_NOT_EQUALS();
	
	public String COMMENT();
			
	public String ANSWER_TEXT();
			
	public String QUESTION_TEXT();
			
	public String QUESTION_SHORT_NAME();
	
	//date advanced search
	public String EQUALS();
	
	public String NOT_EQUALS(); 
	
	public String LESS(); 
	
	public String MORE();
	
	public String CREATED_QUESTION_DATE();
			
	public String CHANGED_QUESTION_DATE();
			
	public String USED_IN_MC_DATE();
	
	//MC advancedSearch
	public String MC_EQUALS();
	
	public String MC_NOT_EQUALS();
	
	//Use type advanced Search
	public String AUTHOR();
			
	public String REVIEWER();
	
	//Media Availability advanced search
	public String MEDIA_AVAILABILITY_EQUALS();
	
	public String MEDIA_AVAILABILITY_NOT_EQUALS();
	
	//Question type advanced Search
	public String QUESTION_TYPE_EQUALS();
	
	public String QUESTION_TYPE_NOT_EQUALS();
	
	public String QUESTION_TYPE();
	
	public String QUESTION_TYPE_NAME();
	
	public String USERTYPE_EQUALS();
	
	public String USERTYPE_NOT_EQUALS();
}
