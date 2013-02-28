package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.shared.Validity;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;

import com.google.common.base.Function;
import com.google.gwt.place.shared.Place;

public interface MatrixAnswerView {

	public interface Presenter {
        void goTo(Place place);
    }
	
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void saveMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList,PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment);

		void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Function<AnswerProxy, Void> function);

		void saveMatrixValidityValue(MatrixValidityVO matrixValidityVO,Validity validity, Function<MatrixValidityProxy, Void> function);

		void deletedSelectedAnswer(AnswerProxy answerProxy, Boolean isAnswerX,Function<Boolean, Void> function);

	}
	
	 void setDelegate(Delegate delegate);

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser);

	void setRewiewerPickerValues(Collection<PersonProxy> values);

	void display();

	void setValues(List<MatrixValidityProxy> response);
	 
}
