// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Institution", locator = "medizin.server.locator.InstitutionLocator")
@RooGwtProxy(value = "medizin.server.domain.Institution", readOnly = { "version", "id" })
public interface InstitutionProxy extends EntityProxy {

    abstract Long getId();

    abstract String getInstitutionName();

    abstract void setInstitutionName(String institutionName);

    abstract Integer getVersion();
}
