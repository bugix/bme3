package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import java.util.Set;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.CommentProxy;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.McProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionTypeProxy;

public class QuestionProxyRenderer extends ProxyRenderer<QuestionProxy> {

    private static medizin.client.ui.view.roo.QuestionProxyRenderer INSTANCE;

    protected QuestionProxyRenderer() {
        super(new String[] { "questionText" });
    }

    public static medizin.client.ui.view.roo.QuestionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(QuestionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getQuestionText() + " (" + object.getQuestionText() + ")";
    }
}
