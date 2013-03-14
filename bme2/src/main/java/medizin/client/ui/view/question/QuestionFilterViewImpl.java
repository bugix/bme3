package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class QuestionFilterViewImpl extends PopupPanel implements QuestionView {

	private static QuestionFilterPopupUiBinder uiBinder = GWT
			.create(QuestionFilterPopupUiBinder.class);

	interface QuestionFilterPopupUiBinder extends
			UiBinder<Widget, QuestionFilterViewImpl> {
	}

	private class CheckBoxItem {
		public CheckBox checkbox;
		public String name;

		public CheckBoxItem(CheckBox box, String n) {
			checkbox = box;
			name = n;
		}
	}

	private ArrayList<CheckBoxItem> fields = new ArrayList<CheckBoxItem>();
	private int maxApplicableFilters;
	private int minApplicableFilters = 1;
	private boolean selectionChanged = false;
	private boolean addBoxesShown = true;
	private Delegate delegate;
	private Presenter presenter;
	private List<String> tableFilters; // new ArrayList<String>();
	private List<String> whereFilters;// = new ArrayList<String>();

	private MultiWordSuggestOracle keywordoracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle autheroracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle revieweroracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle specificationoracle = new MultiWordSuggestOracle();

	private final BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField
	FocusPanel filterPanelRoot;

	@UiField
	CheckBox auther;

	@UiField
	CheckBox reviewer;
	
	@UiField
	CheckBox questionText;
	
	@UiField
	CheckBox instructionText;
	
	@UiField
	CheckBox keywordText;
	
	@UiField
	CheckBox showNew;

	/*@UiField(provided = true)
	public ValueListBox<Status> status=new ValueListBox<Status>(new AbstractRenderer<Status>() {

		@Override
		public String render(Status object) {
			// TODO Auto-generated method stub
			return object==null ?" ": String.valueOf(object);
		}
	});*/
	
	@UiField(provided = true)
	public ValueListBox<InstitutionProxy> institutionListBox=new ValueListBox<InstitutionProxy>(new AbstractRenderer<InstitutionProxy>() {

		@Override
		public String render(InstitutionProxy object) {
			// TODO Auto-generated method stub
			return object==null ?" ": String.valueOf(object.getInstitutionName());			
		}
	});
	
	@UiField(provided = true)
	public ValueListBox<QuestionEventProxy> specialiationListBox=new ValueListBox<QuestionEventProxy>(new AbstractRenderer<QuestionEventProxy>() {

		@Override
		public String render(QuestionEventProxy object) {
			// TODO Auto-generated method stub
			return object==null ?" ": object.getEventName();
		}
	});
	
	@UiField
	public DateBox createStartDate;
	
	@UiField
	public DateBox createEndDate;
	
	@UiField
	public DateBox usedMcStartDate;
	
	@UiField
	public DateBox usedMcEndDate;
	
	
	/*@UiField
	public DateBox dateTextBox;
	dateTextBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MMM dd, yyyy")));
				dateTextBox.setValue(osceDayProxy.getOsceDate());
	*/
	
	/*@UiField(provided = true)
	public ValueListBox<Status> status = new ValueListBox<Status>(new AbstractRenderer<Status>() {

		public String render(Status obj) {
			return obj == null ? "" : String.valueOf(obj);
		}
	});
*/	
	/*@UiField
	ListBox status;
*/
	@UiField
	IconButton resetButton;

	@UiHandler("resetButton")
	void onClick(ClickEvent e) {
		/*
		 * Iterator<CheckBoxItem> iter = fields.iterator(); while
		 * (iter.hasNext()) { iter.next().checkbox.setValue(false);
		 * 
		 * }
		 */
		auther.setValue(false);
		reviewer.setValue(false);
		questionText.setValue(false);
		instructionText.setValue(false);
		keywordText.setValue(false);
		showNew.setValue(false);
		
		institutionListBox.setValue(null);
		specialiationListBox.setValue(null);
		
		createStartDate.getTextBox().setText("");
		createEndDate.getTextBox().setText("");
		
		usedMcStartDate.getTextBox().setText("");
		usedMcEndDate.getTextBox().setText("");
		//status.setSelectedIndex(status.getItemCount()-1);
		
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

			String msg = "Searching for: ";
			Iterator<String> i = getFilters().iterator();
			while (i.hasNext())
				msg = msg + i.next() + ", ";
			Log.info(msg);
		}
	}

	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for (CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.name);
			}
		}

		getTableName();
		return filters;
	}

	public List<String> getTableFilters() {

		return tableFilters;
	}

	public List<String> getWhereFilters() {

		return whereFilters;
	}

	public void getTableName() {
		// List<String> filters = new ArrayList<String>();
		tableFilters = null;
		whereFilters = null;
		tableFilters = new ArrayList<String>();
		whereFilters = new ArrayList<String>();

	}

	public boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public QuestionFilterViewImpl() {
		super(true);

		add(uiBinder.createAndBindUi(this));

		/*status.addItem("True");
		status.addItem("False");
		status.addItem("   ");*/

		BmeConstants constants = GWT.create(BmeConstants.class);
		resetButton.setText(constants.reset());

		
		/*auther.setText("Author");
		reviewer.setText("Reviwer");
		questionText.setText("Question Text");
		instructionText.setText("Instruction");
		keywordText.setText("Keyword");*/
		// TopicName Keyword ItemName ItemName RoleName CheckListItem ItemValue

		/*
		 * maxApplicableFilters = fields.size(); Iterator<CheckBoxItem>
		 * fieldIter = fields.iterator(); fields.get(0).checkbox.setValue(true);
		 * 
		 * while (fieldIter.hasNext()) { CheckBox box =
		 * fieldIter.next().checkbox; box.addValueChangeHandler(new
		 * CheckBoxChangeHandler()); }
		 */
	}

	public void clearSelectionChanged() {
		selectionChanged = false;
	}

	public boolean selectionChanged() {
		return selectionChanged;
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

	public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return super.addCloseHandler(handler);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return null;
	}

	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub

	}

	@Override
	public CellTable<QuestionProxy> getTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInstitutionFilter(List<InstitutionProxy> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpecialisationFilter(List<QuestionEventProxy> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getSearchFiledValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuickSearchBox getSerachBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSearchValue() {
		List<String> searchField = new ArrayList<String>();
		return searchField;
	}
}
