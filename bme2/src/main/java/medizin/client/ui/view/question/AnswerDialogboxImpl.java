package medizin.client.ui.view.question;

import java.util.Collection;
import java.util.List;

import medizin.client.proxy.PersonProxy;
import medizin.client.shared.Validity;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.view.roo.PersonProxyRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;

public class AnswerDialogboxImpl extends DialogBox implements AnswerDialogbox/*
																			 * ,
																			 * Editor
																			 * <
																			 * AnswerProxy
																			 * >
																			 */{

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

	@UiField
	public CheckBox submitToReviewComitee;

	@UiField
	public TextArea comment;
	
	@UiField
	TabPanel questionTypePanel;

	@UiField
	public Label lblAuther;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> auther;
	
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@UiHandler("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();

	}

	@Override
	public CheckBox getSubmitToReviewerComitee() {
		return submitToReviewComitee;
	}

	@Override
	public TextArea getComment() {
		return comment;
	}

	/*
	 * interface EditorDriver extends RequestFactoryEditorDriver<AnswerProxy,
	 * AnswerDialogboxImpl> {}
	 */
	// private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

	/*
	 * @Override public
	 * RequestFactoryEditorDriver<AnswerProxy,AnswerDialogboxImpl>
	 * createEditorDriver() { RequestFactoryEditorDriver<AnswerProxy,
	 * AnswerDialogboxImpl> driver = GWT.create(EditorDriver.class);
	 * driver.initialize(this); return driver;
	 * 
	 * }
	 */

	/*
	 * @UiField SimplePanel toolbarPanel;
	 */
	@UiField
	public DivElement descriptionValue;
	
	public AnswerDialogboxImpl() {
		
		answerTextArea = new RichTextArea();
		answerTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(answerTextArea);
		toolbar.setWidth("100%");
		
		setWidget(uiBinder.createAndBindUi(this));
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setTitle("Anzahl Fragentypen pro Prüfung hinzufügen");
		setText("Anzahl Fragentypen pro Prüfung hinzufügen");
		questionTypePanel.selectTab(0);
		questionTypePanel.getTabBar().setTabText(0, "Manage Answer");
		questionTypePanel.getTabBar().setTabText(1, "Media");
		lblAuther.setText(constants.auther());
		/*RichTextToolbar toolbar = new RichTextToolbar(answerTextArea);*/

		// toolbarPanel.add(toolbar);
		submitToReviewComitee.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			if(submitToReviewComitee.isChecked())
			{
				DOM.setElementPropertyBoolean(rewiewer.getElement(), "disabled", true);

			}
			else
			{
				
				DOM.setElementPropertyBoolean(rewiewer.getElement(), "disabled", false);
			}
			}
		});

	}

	@Override
	public void setRichPanelHTML(String html) {
		// Log.error(html);
		answerTextArea.setHTML(html);
	}

	@Override
	public String getRichtTextHTML() {
		// Log.info(questionTextArea.getHTML());
		// Log.info(questionTextArea.getText());
		return answerTextArea.getHTML();
		// return new String("<b>hallo</b>");
	}

	@UiField(provided = true)
	public RichTextToolbar toolbar;
	
	@UiField(provided = true)
	RichTextArea answerTextArea;

	private Delegate delegate;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void display() {
		center();
		show();

	}

	@UiField(provided = true)
	ValueListBox<PersonProxy> rewiewer = new ValueListBox<PersonProxy>(
			PersonProxyRenderer.instance(),
			new EntityProxyKeyProvider<PersonProxy>());

	@UiField(provided = true)
	ValueListBox<Validity> validity = new ValueListBox<Validity>(
			new AbstractRenderer<medizin.client.shared.Validity>() {

				public String render(medizin.client.shared.Validity obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			});

	@Override
	public ValueListBox<Validity> getValidity() {
		return validity;
	}

	
	@Override
	public ValueListBox<PersonProxy> getRewiewer()
	{
		return rewiewer;
	}
	
	@Override
	public void setAutherPickerValues(Collection<PersonProxy> values, PersonProxy logedUser) {
	
		
		
	DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) auther.getSuggestOracle();
	suggestOracle1.setPossiblilities((List<PersonProxy>) values);
	 /* Collection<MyObjectType> myCollection = ...;
	 List<MyObjectType> list = new ArrayList<MyObjectType>(myCollection);*/
	auther.setSuggestOracle(suggestOracle1);
	auther.setRenderer(new AbstractRenderer<PersonProxy>() {

		@Override
		public String render(PersonProxy object) {
			// TODO Auto-generated method stub
			if(object!=null)
			{
			return object.getName() + " "+ object.getPrename();
			}
			else
			{
				return "";
			}
		}
	});
	//change {
	if(logedUser.getIsAdmin() == false) {
		
		auther.setSelected(logedUser);
		//answerDialogbox.getAutherSuggestBox().setSelected(userLoggedIn);
		auther.setEnabled(false);
	}
	auther.setWidth(150);
	}
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
	
	
	@Override
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> getAutherSuggestBox() {
		// TODO Auto-generated method stub
		return auther;
	}

}
