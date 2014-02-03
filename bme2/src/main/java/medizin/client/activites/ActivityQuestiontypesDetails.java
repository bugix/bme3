package medizin.client.activites;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceQuestiontypes;
import medizin.client.place.PlaceQuestiontypesDetails;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.view.QuestiontypesDetailsView;
import medizin.client.ui.view.QuestiontypesDetailsViewImpl;
import medizin.client.ui.widget.process.AppLoader;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ActivityQuestiontypesDetails extends AbstractActivityWrapper implements QuestiontypesDetailsView.Delegate {

	private PlaceQuestiontypesDetails questiontypePlace;

	private AcceptsOneWidget widget;
	private QuestiontypesDetailsView view;
	private HandlerRegistration rangeChangeHandler;
	private QuestionTypeProxy questionType;
	private McAppRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public ActivityQuestiontypesDetails(PlaceQuestiontypesDetails place, McAppRequestFactory requests, PlaceController placeController) {
		super(place, requests, placeController);
		this.questiontypePlace = place;
        this.requests = requests;
        this.placeController = placeController;
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

	@Override
	public void start2(AcceptsOneWidget widget, EventBus eventBus) {
		QuestiontypesDetailsView questionTypeDetailsView = new QuestiontypesDetailsViewImpl();
		questionTypeDetailsView.setName("hallo");
		this.widget = widget;
		this.view = questionTypeDetailsView;
        widget.setWidget(questionTypeDetailsView.asWidget());
		view.setDelegate(this);
		AppLoader.setNoLoader();
		requests.find(questiontypePlace.getProxyId()).with("institution").fire(new BMEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if(response instanceof QuestionTypeProxy){
					//Log.info(((QuestionTypeProxy) response).getQuestionTypeName());
					AppLoader.setCurrentLoader(view.getLoadingPopup());
					init((QuestionTypeProxy) response);
				}

				
			}
		});
	}
	
	public void goTo(Place place) {
		  placeController.goTo(place);
	}
    
	private void init(QuestionTypeProxy questionType) {
		this.questionType = questionType;
		view.setValue(questionType);
	}
	
	@Override
	public void deleteClicked() {
		AppLoader.setNoLoader();
		requests.questionTypeRequest().remove().using(questionType).fire(new BMEReceiver<Void>() {

            public void onSuccess(Void ignore) {
            	Log.debug("Sucessfull deleted");
            	placeController.goTo(new PlaceQuestiontypes(PlaceQuestiontypes.PLACE_QUESTIONTYPES,questiontypePlace.getHeight()));
           }
       });
		
	}

	@Override
	public void editClicked() {
		placeController.goTo(new PlaceQuestiontypesDetails(questionType.stableId(), PlaceQuestiontypesDetails.Operation.EDIT,questiontypePlace.getHeight() ));
		
	}

	@Override
	public void placeChanged(Place place) {
	}

	@Override
	public void getQuestionCount(QuestionTypeProxy proxy, final Function<Long, Void> function) {
		
		requests.questionTypeRequest().countQuestionsForQuestionType(proxy.getId()).fire(new BMEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				
				if(function != null) {
					function.apply(response);
				}
			}
			
		});
	}
}
