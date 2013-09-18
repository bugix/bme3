package medizin.client.ui.view.roo;

import medizin.client.proxy.QuestionEventProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class QuestionEventProxyRenderer extends ProxyRenderer<QuestionEventProxy> {

    private static medizin.client.ui.view.roo.QuestionEventProxyRenderer INSTANCE;

    protected QuestionEventProxyRenderer() {
        super(new String[] { "eventName" });
    }

    public static medizin.client.ui.view.roo.QuestionEventProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionEventProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(QuestionEventProxy object) {
        if (object == null) {
            return "";
        }
        return object.getEventName();
    }
}
