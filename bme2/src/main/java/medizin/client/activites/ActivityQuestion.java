package medizin.client.activites;

import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.shared.AccessRights;
import medizin.client.ui.view.question.QuestionView;
import medizin.client.ui.view.question.QuestionViewImpl;
import medizin.client.util.MathJaxs;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class ActivityQuestion extends AbstractActivityWrapper implements
		QuestionView.Presenter, QuestionView.Delegate {

	private PlaceQuestion questionPlace;

	private AcceptsOneWidget widget;
	private QuestionView view;

	private McAppRequestFactory requests;
	private PlaceController placeController;

	private ActivityManager activityManger;
	private ActivityQuestionMapper activityQuestionMapper;
	private SingleSelectionModel<QuestionProxy> selectionModel;

	private CellTable<QuestionProxy> table;

	private HandlerRegistration rangeChangeHandler;

	/*private PersonProxy userLoggedIn;

	private InstitutionProxy institutionActive;*/

	@Inject
	public ActivityQuestion(PlaceQuestion place, McAppRequestFactory requests,
			PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;

		this.activityQuestionMapper = new ActivityQuestionMapper(requests,
				placeController);
		this.activityManger = new ActivityManager(activityQuestionMapper,
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
		// ((SlidingPanel)widget).remove(view.asWidget());
		activityManger.setDisplay(null);

	}

	/*@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		super.start(widget, eventBus);

	}*/

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		// TopPanel topPanel = TopPanel.instance(requests, placeController);

		// if(topPanel.getLoggedUser().getValue()==null){
		// onStop();
		// return;
		// }
		// if(!topPanel.getLoggedUser().getValue().equals("")){
		// this.userLoggedIn = topPanel.getLoggedUser().getValue();
		// }
		// else{
		// onStop();
		// return;
		// }
		// if(topPanel.getInstitutionListBox().getValue()==null){
		// onStop();
		// return;
		// }
		// if(!topPanel.getInstitutionListBox().getValue().equals("")){
		// this.institutionActive = topPanel.getInstitutionListBox().getValue();
		// }
		// else{
		// onStop();
		// return;
		// }
		
		Boolean flag = false;
	
		if (personRightProxy.getIsAdmin())
			flag = true;
		else if (personRightProxy.getIsInstitutionalAdmin())
			flag = true;
		else
		{
			for (UserAccessRightsProxy proxy : personRightProxy.getQuestionEventAccList())
			{
				if (proxy.getAccRights().equals(AccessRights.AccAddQuestions))
				{
					flag = true;
					break;
				}
			}
		}
		
		Log.debug("start()");
		QuestionView questionView = new QuestionViewImpl(eventBus,flag);
		Log.debug("start()");
		// questionView.setName("hallo");
		questionView.setPresenter(this);
		this.widget = widget;
		Log.debug("start()");
		this.view = questionView;
		Log.debug("start()");
		widget.setWidget(questionView.asWidget());

		table = view.getTable();

		Log.debug("start2()");
		/*eventBus.addHandler(PlaceChangeEvent.TYPE,
				new PlaceChangeEvent.Handler() {
					public void onPlaceChange(PlaceChangeEvent event) {

						Place place = event.getNewPlace();
						if (place instanceof PlaceQuestionDetails) {
							init();
						}
						
						if (place instanceof PlaceQuestion) {
							init();
						}
						
					}
				});*/

		activityManger.setDisplay(view.getDetailsPanel());
		
		RecordChangeEvent.register(requests.getEventBus(), (QuestionViewImpl)questionView);

		init();
		
		/*requests.personRequest().myGetLoggedPerson()
				.fire(new BMEReceiver<PersonProxy>() {

					@Override
					public void onSuccess(PersonProxy response) {
						userLoggedIn = response;
						init();

					}

					public void onFailure(ServerFailure error) {
						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
						// onStop();
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
						// onStop();

					}

				});*/
		/*requests.institutionRequest().myGetInstitutionToWorkWith()
				.fire(new BMEReceiver<InstitutionProxy>() {

					@Override
					public void onSuccess(InstitutionProxy response) {
						institutionActive = response;
						init();

					}

					public void onFailure(ServerFailure error) {
						ErrorPanel erorPanel = new ErrorPanel();
						erorPanel.setErrorMessage(error.getMessage());
						Log.error(error.getMessage());
						// onStop();
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
						// onStop();

					}

				});*/
		// Inherit the view's key provider
		ProvidesKey<QuestionProxy> keyProvider = ((AbstractHasData<QuestionProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<QuestionProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						QuestionProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getQuestionText()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);

	}

	protected void showDetails(QuestionProxy question) {
		Log.debug("Question Stable id: " + question.stableId() + " "
				+ PlaceQuestionDetails.Operation.DETAILS);
		placeController.goTo(new PlaceQuestionDetails(question.stableId()));
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	private void init() {

		if (institutionActive == null || userLoggedIn == null) {
			// onStop();
			return;
		} else {
			Document.get()
					.getElementById("userLoggedIn")
					.setInnerHTML(
							"Eingeloggt als: " + userLoggedIn.getName() + " "
									+ userLoggedIn.getPrename());
			Document.get()
					.getElementById("institutionActive")
					.setInnerHTML(
							"Institution: "
									+ institutionActive.getInstitutionName());

		}

		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
			rangeChangeHandler = null;
		}
	
		requests.questionRequest()
				.countQuestionsByPerson(this.userLoggedIn.getShidId(),
						this.institutionActive.getId(), view.getSerachBox().getValue(), view.getSearchValue())
				.fire(new BMEReceiver<Long>() {
					@Override
					public void onSuccess(Long response) {
						if (view == null) {
							// This activity is dead
							return;
						}
						Log.debug("Geholte Questions (Pr�fungen) aus der Datenbank: "
								+ response);
						view.getTable().setRowCount(response.intValue(), true);
						onRangeChanged();
					}
					
					/*@Override
					public void onFailure(ServerFailure error) {
						System.out.println("ERROR : " + error.getMessage());
					}
					
					@Override
					public void onViolation(Set<Violation> errors) {
						System.out.println("ERRORS SIZE : " + errors.size());
					}*/
				});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ActivityQuestion.this.onRangeChanged();
					}
				});
	/*requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

			@Override
			public void onSuccess(List<InstitutionProxy> response) {
				view.setInstitutionFilter(response);
			}
		});*/
		
		requests.questionEventRequest().findQuestionEventByInstitution(this.institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {

			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				view.setSpecialisationFilter(response);
			}
		});
	}

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		requests.questionRequest()
				.findQuestionEntriesByPerson(this.userLoggedIn.getShidId(),
						this.institutionActive.getId(), view.getSerachBox().getValue(), view.getSearchValue(), range.getStart(),
						range.getLength(),false,"","","").with(view.getPaths())
				.fire(new BMEReceiver<List<QuestionProxy>>() {
					@Override
					public void onSuccess(List<QuestionProxy> values) {
						if (view == null) {
							// This activity is dead
							Log.debug("view ist null");
							return;
						}

						table.setRowData(range.getStart(), values);

						if (widget != null) {
							widget.setWidget(view.asWidget());
						}
						MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
					}
				});

	}

	@Override
	public void newClicked() {
		placeController.goTo(new PlaceQuestionDetails(
				PlaceQuestionDetails.Operation.CREATE));

	}

	@Override
	public void performSearch(String searchText) {
		requests.questionRequest()
		.countQuestionsByPerson(this.userLoggedIn.getShidId(),
				this.institutionActive.getId(), view.getSerachBox().getValue(), view.getSearchValue())
		.fire(new BMEReceiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Questions (Pr�fungen) aus der Datenbank: "
						+ response);
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
			
		/*	@Override
			public void onFailure(ServerFailure error) {
				System.out.println("ERROR : " + error.getMessage());
			}
			
			@Override
			public void onViolation(Set<Violation> errors) {
				System.out.println("ERRORS SIZE : " + errors.size());
			}*/
		});
	}

	@Override
	public void placeChanged(Place place) {
		/*if (place instanceof PlaceQuestionDetails) {
			init();
		}*/
		
		if (place instanceof PlaceQuestion) {
			init();
		}
	}

}
