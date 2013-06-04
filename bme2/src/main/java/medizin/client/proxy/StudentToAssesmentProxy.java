// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.StudentToAssesment", locator = "medizin.server.locator.StudentToAssesmentLocator")
@RooGwtProxy(value = "medizin.server.domain.StudentToAssesment", readOnly = { "version", "id" })
public interface StudentToAssesmentProxy extends EntityProxy {

    abstract Long getId();

    abstract Boolean getIsEnrolled();

    abstract void setIsEnrolled(Boolean isEnrolled);

    abstract AssesmentProxy getAssesment();

    abstract void setAssesment(AssesmentProxy assesment);

    abstract StudentProxy getStudent();

    abstract void setStudent(StudentProxy student);

    abstract Integer getVersion();
}
