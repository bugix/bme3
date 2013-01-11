package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AnswerToAssQuestionProxy;
import medizin.client.proxy.AssesmentQuestionProxy;

public class AnswerToAssQuestionProxyRenderer extends ProxyRenderer<AnswerToAssQuestionProxy> {

    private static medizin.client.ui.view.roo.AnswerToAssQuestionProxyRenderer INSTANCE;

    protected AnswerToAssQuestionProxyRenderer() {
        super(new String[] { "id" });
    }

    public static medizin.client.ui.view.roo.AnswerToAssQuestionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswerToAssQuestionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnswerToAssQuestionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
