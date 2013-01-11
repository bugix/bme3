package medizin.client.ui.view.roo;

import medizin.client.proxy.AnswerProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AnswerProxyRenderer extends ProxyRenderer<AnswerProxy> {

    private static medizin.client.ui.view.roo.AnswerProxyRenderer INSTANCE;

    protected AnswerProxyRenderer() {
        super(new String[] { "answerText" });
    }

    public static medizin.client.ui.view.roo.AnswerProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswerProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AnswerProxy object) {
        if (object == null) {
            return "";
        }
        return object.getAnswerText() + " (" + object.getAnswerText() + ")";
    }
}
