// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Mc", locator = "medizin.server.locator.McLocator")
@RooGwtProxy(value = "medizin.server.domain.Mc", readOnly = { "version", "id" })
public interface McProxy extends EntityProxy {

    abstract Long getId();

    abstract String getMcName();

    abstract void setMcName(String mcName);

    abstract Integer getVersion();
}
