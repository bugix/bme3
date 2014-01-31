package medizin.client.ui.view;

import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AcceptAnswerViewImpl extends Composite implements AcceptAnswerView  {

	private static AcceptAnswerViewImplUiBinder uiBinder = GWT
			.create(AcceptAnswerViewImplUiBinder.class);

	interface AcceptAnswerViewImplUiBinder extends
			UiBinder<Widget, AcceptAnswerViewImpl> {
	}
	
	
	@UiField
	VerticalPanel questionPanel;
	
	@Override
	public VerticalPanel getQuestionPanel(){
		return questionPanel;
	}

	private Delegate delegate;


	public AcceptAnswerViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

	}
	
	@UiField
	ApplicationLoadingView loadingPopup;




	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
		
	}
		

	@Override
	public ApplicationLoadingView getLoadingPopup() {
		return loadingPopup;
	}


}
