package medizin.client.ui.view.assignquestion;

import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.util.ClientUtility;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class AsignAssQuestionViewImpl extends Composite implements AsignAssQuestionView  {

	private static AsignAssQuestionViewImplUiBinder uiBinder = GWT
			.create(AsignAssQuestionViewImplUiBinder.class);

	interface AsignAssQuestionViewImplUiBinder extends
			UiBinder<Widget, AsignAssQuestionViewImpl> {
	}

	private Presenter presenter;

	public AsignAssQuestionViewImpl() {
		
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.ASIGNASSQUESTION_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		initWidget(uiBinder.createAndBindUi(this));
		questionAdvancedSearchSubViewImpl.getAddMc().setVisible(false);
		
		//setting widget width from cookie.
        setWidgetWidth();
	}


	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.ASIGNASSQUESTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}
	
	@Override
	public QuestionPanelImpl getQuestionPanel() {
		return questionPanel;
	}



	@Override
	public AssesmentTabPanelImpl getAssesmentTabPanel() {
		return assementTabPanel;
	}


	@Override
	public AssesmentQuestionPanelImpl getAssesmentQuestionPanel() {
		return assementQuestionPanel;
	}


	@Override
	public AddQuestionsTabPanelImpl getAddQuestionsTabPanel() {
		return addQuestionsTabPanel;
	}

	@UiField
	AssesmentTabPanelImpl assementTabPanel;
	@UiField
	AssesmentQuestionPanelImpl assementQuestionPanel;
	@UiField
	AddQuestionsTabPanelImpl addQuestionsTabPanel;
	@UiField
	QuestionPanelImpl questionPanel;

	
	
	@UiField
	QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl;

	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	ScrollPanel scrollDetailPanel;
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl() {
		return questionAdvancedSearchSubViewImpl;
	}

	@Override
	public ScrollPanel getScrollDetailPanel() {
		return scrollDetailPanel;
	}	

}
