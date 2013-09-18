package medizin.client.ui.view.assignquestion;

import java.text.ParseException;

import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionPanelImpl extends Composite implements QuestionPanel {

	private static QuestionPanelImplUiBinder uiBinder = GWT
			.create(QuestionPanelImplUiBinder.class);

	interface QuestionPanelImplUiBinder extends
			UiBinder<Widget, QuestionPanelImpl> {
	}
	
	private BmeConstants constants=GWT.create(BmeConstants.class);

	private Delegate delegate;
	
	@UiField
	VerticalPanel questionPanel;
	
	private SearchQuestionPopupView searchQuestionPopupView;
	
	public SearchQuestionPopupView getSearchQuestionPopupView() {
		return searchQuestionPopupView;
	}

	@UiField
	IconButton searchButton;
	
	/*@UiField(provided = true)
	ValueListBox<PersonProxy> authorListBox=new ValueListBox<PersonProxy>(new AbstractRenderer<PersonProxy>() {

		@Override
		public String render(PersonProxy object) {
			// TODO Auto-generated method stub
			if(object!=null)
				return object.getPrename() +" "+ object.getName();
			else
			{
				return "";
			}
		}
	});

	public ValueListBox<PersonProxy> getAuthorListBox() {
		return authorListBox;
	}*/

	

	public QuestionPanelImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		questionPanel.setSpacing(5);
		
		/*authorListBox.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				delegate.authorValueChangedFromRightSide(event.getValue());
				
			}
		});*/
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@Override
	public void addQuestion(QuestionView question) {
		questionPanel.add(question);
		
	}

	@Override
	public void addAssesmentQuestion(AssesmentQuestionView question) {
		questionPanel.add(question);
		
	}
	
	@Override
	public void removeAll() {
		questionPanel.clear();
		
	}

	@Override
	public VerticalPanel getQuestionDisplayPanel() {
		
		return questionPanel;
	}
	
	@UiHandler("searchButton")
	public void showSearchPopup(ClickEvent event)
	{
		if(searchQuestionPopupView==null)
		{
			searchQuestionPopupView=new SearchQuestionPopupViewImpl();
			((SearchQuestionPopupViewImpl)searchQuestionPopupView).setAnimationEnabled(true);
			((SearchQuestionPopupViewImpl)searchQuestionPopupView).setAutoHideEnabled(true);
			((SearchQuestionPopupViewImpl)searchQuestionPopupView).getSearchBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					try
					{
					Integer questionId=((SearchQuestionPopupViewImpl)searchQuestionPopupView).getQuestionIdTxt().getValueOrThrow();
					String questionshortName=((SearchQuestionPopupViewImpl)searchQuestionPopupView).getQuestionShortNameTxt().getValueOrThrow();
					if(questionshortName==null)
					{
						questionshortName="";
					}
					String questionType=((SearchQuestionPopupViewImpl)searchQuestionPopupView).getQuestionTypeTxt().getValueOrThrow();
					if(questionType==null)
					{
						questionType="";
					}
					
					delegate.searchQuestion(questionshortName,questionId,questionType);
					}
					catch(ParseException e)
					{
						ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.questionIdIsInteger());
					}
					
				}
			});
		
		}
		
		searchQuestionPopupView.reset();
		((SearchQuestionPopupViewImpl)searchQuestionPopupView).setPopupPosition(searchButton.getAbsoluteLeft()-230, searchButton.getAbsoluteTop()-100);
		((SearchQuestionPopupViewImpl)searchQuestionPopupView).show();
	}

}
