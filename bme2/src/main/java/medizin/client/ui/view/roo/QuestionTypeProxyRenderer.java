package medizin.client.ui.view.roo;

import medizin.client.proxy.QuestionTypeProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class QuestionTypeProxyRenderer extends ProxyRenderer<QuestionTypeProxy> {

    private static medizin.client.ui.view.roo.QuestionTypeProxyRenderer INSTANCE;

    protected QuestionTypeProxyRenderer() {
        super(new String[] { "shortName" });
    }

    public static medizin.client.ui.view.roo.QuestionTypeProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionTypeProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(QuestionTypeProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortName();
    }
}
