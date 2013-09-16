package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.McProxy;

public class McProxyRenderer extends ProxyRenderer<McProxy> {

    private static medizin.client.ui.view.roo.McProxyRenderer INSTANCE;

    protected McProxyRenderer() {
        super(new String[] { "mcName" });
    }

    public static medizin.client.ui.view.roo.McProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new McProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(McProxy object) {
        if (object == null) {
            return "";
        }
        return object.getMcName();
    }
}
