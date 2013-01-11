// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.StaticContent", locator = "medizin.server.locator.StaticContentLocator")
@RooGwtProxy(value = "medizin.server.domain.StaticContent", readOnly = { "version", "id" })
public interface StaticContentProxy extends EntityProxy {

    abstract Long getId();

    abstract String getStaticText();

    abstract void setStaticText(String staticText);

    abstract Integer getVersion();
}
