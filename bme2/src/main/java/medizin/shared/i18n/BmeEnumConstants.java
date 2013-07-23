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

	//UserType
	public String ADMIN();
	public String USER();
}
