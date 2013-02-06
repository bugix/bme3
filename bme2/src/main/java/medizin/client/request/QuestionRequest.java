package medizin.client.request;

import java.util.List;

import medizin.client.proxy.QuestionProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Question")
@ServiceName("medizin.server.domain.Question")
public interface QuestionRequest extends QuestionRequest_Roo_Gwt {
	
	Request<List<QuestionProxy>> findQuestionAccessByPersonNonRoo(Long id,
			int start, int length);

	Request<java.lang.Long>  countQuestionAccessByPersonNonRoo(Long id);

	Request<java.lang.Long> countQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
			Long institutionId, Long eventId, String questiuonStringFilter,
			Boolean filterQuestionText, Boolean filterKeywords);
	
	Request<List<QuestionProxy>>  findQuestionByInstitutionOrEventOrQuestionNameOrKeyword(
			Long institutionId, Long eventId, String questiuonStringFilter,
			Boolean filterQuestionText, Boolean filterKeywords, int start, int length);
	
	Request<List<QuestionProxy>> findQuestionsByMc(Long mcId);

	Request<java.lang.Long> countQuestionsNonAcceptedAdmin();

	Request<List<QuestionProxy>> findQuestionsEntriesNonAcceptedAdmin(
			int start, int length);

	Request<List<QuestionProxy>> findQuestionsAnswersNonAcceptedAdmin();

	Request<Long>  countQuestionsByPerson(String string, Long institutionId);

	Request<List<QuestionProxy>>  findQuestionEntriesByPerson(String string, Long institutionId, int start, int length);
	
	InstanceRequest<QuestionProxy, Void> persistAndSetPreviousInactive();

	InstanceRequest<QuestionProxy, Void>  persist();

	InstanceRequest<QuestionProxy, Void>  generateNewVersion();

	Request<Boolean> deletePictureFromDisk(String picturePath);
}
