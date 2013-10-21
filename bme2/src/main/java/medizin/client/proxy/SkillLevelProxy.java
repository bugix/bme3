// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.SkillLevel", locator = "medizin.server.locator.SkillLevelLocator")
@RooGwtProxy(value = "medizin.server.domain.SkillLevel", readOnly = { "version", "id" })
public interface SkillLevelProxy extends EntityProxy {

    abstract Long getId();

    abstract Integer getLevelNumber();

    abstract void setLevelNumber(Integer levelNumber);

    abstract Integer getVersion();
}
