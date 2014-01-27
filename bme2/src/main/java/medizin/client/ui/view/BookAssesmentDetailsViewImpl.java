package medizin.client.ui.view;

import java.util.HashSet;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.FileDownloaderProps;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class BookAssesmentDetailsViewImpl extends Composite implements BookAssesmentDetailsView  {

	private static BookAssesmentDetailsViewImplUiBinder uiBinder = GWT.create(BookAssesmentDetailsViewImplUiBinder.class);

	interface BookAssesmentDetailsViewImplUiBinder extends UiBinder<Widget, BookAssesmentDetailsViewImpl> {
	}

	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private Presenter presenter;

	/*private McAppRequestFactory requests;
	private PlaceController placeController;*/

	@Inject
	public BookAssesmentDetailsViewImpl(McAppRequestFactory requests, PlaceController placeController) {
       /* this.requests = requests;
        this.placeController = placeController;*/
		initWidget(uiBinder.createAndBindUi(this));

	}


	@Override
	public void setName(String helloName) {
		// todo Auto-generated method stub
		
	}



	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	

	@UiField
	AbsolutePanel content;
	
	@UiField
	ScrollPanel scrollContainer;
	
	

	protected Set<String> paths = new HashSet<String>();
	/*private Integer tabId=0;
	private HashMap<Integer, EntityProxyId<?>> idMap = new HashMap<Integer, EntityProxyId<?>>();*/
	

	@Override
	public void reload(AssesmentProxy assesmentProxy){
		presenter.goTo(new PlaceBookAssesmentDetails(assesmentProxy.stableId())); 
	}


	@Override
	public AbsolutePanel getWorkingArea() {
		
		return content;
	}
	@Override
	public ScrollPanel getScrollContainer(){
		return scrollContainer;
	}
	/*@UiField
	Button printAButton;*/

	private Delegate delegate;

	private CheckBox disallowSortingChk;
	
	@Override
	public
	void setDelegate(Delegate delegate){
		this.delegate = delegate;
	}
	
	/*@UiHandler ("printAButton")
	void printAButtonClicked(ClickEvent event){
		
		String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.DOCX_PAPER.ordinal()));          
		String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
				.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""));
		Log.info("--> url is : " +url);
		
		Window.open(url, "", "");
		//delegate.createAssementBook(true);
	}
*/

	@Override
	public void addButtons(final Boolean disallowSorting) {
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		if(disallowSorting == null || disallowSorting == false) {
			disallowSortingChk = new CheckBox(constants.disallowSorting());
			horizontalPanel.add(disallowSortingChk);
			
			IconButton shuffle = new IconButton(constants.shuffle());
			shuffle.setIcon("shuffle");
			shuffle.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					delegate.shuffleAssementQuestionsAnswers(getDisallowSorting(disallowSorting,disallowSortingChk));
				}
			});
			horizontalPanel.add(shuffle);
		}else {
			disallowSortingChk = null;
		}
		
		IconButton printAVersion = new IconButton(constants.printAVersion());
		printAVersion.setIcon("print");
		printAVersion.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.DOCX_PAPER.ordinal()));          
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
						.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""))
						.concat("&").concat(FileDownloaderProps.VERSION).concat("=").concat(FileDownloaderProps.A_VERSION)
						.concat("&").concat(FileDownloaderProps.DISALLOW_SORTING).concat("=").concat(URL.encodeQueryString(getDisallowSorting(disallowSorting,disallowSortingChk) + ""));
				
				Log.info("--> url is : " +url);
				Window.open(url, "", "");
				
			}
		});
		
		IconButton printBVersion = new IconButton(constants.printBVersion());
		printBVersion.setIcon("print");
		printBVersion.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.DOCX_PAPER.ordinal()));          
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
						.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""))
						.concat("&").concat(FileDownloaderProps.VERSION).concat("=").concat(FileDownloaderProps.B_VERSION)
						.concat("&").concat(FileDownloaderProps.DISALLOW_SORTING).concat("=").concat(URL.encodeQueryString(getDisallowSorting(disallowSorting,disallowSortingChk) + ""));
				
				Log.info("--> url is : " +url);
				Window.open(url, "", "");
				
			}
		});
		
		IconButton exportXML = new IconButton(constants.exportXML());
		exportXML.setIcon("disk");
		exportXML.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.XML_PAPER.ordinal()));          
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
						.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""))
						.concat("&").concat(FileDownloaderProps.DISALLOW_SORTING).concat("=").concat(URL.encodeQueryString(getDisallowSorting(disallowSorting,disallowSortingChk) + ""));
				Log.info("--> url is : " +url);
				
				Window.open(url, "", "");
				
			}
		});
		
		IconButton printWithNonAcceptedQuestions= new IconButton(constants.printWithNonAcceptedQuestions());
		printWithNonAcceptedQuestions.setIcon("print");
		printWithNonAcceptedQuestions.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.DOCX_PAPER_ALL.ordinal()));          
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
						.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""))
						.concat("&").concat(FileDownloaderProps.DISALLOW_SORTING).concat("=").concat(URL.encodeQueryString(getDisallowSorting(disallowSorting,disallowSortingChk) + ""));
				Log.info("--> url is : " +url);
				
				Window.open(url, "", "");
				
			}
		});
		
		IconButton solutionKey = new IconButton(constants.solutionKey());
		solutionKey.setIcon("disk");
		solutionKey.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.SOLUTION_KEY.ordinal()));          
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
						.concat("&").concat(FileDownloaderProps.ASSIGNMENT).concat("=").concat(URL.encodeQueryString(delegate.getAssignemtId() + ""))
						.concat("&").concat(FileDownloaderProps.DISALLOW_SORTING).concat("=").concat(URL.encodeQueryString(getDisallowSorting(disallowSorting,disallowSortingChk) + ""));
				Log.info("--> url is : " +url);
				
				Window.open(url, "", "");
			}
		});
		
		IconButton save = new IconButton(constants.save());
		save.setIcon("disk");
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.saveAllAssesmentQuestionChanges();
			}
		});
		horizontalPanel.setStyleName("bookingAssesmentDetailViewHP");
		horizontalPanel.add(printAVersion);
		horizontalPanel.add(printBVersion);
		horizontalPanel.add(exportXML);
		horizontalPanel.add(printWithNonAcceptedQuestions);
		horizontalPanel.add(solutionKey);
		horizontalPanel.add(save);
		horizontalPanel.setSpacing(5);
		horizontalPanel.getElement().getStyle().setPaddingTop(10, Unit.PX);
		content.add(horizontalPanel);
	}
	
	private Boolean getDisallowSorting(Boolean disallowSorting, CheckBox disallowSortingChk) {
		
		if(disallowSorting != null && disallowSorting == true) {
			return disallowSorting;
		}
		
		if(disallowSortingChk != null) {
			return disallowSortingChk.getValue();
		}
		
		return false;
	}


	@Override
	public Boolean getDisallowSorting(Boolean disallowSorting) {
		return getDisallowSorting(disallowSorting,disallowSortingChk);
	}

}
