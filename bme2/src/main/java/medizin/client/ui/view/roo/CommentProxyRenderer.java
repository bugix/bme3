package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.CommentProxy;

public class CommentProxyRenderer extends ProxyRenderer<CommentProxy> {

    private static medizin.client.ui.view.roo.CommentProxyRenderer INSTANCE;

    protected CommentProxyRenderer() {
        super(new String[] { "comment" });
    }

    public static medizin.client.ui.view.roo.CommentProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(CommentProxy object) {
        if (object == null) {
            return "";
        }
        return object.getComment() + " (" + object.getComment() + ")";
    }
}
