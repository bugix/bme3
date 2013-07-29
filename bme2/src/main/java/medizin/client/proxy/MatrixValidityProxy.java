// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import medizin.shared.Validity;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.MatrixValidity", locator = "medizin.server.locator.MatrixValidityLocator")
@RooGwtProxy(value = "medizin.server.domain.MatrixValidity", readOnly = { "version", "id" })
public interface MatrixValidityProxy extends EntityProxy {

    abstract Long getId();

    abstract AnswerProxy getAnswerX();

    abstract void setAnswerX(AnswerProxy answerX);

    abstract AnswerProxy getAnswerY();

    abstract void setAnswerY(AnswerProxy answerY);

    abstract Validity getValidity();

    abstract void setValidity(Validity validity);

    abstract Integer getVersion();
}
