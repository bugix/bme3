package medizin.client.request;

import java.util.List;
import java.util.Set;

import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.shared.Status;

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

	Request<Long>  countQuestionsByPerson(String string, Long institutionId, String searchText, List<String> searchField);

	Request<List<QuestionProxy>>  findQuestionEntriesByPerson(String string, Long institutionId, String searchText, List<String> searchField, int start, int length);
	
	InstanceRequest<QuestionProxy, Void> persistAndSetPreviousInactive();

	InstanceRequest<QuestionProxy, Void>  persist();

	InstanceRequest<QuestionProxy, Void>  generateNewVersion();

	Request<Boolean> deleteMediaFileFromDisk(String path);

	Request<QuestionProxy> persistNewQuestion(Long questionTypeId, String questionShortName, String questionText, Long autherId,Long reviewerId,
			Boolean submitToReviewComitee,Long questionEventId, List<Long> mcIds, String questionComment, int questionVersion, int questionSubVersion, String picturePath, Status status, Set<QuestionResourceProxy> proxies, Long oldQuestionId);
	
	Request<Void> questionAccepted(QuestionProxy question, Boolean isAdminOrInstitutionalAdmin);

	InstanceRequest<QuestionProxy, Void> deactivatedQuestion();

	InstanceRequest<QuestionProxy, Void> questionResendToReviewWithMajorVersion(boolean b);
}
