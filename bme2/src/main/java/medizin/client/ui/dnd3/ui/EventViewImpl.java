package medizin.client.ui.dnd3.ui;

import java.util.List;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.ui.widget.process.ApplicationLoadingView;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

/**
 * A draggable event with a QuestionEventProxy.
 * @author masterthesis
 *
 */

public class EventViewImpl extends VerticalPanel implements EventView {


	private QuestionEventProxy eventProxy; 
	private Label headerNamelbl = new Label();
	private VerticalPanel questionsContainer = new VerticalPanel();
	
	private AbsolutePanel questionEventContent = new AbsolutePanel();
	
	private EventViewImpl eventView;
		
	public EventViewImpl getEventView() {
		return eventView;
	}

	public void setEventView(EventViewImpl eventView) {
		this.eventView = eventView;
	}
	private List<Long> questionTypesId;

	//Getters and Setters
	
	public List<Long> getQuestionTypesId() {
		return questionTypesId;
	}
	
	public void setQuestionTypesId(List<Long> questionTypesId) {
		this.questionTypesId = questionTypesId;
	}
	
	public AbsolutePanel getQuestionEventContent() {
		return questionEventContent;
	}

	public VerticalPanel getQuestionsContainer() {
		return questionsContainer;
	}

	public void setQuestionsContainer(VerticalPanel questionsContainer) {
		this.questionsContainer = questionsContainer;
	}

	public void setQuestionEventContent(AbsolutePanel questionEventContent) {
		this.questionEventContent = questionEventContent;
	}

	public Label getHeaderNamelbl() {
		return headerNamelbl;
	}

	public void setHeaderNamelbl(Label headerNamelbl) {
		this.headerNamelbl = headerNamelbl;
	}
	private VerticalPanel verticalPanel;
	private Integer orderAversion;

	public EventViewImpl() {
		this.eventView = this;		
	}

	public QuestionEventProxy getEventProxy() {
		return eventProxy;
	}

	@Override
	public void  setEventProxy(QuestionEventProxy eventProxy)
	 {
		this.eventProxy = eventProxy;		
	}

	private Delegate delegate;
	
	
	@Override
	public void setDelegate(Delegate delegate) {
	
		this.delegate = delegate;
	}
	@Override
	public void setVerticalPanel(VerticalPanel vertPanel){
		this.verticalPanel = vertPanel;
	}

	ApplicationLoadingView loadingView;
	public ApplicationLoadingView getLoadingView() {
		return loadingView;
	}

	public void setLoadingView(ApplicationLoadingView loadingView) {
		this.loadingView = loadingView;
	}

	public void init(){
		
		addStyleName("applicationLoadingPopupViewStyle");
		loadingView = new ApplicationLoadingView();
		loadingView.setVisible(false);
		add(loadingView);
		/**
		 *Set up buttons and clickable labels
		 */
		questionEventContent.setStyleName("questionEventContent");
		
		final Label twistieOpenQuestionEvent = new Label();
		twistieOpenQuestionEvent.addStyleName("ui-icon ui-icon-triangle-1-e");

		final Label twistieCloseQuestionEvent = new Label();
		twistieCloseQuestionEvent.addStyleName("ui-icon ui-icon-triangle-1-s");

		
		
		final Label tagLabelQuestionEvent = new Label();
		tagLabelQuestionEvent.addStyleName("ui-icon ui-icon-tag");
		
		
		AbsolutePanel eventHeader = new AbsolutePanel();

		eventHeader.setPixelSize(1140, 25);
		eventHeader.setStyleName("eventHeader");
		
		headerNamelbl.setText(eventProxy.getEventName());
		headerNamelbl.setWidth("910px");
		
		eventHeader.add(twistieOpenQuestionEvent, 2,8);
		twistieCloseQuestionEvent.setVisible(false);
		eventHeader.add(twistieCloseQuestionEvent,2,8);
		
		eventHeader.add(headerNamelbl,19,5);
		
		//eventHeader.add(tagLabelQuestionEvent,740,3);
		
		questionEventContent.setVisible(false);
		
		

		this.add(eventHeader);
		this.add(questionEventContent);
		questionEventContent.add(questionsContainer);

		twistieOpenQuestionEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Log.debug("Twistie Themenblock anzeigen geklickt!");
				twistieOpenQuestionEvent.setVisible(false);
				twistieCloseQuestionEvent.setVisible(true);

				tagLabelQuestionEvent.setVisible(false);
				questionEventContent.setVisible(true);					
				delegate.openEventContainer(eventView.getEventProxy(), eventView);
				
			}

		});

		twistieCloseQuestionEvent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Log.debug("Twistie Themenblock schliessen geklickt!");
				twistieOpenQuestionEvent.setVisible(true);
				twistieCloseQuestionEvent.setVisible(false);
				tagLabelQuestionEvent.setVisible(true);
				questionEventContent.setVisible(false);
			}

		});
	}


}
