package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Assesment")
@ServiceName("medizin.server.domain.Assesment")
public interface AssesmentRequest extends AssesmentRequest_Roo_Gwt {
	
	abstract Request<List<AssesmentProxy>> findAssesmentsOpenBetween();
	abstract Request<List<AssesmentProxy>> findActiveAssesments(Long selectedInstitutionId);
	abstract Request<List<AssesmentProxy>> findAssesmentByInsitute(int firstResult, int maxResults);
	abstract Request<Long> countAssesmentByInsitute();

	abstract Request<List<AssesmentProxy>> findAssessmentByLoggedUser(Long loggedUserId, boolean isAdminOrInstitutionalAdmin);
	
	abstract Request<Boolean> systemOverviewSendMail(Long personId, List<AssesmentProxy> assesmentList, String messageContent, String mailSubject);
}
