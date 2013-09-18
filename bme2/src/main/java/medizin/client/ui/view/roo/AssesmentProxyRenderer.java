package medizin.client.ui.view.roo;

import medizin.client.proxy.AssesmentProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class AssesmentProxyRenderer extends ProxyRenderer<AssesmentProxy> {

    private static medizin.client.ui.view.roo.AssesmentProxyRenderer INSTANCE;

    protected AssesmentProxyRenderer() {
        super(new String[] { "name" });
    }

    public static medizin.client.ui.view.roo.AssesmentProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new AssesmentProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(AssesmentProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName();
    }
}
