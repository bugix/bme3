package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import medizin.client.ui.widget.IconButton;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


public class QuestionPrintFilterViewImpl extends PopupPanel {

	private static QuestionPrintFilterViewImplUiBinder uiBinder = GWT
			.create(QuestionPrintFilterViewImplUiBinder.class);

	interface QuestionPrintFilterViewImplUiBinder extends
			UiBinder<Widget, QuestionPrintFilterViewImpl> {
	}

	static QuestionPrintFilterViewImpl questionPrintFilterViewImpl;

	private ArrayList<CheckBoxItem> fields = new ArrayList<CheckBoxItem>();
	
	private int maxApplicableFilters;
	
	private int minApplicableFilters = 1;
	
	private boolean selectionChanged = false;
	
	@UiField
	SpanElement printFor;
	
	@UiField
	FocusPanel filterPanelRoot;

	@UiField
	CheckBox details;
	
	@UiField
	CheckBox keywords;

	@UiField
	CheckBox learningObjective;
	
	@UiField
	CheckBox usedInMC;
	
	@UiField
	CheckBox answer;
	
	@UiField
	IconButton resetButton;

	@UiField
	IconButton printButton;

	@UiHandler("resetButton")
	void onResetButtonClick(ClickEvent e) {
		this.resetAllCheckBox();
	}

	private void resetAllCheckBox() {
		Iterator<CheckBoxItem> iter = fields.iterator();
		while (iter.hasNext()) {
			CheckBoxItem checkBoxItem = iter.next();
			checkBoxItem.checkbox.setValue(false);
			checkBoxItem.checkbox.setEnabled(true);
		}
		fields.get(0).checkbox.setValue(true);
		fields.get(0).checkbox.setEnabled(false);
	}

	private class CheckBoxItem {
		public CheckBox checkbox;
		public String name;

		public CheckBoxItem(CheckBox box, String n) {
			checkbox = box;
			name = n;
		}
	}


	private class CheckBoxChangeHandler implements ValueChangeHandler<Boolean> {

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			selectionChanged = true;
			int uncheckedBoxes = 0;
			for (CheckBoxItem item : fields) {
				if (item.checkbox.getValue() == false) {
					uncheckedBoxes++;
				}
				item.checkbox.setEnabled(true);
			}

			if (uncheckedBoxes >= fields.size() - minApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			} else if (fields.size() - uncheckedBoxes >= maxApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (!item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			}
		}
	}

	public QuestionPrintFilterViewImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				int mouseX = event.getClientX();
				int mouseY = event.getClientY();

				if (mouseX < getAbsoluteLeft()
						|| mouseX > getAbsoluteLeft() + getOffsetWidth()
						|| mouseY < getAbsoluteTop()
						|| mouseY > getAbsoluteTop() + getOffsetHeight()) {

					// TODO: handle it from view
					// view.updateSearch();
					hide();
				}
			}
		});

		BmeConstants constants = GWT.create(BmeConstants.class);
		printFor.setInnerText(constants.printFor());
		printButton.setText(constants.print());
		resetButton.setText(constants.resetFilters());
		
		initCheckBox(details, "details", constants.details());
		initCheckBox(keywords, "keywords", constants.keyword());
		initCheckBox(usedInMC, "usedInMC", constants.usedInMC());
		initCheckBox(learningObjective, "learningObjective", constants.learning());
		initCheckBox(answer, "answer", constants.answer());

		maxApplicableFilters = fields.size();

		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBox box = fieldIter.next().checkbox;
			box.addValueChangeHandler(new CheckBoxChangeHandler());
		}
		this.setAutoHideEnabled(true);
	}

	private void initCheckBox(CheckBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new CheckBoxItem(uiField, name));
	}

	public void setMaxApplicableFilters(int n) {
		maxApplicableFilters = n;
	}

	public void setMinApplicableFilters(int n) {
		if (minApplicableFilters > fields.size())
			minApplicableFilters = fields.size();
		minApplicableFilters = n;
	}

	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for (CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.checkbox.getText());
			}
		}
		return filters;
	}
	
	public com.google.gwt.event.shared.HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return super.addCloseHandler(handler);
	}

	public void clearSelectionChanged() {
		selectionChanged = false;
	}

	public boolean selectionChanged() {
		return selectionChanged;
	}

	@Override
	public void show() {
		this.resetAllCheckBox();
		super.show();
	}

	public IconButton getPrintButton() {
		return printButton;
	}
	
	public String getDetailsCheckBoxValue()
	{
		return details.getValue().toString();
	}
	
	public String getKeywordCheckBoxValue()
	{
		return keywords.getValue().toString();
	}
	
	public String getLearningObjectiveCheckBoxValue()
	{
		return learningObjective.getValue().toString();
	}
	
	public String getUsedInMcCheckBoxValue()
	{
		return usedInMC.getValue().toString();
	}
	
	public String getAnswerCheckBoxValue()
	{
		return answer.getValue().toString();
	}
}
