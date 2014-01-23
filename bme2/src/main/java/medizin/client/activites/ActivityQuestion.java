package medizin.client.activites;

import java.util.ArrayList;
import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestion;
import medizin.client.place.PlaceQuestionDetails;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.QuestionPrintFilterViewImpl;
import medizin.client.ui.view.question.QuestionView;
import medizin.client.ui.view.question.QuestionViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchAbstractPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchDatePopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchKeywordPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchMCPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchMediaAvailabilityPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchPopupView;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchQuestionEventPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchQuestionTypePopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubView;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchTextSearchPopupViewImpl;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchUserTypePopupViewImpl;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.process.AppLoader;
import medizin.client.util.MathJaxs;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.AdvancedSearchCriteriaUtils;
import medizin.shared.utils.FileDownloaderProps;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityQuestion extends AbstractActivityWrapper implements QuestionView.Delegate, QuestionAdvancedSearchSubView.Presenter, QuestionAdvancedSearchSubView.Delegate, QuestionAdvancedSearchPopupView.Delegate{

	private PlaceQuestion questionPlace;

	private AcceptsOneWidget widget;
	private QuestionView view;

	private McAppRequestFactory requests;
	private PlaceController placeController;

	private ActivityManager activityManger;
	private ActivityQuestionMapper activityQuestionMapper;
	private SingleSelectionModel<QuestionProxy> selectionModel;

	private AdvanceCellTable<QuestionProxy> table;

	private HandlerRegistration rangeChangeHandler;

	/*private PersonProxy userLoggedIn;

	private InstitutionProxy institutionActive;*/
	
	private QuestionAdvancedSearchSubViewImpl advancedSearchSubViewImpl;
	
	private List<AdvancedSearchCriteria> advancedSearchCriteriaList = new ArrayList<AdvancedSearchCriteria>();
	
	private CellTable<AdvancedSearchCriteria> criteriaTable;
	
	private QuestionAdvancedSearchAbstractPopupViewImpl advancedSearchAbstractPopupViewImpl;
		
	private EntityProxyId<?> proxyId = null;

	private Sorting sortorder = Sorting.ASC;
	private String sortname = "id";
	
	@Inject
	public ActivityQuestion(PlaceQuestion place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionPlace = place;
		this.requests = requests;
		this.placeController = placeController;
		this.activityQuestionMapper = new ActivityQuestionMapper(requests,placeController);
		this.activityManger = new ActivityManager(activityQuestionMapper,requests.getEventBus());
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
		
		Log.debug("in Ativity Question");
		
		QuestionView questionView = new QuestionViewImpl(eventBus,hasQuestionAddRights(), true);
		//questionView.setPresenter(this);
		this.widget = widget;
		this.view = questionView;
		widget.setWidget(questionView.asWidget());

		//Setting splitlayout panel width from Cookie
		setWidthOfWidget();
		
		table = view.getTable();
		advancedSearchSubViewImpl = view.getQuestionAdvancedSearchSubViewImpl();
		advancedSearchSubViewImpl.setDelegate(this);
		criteriaTable = advancedSearchSubViewImpl.getTable();
		criteriaTable.setRowCount(advancedSearchCriteriaList.size());
		criteriaTable.setRowData(advancedSearchCriteriaList);
		activityManger.setDisplay(view.getDetailsPanel());
		
		RecordChangeEvent.register(requests.getEventBus(), (QuestionViewImpl)questionView);

		//adding column mouse out on table.
		((QuestionViewImpl)view).addColumnOnMouseout();
			
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				ActivityQuestion.this.onRangeChanged();
			}
		});
	
/*		requests.questionEventRequest().findQuestionEventByInstitution(this.institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {
		
			@Override
			public void onSuccess(List<QuestionEventProxy> response) {
				view.setSpecialisationFilter(response);
			}
		});*/
		
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
	private void setWidthOfWidget() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.QUESTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	((QuestionViewImpl)view).getSplitLayoutPanel().setWidgetSize(((QuestionViewImpl)view).getScrollpanel(),Double.valueOf(widgetWidthFromCookie));	
        }
		
	}

	protected void showDetails(QuestionProxy question) {
		Log.debug("Question Stable id: " + question.stableId() + " "
				+ PlaceQuestionDetails.Operation.DETAILS);
		placeController.goTo(new PlaceQuestionDetails(question.stableId(),view.getScrollDetailPanel().getOffsetHeight()));
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	private void init() {

		/*if (institutionActive == null || userLoggedIn == null) {
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
					
					@Override
					public void onFailure(ServerFailure error) {
						System.out.println("ERROR : " + error.getMessage());
					}
					
					@Override
					public void onViolation(Set<Violation> errors) {
						System.out.println("ERRORS SIZE : " + errors.size());
					}
				});*/
		
		List<String> encodedStringList = new ArrayList<String>();
		encodedStringList = AdvancedSearchCriteriaUtils.encodeList(advancedSearchCriteriaList);
		AppLoader.setNoLoader();
		requests.questionRequest().countQuestionByAdvancedSearchByLoginUserAndInstitute(encodedStringList, view.getSearchValue(), view.getSerachBox().getValue()).fire(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				if (view == null) {
					return;
				}
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged();
			}
		});
	}

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		/*requests.questionRequest()
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
				});*/
		
		List<String> encodedStringList = new ArrayList<String>();
		encodedStringList = AdvancedSearchCriteriaUtils.encodeList(advancedSearchCriteriaList);
		AppLoader.setNoLoader();
		requests.questionRequest().findQuestionByAdvancedSearchByLoginUserAndInstitute(sortname,sortorder,encodedStringList, view.getSearchValue(), view.getSerachBox().getValue(), range.getStart(), range.getLength()).with(view.getPaths()).with("autor").fire(new BMEReceiver<List<QuestionProxy>>() {

			@Override
			public void onSuccess(List<QuestionProxy> response) {
				if (view == null) {
					return;
				}

				table.setRowData(range.getStart(), response);
				
				selectRow(range);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
				
				MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
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
	public void newClicked() {
		placeController.goTo(new PlaceQuestionDetails(
				PlaceQuestionDetails.Operation.CREATE, view.getScrollDetailPanel().getOffsetHeight()));

	}

	@Override
	public void performSearch(String searchText) {
		/*requests.questionRequest()
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
			
			@Override
			public void onFailure(ServerFailure error) {
				System.out.println("ERROR : " + error.getMessage());
			}
			
			@Override
			public void onViolation(Set<Violation> errors) {
				System.out.println("ERRORS SIZE : " + errors.size());
			}
		});*/
		
		init();
	}

	@Override
	public void placeChanged(Place place) {
		if (place instanceof PlaceQuestionDetails) {
			if (((PlaceQuestionDetails)place).getProxyId() != null)
				proxyId = ((PlaceQuestionDetails)place).getProxyId();			
		}
		
		if (place instanceof PlaceQuestion) {
			Log.info("in place changed");
			if(((PlaceQuestion) place).getProxyId() != null) {
				proxyId = ((PlaceQuestion) place).getProxyId();	
			}
			
			init();
		}
	}

	@Override
	public void keywordAddClicked(final IconButton addKeyword) {		
		// Added this to show key word by name as ASC. 
		requests.keywordRequest().findAllKeywordsByNameASC().fire(new BMEReceiver<List<KeywordProxy>>() {
		//requests.keywordRequest().findAllKeywords().fire(new BMEReceiver<List<KeywordProxy>>() {

			@Override
			public void onSuccess(List<KeywordProxy> response) {
				QuestionAdvancedSearchKeywordPopupViewImpl keywordPopupView = new QuestionAdvancedSearchKeywordPopupViewImpl();
				hidePreviousPopup(keywordPopupView);
				keywordPopupView.setDelegate(ActivityQuestion.this);
				keywordPopupView.setKeywordSuggsetBoxValue(response);
				keywordPopupView.display(addKeyword);
			}
		});		
	}

	private void hidePreviousPopup(QuestionAdvancedSearchAbstractPopupViewImpl popupView) {
		if (advancedSearchAbstractPopupViewImpl != null && advancedSearchAbstractPopupViewImpl.isShowing())
			advancedSearchAbstractPopupViewImpl.hide();
		
		advancedSearchAbstractPopupViewImpl = popupView;
	}

	@Override
	public void advancedSearchCriteriaClicked(AdvancedSearchCriteria criteria) {
		advancedSearchCriteriaList.add(criteria);
		initSearch();
	}
	
	@Override
	public void deleteAdvancedSearchCriteriaClicked(AdvancedSearchCriteria object) {
		advancedSearchCriteriaList.remove(object);
		initSearch();
	}
	
	public void initSearch()
	{
		criteriaTable.setRowCount(advancedSearchCriteriaList.size());
		criteriaTable.setRowData(advancedSearchCriteriaList);
		init();
	}

	@Override
	public void questionEventAddClicked(final IconButton addKeyword) {
		if (institutionActive != null)
		{
			requests.questionEventRequest().findQuestionEventByInstitution(institutionActive).fire(new BMEReceiver<List<QuestionEventProxy>>() {

				@Override
				public void onSuccess(List<QuestionEventProxy> response) {
					QuestionAdvancedSearchQuestionEventPopupViewImpl questionEventView = new QuestionAdvancedSearchQuestionEventPopupViewImpl();
					hidePreviousPopup(questionEventView);
					questionEventView.setDelegate(ActivityQuestion.this);
					questionEventView.setQuestionEventSuggsetBoxValue(response);
					questionEventView.display(addKeyword);
				}
			});
		}
		
	}

	@Override
	public void textSearchAddClicked(IconButton addTextSearch) {
		QuestionAdvancedSearchTextSearchPopupViewImpl textSearchView = new QuestionAdvancedSearchTextSearchPopupViewImpl();
		hidePreviousPopup(textSearchView);
		textSearchView.setDelegate(this);
		textSearchView.display(addTextSearch);
	}

	@Override
	public void dateAddClicked(IconButton addDate) {
		QuestionAdvancedSearchDatePopupViewImpl dateSearchView = new QuestionAdvancedSearchDatePopupViewImpl();
		hidePreviousPopup(dateSearchView);
		dateSearchView.setDelegate(this);
		dateSearchView.display(addDate);
	}

	@Override
	public void mcAddClicked(final IconButton addMc)
	{
		requests.mcRequest().findAllMcs().fire(new BMEReceiver<List<McProxy>>() {

			@Override
			public void onSuccess(List<McProxy> response) {
				QuestionAdvancedSearchMCPopupViewImpl mcSearchView = new QuestionAdvancedSearchMCPopupViewImpl();
				hidePreviousPopup(mcSearchView);
				mcSearchView.setDelegate(ActivityQuestion.this);
				mcSearchView.setProxyToMcListBox(response);
				mcSearchView.display(addMc);		
			}
		});
	}
	
	@Override
	public void userTypeAddClicked(final IconButton addUserType)
	{
		requests.personRequest().findAllPeopleByNameASC().fire(new BMEReceiver<List<PersonProxy>>() {
		//requests.personRequest().findAllPeople().fire(new BMEReceiver<List<PersonProxy>>() {

			@Override
			public void onSuccess(List<PersonProxy> response) {
				QuestionAdvancedSearchUserTypePopupViewImpl userTypeView = new QuestionAdvancedSearchUserTypePopupViewImpl();
				hidePreviousPopup(userTypeView);
				userTypeView.setDelegate(ActivityQuestion.this);
				userTypeView.setPersonSuggsetBoxValue(response);
				userTypeView.display(addUserType);
			}
		});
	}
	
	public void mediaAvailabilityAddClicked(IconButton addMediaAvailability)
	{
		QuestionAdvancedSearchMediaAvailabilityPopupViewImpl mediaAvailabilityView = new QuestionAdvancedSearchMediaAvailabilityPopupViewImpl();
		hidePreviousPopup(mediaAvailabilityView);
		mediaAvailabilityView.setDelegate(this);
		mediaAvailabilityView.display(addMediaAvailability);
	}
	
	public void questionTypeAddClicked(IconButton addQuestionType)
	{
		QuestionAdvancedSearchQuestionTypePopupViewImpl questionTypeView = new QuestionAdvancedSearchQuestionTypePopupViewImpl();
		hidePreviousPopup(questionTypeView);
		questionTypeView.setDelegate(this);
		questionTypeView.display(addQuestionType);
		questionTypeView.disableSearchTextBox();
	}

	@Override
	public void splitLayoutPanelResized() {
		Double newWidth =(((QuestionViewImpl)view).getSplitLayoutPanel()).getWidgetSize(((QuestionViewImpl)view).getScrollpanel());
       	Cookies.setCookie(McAppConstant.QUESTION_VIEW_WIDTH, String.valueOf(newWidth));
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		this.sortname=sortname;
		this.sortorder=sortorder;
		init();
	}

	@Override
	public void printPdfClicked(int left, int top) {
		
		final QuestionPrintFilterViewImpl filterView = new QuestionPrintFilterViewImpl();
		filterView.getPrintButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				filterView.hide();
				
				List<String> encodedStringList = new ArrayList<String>();
				encodedStringList = AdvancedSearchCriteriaUtils.encodeList(advancedSearchCriteriaList);
				
				requests.questionRequest().findQuestionByAdvancedSearchForPrint(sortname,sortorder,encodedStringList, view.getSearchValue(), view.getSerachBox().getValue()).fire(new BMEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) {
						String ordinal = URL.encodeQueryString(String.valueOf(FileDownloaderProps.Method.PRINT_QUESTION_PDF.ordinal()));          
						String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(FileDownloaderProps.METHOD_KEY).concat("=").concat(ordinal)
								.concat("&").concat(FileDownloaderProps.ID).concat("=").concat(URL.encodeQueryString("ALL"))
								.concat("&").concat(FileDownloaderProps.QUESTION_DETAIL).concat("=").concat(URL.encodeQueryString(filterView.getDetailsCheckBoxValue()))
								.concat("&").concat(FileDownloaderProps.KEYWORD).concat("=").concat(URL.encodeQueryString(filterView.getKeywordCheckBoxValue()))
								.concat("&").concat(FileDownloaderProps.LEARNING_OBJECTIVE).concat("=").concat(URL.encodeQueryString(filterView.getLearningObjectiveCheckBoxValue()))
								.concat("&").concat(FileDownloaderProps.USED_IN_MC).concat("=").concat(URL.encodeQueryString(filterView.getUsedInMcCheckBoxValue()))
								.concat("&").concat(FileDownloaderProps.ANSWER).concat("=").concat(URL.encodeQueryString(filterView.getAnswerCheckBoxValue()))
								.concat("&").concat(FileDownloaderProps.LOCALE).concat("=").concat(URL.encodeQueryString(LocaleInfo.getCurrentLocale().getLocaleName()));
								
						Log.info("--> url is : " +url);
						
						Window.open(url, "", "");
					}
				});				
			}
		});		
		filterView.setPopupPosition(left-200, top-5);
		filterView.show();	
	}
}
