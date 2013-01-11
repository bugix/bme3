package medizin.client.ui.view.roo;

import medizin.client.proxy.PersonProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class PersonProxyRenderer extends ProxyRenderer<PersonProxy> {

    private static medizin.client.ui.view.roo.PersonProxyRenderer INSTANCE;

    protected PersonProxyRenderer() {
        super(new String[] { "name" });
    }

    public static medizin.client.ui.view.roo.PersonProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(PersonProxy object) {
        if (object == null) {
            return "";
        }
        return object.getName() + " (" + object.getName() + ")";
    }
}
