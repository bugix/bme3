package medizin.client.ui.view;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.util.ClientUtility;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;

public class AnswerValue {
	
	 public static BmeConstants constants = GWT.create(BmeConstants.class);
	 
	public static String getValue(QuestionTypeProxy object){

		if(object==null)
		{
			return "";
		}
		if(object.getQuestionType()!=null)
		{
			if(object.getQuestionType()==QuestionTypes.Textual || object.getQuestionType()==QuestionTypes.Sort)
				return constants.sum()+"("+ClientUtility.sumAnswerValue(object.getSumAnswer())+ ")"+"/"+constants.trueAns()+"("+object.getSumTrueAnswer()+ ")"+"/"+constants.falseAns()+"("+object.getSumFalseAnswer()+ ")";
			
			if(object.getQuestionType()==QuestionTypes.Imgkey)
				return constants.count()+"("+object.getKeywordCount()+ ")"+"/"+constants.keyOnly()+"("+object.getIsDictionaryKeyword()+ ")"+"/"+constants.maxLen()+"("+object.getAnswerLength()+ ")";
			
			if(object.getQuestionType()==QuestionTypes.Matrix)
				return constants.single()+"("+object.getAllowOneToOneAss()+ ")"+"/"+constants.maxLen()+"("+object.getAnswerLength()+ ")";
			
			if(object.getQuestionType()==QuestionTypes.ShowInImage)
				return "";
			
			if(object.getQuestionType()==QuestionTypes.LongText)
				return "";
			
			if(object.getQuestionType()==QuestionTypes.MCQ)
				return constants.media() + "(" + object.getMultimediaType() + ")" + "/" + constants.selectionType() + "(" + object.getSelectionType() + ")";
			
			if (object.getQuestionType() == QuestionTypes.Drawing)
				return "";
			
			
		}
		return "";

		}


}
