package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.KeywordProxy;

public class KeywordProxyRenderer extends ProxyRenderer<KeywordProxy> {

    private static medizin.client.ui.view.roo.KeywordProxyRenderer INSTANCE;

    protected KeywordProxyRenderer() {
        super(new String[] { "name" });
    }

    public static medizin.client.ui.view.roo.KeywordProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new KeywordProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(KeywordProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getName() + ")";
    }
}
