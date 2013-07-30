package medizin.client.request;

import java.util.List;

import medizin.client.proxy.MatrixValidityProxy;
import medizin.server.domain.MatrixValidity;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.MatrixValidity")
@ServiceName("medizin.server.domain.MatrixValidity")
public interface MatrixValidityRequest extends MatrixValidityRequest_Roo_Gwt {

	Request<List<MatrixValidityProxy>> findAllMatrixValidityForQuestion(Long id);

	Request<Boolean> deleteAnswerAndItsMatrixValidity(Long id, Boolean isAnswerX);

	Request<Long> countAllMatrixValidityForQuestion(Long id);
	
	Request<Long> countAllMatrixValidityForQuestionForAcceptAnswerView(Long id, Boolean isInstitutionalAdmin);
	
	Request<List<MatrixValidityProxy>> findAllMatrixValidityForQuestionForAcceptAnswerView(Long id,Boolean isInstitutionalAdmin, Integer start, Integer length);
	
	Request<Long> countAllMatrixValidityForAcceptQuestion(Long id);
	
	Request<List<MatrixValidityProxy>> findAllMatrixValidityForAcceptQuestion(Long id, Integer start, Integer length);
	
	Request<Long> countAllMatrixValidityForForceActiveByQuestion(Long id,Boolean isInstitutionalAdmin);
	
	Request<List<MatrixValidityProxy>> findAllMatrixValidityForForceActiveByQuestion(Long id, Boolean isInstitutionalAdmin, Integer start, Integer length);
}
