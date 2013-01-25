package medizin.client.ui.widget.upload;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.shared.BMEFileUploadConstant;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ResourceUpload extends Composite {

	private static ResourceUploadUiBinder uiBinder = GWT
			.create(ResourceUploadUiBinder.class);

	private final BmeConstants constants = GWT.create(BmeConstants.class);

	private final String uploadUrl = GWT.getHostPageBaseURL()
			+ "fileUploadServlet";

	private  ResourceViewer viewer;
	
	private  QuestionTypeProxy questionType;

	@UiField
	FormPanel uploadFormPanel;

	@UiField
	FileUpload fileUpload;

	@UiField
	HorizontalPanel panel;
	
	private String fileName = "";
	
	interface ResourceUploadUiBinder extends UiBinder<Widget, ResourceUpload> {
	}

	public ResourceUpload() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {

		this.uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		this.uploadFormPanel.setAction(uploadUrl);

		System.out.println("url for FileUploadServlet " + uploadUrl);

		uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				
				Log.info(fileUpload.getFilename());
				
				if (!"".equalsIgnoreCase(fileUpload.getFilename())) {
					
					Log.info("UPLOADING");
				} else {
					Log.info("UPLOADING cancel");
					event.cancel();
				}
			}
		});

		uploadFormPanel
				.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						Log.info("PS Submit is Complete " + event.getResults()); 
						
						uploadFormPanel.reset();

						//Window.alert("Upload completed");
	
						// for image
						boolean flag = false;
						
						flag = checkImgFileExtension();
						
						if(flag == true) {
							
							String url = new String(GWT.getHostPageBaseURL() + "public/images/" + fileName);
							
							if(checkImageSize(url,questionType)) {
							
								if (viewer != null) {
									Log.info(GWT.getHostPageBaseURL() + "public/images/" + fileName);
									viewer.setUrl(GWT.getHostPageBaseURL() + "public/images/" + fileName);
								}
							}else {
							
								Window.alert("Only Upload image of size" + questionType.getImageWidth() + "*" + questionType.getImageHeight());
								
								deleteImage(url.replaceAll(GWT.getHostPageBaseURL(), ""));
							}

						}else {
							Window.alert("Not a valid Extension");
						}
					}

					private boolean checkImgFileExtension() {
						boolean flag = false;
						
						String extension = getExtension(fileName);
						Log.info("Current extension : " + extension);
						for (String ext : BMEFileUploadConstant.imageExtension) {
							
							if(ext.equalsIgnoreCase(extension)) {
								flag = true;
								break;
							}
						}
						return flag;
					}

					private void deleteImage(String url) {
						// TODO Auto-generated method stub
						//Window.alert("To delete this file");
					}

					private boolean checkImageSize(final String url,
							QuestionTypeProxy questionType) {
						boolean flag = false;
						
						Image image = new Image(new SafeUri() {
							
							@Override
							public String asString() {
								return url;
							}
						});
						
						
						Log.info("Image width * height : " + image.getWidth() + "*" + image.getHeight());
						if(questionType.getImageWidth() != null && questionType.getImageWidth().equals(image.getWidth()) && questionType.getImageHeight() != null && questionType.getImageHeight().equals(image.getHeight())) {
							flag = true;
						}
						
						return flag;
					}

					private String getExtension(String fileName) {
					
						int index = fileName.indexOf(".");
						return fileName.substring(index+1);
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
			fileName = fileUpload.getFilename();
			uploadFormPanel.submit();
		} else {
			Window.alert("Error");
		}
	}

	public void setResouceViewer(ResourceViewer viewer) {
		this.viewer = viewer;
	}
	
	public void setQuestionType(QuestionTypeProxy questionType) {
		
		if(questionType.getImageWidth() != null && questionType.getImageHeight() != null) {
			this.questionType = questionType;
		}else {
			Window.alert("Specify Question type width and height");
		}
	}

}
