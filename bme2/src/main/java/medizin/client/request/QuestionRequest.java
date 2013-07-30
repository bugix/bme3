package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.server.domain.Question;

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
	
	Request<List<QuestionProxy>> findQuestionsByMc(Long mcId, String questionId, String questionType, String questionName,AssesmentProxy a,PersonProxy author);

	Request<java.lang.Long> countQuestionsNonAcceptedAdmin();

	Request<List<QuestionProxy>> findQuestionsEntriesNonAcceptedAdmin(
			int start, int length);

	Request<List<QuestionProxy>> findQuestionsAnswersNonAcceptedAdmin();

	Request<Long>  countQuestionsByPerson(String string, Long institutionId, String searchText, List<String> searchField);

	Request<List<QuestionProxy>>  findQuestionEntriesByPerson(String string, Long institutionId, String searchText, List<String> searchField, int start, int length,boolean newQuestion,String questionId,String questionType,String questionName);
	
	InstanceRequest<QuestionProxy, Void> persistAndSetPreviousInactive();

	InstanceRequest<QuestionProxy, Void>  persist();

	InstanceRequest<QuestionProxy, Void>  generateNewVersion();

	Request<Boolean> deleteMediaFileFromDisk(String path);
	
	Request<Void> questionAccepted(QuestionProxy question, Boolean isAdminOrInstitutionalAdmin);

	InstanceRequest<QuestionProxy, Void> deactivatedQuestion();

	InstanceRequest<QuestionProxy, QuestionProxy> questionResendToReviewWithMajorVersion(boolean b);

	Request<Long> countNotActivatedQuestionsByPerson(String searchText, List<String> searchField);

	Request<List<QuestionProxy>> findAllNotActivatedQuestionsByPerson(String searchText, List<String> searchField, int start, int length);

	Request<Void> forcedActiveQuestion(Long questionId);

	InstanceRequest<QuestionProxy, Void> persistQuestion();
	
	Request<List<QuestionProxy>> findAllQuestionsAnswersNotActivatedByPerson();
}
