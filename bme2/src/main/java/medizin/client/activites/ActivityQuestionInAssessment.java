package medizin.client.activites;

import java.util.ArrayList;
import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestionInAssessment;
import medizin.client.place.PlaceQuestionInAssessmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.QuestionInAssessmentView;
import medizin.client.ui.view.QuestionInAssessmentViewImpl;
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
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.AdvancedSearchCriteriaUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class ActivityQuestionInAssessment extends AbstractActivityWrapper implements QuestionInAssessmentView.Delegate, QuestionAdvancedSearchSubView.Presenter, QuestionAdvancedSearchSubView.Delegate, QuestionAdvancedSearchPopupView.Delegate {

	private final PlaceQuestionInAssessment questionInAssessmentPlace;
	private final McAppRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	private QuestionInAssessmentView view;
	private AdvanceCellTable<AssesmentQuestionProxy> table;
	private QuestionAdvancedSearchSubViewImpl advancedSearchSubViewImpl;
	private CellTable<AdvancedSearchCriteria> criteriaTable;
	private List<AdvancedSearchCriteria> advancedSearchCriteriaList = new ArrayList<AdvancedSearchCriteria>();
	private ActivityManager activityManger;
	private ActivityQuestionInAssessmentMapper activityQuestionInAssessmentMapper;
	private SingleSelectionModel<AssesmentQuestionProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "id";
	private EntityProxyId<?> proxyId = null;
	private QuestionAdvancedSearchAbstractPopupViewImpl advancedSearchAbstractPopupViewImpl;
	
	@Inject
	public ActivityQuestionInAssessment(PlaceQuestionInAssessment place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questionInAssessmentPlace = place;
		this.placeController = placeController;
		this.requests = requests;	
		this.activityQuestionInAssessmentMapper = new ActivityQuestionInAssessmentMapper(requests,placeController);
		this.activityManger = new ActivityManager(activityQuestionInAssessmentMapper,requests.getEventBus());
	}

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		Log.debug("in ActivityQuestionInAssessment");
		
		QuestionInAssessmentView questionInAssessmentView = new QuestionInAssessmentViewImpl(eventBus, hasQuestionAddRights());
		this.widget = widget;
		this.view = questionInAssessmentView;
		widget.setWidget(questionInAssessmentView.asWidget());

		setWidthOfWidget();
		
		table = view.getTable();
		advancedSearchSubViewImpl = view.getQuestionAdvancedSearchSubViewImpl();
		advancedSearchSubViewImpl.setDelegate(this);
		criteriaTable = advancedSearchSubViewImpl.getTable();
		criteriaTable.setRowCount(advancedSearchCriteriaList.size());
		criteriaTable.setRowData(advancedSearchCriteriaList);
		activityManger.setDisplay(view.getDetailsPanel());
		
		RecordChangeEvent.register(requests.getEventBus(), (QuestionInAssessmentViewImpl)view);

		//adding column mouse out on table.
		((QuestionInAssessmentViewImpl)view).addColumnOnMouseout();
			
		rangeChangeHandler = table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				if (view.getAssessmentSuggestBox().getSelected() != null)
				{
					Long assessmentId = view.getAssessmentSuggestBox().getSelected().getId();
					ActivityQuestionInAssessment.this.onRangeChanged(assessmentId);
				}				
			}
		});
		
		initAssessmentSuggestBox();	
		
		ProvidesKey<AssesmentQuestionProxy> keyProvider = ((AbstractHasData<AssesmentQuestionProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<AssesmentQuestionProxy>(keyProvider);	
		
		table.setSelectionModel(selectionModel);		

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						AssesmentQuestionProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {							
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
	}

	private void initAssessmentSuggestBox() {
		requests.assesmentRequest().findAllAssesmentByInstituteDescDateOfAssessment().with("mc").fire(new BMEReceiver<List<AssesmentProxy>>() {

			@Override
			public void onSuccess(List<AssesmentProxy> response) {
				view.setAssessmentSuggestBoxPickerValues(response);
				
				if (response.size() > 0)
				{
					Long assessmentId = response.get(0).getId();
					init(assessmentId);
				}
				else
				{
					table.setRowCount(0);
					table.setRowData(Lists.<AssesmentQuestionProxy>newArrayList());
				}
			}
		});
	}

	protected void showDetails(AssesmentQuestionProxy selectedObject) {
		placeController.goTo(new PlaceQuestionInAssessmentDetails(selectedObject.stableId()));
	}
	
	public void displayQuestionByAssessment(Long assessmentID)
	{
		init(assessmentID);
	}

	private void init(final Long assessmentId) {
		List<String> encodedStringList = new ArrayList<String>();
		encodedStringList = AdvancedSearchCriteriaUtils.encodeList(advancedSearchCriteriaList);
		
		requests.assesmentQuestionRequest().countAssessmentQuestionByAssessment(assessmentId, encodedStringList).fire(new BMEReceiver<Integer>() {

			@Override
			public void onSuccess(Integer response) {
				if (view == null) {
					return;
				}
				view.getTable().setRowCount(response.intValue(), true);
				onRangeChanged(assessmentId);
			}
		});
	}

	protected void onRangeChanged(Long assessmentId) {
		final Range range = table.getVisibleRange();
		List<String> encodedStringList = new ArrayList<String>();
		encodedStringList = AdvancedSearchCriteriaUtils.encodeList(advancedSearchCriteriaList);
		
		requests.assesmentQuestionRequest().findAssessmentQuestionByAssessmentForAdmin(assessmentId, sortname, sortorder, encodedStringList, range.getStart(), range.getLength()).with(view.getPaths()).with("question", "question.questionType", "question.autor").fire(new BMEReceiver<List<AssesmentQuestionProxy>>() {

			@Override
			public void onSuccess(List<AssesmentQuestionProxy> response) {
				if (view == null) {
					return;
				}

				table.setRowData(range.getStart(), response);
				
				selectRow(range);
/*
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}*/
				
				/*MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());*/
			}
		});

	}
	
	private void selectRow(final Range range) {
		if (proxyId != null)
		{
			requests.find(proxyId).fire(new BMEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if (response != null && response instanceof AssesmentQuestionProxy)
					{
						AssesmentQuestionProxy selectedProxy = (AssesmentQuestionProxy) response;
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
		
	}
	
	private void setWidthOfWidget() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.QUESTION_IN_ASSESSMENT_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	((QuestionInAssessmentViewImpl)view).getSplitLayoutPanel().setWidgetSize(((QuestionInAssessmentViewImpl)view).getScrollpanel(),Double.valueOf(widgetWidthFromCookie));	
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
				keywordPopupView.setDelegate(ActivityQuestionInAssessment.this);
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
		if (view.getAssessmentSuggestBox().getSelected() != null)
		{
			init(view.getAssessmentSuggestBox().getSelected().getId());
		}
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
					questionEventView.setDelegate(ActivityQuestionInAssessment.this);
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
				mcSearchView.setDelegate(ActivityQuestionInAssessment.this);
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
				userTypeView.setDelegate(ActivityQuestionInAssessment.this);
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
	public void goTo(Place place) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitLayoutPanelResized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void columnClickedForSorting(String sortname, Sorting sortorder) {
		// TODO Auto-generated method stub
		
	}
}
