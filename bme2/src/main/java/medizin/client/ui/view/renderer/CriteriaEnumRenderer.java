package medizin.client.ui.view.renderer;

import medizin.shared.criteria.Comparison;
import medizin.shared.i18n.BmeEnumConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;

public class CriteriaEnumRenderer<T extends Enum<?>> extends AbstractRenderer<T> {
	
	protected final BmeEnumConstants constants = GWT.create(BmeEnumConstants.class);
	private final Type rendererType;
	
	public static enum Type {
		DEFAULT, KEYWORD, QUESTION_EVENT, TEXT_SEARCH, MC, USER_TYPE, MEDIA_AVAILABILITY, QUESTION_TYPE, USERTYPE;
	}
	
	public CriteriaEnumRenderer() {
		this(Type.DEFAULT);
	}
	
	public CriteriaEnumRenderer(Type rendererType) {
		this.rendererType = rendererType;
	}
	
	protected String getIdentifier(T object) {
		if (!(object instanceof Comparison ) || rendererType == Type.DEFAULT) {
			return object.name();
		} else {
			return rendererType.name() + "_" + object.name();
		}
	}

	@Override
	public String render(T object) {
		if (object == null) return "";
		return constants.getString(getIdentifier(object));
	}
	

}
