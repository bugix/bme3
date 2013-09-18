package medizin.client.ui.view.roo;

import medizin.client.proxy.UserAccessRightsProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

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
        return object.getId().toString();
    }
}
