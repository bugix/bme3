package medizin.shared.criteria;

public class AdvancedSearchCriteria {
	
	private PossibleFields possibleFields;
	
	private BindType bindType;
	
	private Comparison comparison;
	
	private String value;
	
	//used in display purpose only
	private String shownValue;

	public PossibleFields getPossibleFields() {
		return possibleFields;
	}

	public void setPossibleFields(PossibleFields possibleFields) {
		this.possibleFields = possibleFields;
	}

	public BindType getBindType() {
		return bindType;
	}

	public void setBindType(BindType bindType) {
		this.bindType = bindType;
	}

	public Comparison getComparison() {
		return comparison;
	}

	public void setComparison(Comparison comparison) {
		this.comparison = comparison;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getShownValue() {
		return shownValue;
	}

	public void setShownValue(String shownValue) {
		this.shownValue = shownValue;
	}
}