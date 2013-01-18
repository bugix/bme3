package medizin.client.ui.view.question;

import java.util.Collection;

import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.view.question.QuestionEditView;
import medizin.client.ui.view.question.QuestionEditView.Delegate;
import medizin.client.ui.view.question.QuestionEditView.Presenter;
import medizin.client.place.PlaceUserDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.request.PersonRequest;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.roo.AnswerSetEditor;
import medizin.client.ui.view.roo.KeywordSetEditor;
import medizin.client.ui.view.roo.McSetEditor;
import medizin.client.ui.view.roo.QuestionAccessSetEditor;
import medizin.client.ui.view.roo.QuestionTypeProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class QuestionEditViewImpl extends Composite implements QuestionEditView/*, Editor<QuestionProxy>*/  {

	private static QuestionEditViewImplUiBinder uiBinder = GWT
			.create(QuestionEditViewImplUiBinder.class);

	interface QuestionEditViewImplUiBinder extends
			UiBinder<Widget, QuestionEditViewImpl> {
	}
	
	/*interface EditorDriver extends RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<QuestionProxy,QuestionEditViewImpl> createEditorDriver() {
        RequestFactoryEditorDriver<QuestionProxy, QuestionEditViewImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
    }*/

	private Presenter presenter;
    private Delegate delegate;
	private QuestionProxy proxy;
	private McAppRequestFactory requests;
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField
    public IconButton cancel;

    @UiField
    public IconButton save;

    @UiField
	TabPanel questionTypePanel;
    
    @UiField
	SpanElement title;
    
	@UiField
	public Label lblQuestionType;
	
	@UiField(provided = true)
    public ValueListBox<QuestionTypeProxy> questionType = new ValueListBox<QuestionTypeProxy>(QuestionTypeProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.QuestionTypeProxy>());

	@UiField
	public Label lblQuestionText;
	
	@UiField(provided=true)
	public RichTextToolbar toolbar;
	
	@UiField
	public DivElement descriptionValue;
	
	@UiField(provided=true)
	public RichTextArea questionTextArea;
	
	
	
	@UiField
	public Label lblAuther;
	
	@UiField(provided = true)
	public ValueListBox<PersonProxy> autor = new ValueListBox<PersonProxy>(medizin.client.ui.view.roo.PersonProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());	
	
	
	
	@UiField
	public Label lblReviewer;
	
	@UiField(provided = true)
    public ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(medizin.client.ui.view.roo.PersonProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());
	
	@UiField
	public Label lblQuestionEvent;
	  
	@UiField(provided = true)
	public ValueListBox<QuestionEventProxy> questEvent = new ValueListBox<QuestionEventProxy>(medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.QuestionEventProxy>());
	
	
	
	@UiField
	public Label lblQuestionComment;
	
	@UiField(provided=true)
	public RichTextToolbar commentToolbar;
	
	@UiField
	public DivElement commentValue;
	
	@UiField(provided=true)
	public RichTextArea questionComment;
	
	
//	  @UiField
//	    DateBox dateAdded;
//
//	    @UiField
//	    DateBox dateChanged;

	    

	   
//
//	    @UiField  
//	    RichTextArea questionText;

	   /* @UiField  
	    RichTextArea questionTextArea;
*/

	  

	    

	  /*  @UiField
	    McSetEditor mcs;*/

    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
    	if(!edit){
    		delegate.saveClicked(false);
    	} else {
    		ConfirmQuestionChangesPopup confirm = new ConfirmQuestionChangesPopup(delegate);
    	}
		
    	
        //delegate.saveClicked(false);
    }


//    @UiField
//    SpanElement displayRenderer;


    

/*@UiField 
SimplePanel toolbarPanel;*/

	public QuestionEditViewImpl() {
		
		questionTextArea = new RichTextArea();
		questionTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(questionTextArea);
		toolbar.setWidth("100%");
		
		questionComment = new RichTextArea();
		questionComment.setSize("100%", "14em");
		commentToolbar = new RichTextToolbar(questionComment);
		commentToolbar.setWidth("100%");
		
		
		initWidget(uiBinder.createAndBindUi(this));
		questionTypePanel.selectTab(0);
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		
		questionTypePanel.getTabBar().setTabText(0, constants.manageQuestion());
		
		lblQuestionType.setText(constants.questionType());
		lblQuestionText.setText(constants.questionText());
		lblAuther.setText(constants.auther()); 
		lblReviewer.setText(constants.reviewer());
		lblQuestionComment.setText(constants.comment());
		lblQuestionEvent.setText("Question Event");
		//RichTextToolbar toolbar=new RichTextToolbar(questionTextArea);
		//toolbarPanel.add(toolbar);

	}
	
	@Override
	public void setRichPanelHTML(String html){
		Log.error(html);
		questionTextArea.setHTML(html);
	}

	@Override
	public void setValue(QuestionProxy question){
		//questionTextArea.setHTML(html);
		questionType.setValue(question.getQuestionType());
		questionTextArea.setHTML(question.getQuestionText());
		autor.setValue(question.getAutor());
		rewiewer.setValue(question.getRewiewer());
		questEvent.setValue(question.getQuestEvent());
		questionComment.setHTML(question.getComment().getComment());
		
	}
	
	@Override
	public String getRichtTextHTML(){
//		Log.info(questionTextArea.getHTML());
//		Log.info(questionTextArea.getText());
		return questionTextArea.getHTML();
//		return new String("<b>hallo</b>");
	}
	


	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	    
	    @Override
	    public void setRewiewerPickerValues(Collection<PersonProxy> values) {
	        rewiewer.setAcceptableValues(values);
	    }
	    @Override
	    public void setQuestEventPickerValues(Collection<QuestionEventProxy> values) {
	        questEvent.setAcceptableValues(values);
	    }
	    @Override
	    public void setAutorPickerValues(Collection<PersonProxy> values) {
	        autor.setAcceptableValues(values);
	    }
	    
	    
	    @Override
	    public void setQuestionTypePickerValues(Collection<QuestionTypeProxy> values) {
	        questionType.setAcceptableValues(values);
	    }

	    @Override
	    public void setMcsPickerValues(Collection<McProxy> values) {
	        //mcs.setAcceptableValues(values);
	    }
	    
	    
		/*@UiField
		Element  editTitle;
		@UiField
		Element  createTitle;*/


		private boolean edit;
		
		
	    
		@Override
		public void setEditTitle(boolean edit) {
			this.edit = edit;
		      if (edit) {
		    	  title.setInnerText("Edit");
		    	 // questionTypePanel.getTabBar().setTabText(0, "Edit Question");
		            //editTitle.getStyle().clearDisplay();
		            //createTitle.getStyle().setDisplay(Display.NONE);
		        } else {
		        	title.setInnerText("Create");
		        //	questionTypePanel.getTabBar().setTabText(0, "New Question");
		            //editTitle.getStyle().setDisplay(Display.NONE);
		            //createTitle.getStyle().clearDisplay();
		        }
			
		}
		

		@Override
		public ValueListBox<QuestionTypeProxy> getQuestionType()
		{
			return questionType;
		}
		
		@Override
		public RichTextArea getQuestionTextArea()
		{
			return questionTextArea;
		}
		
		@Override
		public ValueListBox<PersonProxy> getAuther()
		{
			return autor;
		}
		
		@Override
		public ValueListBox<PersonProxy> getReviewer()
		{
			return rewiewer;
		}
		
		@Override
		public ValueListBox<QuestionEventProxy> getQuestionEvent()
		{
			return questEvent;
		}
		
		
		@Override
		public RichTextArea getQuestionComment()
		{
			return questionComment;
		}
		
		
	

}
