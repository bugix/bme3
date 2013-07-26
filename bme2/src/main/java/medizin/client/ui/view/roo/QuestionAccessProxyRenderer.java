package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.shared.AccessRights;

public class QuestionAccessProxyRenderer extends ProxyRenderer<UserAccessRightsProxy> {

    private static medizin.client.ui.view.roo.QuestionAccessProxyRenderer INSTANCE;

    protected QuestionAccessProxyRenderer() {
        super(new String[] { "id" });
    }

    public static medizin.client.ui.view.roo.QuestionAccessProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionAccessProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(UserAccessRightsProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
