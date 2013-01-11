package medizin.client.ui;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
/**
 * delegates acceptClicked and rejectClicked
 * @author masterthesis
 *
 */
public interface DeclineEmailPopupDelagate {
    void acceptClicked(EntityProxy entityProxy);
    void rejectClicked(EntityProxy entityProxy, String message);
}
