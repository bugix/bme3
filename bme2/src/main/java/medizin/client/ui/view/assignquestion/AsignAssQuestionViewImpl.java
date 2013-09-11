package medizin.client.ui.view.assignquestion;

import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AsignAssQuestionViewImpl extends Composite implements AsignAssQuestionView  {

	private static AsignAssQuestionViewImplUiBinder uiBinder = GWT
			.create(AsignAssQuestionViewImplUiBinder.class);

	interface AsignAssQuestionViewImplUiBinder extends
			UiBinder<Widget, AsignAssQuestionViewImpl> {
	}

	private Presenter presenter;

	public AsignAssQuestionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		questionAdvancedSearchSubViewImpl.getAddMc().setVisible(false);
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl() {
		return questionAdvancedSearchSubViewImpl;
	}

	

}
