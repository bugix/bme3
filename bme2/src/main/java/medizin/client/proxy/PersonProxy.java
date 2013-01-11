// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Person", locator = "medizin.server.locator.PersonLocator")
@RooGwtProxy(value = "medizin.server.domain.Person", readOnly = { "version", "id" })
public interface PersonProxy extends EntityProxy {

    abstract Long getId();

    abstract String getName();

    abstract void setName(String name);

    abstract String getPrename();

    abstract void setPrename(String prename);

    abstract String getShidId();

    abstract void setShidId(String shidId);

    abstract String getEmail();

    abstract void setEmail(String email);

    abstract String getAlternativEmail();

    abstract void setAlternativEmail(String alternativEmail);

    abstract String getPhoneNumber();

    abstract void setPhoneNumber(String phoneNumber);

    abstract Boolean getIsAdmin();

    abstract void setIsAdmin(Boolean isAdmin);

    abstract Boolean getIsAccepted();

    abstract void setIsAccepted(Boolean isAccepted);

    abstract Set<medizin.client.proxy.QuestionAccessProxy> getQuestionAccesses();

    abstract void setQuestionAccesses(Set<medizin.client.proxy.QuestionAccessProxy> questionAccesses);

    abstract Integer getVersion();
}
