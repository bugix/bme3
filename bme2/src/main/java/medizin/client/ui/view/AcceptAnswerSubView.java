package medizin.client.ui.view;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.DeclineEmailPopupDelagate;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface AcceptAnswerSubView extends IsWidget {


    void setDelegate(Delegate delegate);

    public interface Delegate {

		void onRangeChanged(QuestionProxy questionProxy,
				AbstractHasData<AnswerProxy> table);

		void acceptClicked(AnswerProxy answerProxy, AcceptAnswerSubView acceptAnswerSubView);
		
		void forcedAcceptClicked(AnswerProxy answerProxy, AcceptAnswerSubView acceptAnswerSubView);
       
		void rejectClicked(AnswerProxy answerProxy);

		void acceptAllAnswerClicked(QuestionProxy questionProxy);
    }


	AbstractHasData<AnswerProxy> getTable();

	void setProxy(QuestionProxy questionProxy);

	void setDelegatePopup(DeclineEmailPopupDelagate delegate);

	public DisclosurePanel getQuestionDisclosurePanel();
	
	public void setAcceptAnswerSubView(AcceptAnswerSubView acceptAnswerSubView);
}
