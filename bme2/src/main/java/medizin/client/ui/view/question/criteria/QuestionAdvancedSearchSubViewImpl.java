package medizin.client.ui.view.question.criteria;

import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.shared.criteria.AdvancedSearchCriteria;
import medizin.shared.criteria.BindType;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RowCountChangeEvent;

public class QuestionAdvancedSearchSubViewImpl extends Composite implements
		QuestionAdvancedSearchSubView {

	private static QuestionAdvancedSearchSubViewImplUiBinder uiBinder = GWT
			.create(QuestionAdvancedSearchSubViewImplUiBinder.class);
	
	interface QuestionAdvancedSearchSubViewImplUiBinder extends
			UiBinder<Widget, QuestionAdvancedSearchSubViewImpl> {
	}
	
	private Delegate delegate;
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField
	IconButton addKeyword;
	
	@UiField
	IconButton addQuestionEvent;
	
	@UiField
	IconButton addTextSearch;
	
	@UiField
	IconButton addDate;
	
	@UiField
	IconButton addMc;
	
	@UiField
	IconButton addUserType;
	
	@UiField
	IconButton addMediaAvailability;
	
	@UiField
	IconButton addQuestionType;
	
	@UiField(provided = true)
	CellTable<AdvancedSearchCriteria> table;
	
	@UiField(provided = true)
	SimplePager pager;
		
	public QuestionAdvancedSearchSubViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AdvancedSearchCriteria>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		addKeyword.setText(constants.keyword());
		addQuestionEvent.setText(constants.questionEventAdvSearch());
		addTextSearch.setText(constants.textSearch());
		addDate.setText(constants.date());
		addMc.setText(constants.mcAdvSearch());
		addUserType.setText(constants.userType());
		addMediaAvailability.setText(constants.mediaAvailability());
		addQuestionType.setText(constants.questionType());
		
		init();
	}

	private void init() {
		table.addColumn(new Column<AdvancedSearchCriteria, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(AdvancedSearchCriteria object) {
				switch (object.getPossibleFields()) {
				
					case KEYWORD:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-tag\"></span>").toSafeHtml();
						
					case QUESTION_EVENT:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-link\"></span>").toSafeHtml();
					
					case COMMENT: 
					case QUESTION_TEXT:
					case ANSWER_TEXT:
					case QUESTION_SHORT_NAME:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-comment\"></span>").toSafeHtml();
						
					case CHANGED_QUESTION_DATE:
					case CREATED_QUESTION_DATE:
					case USED_IN_MC_DATE:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-calendar\"></span>").toSafeHtml();
						
					case MC:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-script\"></span>").toSafeHtml();
						
					case AUTHOR:
					case REVIEWER:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-person\"></span>").toSafeHtml();
						
					case MEDIA_AVAILABILITY:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-image\"></span>").toSafeHtml();
						
					case QUESTION_TYPE:
					case QUESTION_TYPE_NAME:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-suitcase\"></span>").toSafeHtml();
						
						
					default:
						return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-wrench\"></span>").toSafeHtml();
						
				}
			}
		});
		
		table.addColumn(new TextColumn<AdvancedSearchCriteria>() {

			Renderer<BindType> renderer = new EnumRenderer<BindType>();
			
			@Override
			public String getValue(AdvancedSearchCriteria object) {
				return renderer.render(object.getBindType());
			}
		}, constants.bindtype());
		
		table.addColumn(new TextColumn<AdvancedSearchCriteria>() {

			@Override
			public String getValue(AdvancedSearchCriteria object) {
				return object.getShownValue();
			}
		}, constants.criterion());
		
		ActionCell.Delegate<AdvancedSearchCriteria> deleteDelegate = new ActionCell.Delegate<AdvancedSearchCriteria>() {

			@Override
			public void execute(AdvancedSearchCriteria object) {
				delegate.deleteAdvancedSearchCriteriaClicked(object);
			}
		};	
		
		table.addColumn(new IdentityColumn<AdvancedSearchCriteria>(new ActionCell<AdvancedSearchCriteria>(McAppConstant.DELETE_ICON, deleteDelegate)));
		table.addColumnStyleName(table.getColumnCount() - 1, "iconCol");
		
		table.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
			
			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
				if (event.getNewRowCount() > 0){
					table.setVisible(true);
					pager.setVisible(true);
				}
				else{
					table.setVisible(false);
					pager.setVisible(false);
				}
			}
		});
		
		table.setVisible(false);
		pager.setVisible(false);
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	@UiHandler("addKeyword")
	public void addKeywordClicked(ClickEvent event)
	{
		delegate.keywordAddClicked(addKeyword);
	}
	
	@UiHandler("addQuestionEvent")
	public void addQuestionEventClicked(ClickEvent event)
	{
		delegate.questionEventAddClicked(addKeyword);
	}
	
	@UiHandler("addTextSearch")
	public void addTextSearchClicked(ClickEvent event)
	{
		delegate.textSearchAddClicked(addTextSearch);
	}
	
	@UiHandler("addDate")
	public void addDateClicked(ClickEvent event)
	{
		delegate.dateAddClicked(addDate);
	}
	
	@UiHandler("addMc")
	public void addMcClicked(ClickEvent event)
	{
		delegate.mcAddClicked(addMc);
	}
	
	@UiHandler("addUserType")
	public void addUserTypeClicked(ClickEvent event)
	{
		delegate.userTypeAddClicked(addUserType);
	}
	
	@UiHandler("addMediaAvailability")
	public void addMediaAvailabilityClicked(ClickEvent event)
	{
		delegate.mediaAvailabilityAddClicked(addMediaAvailability);
	}
	
	@UiHandler("addQuestionType")
	public void addQuestionTypeClicked(ClickEvent event)
	{
		delegate.questionTypeAddClicked(addQuestionType);
	}
	
	public IconButton getAddMc() {
		return addMc;
	}

	public CellTable<AdvancedSearchCriteria> getTable() {
		return table;
	}

	public void setTable(CellTable<AdvancedSearchCriteria> table) {
		this.table = table;
	}	
}
