package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.InstitutionProxy;

public class InstitutionProxyRenderer extends ProxyRenderer<InstitutionProxy> {

    private static medizin.client.ui.view.roo.InstitutionProxyRenderer INSTANCE;

    protected InstitutionProxyRenderer() {
        super(new String[] { "institutionName" });
    }

    public static medizin.client.ui.view.roo.InstitutionProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new InstitutionProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(InstitutionProxy object) {
        if (object == null) {
            return "";
        }
        return object.getInstitutionName() + " (" + object.getInstitutionName() + ")";
    }
}
