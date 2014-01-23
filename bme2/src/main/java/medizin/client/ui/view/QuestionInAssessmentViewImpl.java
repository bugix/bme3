package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.view.roo.McProxyRenderer;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionInAssessmentViewImpl extends Composite implements QuestionInAssessmentView, RecordChangeHandler {

	private static QuestionInAssessmentViewImplUiBinder uiBinder = GWT
			.create(QuestionInAssessmentViewImplUiBinder.class);

	interface QuestionInAssessmentViewImplUiBinder extends
			UiBinder<Widget, QuestionInAssessmentViewImpl> {
	}

	protected List<String> paths = new ArrayList<String>();
	private Delegate delegate;
	private BmeConstants constants = GWT.create(BmeConstants.class);
	private Map<String, String> columnName=new HashMap<String, String>();
	private List<String> columnNameorder = new ArrayList<String>();
	private List<String> path = new ArrayList<String>();
	private String columnHeader;
	public int x;
	public int y;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "name";
	private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");

	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	ScrollPanel scrollpanel;
	
	@UiField
	ScrollPanel scrollDetailPanel;

	@UiField(provided = true)
	AdvanceCellTable<AssesmentQuestionProxy> table;

	@UiField(provided = true)
	public MySimplePager pager;
	
	@UiField
	QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl;
	
	Map<String, Object> serachField;
	
	List<String> searchField = new ArrayList<String>();
	
	private final EventBus eventBus;

	@UiField
	@Ignore
	public DefaultSuggestBox<AssesmentProxy, EventHandlingValueHolderItem<AssesmentProxy>> assessmentSuggestBox;

	public QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl() {
		return questionAdvancedSearchSubViewImpl;
	}

	public void setQuestionAdvancedSearchSubViewImpl(QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl) {
		this.questionAdvancedSearchSubViewImpl = questionAdvancedSearchSubViewImpl;
	}

	
	public QuestionInAssessmentViewImpl(EventBus eventBus,Boolean questionAddRightsFlag) {
		this.eventBus = eventBus;
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<AssesmentQuestionProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);

		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	delegate.splitLayoutPanelResized();
            }
        };  
        
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);
	
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style","position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		init();
		
		assessmentSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (assessmentSuggestBox.getSelected() != null)
				{
					Long assessmentId = assessmentSuggestBox.getSelected().getId();
					delegate.displayQuestionByAssessment(assessmentId);
				}
			}
		});
	}
	
	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.QUESTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollpanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}

	@Override
	public void setAssessmentSuggestBoxPickerValues(List<AssesmentProxy> values) {
		DefaultSuggestOracle<AssesmentProxy> suggestOracle1 = (DefaultSuggestOracle<AssesmentProxy>) assessmentSuggestBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		assessmentSuggestBox.setSuggestOracle(suggestOracle1);
		assessmentSuggestBox.setRenderer(new AbstractRenderer<AssesmentProxy>() {

			@Override
			public String render(AssesmentProxy object) {
				if(object!=null)
				{
					return object == null ? "" : object.getName() + " - " + McProxyRenderer.instance().render(object.getMc()) + " - " + dateTimeFormat.format(object.getDateOfAssesment());
				}
				else
				{
					return "";
				}
			}
		});
		assessmentSuggestBox.setWidth(250);
		if (values.size() > 0)
			assessmentSuggestBox.setSelected(values.get(0));
	}
	
	private void init() {

		columnName.put(constants.id(), "id");
		columnNameorder.add(constants.id());
		paths.add("id");
		table.addColumn(new TextColumn<AssesmentQuestionProxy>() {

			Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

				public String render(java.lang.Long obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object == null ? null : object.getQuestion().getId());
			}
		}, constants.id(),true);
		
		columnName.put(constants.questionShortName(), "questionShortName");
		columnNameorder.add(constants.questionShortName());
		paths.add("question.questionShortName");
		table.addColumn(new TextColumn<AssesmentQuestionProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object == null ? null : object.getQuestion().getQuestionShortName()==null?"":object.getQuestion().getQuestionShortName());
			}
		},constants.questionShortName(),true );
		
		
	
		
		columnName.put(constants.questionText(), "questionText");
		columnNameorder.add(constants.questionText());
		paths.add("question.questionText");
		table.addColumn(new Column<AssesmentQuestionProxy, AssesmentQuestionProxy>(
				new QuestionTextCell()) {
			@Override
			public AssesmentQuestionProxy getValue(AssesmentQuestionProxy object) {
				return object == null ? null : object;
			}
		}, constants.questionText(),true);

	
		columnName.put(constants.questionType(), "questionType");
		columnNameorder.add(constants.questionType());
		paths.add("question.questionType");
		table.addColumn(new TextColumn<AssesmentQuestionProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object == null ? null : object.getQuestion().getQuestionType()==null?"":object.getQuestion().getQuestionType().getShortName());
			}
		},constants.questionType(),true );
		
		// Alternative columns
		/*columnName.put(constants.auther(), "autor");
		columnNameorder.add(constants.auther());
		paths.add("autor");
		table.addColumn(new TextColumn<AssesmentQuestionProxy>() {

			Renderer<String> renderer = new AbstractRenderer<String>(){

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object == null ? null : object.getAutor().getName());
			}
		},constants.auther(),false);*/
		
		path=getPath();
		addMouseDownHandler();
		addColumnSortHandler();
	}

	public void addColumnOnMouseout()
	{
		Set<String> selectedItems = table.getPopup().getMultiSelectionModel().getSelectedSet();
		
		int j = table.getColumnCount();
		while (j > 0) {
			
			table.removeColumn(0);
			j--;
		}

		path.clear();

		Iterator<String> i;
		if (selectedItems.size() == 0) {

			i = table.getPopup().getDefaultValue().iterator();

		} else {
			i = selectedItems.iterator();
		}

		Iterator<String> i1=getColumnNameorder().iterator();

		while (i1.hasNext()) {
		
			
			String colValue=i1.next();

			if(selectedItems.contains(colValue) || table.getInitList().contains(colValue))
			{
				
				if(table.getInitList().contains(colValue))
				{
					table.getInitList().remove(colValue);
				}
			columnHeader = colValue;
			String colName=(String)columnName.get(columnHeader);
			path.add(colName.toString());
				

			if(columnHeader==constants.questionText()){
				table.addColumn(new Column<AssesmentQuestionProxy, AssesmentQuestionProxy>(
						new QuestionTextCell()) {
					{
						this.setSortable(true);
					}
					@Override
					public AssesmentQuestionProxy getValue(AssesmentQuestionProxy object) {
						return object == null ? null : object;
					}
				}, constants.questionText(),false);
				
			}else {
				table.addColumn(new TextColumn<AssesmentQuestionProxy>() {
	
					{
						this.setSortable(true);
					}
	
					Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
	
						public String render(java.lang.String obj) {
							return obj == null ? "" : String.valueOf(obj);
						}
					};
	
					
					String tempColumnHeader = columnHeader;
	
					@Override
					public String getValue(AssesmentQuestionProxy object) {
	
						if(object !=null ){
							if (tempColumnHeader == constants.id()) {
								return renderer.render(object.getId()!=null?String.valueOf(object.getId()):"");
								
							} else if (tempColumnHeader == constants.questionShortName()) {
								return renderer.render(object.getQuestion() == null ? "" : object.getQuestion().getQuestionShortName()!=null?object.getQuestion().getQuestionShortName():"");
								
							} else if (tempColumnHeader == constants.questionType()) {
								return renderer.render(object.getQuestion() == null ? "" : object.getQuestion().getQuestionType().getShortName()!=null?object.getQuestion().getQuestionType().getShortName():"");
								
							} 
							else {
								return "";
							}
						}else{
							return "";
						}
					}
				}, columnHeader, false);
		  }
		}
	}
		table.addLastColumn();
}
	private void addMouseDownHandler() {
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				Log.info("mouse down");

				x = event.getClientX();
				y = event.getClientY();	
			}
		}, MouseDownEvent.getType());
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				table.getPopup().hide();
				addColumnOnMouseout();
				
			}
		}, MouseOutEvent.getType());
	}
	
	 public void addColumnSortHandler(){
	    	
			table.addColumnSortHandler(new ColumnSortEvent.Handler() {

				@Override
				public void onColumnSort(ColumnSortEvent event) {
		
					Column<AssesmentQuestionProxy, String> col = (Column<AssesmentQuestionProxy, String>) event.getColumn();
					
					
					int index = table.getColumnIndex(col);
					
					Log.info("call for sort " + path.size() + "--index--" + index+ "cc=" + table.getColumnCount());
					if (index == (table.getColumnCount() - 1)) {
						
						table.getPopup().setPopupPosition(x-10, y);
						table.getPopup().show();
		
					} else {
						if(table.getRowCount() > 0 ){
							Log.info("call for sort " + path.size() + "--index--"+ index);
							sortname = path.get(index);
							Log.info("sort column name is " + sortname);
							sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
							Log.info("Call Init Search from addColumnSortHandler");
							delegate.columnClickedForSorting(sortname,sortorder);
						}
					}
				}
			});
		}
	 
	@Override
	public AdvanceCellTable<AssesmentQuestionProxy> getTable() {

		return table;
	}

	@Override
	public String[] getPaths() {

		return paths.toArray(new String[paths.size()]);

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@UiField
	SimplePanel detailsPanel;

	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}

	private static class QuestionTextCell extends AbstractCell<AssesmentQuestionProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				AssesmentQuestionProxy value, SafeHtmlBuilder sb) {
			// Always do a null check on the value. Cell widgets can pass null
			// to cells
			// if the underlying data contains a null, or if the data arrives
			// out of order.
			

			if (value == null || value.getQuestion() == null) {
				return;
			}
			String text = SafeHtmlUtils.fromString(new HTML(value.getQuestion().getQuestionText()).getText()).asString();
			
			String beginn = "<div title='"+ text +"' style=\"white-space:normal;";
			String end = "</div>";
			
			if (Status.DEACTIVATED.equals(value.getQuestion().getStatus()) == false && Status.ACTIVE.equals(value.getQuestion().getStatus()) == false)
			{
				if (!value.getQuestion().getIsAcceptedAdmin()) {
					beginn += "color:red; ";
				}
				if (!value.getQuestion().getIsAcceptedRewiever()) {
					beginn += "font-style:italic; ";
				}
				//if (!value.getIsActive()) {
				if (!Status.ACTIVE.equals(value.getQuestion().getStatus())) {
					beginn += "text-decoration: line-through; ";
				}
			}

			beginn += "\">";
			sb.appendHtmlConstant(beginn);
			//sb.appendHtmlConstant(value.getQuestionText());
			sb.appendHtmlConstant(getFirstSomeChar(text));
			sb.appendHtmlConstant(end);

		}
		
		private String getFirstSomeChar(String text) {
			if(text.length() > 100) {
				return text.substring(0,100) + "...";
			}
			return text;
		}
	}

	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			McAppConstant.TABLE_PAGE_SIZE = pagesize;
		} else if (event.getRecordValue().matches("\\d+")) {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);		
	}
	
	public void removeAdvancedSearchFromView()
	{
		Document.get().getElementById("advancedSearchViewDiv").removeFromParent();
	}
	
	public SplitLayoutPanel getSplitLayoutPanel() {
		return splitLayoutPanel;
	}

	public ScrollPanel getScrollpanel() {
		return scrollpanel;
	}

	public Map<String, String> getColumnName() {
		return columnName;
	}

	public List<String> getColumnNameorder() {
		return columnNameorder;
	}
	public List<String> getPath(){
		return this.paths;
	}
	
	public DefaultSuggestBox<AssesmentProxy, EventHandlingValueHolderItem<AssesmentProxy>> getAssessmentSuggestBox() {
		return assessmentSuggestBox;
	}
	
	@Override
	public ScrollPanel getScrollDetailPanel() {
		return scrollDetailPanel;
	}
}
