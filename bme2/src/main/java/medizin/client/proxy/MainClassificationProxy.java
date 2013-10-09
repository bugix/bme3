// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.MainClassification", locator = "medizin.server.locator.MainClassificationLocator")
@RooGwtProxy(value = "medizin.server.domain.MainClassification", readOnly = { "version", "id" })
public interface MainClassificationProxy extends EntityProxy {

    abstract Long getId();

    abstract String getShortcut();

    abstract void setShortcut(String shortcut);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract Integer getVersion();
}
