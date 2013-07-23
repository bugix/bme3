package medizin.client.ui.view.renderer;

import java.io.IOException;

import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;

public class EnumRenderer<T extends Enum<?>> extends AbstractRenderer<T> {
	protected final BmeEnumConstants constants = GWT.create(BmeEnumConstants.class);

	@Override
	public String render(T object) {
		if (object == null) return "";
		return constants.getString(object.name());
	}
	
	@Override
	public void render(T object, Appendable appendable)
			throws IOException {
		String s = render(object);
		appendable.append(s);	
	}
}
