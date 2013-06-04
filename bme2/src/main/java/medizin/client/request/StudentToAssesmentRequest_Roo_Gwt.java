// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.StudentToAssesment")
@ServiceName("medizin.server.domain.StudentToAssesment")
public interface StudentToAssesmentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countStudentToAssesments();

    abstract Request<java.util.List<medizin.client.proxy.StudentToAssesmentProxy>> findAllStudentToAssesments();

    abstract Request<java.util.List<medizin.client.proxy.StudentToAssesmentProxy>> findStudentToAssesmentEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.StudentToAssesmentProxy> findStudentToAssesment(Long id);

    abstract InstanceRequest<medizin.client.proxy.StudentToAssesmentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.StudentToAssesmentProxy, java.lang.Void> remove();
}
