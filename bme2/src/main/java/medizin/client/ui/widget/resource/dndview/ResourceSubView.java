package medizin.client.ui.widget.resource.dndview;

import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.ui.widget.resource.event.ResourceDeletedEvent;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.util.ClientUtility;
import medizin.shared.QuestionTypes;
import medizin.shared.i18n.BmeConstants;

import com.google.common.base.Function;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceSubView extends Composite {

	private static ResourceSubViewUiBinder uiBinder = GWT
			.create(ResourceSubViewUiBinder.class);

	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	private QuestionResourceClient questionResource;

	private ResourceView resourceView;

	private ResourceSubView resourceSubView;

	@UiField
	AbsolutePanel absPanel;

	@UiField
	Label htmlText;

	/*@UiField
	HTMLPanel hPanel;*/

	@UiField
	IconButton deleteButton;

	@UiField
	IconButton viewButton;

	private String imageDim = "12px";
	//private String textDim = "275px";
	private String textDim = "80%";

	private QuestionTypes questionType;

	private final EventBus eventBus;

	interface ResourceSubViewUiBinder extends UiBinder<Widget, ResourceSubView> {
	}

	public ResourceSubView(EventBus eventBus) {
		initWidget(uiBinder.createAndBindUi(this));
		resourceSubView = this;
		this.eventBus = eventBus;
	}

	public void setDetails(final QuestionResourceClient questionResource,QuestionTypes questionType,
			final ResourceView resourceView) {
		this.questionResource = questionResource;
		this.resourceView = resourceView;
		this.questionType = questionType;
		init();
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				questionResource.setState(State.DELETED);
				resourceSubView.removeFromParent();
				resourceView.onDragEnd(null);
				eventBus.fireEvent(new ResourceDeletedEvent(questionResource));
				
			}
		});
	}

	public void init() {

//		htmlText.setHTML(getName(questionResource.getPath(),
//				questionResource.getType()));
//		htmlText.setHeight(imageDim);
//		htmlText.setWidth(textDim);
//		htmlText.setWordWrap(true);
//
//		htmlText.setVisible(true);
		
		htmlText.setText(ClientUtility.getFileName(questionResource.getPath(), questionResource.getType()));
		htmlText.setHeight(imageDim);
		htmlText.setWidth(textDim);

		switch (questionResource.getType()) {
		case Image: {
			viewButton.setIcon("image");
			viewButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					final ImageViewer viewer = new ImageViewer();
					viewer.setUrl(questionResource.getPath(), questionType);
					DialogBox dialogBox = createDialogBox(constants.mediaViewer(),viewer,new Function<Boolean,Void>(){

						@Override
						public Void apply(Boolean input) {
							if(input != null && input.equals(true)) {
								viewer.closed();	
							}
							return null;
						}
					});
					dialogBox.center();
		            dialogBox.show();
				}
			});

			break;
		}
		case Sound: {
			viewButton.setIcon("signal-diag");
			viewButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					final AudioViewer viewer = new AudioViewer(questionResource.getPath());
					DialogBox dialogBox = createDialogBox(constants.mediaViewer(),viewer,new Function<Boolean,Void>(){

						@Override
						public Void apply(Boolean input) {
							if(input != null && input.equals(true)) {
								viewer.closed();	
							}
							return null;
						}
					});
					dialogBox.center();
		            dialogBox.show();
				}
			});
			break;
		}
		case Video: {
			viewButton.setIcon("video");
			viewButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					final VideoViewer viewer = new VideoViewer(questionResource.getPath());
					DialogBox dialogBox = createDialogBox(constants.mediaViewer(),viewer,new Function<Boolean,Void>(){

						@Override
						public Void apply(Boolean input) {
							if(input != null && input.equals(true)) {
								viewer.closed();	
							}
							return null;
						}
					});
					dialogBox.center();
		            dialogBox.show();
				}
			});
			break;
		}
		default:
			break;
		}

	}

	/**
	 * Create the dialog box for this example.
	 * @param function 
	 * 
	 * @return the new dialog box
	 */
	private DialogBox createDialogBox(String title,Widget widget, final Function<Boolean, Void> function) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText(title);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("100%");
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);

		// Add an image viewer to the dialog
		
		dialogContents.add(widget);
		dialogContents.setCellHorizontalAlignment(widget,
				HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button(constants.close());
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				function.apply(true);
			}
		});
		
		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Return the dialog box
		return dialogBox;
	}

	public ResourceView getResourceView() {
		return resourceView;
	}

	/*private HTMLPanel getPanel() {
		return hPanel;
	}*/

	public Label getText() {
		return htmlText;
	}
	
//	public HTML getText() {
//		return htmlText;
//	}

	public IconButton getDeleteButton() {
		return deleteButton;
	}

	public QuestionResourceClient getQuestionResourceClient() {
		return questionResource;
	}

	public void setQuestionResourceClient(QuestionResourceClient questionResource) {
		this.questionResource = questionResource;
	}

	public AbsolutePanel getAbsPanel() {
		return absPanel;
	}

	public void setAbsPanel(AbsolutePanel absPanel) {
		this.absPanel = absPanel;
	}

}
