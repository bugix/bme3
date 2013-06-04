// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import medizin.shared.Gender;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Student", locator = "medizin.server.locator.StudentLocator")
@RooGwtProxy(value = "medizin.server.domain.Student", readOnly = { "version", "id" })
public interface StudentProxy extends EntityProxy {

    abstract Long getId();

    abstract Gender getGender();

    abstract void setGender(Gender gender);

    abstract String getName();

    abstract void setName(String name);

    abstract String getPreName();

    abstract void setPreName(String preName);

    abstract String getEmail();

    abstract void setEmail(String email);

    abstract String getStudentId();

    abstract void setStudentId(String studentId);

    abstract String getStreet();

    abstract void setStreet(String street);

    abstract String getCity();

    abstract void setCity(String city);

    abstract Integer getVersion();
}
