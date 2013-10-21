// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.MainQuestionSkill", locator = "medizin.server.locator.MainQuestionSkillLocator")
@RooGwtProxy(value = "medizin.server.domain.MainQuestionSkill", readOnly = { "version", "id" })
public interface MainQuestionSkillProxy extends EntityProxy {

    abstract Long getId();

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract SkillProxy getSkill();

    abstract void setSkill(SkillProxy skill);

    abstract Integer getVersion();
}
