package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceAcceptQuestion;
import medizin.client.place.PlaceAcceptQuestionDetails;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.AcceptQuestionView;
import medizin.client.ui.view.AcceptQuestionViewImpl;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.process.AppLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityAcceptQuestion extends AbstractActivityWrapper implements AcceptQuestionView.Presenter,AcceptQuestionView.Delegate {

	private PlaceAcceptQuestion questionPlace;

	private AcceptsOneWidget widget;
	private AcceptQuestionView view;


	private McAppRequestFactory requests;
	private PlaceController placeController;

	//protected PersonProxy loggedUser;

	private SingleSelectionModel<QuestionProxy> selectionModel;
	private ActivityManager activityManger;
	private ActivityAcceptQuestionMapper activityAcceptQuestionMapper;
	
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "id";
	
	@Inject
	public ActivityAcceptQuestion(PlaceAcceptQuestion place,
			McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;	
		this.requests = requests;
        this.placeController = placeController;
        
        this.activityAcceptQuestionMapper = new ActivityAcceptQuestionMapper(requests,
				placeController);
		this.activityManger = new ActivityManager(activityAcceptQuestionMapper,
				requests.getEventBus());
	}

	@Override
	public String mayStop() {
		
		return null;
	}

	@Override
	public void onCancel() {
		onStop();

	}

	@Override
	public void onStop() {
//		((SlidingPanel)widget).remove(view.asWidget());


	}
	
	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		AcceptQuestionView acceptQuestionView = new AcceptQuestionViewImpl();
		
		acceptQuestionView.setPresenter(this);
		//acceptQuestionView.setDelegate(this);
		this.widget = widget;
		this.view = acceptQuestionView;
        widget.setWidget(acceptQuestionView.asWidget());
        
        table=view.getTable();
        acceptQuestionView.setDelegate(this);
        /*eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {

						Place place = event.getNewPlace();
						if (place instanceof PlaceQuestionDetails) {
							init();
						}
						if(place instanceof PlaceAcceptQuestion) {
							init();
						}
					}
				});*/

		activityManger.setDisplay(view.getDetailsPanel());
        
        ProvidesKey<QuestionProxy> keyProvider = ((AbstractHasData<QuestionProxy>) table).getKeyProvider();
        selectionModel = new SingleSelectionModel<QuestionProxy>(keyProvider);
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
        	
			public void onSelectionChange(SelectionChangeEvent event) {
			
				QuestionProxy selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					Log.debug(selectedObject.getQuestionText() + " selected!");
					showDetails(selectedObject);
				}
			}
		});

        RecordChangeEvent.register(requests.getEventBus(), (AcceptQuestionViewImpl)view);
        
      //adding column mouse out of table.
      	((AcceptQuestionViewImpl)view).addColumnOnMouseout();
      		
        init();
        
    	/*requests.personRequest().myGetLoggedPerson()
		.fire(new Receiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				loggedUser = response;
				init();

			}

			public void onFailure(ServerFailure error) {
				ErrorPanel erorPanel = new ErrorPanel();
				erorPanel.setErrorMessage(error.getMessage());
				Log.error(error.getMessage());
				onStop();
			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(McAppConstant.ERROR_WHILE_DELETE_VIOLATION
						+ " in Antwort löschen -" + message);

				ErrorPanel erorPanel = new ErrorPanel();
				erorPanel.setErrorMessage(message);
				onStop();

			}

		});*/


	}

	private void showDetails(QuestionProxy question) {
		Log.debug("Question Stable id: " + question.stableId() + " " + PlaceQuestionDetails.Operation.DETAILS);
		placeController.goTo(new PlaceAcceptQuestionDetails(question.stableId(),view.getScrollDetailPanel().getOffsetHeight()));
	}
	

	@Override
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
	
	AbstractHasData<QuestionProxy> table;

	private HandlerRegistration rangeChangeHandler;

	private EntityProxyId<?> proxyId;

	private void init() {
		
		if (rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler=null;
		}
		AppLoader.setNoLoader();
		requests.questionRequest().countQuestionsNonAcceptedAdmin().fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Questions (Pr�fungen) aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
		});
		
		
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityAcceptQuestion.this.onRangeChanged();
			}
		});
	}   

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();
		
		AppLoader.setNoLoader();
		requests.questionRequest().findQuestionsEntriesNonAcceptedAdmin(sortname,sortorder,range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<QuestionProxy>>() {
			@Override
			public void onSuccess(List<QuestionProxy> values) {
				if (view == null) {
					Log.debug("view ist null");
					return;
				}

				table.setRowData(range.getStart(), values);
				
				selectRow(range);

				if (widget != null) {
			          widget.setWidget(view.asWidget());
					}
				}
		});
	}
	
	private void selectRow(final Range range) {
		if (proxyId != null)
		{
			requests.find(proxyId).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response != null && response instanceof QuestionProxy)
					{
						QuestionProxy selectedProxy = (QuestionProxy) response;
						selectionModel.setSelected(selectedProxy, true);
						int start = table.getRowCount() - range.getLength();
						table.setPageStart((start < 0 ? 0 : start));
					}
					proxyId = null;
				}
			});					
		}
	}
	
	@Override
	public void placeChanged(Place place) {
		
		if (place instanceof PlaceAcceptQuestionDetails) {
			if (((PlaceAcceptQuestionDetails)place).getProxyId() != null)
				proxyId = ((PlaceAcceptQuestionDetails)place).getProxyId();
		}
		
		if (place instanceof PlaceAcceptQuestion) {
			init();
		}
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		this.sortname=sortname;
		this.sortorder=sortorder;
		init();
		
	}

}
