package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;

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
        return object.getEventName() + " (" + object.getEventName() + ")";
    }
}
