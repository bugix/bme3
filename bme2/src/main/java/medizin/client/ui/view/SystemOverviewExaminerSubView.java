package medizin.client.ui.view;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface SystemOverviewExaminerSubView extends IsWidget {
	
	interface Delegate{
		void sendMailBtnClicked(PersonProxy personProxy, List<AssesmentProxy> assesmentProxy, String messageContent);

		void loadTemplate(SystemOverviewExaminerSubViewImpl examinerSubViewImpl);
	}

	public Label getAcceptQueAnswerLbl();
	
	public Label getMcMsgLabel();
	
	public VerticalPanel getExaminerVerticalPanel();

	public void setAcceptAnswerAndQuestion(String examinerName, Long acceptQuestionCount, Long acceptAnswerCount);
	
	public void setPersonProxy(PersonProxy personProxy);
	
	public void setAssesmentProxy(List<AssesmentProxy> assesmentProxy);
	
	public void setDelegate(Delegate delegate);
	
	public void displayMail(String response);
	
	public DisclosurePanel getExaminerDisclosurePanel();
}
