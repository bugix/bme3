package medizin.client.ui.view.assignquestion;

import medizin.client.proxy.PersonProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionPanelImpl extends Composite implements QuestionPanel {

	private static QuestionPanelImplUiBinder uiBinder = GWT
			.create(QuestionPanelImplUiBinder.class);

	interface QuestionPanelImplUiBinder extends
			UiBinder<Widget, QuestionPanelImpl> {
	}

	private Delegate delegate;
	@UiField
	VerticalPanel questionPanel;
	
	@UiField(provided = true)
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
	}

	

	public QuestionPanelImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		questionPanel.setSpacing(5);
		
		/*authorListBox.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				delegate.authorValueChanged(event.getValue());
				
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

}
