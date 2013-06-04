// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Student")
@ServiceName("medizin.server.domain.Student")
public interface StudentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countStudents();

    abstract Request<java.util.List<medizin.client.proxy.StudentProxy>> findAllStudents();

    abstract Request<java.util.List<medizin.client.proxy.StudentProxy>> findStudentEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.StudentProxy> findStudent(Long id);

    abstract InstanceRequest<medizin.client.proxy.StudentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.StudentProxy, java.lang.Void> remove();
}
