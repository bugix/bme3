package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class PlaceQuestionInAssessment extends AbstractPlace {
	
	public static final String PLACE_QUESTION_IN_ASSESSMENT = "PlaceQuestionInAssessment";
	
	public PlaceQuestionInAssessment(String placeName) {
		super(placeName);
	}

	public PlaceQuestionInAssessment(String placeName, boolean reload) {
		super(placeName, reload);
	}

	public PlaceQuestionInAssessment(String placeName, EntityProxyId<?> stableId) {
		super(placeName,stableId);
	}

	public PlaceQuestionInAssessment(String placeQuestionInAssessment,
			boolean reload, Integer height) {
		super(placeQuestionInAssessment, reload, height);
	}

	@Prefix(PLACE_QUESTION_IN_ASSESSMENT)
	public static class Tokenizer extends AbstractPlace.AbstractTokenizer<PlaceQuestionInAssessment> {

		public Tokenizer(McAppRequestFactory requestFactory) {
			super(requestFactory);
		}

		@Override
		public PlaceQuestionInAssessment getPlace(String token) {
			return new PlaceQuestionInAssessment(token);
		}
	}
}
