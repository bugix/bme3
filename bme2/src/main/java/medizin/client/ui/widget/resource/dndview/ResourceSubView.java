package medizin.client.ui.widget.resource.dndview;

import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.utils.SharedConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceSubView extends Composite {

	private static ResourceSubViewUiBinder uiBinder = GWT
			.create(ResourceSubViewUiBinder.class);

	private QuestionResourceClient questionResource;

	private ResourceView resourceView;

	private ResourceSubView resourceSubView;

	@UiField
	AbsolutePanel absPanel;

	@UiField
	Label htmlText;

	@UiField
	HTMLPanel hPanel;

	@UiField
	IconButton deleteButton;

	@UiField
	IconButton viewButton;

	private String imageDim = "12px";
	private String textDim = "275px";

	private QuestionTypes questionType;

	interface ResourceSubViewUiBinder extends UiBinder<Widget, ResourceSubView> {
	}

	public ResourceSubView() {
		initWidget(uiBinder.createAndBindUi(this));
		resourceSubView = this;
	}

	public void setDetails(QuestionResourceClient questionResource,QuestionTypes questionType,
			ResourceView resourceView) {
		this.questionResource = questionResource;
		this.resourceView = resourceView;
		this.questionType = questionType;
		init();
	}

	public void init() {

//		htmlText.setHTML(getName(questionResource.getPath(),
//				questionResource.getType()));
//		htmlText.setHeight(imageDim);
//		htmlText.setWidth(textDim);
//		htmlText.setWordWrap(true);
//
//		htmlText.setVisible(true);
		
		htmlText.setText(getName(questionResource.getPath(), questionResource.getType()));
		htmlText.setHeight(imageDim);
		htmlText.setWidth(textDim);

		switch (questionResource.getType()) {
		case Image: {
			viewButton.setIcon("image");
			viewButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ImageViewer viewer = new ImageViewer();
					viewer.setUrl(questionResource.getPath(), questionType);
					DialogBox dialogBox = createImageDialogBox("Image Viewer",viewer);
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
//					ImageViewer viewer = new ImageViewer();
//					viewer.setUrl(questionResource.getPath(), questionType);
//					DialogBox dialogBox = createImageDialogBox("Image Viewer",viewer);
//					dialogBox.center();
//		            dialogBox.show();
				}
			});
			break;
		}
		case Video: {
			viewButton.setIcon("video");
			viewButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					VideoViewer viewer = new VideoViewer();
					viewer.setVideoMediaContent(questionResource.getPath());
					DialogBox dialogBox = createImageDialogBox("Video Viewer",viewer);
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
	 * 
	 * @return the new dialog box
	 */
	private DialogBox createImageDialogBox(String title,Widget widget) {
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
		Button closeButton = new Button("Closed");
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});
		
		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Return the dialog box
		return dialogBox;
	}

	private String getName(String path, MultimediaType multimediaType) {
		String fileName = "";

		switch (multimediaType) {
		case Image: {
			fileName = path.replace(
					SharedConstant.UPLOAD_QUESTION_IMAGES_PATH, "");
			break;
		}
		case Sound: {
			fileName = path.replace(
					SharedConstant.UPLOAD_QUESTION_SOUND_PATH, "");
			break;
		}
		case Video: {
			fileName = path.replace(
					SharedConstant.UPLOAD_QUESTION_VIDEO_PATH, "");
			break;
		}
		default:
			break;
		}

		return fileName;
	}

	@UiHandler("deleteButton")
	public void deleteButtonClicked(ClickEvent e) {

		Log.info("Delete button clicked");
		// final VisitorDialogBox dialogBox = new
		// VisitorDialogBox(constants.delete());
		//
		// dialogBox.getYesButton().addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent arg0) {
		// customContentView.setSelectedView(contentSubViewImpl);
		// delegate.deleteCustomContentDetail(customContentProxy,
		// contentSubViewImpl);
		// customContentView.onDragEnd(null);
		// dialogBox.hide();
		// }
		// });
		// dialogBox.getNoButton().addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// dialogBox.hide();
		// }
		// });
		//
		// dialogBox.showYesNoDialog(constants.reallyDelete());

	}

	public ResourceView getResourceView() {
		return resourceView;
	}

	public HTMLPanel getPanel() {
		return hPanel;
	}

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
