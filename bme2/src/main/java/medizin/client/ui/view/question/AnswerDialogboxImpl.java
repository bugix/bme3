package medizin.client.ui.view.question;

import java.util.Collection;

import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.view.roo.PersonProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.shared.Validity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;


public class AnswerDialogboxImpl extends DialogBox implements AnswerDialogbox, Editor<AnswerProxy> {

	private static EventAccessDialogboxImplUiBinder uiBinder = GWT
			.create(EventAccessDialogboxImplUiBinder.class);

	interface EventAccessDialogboxImplUiBinder extends
			UiBinder<Widget, AnswerDialogboxImpl> {
	}

	private Presenter presenter;
	
	@UiField
    IconButton save;
    
	@UiField
	IconButton closeButton;
	
	@UiHandler ("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
            hide();
            
          }

	interface EditorDriver extends RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> {}
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
	
    @Override
    public RequestFactoryEditorDriver<AnswerProxy,AnswerDialogboxImpl> createEditorDriver() {
        RequestFactoryEditorDriver<AnswerProxy, AnswerDialogboxImpl> driver = GWT.create(EditorDriver.class);
        driver.initialize(this);
        return driver;
        
    }
	
    
    @UiField 
    SimplePanel toolbarPanel;

	public AnswerDialogboxImpl() {
		setWidget(uiBinder.createAndBindUi(this));
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    setTitle("Anzahl Fragentypen pro Prüfung hinzufügen");
	    setText("Anzahl Fragentypen pro Prüfung hinzufügen");
	    
RichTextToolbar toolbar=new RichTextToolbar(answerTextArea);
		
		toolbarPanel.add(toolbar);

	}

	@Override
	public void setRichPanelHTML(String html){
		//Log.error(html);
		answerTextArea.setHTML(html);
	}

	@Override
	public String getRichtTextHTML(){
//		Log.info(questionTextArea.getHTML());
//		Log.info(questionTextArea.getText());
		return answerTextArea.getHTML();
//		return new String("<b>hallo</b>");
	}
    @UiField  
    RichTextArea answerTextArea;
	
	private Delegate delegate;

	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate= delegate;
		
	}

	@Override
	public void display() {
		center();
		show();
		
	}
	


	    @UiField(provided = true)
	    ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(PersonProxyRenderer.instance(), new EntityProxyKeyProvider<PersonProxy>());





	    @UiField(provided = true)
	    ValueListBox<Validity> validity = new ValueListBox<Validity>(new AbstractRenderer<medizin.client.shared.Validity>() {

	        public String render(medizin.client.shared.Validity obj) {
	            return obj == null ? "" : String.valueOf(obj);
	        }
	    });



	    @Override
	    public void setRewiewerPickerValues(Collection<PersonProxy> values) {
	        rewiewer.setAcceptableValues(values);
	    }



	    @Override
	    public void setValidityPickerValues(Collection<Validity> values) {
	        validity.setAcceptableValues(values);
	    }



  
    
    @UiHandler("save")
    void onSave(ClickEvent event) {
        delegate.addAnswerClicked();
       // hide();
    }


	@Override
	public void close() {
		hide();
		
	}



}
