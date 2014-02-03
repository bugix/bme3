package medizin.client.request;

import java.util.List;

import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.ui.widget.Sorting;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionType")
@ServiceName("medizin.server.domain.QuestionType")
public interface QuestionTypeRequest extends QuestionTypeRequest_Roo_Gwt {
	
	Request<Long> countAllQuestionType(String searchValue);
	Request<List<QuestionTypeProxy>> findAllQuestionType(Integer start,Integer end,String sortBy,Sorting sortOrder,String searchValue);
	Request<List<QuestionTypeProxy>> findAllQuestionTypesByAssesment(Long assesmentId);
	Request<Long> countQuestionsForQuestionType(Long questionTypeId);
	Request<List<QuestionTypeProxy>> findAllQuestionTypesForInstituteInSession();
	Request<List<QuestionTypeProxy>> findAllQuestionTypesForInstituteInSession(AssesmentProxy a);
	Request<Integer> findRangeStartForQuestionType(Long questionTypeId, Integer length, String sortname, Sorting sortorder, String searchValue);

}
