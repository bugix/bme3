// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

import medizin.shared.AccessRights;

import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.UserAccessRights", locator = "medizin.server.locator.UserAccessRightsLocator")
@RooGwtProxy(value = "medizin.server.domain.UserAccessRights", readOnly = { "version", "id" })
public interface UserAccessRightsProxy extends EntityProxy {

    abstract Long getId();

    abstract AccessRights getAccRights();

    abstract void setAccRights(AccessRights accRights);

    abstract PersonProxy getPerson();

    abstract void setPerson(PersonProxy person);

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract QuestionEventProxy getQuestionEvent();

    abstract void setQuestionEvent(QuestionEventProxy questionEvent);

    abstract InstitutionProxy getInstitution();

    abstract void setInstitution(InstitutionProxy institution);

    abstract Integer getVersion();
}
