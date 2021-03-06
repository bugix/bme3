package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;

import com.google.common.base.Function;

public interface MatrixAnswerView {

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		//void saveMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList,PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment);

		//void saveAnswerProxy(AnswerProxy answerProxy, String answerText, PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Validity validity, String points, String mediaPath, String additionalKeywords, Integer sequenceNumber, Function<AnswerProxy, Void> function);
		
		//void saveMatrixValidityValue(MatrixValidityVO matrixValidityVO,Validity validity, Function<MatrixValidityProxy, Void> function);

		void deletedSelectedAnswer(AnswerProxy answerProxy, Boolean isAnswerX,Function<Boolean, Void> function);

		void closedMatrixValidityView();

		void saveAllTheValuesToAnswerAndMatrixAnswer(List<MatrixValidityProxy> currentMatrixValidityProxy, Matrix<MatrixValidityVO> matrixList,PersonProxy author, PersonProxy rewiewer, Boolean submitToReviewComitee, String comment, Boolean forcedActive);

	}
	
	void setDelegate(Delegate delegate);

	void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser, boolean isAdminOrInstitutionalAdmin);

	void setRewiewerPickerValues(List<PersonProxy> values);

	void display();

	void setValues(List<MatrixValidityProxy> response, boolean isNew, boolean isEdit, boolean isDelete);

	ApplicationLoadingView getLoadingPopup();
	 
}
