// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Keyword", locator = "medizin.server.locator.KeywordLocator")
@RooGwtProxy(value = "medizin.server.domain.Keyword", readOnly = { "version", "id" })
public interface KeywordProxy extends EntityProxy {

    abstract Long getId();

    abstract String getName();

    abstract void setName(String name);

    abstract Integer getVersion();
}
