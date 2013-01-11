// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.QuestionEvent", locator = "medizin.server.locator.QuestionEventLocator")
@RooGwtProxy(value = "medizin.server.domain.QuestionEvent", readOnly = { "version", "id" })
public interface QuestionEventProxy extends EntityProxy {

    abstract Long getId();

    abstract String getEventName();

    abstract void setEventName(String eventName);

    abstract InstitutionProxy getInstitution();

    abstract void setInstitution(InstitutionProxy institution);

    abstract Integer getVersion();
}
