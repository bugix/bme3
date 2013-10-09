// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.MinorQuestionSkill", locator = "medizin.server.locator.MinorQuestionSkillLocator")
@RooGwtProxy(value = "medizin.server.domain.MinorQuestionSkill", readOnly = { "version", "id" })
public interface MinorQuestionSkillProxy extends EntityProxy {

    abstract Long getId();

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract SkillProxy getSkill();

    abstract void setSkill(SkillProxy skill);

    abstract Integer getVersion();
}
