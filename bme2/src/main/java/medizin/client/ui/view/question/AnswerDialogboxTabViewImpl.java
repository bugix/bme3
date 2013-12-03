package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.ui.richtext.RichTextToolbar;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.resource.image.polygon.ImagePolygonViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.ClientUtility;
import medizin.client.util.ImageWidthHeight;
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.i18n.BmeMessages;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnswerDialogboxTabViewImpl extends DialogBox implements AnswerDialogboxTabView {

	private static AnswerDialogboxTabViewImplUiBinder uiBinder = GWT.create(AnswerDialogboxTabViewImplUiBinder.class);

	interface AnswerDialogboxTabViewImplUiBinder extends UiBinder<Widget, AnswerDialogboxTabViewImpl> {}
	
	@UiField(provided = true)
	RichTextToolbar toolbar;
	
	@UiField(provided = true)
	RichTextArea answerTextArea;
	
	@UiField
	IconButton save;

	@UiField
	IconButton closeButton;
	
	IconButton btnKeywordNotOnImg = new IconButton();
	
	IconButton btnClose = new IconButton();

	@UiField
	public CheckBox submitToReviewComitee;

	@UiField
	public TextArea comment;
	
	@UiField
	VerticalPanel viewContainer;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> author;
	
	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;
	
	@UiField
	Label lblAdditionalKeyword;
	
	@UiField
	Button btnAdditionalKeyword;
	
	@UiField
	TextArea txtAdditionalKeyword;
	
	@UiField
	TabPanel mainTabPanel;
	
	@UiField(provided = true)
	ValueListBox<Validity> validity = new ValueListBox<Validity>(new AbstractRenderer<medizin.shared.Validity>() {

		public String render(medizin.shared.Validity obj) {
			return obj == null ? "" : String.valueOf(obj);
		}
	});
	
	private Delegate delegate;

	private AnswerProxy answer;
    
	private final QuestionProxy question;
	
	public final static BmeMessages bmeMessages = GWT.create(BmeMessages.class);
	
	public final static BmeConstants constants = GWT.create(BmeConstants.class);

	private ImageRectangleViewer imageRectangleViewer;
	
	private ImagePolygonViewer imagePolygonViewer;
	
	@UiHandler("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();
	}
	
	public void onKeywordNotOnImgClicked()
	{
		mainTabPanel.selectTab(1);
	}

	public AnswerDialogboxTabViewImpl(QuestionProxy questionProxy, EventBus eventBus, Map<String, Widget> reciverMap) {
		this.question = questionProxy;
		
		answerTextArea = new RichTextArea();
		answerTextArea.setSize("100%", "14em");
		toolbar = new RichTextToolbar(answerTextArea);
		toolbar.setWidth("100%");
		
		setWidget(uiBinder.createAndBindUi(this));
		
		btnKeywordNotOnImg.setText(constants.keywordNotOnImg());
		btnKeywordNotOnImg.setIcon("seek-next");
		
		btnClose.setText(constants.close());
		btnClose.setIcon("closethick");
		
		btnClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onCloseButtonClick(event);
			}
		});
		
		btnKeywordNotOnImg.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onKeywordNotOnImgClicked();
			}
		});
		
		reciverMap.put("answerText",answerTextArea);
		reciverMap.put("autor", author.getTextField().advancedTextBox);
		reciverMap.put("rewiewer", rewiewer.getTextField().advancedTextBox);
		reciverMap.put("validity", validity);
		reciverMap.put("submitToReviewComitee", submitToReviewComitee);
		reciverMap.put("additionalKeywords", txtAdditionalKeyword);
		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setTitle(constants.answerDialogBoxTitle());
		setText(constants.answerDialogBoxTitle());
		setHeight("100%");
		
		submitToReviewComitee.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(submitToReviewComitee.getValue())
				{
					rewiewer.setEnabled(false);
				}
				else
				{
					rewiewer.setEnabled(true);
				}
			}
		});
		
		this.addStyleName("mainAnswerDialogPanel");
	}
	
	private void addForShowInImage() {

		Log.info("Question id :" + question.getId());
		
		Long currentAnswerId = answer != null ? answer.getId() : null;
		delegate.findAllAnswersPoints(question.getId(),currentAnswerId,new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> polygons) {
				
				List<PolygonPath> polygonPaths = PolygonPath.getPolygonPaths(polygons);
						
				if(question != null && question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) && question.getQuestionResources() != null && question.getQuestionResources().isEmpty() == false /*&& question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null*/) {
					final QuestionResourceProxy questionResourceProxy = Lists.newArrayList(question.getQuestionResources()).get(0);
					final Integer imageWidth = questionResourceProxy.getImageWidth();
					final Integer imageHeight = questionResourceProxy.getImageHeight();
					final String path = questionResourceProxy.getPath();
					imagePolygonViewer = new ImagePolygonViewer(path, imageWidth, imageHeight,polygonPaths, true);
					
					imagePolygonViewer.addPathClosedHandler(new Function<Void, Void>() {
						
						@Override
						public Void apply(Void input) {
							onKeywordNotOnImgClicked();
							return null;
						}
					});
					
					if(answer != null && answer.getPoints() != null) {
						imagePolygonViewer.setCurrentPolygon(PolygonPath.getPolygonPath(answer.getPoints()));
					}
					imagePolygonViewer.getBtnHPPanel().add(btnClose);
					viewContainer.add(imagePolygonViewer);
				}
				
				return null;
			}
			
		});
	}

	private void addForImageKey() {

		Log.info("Question id :" + question.getId());
		Long currentAnswerId = answer != null ? answer.getId() : null;
		delegate.findAllAnswersPoints(question.getId(),currentAnswerId,new Function<List<String>, Void>(){

			@Override
			public Void apply(List<String> points) {
				
				final List<Point> rectanglePoints = Point.getPoints(points);
				
				if(question != null && question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) && question.getQuestionResources() != null && question.getQuestionResources().isEmpty() == false  /*&& question.getPicturePath() != null && question.getPicturePath().length() > 0 && question.getQuestionType().getImageWidth() != null && question.getQuestionType().getImageHeight() != null*/) {
					
					final QuestionResourceProxy questionResourceProxy = Lists.newArrayList(question.getQuestionResources()).get(0);
					final Integer imageWidth = questionResourceProxy.getImageWidth();
					final Integer imageHeight = questionResourceProxy.getImageHeight();
					final String path = questionResourceProxy.getPath();
					
					if (imageWidth != null &&imageHeight != null)
					{
						imageRectangleViewer = new ImageRectangleViewer(path, imageWidth, imageHeight, rectanglePoints, true);
						if(answer != null && answer.getPoints() != null) {
							imageRectangleViewer.setCurrentPoint(Point.getPoint(answer.getPoints()));
						}
						imageRectangleViewer.addPointClicked(new Function<Void, Void>() {
							
							@Override
							public Void apply(Void input) {
								onKeywordNotOnImgClicked();
								return null;
							}
						});
						
						imageRectangleViewer.getBtnHPPanel().add(btnKeywordNotOnImg);
						imageRectangleViewer.getBtnHPPanel().add(btnClose);
						viewContainer.add(imageRectangleViewer);
						
					}
					else
					{
						ClientUtility.getImageWidthHeight(path, new ImageWidthHeight() {
							
							@Override
							public void apply(Integer width, Integer height) {
								imageRectangleViewer = new ImageRectangleViewer(path, width, height, rectanglePoints, true);
								if(answer != null && answer.getPoints() != null) {
									imageRectangleViewer.setCurrentPoint(Point.getPoint(answer.getPoints()));
								}
								viewContainer.add(imageRectangleViewer);
							}
						});
					}
				}
				
				viewContainer.setVisible(true);
				return null;
			}
		});
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void display(QuestionTypes types) {
		
		mainTabPanel.selectTab(0);
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			
			if(QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType())) {
				lblAdditionalKeyword.setVisible(true);
				lblAdditionalKeyword.setStyleName("label");
				btnAdditionalKeyword.setVisible(true);
				txtAdditionalKeyword.setVisible(true);
			}		
		}
		
		//image viewer
		if(question != null && question.getQuestionType() != null && question.getQuestionType().getQuestionType() != null) {
			viewContainer.clear();
			switch (question.getQuestionType().getQuestionType()) {
			case ShowInImage:
			{
				addForShowInImage();
				break;
			}
			case Imgkey:
			{
				addForImageKey();
				break;
			}
			}
		}
		center();
		show();
		
		if (QuestionTypes.ShowInImage.equals(types)){
			validity.setValue(Validity.Wahr);
			Document.get().getElementById("validity").getStyle().setDisplay(Display.NONE);
		}
		
		if(QuestionTypes.Imgkey.equals(types)) {
			Element answerDialogElement = Document.get().getElementById("answerDialogText");
			if(answerDialogElement != null) {
				answerDialogElement.removeFromParent();
			}
		}
	}

	@Override
	public void setAutherPickerValues(Collection<PersonProxy> values, PersonProxy logedUser,  boolean isAdminOrInstitutionalAdmin) {	
		
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) author.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		author.setSuggestOracle(suggestOracle1);
		author.setRenderer(new AbstractRenderer<PersonProxy>() {
	
			@Override
			public String render(PersonProxy object) {
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
		
		if(isAdminOrInstitutionalAdmin == false) {
			author.setSelected(logedUser);
			author.setEnabled(false);
		} else {
			author.setSelected(question.getAutor());
		}
		author.setWidth(150);
		rewiewer.setWidth(150);
	}
	
	@Override
	public void setRewiewerPickerValues(List<PersonProxy> values) {
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) rewiewer.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		rewiewer.setSuggestOracle(suggestOracle1);
		rewiewer.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
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
		
		PersonProxy lastSelectedReviwer = ClientUtility.getAnswerReviwerPersonProxyFromCookie(values);
		if (lastSelectedReviwer != null) {
			rewiewer.setSelected(lastSelectedReviwer);
		}
			
	}

	@Override
	public void setValidityPickerValues(Collection<Validity> values) {
		validity.setAcceptableValues(values);
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		if(validationOfFields()) {
		
			String points = null; 
			String mediaPath = null;
			String additionalKeywords = null; 
			Integer sequenceNumber = null;
			String answerText = "";
			switch (question.getQuestionType().getQuestionType()) {
			case ShowInImage:
			{
				points = imagePolygonViewer.getPoints();
				answerText = answerTextArea.getHTML();
				break;
			}
			case Imgkey: 
			{
				additionalKeywords = txtAdditionalKeyword.getText();
				if(Validity.Wahr.equals(validity.getValue()) == true) {
					points = imageRectangleViewer.getPoint();	
				}
				break;
			}
			}
			
			delegate.saveAnswerProxy(answer, answerText, author.getSelected(), rewiewer.getSelected(), submitToReviewComitee.getValue(), (comment.getText().isEmpty() ? " " : comment.getText()),validity.getValue(),points,mediaPath,additionalKeywords,sequenceNumber, new Function<AnswerProxy,Void>() {

				@Override
				public Void apply(AnswerProxy input) {
					
					hide();
					return null;
				}
				
			});

		}else {
			Log.info("Validation failed");
		}
	}

	private boolean validationOfFields() {
		
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		
		answerTextArea.removeStyleName("higlight_onViolation");
		rewiewer.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		author.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		submitToReviewComitee.removeStyleName("higlight_onViolation");
		validity.removeStyleName("higlight_onViolation");
		if(imagePolygonViewer != null) imagePolygonViewer.removeStyleName("higlight_onViolation");
		if(imageRectangleViewer != null) imageRectangleViewer.removeStyleName("higlight_onViolation");
	
		if(submitToReviewComitee.getValue()){
			rewiewer.setSelected(null);
		}else if(rewiewer.getSelected() != null){
			submitToReviewComitee.setValue(false);
		}else {
			flag = false;
			messages.add(constants.selectReviewerOrComitee());
			rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			submitToReviewComitee.addStyleName("higlight_onViolation");
		}
		
		if(author.getSelected() != null && rewiewer.getSelected() != null && author.getSelected().getId().equals(rewiewer.getSelected().getId()) == true) {
			flag = false;
			messages.add(constants.authorReviewerMayNotBeSame());
			author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
		}
		
		if(question.getQuestionType() != null && QuestionTypes.ShowInImage.equals(question.getQuestionType().getQuestionType()) == true ) {
			Log.info("IN ShowInImage Question type");
			if(imagePolygonViewer == null || imagePolygonViewer.isValidPolygon() == false) {
				flag = false;
				Log.error("Polygon is not property added. Try again");
				messages.add(constants.polygonErrorMessage());
				imagePolygonViewer.addStyleName("higlight_onViolation");
			}
			
			if(answerTextArea.getText() == null || answerTextArea.getText().length() <= 0) {
				flag = false;
				messages.add(constants.answerTextErrorMessage());
				answerTextArea.addStyleName("higlight_onViolation");
			}
		}else if(question.getQuestionType() != null && QuestionTypes.Imgkey.equals(question.getQuestionType().getQuestionType()) == true && validity.getValue() != null && Validity.Wahr.equals(validity.getValue())) {
			Log.info("IN Imgkey Question type");
			if(imageRectangleViewer == null || imageRectangleViewer.getPoint() == null) {
				flag = false;
				messages.add(constants.rectangleErrorMessage());
				imageRectangleViewer.removeStyleName("higlight_onViolation");
				Log.error("Rectangle is not property added. Try again");
			}
		}
		
		if(author.getSelected() == null) {
			flag = false;
			messages.add(constants.authorMayNotBeNull());
			author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
		}
		
		if(validity.getValue() == null) {
			flag = false;
			messages.add(constants.validityMayNotBeNull());
			validity.addStyleName("higlight_onViolation");
		}
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		return flag;
	}

	@Override
	public void close() {
		hide();
	}

	@Override
	public void setValues(AnswerProxy answer) {
		this.answer = answer;
		answerTextArea.setHTML(answer.getAnswerText());
		txtAdditionalKeyword.setText(answer.getAdditionalKeywords());
		author.setSelected(answer.getAutor());
		rewiewer.setSelected(answer.getRewiewer());
		validity.setValue(answer.getValidity());
		submitToReviewComitee.setValue(answer.getSubmitToReviewComitee());
		comment.setText(answer.getComment() != null?answer.getComment() : null);
		
	}
}
