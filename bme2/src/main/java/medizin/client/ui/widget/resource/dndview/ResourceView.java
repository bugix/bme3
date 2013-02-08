package medizin.client.ui.widget.resource.dndview;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.ui.widget.resource.dndview.vo.QuestionResourceClient;
import medizin.client.ui.widget.resource.dndview.vo.State;
import medizin.client.ui.widget.resource.event.ResourceAddedEvent;
import medizin.client.ui.widget.resource.event.ResourceAddedEventHandler;
import medizin.client.ui.widget.resource.event.ResourceDeletedEvent;
import medizin.client.ui.widget.resource.event.ResourceDeletedEventHandler;
import medizin.client.ui.widget.resource.event.ResourceSequenceChangedEvent;
import medizin.client.ui.widget.resource.event.ResourceSequenceChangedHandler;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceView extends Composite implements DragHandler {

	private static ResourceViewUiBinder uiBinder = GWT
			.create(ResourceViewUiBinder.class);

	interface ResourceViewUiBinder extends UiBinder<Widget, ResourceView> {
	}
	
	private List<QuestionResourceClient> questionResources;
	
	private int frontRowNumber = 0;
	
	private ResourceView impl;
	private ResourceSubView resourceSubView;

	
	@UiField
	VerticalPanel customContentPanel;
	/*@UiField
	AbsolutePanel absPanel;*/
	
	PickupDragController dragController1;
	PickupDragController dragController2;

	VerticalPanelDropController dropController1;
	private QuestionTypes questionType;

	private final EventBus eventBus;

	private final Boolean queHaveImage;
	private final Boolean queHaveSound;
	private final Boolean queHaveVideo;
	
	public VerticalPanelDropController getDropController2() {
		return dropController1;
	}
	
	public ResourceView(EventBus eventBus, List<QuestionResourceClient> questionResources,QuestionTypes questionType,Boolean queHaveImage, Boolean queHaveSound, Boolean queHaveVideo) {
		this.eventBus = eventBus;
		this.questionResources = questionResources;
		this.questionType = questionType;
		
		if(queHaveImage == null) {
			this.queHaveImage = false;
		}else {
			this.queHaveImage = queHaveImage;
		}
		
		if(queHaveVideo == null) {
			this.queHaveVideo = false;
		}else {
			this.queHaveVideo = queHaveVideo;	
		}
		
		if(queHaveSound == null) {
			this.queHaveSound = false;
		}else {
			this.queHaveSound = queHaveSound;	
		}
		
		
		initWidget(uiBinder.createAndBindUi(this));
		impl = this;
		frontRowNumber = questionResources.size();
		
		init();
	}

	private void init() {

		for (QuestionResourceClient proxy : questionResources) {	
			createResourceSubView(proxy);
		}
	}

	public void addResourceView(ResourceSubView resourceSubView, boolean isNewAdded) {

		// TODO: Create new AbsolutePanel & vertical Panel & then assign
		// customContentSubViewImpl in it.

		// AbsolutePanel absolutePanel = new AbsolutePanel();
		// VerticalPanel panel = new VerticalPanel();

		// panel.add(customContentSubViewImpl);
		// absolutePanel.add(panel);

		// absolutePanel.add(customContentSubViewImpl);

		PickupDragController dragController = new PickupDragController(resourceSubView.getAbsPanel(), false);
		dragController.setBehaviorDragProxy(true);
		dragController.addDragHandler(this);
		dragController.makeDraggable(resourceSubView.asWidget(), resourceSubView.getText());
		
		
		VerticalPanelDropController dropController = new VerticalPanelDropController(customContentPanel);
		dragController.registerDropController(dropController);
		

		Log.info("Count : " + customContentPanel.getWidgetCount());
		
//		if (customContentPanel.getWidgetCount() == 0 || !isNewAdded) {
//			customContentPanel.add(resourceSubView);
//		} else {
//			customContentPanel.insert(resourceSubView, 0);
//		}
		
		if (customContentPanel.getWidgetCount() == 0 || !isNewAdded) {
			customContentPanel.add(resourceSubView);
		} else {
			customContentPanel.insert(resourceSubView, 0);
		}

	}
	
	private void addUrl(String url,MultimediaType type,boolean added) {
		
		QuestionResourceClient proxy =  new QuestionResourceClient();
		
		if(added == true) {
			proxy.setPath(url);
			proxy.setSequenceNumber(frontRowNumber);
			proxy.setType(type);
			proxy.setState(State.NEW);
			frontRowNumber++;
			
			questionResources.add(proxy);
			createResourceSubView(proxy);
		}
			
		eventBus.fireEvent(new ResourceAddedEvent(added,proxy));
		
	
	}

	private void createResourceSubView(QuestionResourceClient proxy) {
		ResourceSubView subView = new ResourceSubView(eventBus);
		subView.setDetails(proxy, questionType, impl);
		addResourceView(subView,true);
	}

	public void addVideoUrl(String url) {
		addUrl(url, MultimediaType.Video,queHaveVideo);
	}

	public void addSoundUrl(String url) {
		addUrl(url, MultimediaType.Sound,queHaveSound);
	}

	public void addImageUrl(String url) {
		addUrl(url, MultimediaType.Image,queHaveImage);		
	}

	// drag hander methods.
	@Override
	public void onDragEnd(DragEndEvent event) {
		ResourceSubView resourceSubView;
		int size = customContentPanel.getWidgetCount();
		if (event != null) {
			Log.info("in onDragEnd" + event.getSource());

			// {
			// Log.info("Element" +
			// customContentPanel.getWidget(0).getElement());
			// Log.info("Element:child : 1 " + customContentPanel.getWidget(0));
			// Log.info("Last widget" + ((CustomContentSubView)
			// customContentPanel.getWidget(0)));
			//
			// // Log.info("Element:child : 2 " +
			// // customContentPanel.getWidget(0));
			// }

//			Log.info("!!" + ((VerticalPanel) ((ResourceSubView) event.getSource()).asWidget().getParent()));
//
//			Log.info("!!" + ((ResourceSubView) absPanel.getWidget(0).asWidget()));
		}
//		List<QuestionResourceClient> proxies = new ArrayList<QuestionResourceClient>();
		
		for (int i = 0; i < size; i++) {

			resourceSubView = (ResourceSubView) customContentPanel.getWidget(i).asWidget();
			// CustomContentProxy customContentProxy =
			QuestionResourceClient client = resourceSubView.getQuestionResourceClient();
			client.setSequenceNumber(i);
			client.setState(State.EDITED);
			if(questionResources.contains(client) == false) {
				questionResources.add(client);
			}
			Log.info("resource view : " + client);
		}
		
		for (QuestionResourceClient iterable_element : questionResources) {
			Log.info("orginal resource view : " + iterable_element);	
		}
		
		// delegate.updateCustomContent(contentSubView, customContentProxy, i +
		// 1);
		//delegate.updateCustomContent(customContentProxies);
		
	}



	@Override
	public void onDragStart(DragStartEvent event) {
		Log.info("Drag event started");
		
	}



	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		Log.info("Drag event onPreviewDragEnd");
		
	}



	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		Log.info("Drag event onPreviewDragStartw");		
	}

	public Set<QuestionResourceClient> getQuestionResources() {
		return new HashSet<QuestionResourceClient>(questionResources);
	}

	public void addResourceDeletedHandler(
			ResourceDeletedEventHandler handler) {
		eventBus.addHandler(ResourceDeletedEvent.TYPE, handler);
	}

	public void addResourceAddedHandler(
			ResourceAddedEventHandler handler) {
		eventBus.addHandler(ResourceAddedEvent.TYPE, handler);
	}

	public void addResourceSequenceChangedHandler(
			ResourceSequenceChangedHandler handler) {
		eventBus.addHandler(ResourceSequenceChangedEvent.TYPE, handler);
	}
	
}
