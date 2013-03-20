package medizin.client.ui.view;

import java.util.List;

import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.view.question.MatrixAnswerListView;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface AcceptMatrixAnswerSubView extends IsWidget {

	 void setDelegate(Delegate delegate);

	    public interface Delegate {

			void onMatrixRangeChanged(QuestionProxy questionProxy, AbstractHasData<MatrixValidityProxy> table, AcceptMatrixAnswerSubView acceptMatrixAnswerSubView);

			void matrixAcceptClicked(QuestionProxy questionProxy);
	       
			void matrixRejectClicked(QuestionProxy questionProxy);
	    }

		AbstractHasData<MatrixValidityProxy> getTable();

		void setProxy(QuestionProxy proxy);

		void setDelegatePopup(DeclineEmailPopupDelagate delegate);
		
		public List<MatrixValidityProxy> getMatrixAnswerList();
		
		public void setMatrixAnswerList(List<MatrixValidityProxy> matrixAnswerList);

		public DisclosurePanel getQuestionDisclosurePanel();
}
