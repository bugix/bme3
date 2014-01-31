package medizin.client.place;

import medizin.client.factory.request.McAppRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

public class McPlaceHistoryFactory {
	
private final PlaceSystemOverview.Tokenizer placeSystemOverviewTokenizer ;
private final PlaceAcceptAnswer.Tokenizer placeAcceptAnswerTokenizer ;
private final PlaceAcceptQuestion.Tokenizer placeAcceptQuestionTokenizer ;
private final PlaceAsignAssQuestion.Tokenizer placeAsignAssQuestionTokenizer ;
private final PlaceAssesment.Tokenizer placeAssesmentTokenizer ;
private final PlaceBookAssesment.Tokenizer placeBookAssesmentTokenizer;
private final PlaceQuestion.Tokenizer placeQuestionTokenizer;
private final PlaceQuestionDetails.Tokenizer placeQuestionDetailsTokenizer;
private final PlaceQuestiontypes.Tokenizer placeQuestiontypesTokenizer;
private final PlaceQuestiontypesDetails.Tokenizer placeQuestiontypesDetailsTokenizer;
private final PlaceUser.Tokenizer placeUserTokenizer;
private final PlaceUserDetails.Tokenizer placeUserDetailsTokenizer;
private final PlaceInstitution.Tokenizer placeInstitutionTokenizer;
private final PlaceQuestionEdit.Tokenizer placeQuestionEditTokenizer;
private final PlaceInstitutionEvent.Tokenizer placeInstitutionEventTokenizer ;
private final PlaceBookAssesmentDetails.Tokenizer placeBookAssesmentDetailsTokenizer ;
private final PlaceUserDetailsEvent.Tokenizer placeUserDetailsEventTokenizer ;
private final PlaceAssesmentDetails.Tokenizer placeAssesmentDetails ;
private final PlaceAcceptQuestionDetails.Tokenizer placeAcceptQuestionDetails ;
private final PlaceNotActivatedQuestion.Tokenizer placeNotActivatedQuestion ;
private final PlaceNotActivatedQuestionDetails.Tokenizer placeNotActivatedQuestionDetails ;
private final PlaceNotActivatedAnswer.Tokenizer placeNotActivatedAnswer;
private final PlaceDeactivatedQuestion.Tokenizer placeDeactivatedQuestion;
private final PlaceDeactivatedQuestionDetails.Tokenizer placeDeactivatedQuestionDetails;
private final PlaceQuestionInAssessment.Tokenizer placeQuestionInAssessment;
private final PlaceQuestionInAssessmentDetails.Tokenizer placeQuestionInAssessmentDetails;

public PlaceUserDetailsEvent.Tokenizer getPlaceUserDetailsEventTokenizer() {
	return placeUserDetailsEventTokenizer;
}


public PlaceSystemOverview.Tokenizer getPlaceSystemOverviewTokenizer() {
	Log.debug("McPlaceHistoryFactory.getPlaceSystemOverviewTokenizer");
	return placeSystemOverviewTokenizer;
}

public PlaceAcceptAnswer.Tokenizer getPlaceAcceptAnswerTokenizer() {
	return placeAcceptAnswerTokenizer;
}

public PlaceAcceptQuestion.Tokenizer getPlaceAcceptQuestionTokenizer() {
	return placeAcceptQuestionTokenizer;
}


public PlaceAsignAssQuestion.Tokenizer getPlaceAsignAssQuestionTokenizer() {
	return placeAsignAssQuestionTokenizer;
}


public PlaceAssesment.Tokenizer getPlaceAssesmentTokenizer() {
	return placeAssesmentTokenizer;
}


public PlaceBookAssesment.Tokenizer getPlaceBookAssesmentTokenizer() {
	return placeBookAssesmentTokenizer;
}

public PlaceQuestion.Tokenizer getPlaceQuestionTokenizer() {
	return placeQuestionTokenizer;
}

public PlaceQuestionDetails.Tokenizer getPlaceQuestionDetailsTokenizer() {
	return placeQuestionDetailsTokenizer;
}


public PlaceQuestiontypes.Tokenizer getPlaceQuestiontypesTokenizer() {
	return placeQuestiontypesTokenizer;
}


public PlaceQuestiontypesDetails.Tokenizer getPlaceQuestiontypesDetailsTokenizer() {
	return placeQuestiontypesDetailsTokenizer;
}

public PlaceUser.Tokenizer getPlaceUserTokenizer() {
	return placeUserTokenizer;
}


public PlaceUserDetails.Tokenizer getPlaceUserDetailsTokenizer() {
	return placeUserDetailsTokenizer;
}


public PlaceInstitution.Tokenizer getPlaceInstitutionTokenizer() {
	return placeInstitutionTokenizer;
}


public PlaceQuestionEdit.Tokenizer getPlaceQuestionEditTokenizer() {
	return placeQuestionEditTokenizer;
}


public PlaceInstitutionEvent.Tokenizer getPlaceInstitutionEventTokenizer() {
	return placeInstitutionEventTokenizer;
}


public PlaceBookAssesmentDetails.Tokenizer getPlaceBookAssesmentDetailsTokenizer() {
	return placeBookAssesmentDetailsTokenizer;
}
public  PlaceAssesmentDetails.Tokenizer getPlaceAssesmentDetailsTokenizer() {
	return placeAssesmentDetails;
}

public  PlaceAcceptQuestionDetails.Tokenizer getPlaceAcceptQuestionDetailsTokenizer() {
	return placeAcceptQuestionDetails;
}

public  PlaceNotActivatedQuestion.Tokenizer getPlaceNotActivatedQuestionTokenizer() {
	return placeNotActivatedQuestion;
}

public  PlaceNotActivatedQuestionDetails.Tokenizer getPlaceNotActivatedQuestionDetailsTokenizer() {
	return placeNotActivatedQuestionDetails;
}

public PlaceNotActivatedAnswer.Tokenizer getPlaceNotActivatedAnswerTokenizer() {
	return placeNotActivatedAnswer;
}

public PlaceDeactivatedQuestion.Tokenizer getPlaceDeactivatedQuestionTokenizer() {
	return placeDeactivatedQuestion;
}

public PlaceDeactivatedQuestionDetails.Tokenizer getPlaceDeactivatedQuestionDetailsTokenizer() {
	return placeDeactivatedQuestionDetails;
}

public PlaceQuestionInAssessment.Tokenizer getPlaceQuestionInAssessmentTokenizer() {
	return placeQuestionInAssessment;
}

public PlaceQuestionInAssessmentDetails.Tokenizer getPlaceQuestionInAssessmentDetailsTokenizer() {
	return placeQuestionInAssessmentDetails;
}
//	private final NationalityPlace.Tokenizer nationalityPlaceTokenizer;
//	private final NationalityDetailsPlace.Tokenizer nationalityDetailsPlaceTokenizer;

	@Inject
	public McPlaceHistoryFactory(McAppRequestFactory requestFactory) {
		Log.debug("McPlaceHistoryFactory.Konstruktor");
		this.placeSystemOverviewTokenizer= new PlaceSystemOverview.Tokenizer(requestFactory) ; 
		this.placeAcceptAnswerTokenizer= new PlaceAcceptAnswer.Tokenizer(requestFactory) ; 
		this.placeAcceptQuestionTokenizer= new PlaceAcceptQuestion.Tokenizer(requestFactory) ; 
		this.placeAsignAssQuestionTokenizer= new PlaceAsignAssQuestion.Tokenizer(requestFactory) ; 
		this.placeAssesmentTokenizer= new PlaceAssesment.Tokenizer(requestFactory) ; 
		this.placeBookAssesmentTokenizer= new PlaceBookAssesment.Tokenizer(requestFactory) ; 
		this.placeQuestionTokenizer= new PlaceQuestion.Tokenizer(requestFactory) ; 
		this.placeQuestionDetailsTokenizer= new PlaceQuestionDetails.Tokenizer(requestFactory) ; 
		this.placeQuestiontypesTokenizer= new PlaceQuestiontypes.Tokenizer (requestFactory) ; 
		this.placeQuestiontypesDetailsTokenizer= new PlaceQuestiontypesDetails.Tokenizer (requestFactory) ; 
		this.placeUserTokenizer= new PlaceUser.Tokenizer(requestFactory) ; 
		this.placeUserDetailsTokenizer= new PlaceUserDetails.Tokenizer(requestFactory) ; 
		this.placeInstitutionTokenizer= new PlaceInstitution.Tokenizer(requestFactory) ; 
		this.placeQuestionEditTokenizer= new PlaceQuestionEdit.Tokenizer(requestFactory) ; 
		this.placeInstitutionEventTokenizer= new PlaceInstitutionEvent.Tokenizer(requestFactory) ; 
		this.placeBookAssesmentDetailsTokenizer= new PlaceBookAssesmentDetails.Tokenizer(requestFactory) ; 
		this.placeUserDetailsEventTokenizer = new PlaceUserDetailsEvent.Tokenizer(requestFactory) ;
//		this.nationalityPlaceTokenizer = new NationalityPlace.Tokenizer(requestFactory);
//		this.nationalityDetailsPlaceTokenizer = new NationalityDetailsPlace.Tokenizer(requestFactory);
		this.placeAssesmentDetails = new PlaceAssesmentDetails.Tokenizer(requestFactory);
		this.placeAcceptQuestionDetails = new PlaceAcceptQuestionDetails.Tokenizer(requestFactory);
		this.placeNotActivatedQuestion= new PlaceNotActivatedQuestion.Tokenizer(requestFactory);
		this.placeNotActivatedQuestionDetails = new PlaceNotActivatedQuestionDetails.Tokenizer(requestFactory);
		this.placeNotActivatedAnswer = new PlaceNotActivatedAnswer.Tokenizer(requestFactory);
		this.placeDeactivatedQuestion = new PlaceDeactivatedQuestion.Tokenizer(requestFactory);
		this.placeDeactivatedQuestionDetails = new PlaceDeactivatedQuestionDetails.Tokenizer(requestFactory);
		this.placeQuestionInAssessment = new PlaceQuestionInAssessment.Tokenizer(requestFactory);
		this.placeQuestionInAssessmentDetails = new PlaceQuestionInAssessmentDetails.Tokenizer(requestFactory);
	}

//	public PlaceTokenizer<NationalityPlace> getNationalityPlaceTokenizer() {
//		return nationalityPlaceTokenizer;
//	}
//	
//	public PlaceTokenizer<NationalityDetailsPlace> getNationalityDetailsPlaceTokenizer() {
//		return nationalityDetailsPlaceTokenizer;
//	}

//	public PlaceTokenizer<ProxyPlace> getProxyPlaceTokenizer() {
//		return proxyPlaceTokenizer;
//	}


}
