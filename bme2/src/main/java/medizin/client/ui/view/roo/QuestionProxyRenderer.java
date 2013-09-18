package medizin.client.ui.view.roo;

import medizin.client.proxy.QuestionProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

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
        return object.getQuestionText();
    }
}
