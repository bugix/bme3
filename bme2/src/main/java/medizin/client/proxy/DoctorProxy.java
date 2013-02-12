// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import medizin.shared.Gender;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Doctor", locator = "medizin.server.locator.DoctorLocator")
@RooGwtProxy(value = "medizin.server.domain.Doctor", readOnly = { "version", "id" })
public interface DoctorProxy extends EntityProxy {

    abstract Long getId();

    abstract Gender getGender();

    abstract void setGender(Gender gender);

    abstract String getTitle();

    abstract void setTitle(String title);

    abstract String getName();

    abstract void setName(String name);

    abstract String getPreName();

    abstract void setPreName(String preName);

    abstract String getEmail();

    abstract void setEmail(String email);

    abstract String getTelephone();

    abstract void setTelephone(String telephone);

    abstract Boolean getIsActive();

    abstract void setIsActive(Boolean isActive);

    abstract Integer getVersion();
}
