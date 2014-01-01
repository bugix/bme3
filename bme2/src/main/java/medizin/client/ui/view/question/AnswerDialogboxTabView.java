package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.PersonProxy;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;

import com.google.common.base.Function;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnswerDialogboxTabView extends IsWidget {

	void display(QuestionTypes questionTypes);
	
	interface Delegate {
		void cancelAnswerClicked();
		void findAllAnswersPoints(Long id,Long currentAnswerId, Function<List<String>, Void> function);
		void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, Boolean forcedActive, final Function<AnswerProxy, Void> function);
	}
    
    void setDelegate(Delegate delegate);

	void setRewiewerPickerValues(List<PersonProxy> values);

	void setValidityPickerValues(Collection<Validity> values);

	void close();

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy userLoggedIn, boolean isAdminOrInstitutionalAdmin);

	void setValues(AnswerProxy answer);
}
