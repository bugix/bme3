package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.StudentToAssesmentProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.StudentToAssesment")
@ServiceName("medizin.server.domain.StudentToAssesment")
public interface StudentToAssesmentRequest extends StudentToAssesmentRequest_Roo_Gwt {
	
	Request<Long> countStudentToAssesmentByAssesment(Long assessmentId);
	
	Request<List<StudentToAssesmentProxy>> findStudentToAssesmentByAssesment(Long assesmentId, int start, int length);
}
