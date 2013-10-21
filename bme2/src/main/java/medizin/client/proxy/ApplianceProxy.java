// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Appliance", locator = "medizin.server.locator.ApplianceLocator")
@RooGwtProxy(value = "medizin.server.domain.Appliance", readOnly = { "version", "id" })
public interface ApplianceProxy extends EntityProxy {

    abstract Long getId();

    abstract String getShortcut();

    abstract void setShortcut(String shortcut);

    abstract Integer getVersion();
}
