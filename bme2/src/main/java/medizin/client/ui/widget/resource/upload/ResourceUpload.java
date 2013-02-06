package medizin.client.ui.widget.resource.upload;

import java.util.ArrayList;

import medizin.client.ui.widget.resource.upload.event.ResourceUploadEvent;
import medizin.client.ui.widget.resource.upload.event.ResourceUploadEventHandler;
import medizin.shared.utils.SharedUtility;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceUpload extends Composite {

	private static ResourceUploadUiBinder uiBinder = GWT
			.create(ResourceUploadUiBinder.class);

	//private final BmeConstants constants = GWT.create(BmeConstants.class);

	private final String uploadUrl = GWT.getHostPageBaseURL()
			+ "fileUploadServlet";
	
	private final ArrayList<String> allowedExtension;

	@UiField
	FormPanel uploadFormPanel;

	@UiField
	FileUpload fileUpload;

	@UiField
	HorizontalPanel panel;
	
	private final EventBus eventBus;
	
	interface ResourceUploadUiBinder extends UiBinder<Widget, ResourceUpload> {
	}

	public ResourceUpload(ArrayList<String> allowedExtension,EventBus eventBus) {
		this.allowedExtension = allowedExtension;
		this.eventBus = eventBus;
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {

		this.uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		this.uploadFormPanel.setAction(uploadUrl);

		Log.info("url for FileUploadServlet " + uploadUrl);

		uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				
				Log.info(fileUpload.getFilename());
				Log.info("PS on Submit" + event); 
				if (!"".equalsIgnoreCase(fileUpload.getFilename())) {
					
					Log.info("UPLOADING");
				} else {
					Log.info("UPLOADING cancel");
					event.cancel();
					eventBus.fireEvent(new ResourceUploadEvent("",false));
				}
			}
		});

		uploadFormPanel
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						Log.info("PS Submit is Complete " + event.getResults()); 
						
						uploadFormPanel.reset();

						eventBus.fireEvent(new ResourceUploadEvent(event.getResults(),true));					
					}
				});

		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Log.info("in on change");
				Log.info(fileUpload.getFilename());
				uploadFile();
			}

		});
		
	}

	public void uploadFile() {

		if (fileUpload.getFilename() != null
				&& fileUpload.getFilename().trim().compareTo("") != 0) {
			String fileName = fileUpload.getFilename();
			
			if(checkFileExtension(fileName)) {
				uploadFormPanel.submit();
			}else {
				Window.alert("Please upload valid file");
			}
			
		} else {
			Window.alert("Error in upload file method");
		}
	}

	private boolean checkFileExtension(String fileName) {
		
		boolean flag = false;
		
		if(allowedExtension != null && allowedExtension.size() > 0 ) {
			String ext = SharedUtility.getFileExtension(fileName);
			if(ext != null && ext.length() > 0) {
				for (String extension : allowedExtension) {	
					if(ext.equalsIgnoreCase(extension)) {
						flag = true;
						break;
					}
				}	
			}
		}
		
		return flag;
	}

	/*public void setQuestionType(QuestionTypeProxy questionType) {
		
		// for image key , MCQ ,area question types 
		if(questionType.getImageWidth() != null && questionType.getImageHeight() != null) {
			this.questionType = questionType;
		}
		// for textual question types
		else if(QuestionTypes.Textual.equals(questionType.getQuestionType())) {
			this.questionType = questionType;
		}
		else {
			Window.alert("Specify Question type width and height");
		}
	}*/
	
	public void addResourceUploadedHandler(ResourceUploadEventHandler handler) {
		eventBus.addHandler(ResourceUploadEvent.TYPE, handler);
	}

}
