package medizin.client.ui.view.assignquestion;

import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.assesment.AssesmentDetailsView.Presenter;
import medizin.client.ui.view.assignquestion.AddQuestionsTabPanel.Delegate;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddQuestionsTabPanelImpl extends TabBar implements  AddQuestionsTabPanel {

	private static AddQuestionsTabPanelImplUiBinder uiBinder = GWT
			.create(AddQuestionsTabPanelImplUiBinder.class);

	interface AddQuestionsTabPanelImplUiBinder extends
			UiBinder<Widget, AddQuestionsTabPanelImpl> {
	}

	private Delegate delegate;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);


	public AddQuestionsTabPanelImpl() {
		//initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {
		addTab(constants.proposedQuestion());
		
		addTab(constants.pastQuestion());
		addTab(constants.newQuestion());
		
		selectTab(0);
		
		addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				delegate.tabQuestionClicked(event.getSelectedItem());
				
			}
		});
		
	
		
		
	}
	

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate= delegate;
		
		
	}

	@Override
	public int getActiveTab() {
		return getSelectedTab();
	}





}
