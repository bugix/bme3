package medizin.client.ui.view.roo;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;

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
        return object.getShortName() + " (" + object.getShortName() + ")";
    }
}
