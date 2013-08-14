// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import medizin.shared.MultimediaType;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.QuestionResource", locator = "medizin.server.locator.QuestionResourceLocator")
@RooGwtProxy(value = "medizin.server.domain.QuestionResource", readOnly = { "version", "id" })
public interface QuestionResourceProxy extends EntityProxy {

    abstract Long getId();

    abstract String getPath();

    abstract void setPath(String path);

    abstract Integer getSequenceNumber();

    abstract void setSequenceNumber(Integer sequenceNumber);

    abstract MultimediaType getType();

    abstract void setType(MultimediaType type);

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract Integer getImageHeight();

    abstract void setImageHeight(Integer imageHeight);

    abstract Integer getImageWidth();

    abstract void setImageWidth(Integer imageWidth);

    abstract Integer getVersion();
}
