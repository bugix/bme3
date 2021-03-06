package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.QuestionSaveEvent;
import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.Sorting;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionViewImpl extends Composite implements QuestionView, RecordChangeHandler {

	private static QuestionViewImplUiBinder uiBinder = GWT.create(QuestionViewImplUiBinder.class);

	interface QuestionViewImplUiBinder extends UiBinder<Widget, QuestionViewImpl> {}

	//private Presenter presenter;
	protected List<String> paths = new ArrayList<String>();
	private Delegate delegate;
	//private McAppRequestFactory requests;
	//private PlaceController placeController;
	private BmeConstants constants = GWT.create(BmeConstants.class);
	private Map<String, String> columnName=new HashMap<String, String>();
	private List<String> columnNameorder = new ArrayList<String>();
	private List<String> path = new ArrayList<String>();
	private String columnHeader;
	public int x;
	public int y;
	private Sorting sortorder = Sorting.ASC;
	private String sortname = "name";
	
	/*
	 * @UiField
	@Ignore
	public DefaultSuggestBox<OsceProxy, EventHandlingValueHolderItem<OsceProxy>> osceValue;

osceMap.put("osceValue", osceValue.getTextField().advancedTextBox);

@Override
	public void setOsceValues(List<OsceProxy> emptyList) {
		// TODO Auto-generated method stub
		//Issue # 122 : Replace pull down with autocomplete.
		//osceValue.setAcceptableValues(emptyList);
		DefaultSuggestOracle<OsceProxy> suggestOracle1 = (DefaultSuggestOracle<OsceProxy>) osceValue.getSuggestOracle();
		suggestOracle1.setPossiblilities(emptyList);
		osceValue.setSuggestOracle(suggestOracle1);
		osceValue.setRenderer(new AbstractRenderer<OsceProxy>() {

			@Override
			public String render(OsceProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		//change {
		osceValue.setWidth(110);
		//change }
		//osceValue.setRenderer(new SpecialisationProxyRenderer());
		//copiedOsce.setAcceptableValues(emptyList);
		//copiedOsce.setSuggestOracle(suggestOracle1);
		//copiedOsce.setRenderer(new AbstractRenderer<OsceProxy>() {

		DefaultSuggestOracle<OsceProxy> suggestOracle = (DefaultSuggestOracle<OsceProxy>) copiedOsce.getSuggestOracle();
		suggestOracle1.setPossiblilities(emptyList);
		copiedOsce.setSuggestOracle(suggestOracle1);
		copiedOsce.setRenderer(new AbstractRenderer<OsceProxy>() {

			@Override
			public String render(OsceProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getName();
				}
				else
				{
					return "";
				}
			}
		});

		// change {
		copiedOsce.setWidth(110);
		
		// change }
		//Issue # 122 : Replace pull down with autocomplete.
		
	}
	
	
	@Override
	public Set<TaskProxy> getTaskValue()
	{
		//Issue # 122 : Replace pull down with autocomplete.
		//return osceValue.getValue().getTasks();
		if(osceValue.getSelected()!=null)
		{
		return osceValue.getSelected().getTasks();
		}
		else
			return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	 * 
	 */
	
	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	ScrollPanel scrollpanel;
	
	@UiField(provided = true)
	QuickSearchBox searchBox;

	QuestionFilterViewImpl filterPanel = new QuestionFilterViewImpl();

	@UiField(provided = true)
	AdvanceCellTable<QuestionProxy> table;

	@UiField(provided = true)
	public MySimplePager pager;

	@UiField
	public IconButton filterButton;

	@UiField
	IconButton newQuestion;
	
	@UiField
	IconButton printPdf;
	
	@UiField
	QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl;
	
	@UiField
	ScrollPanel scrollDetailPanel;

	@UiHandler(value = { "newQuestion" })
	public void newButtonClicked(ClickEvent e) {
		delegate.newClicked();
	}
	
	Map<String, Object> serachField;
	
	List<String> searchField = new ArrayList<String>();
	private final EventBus eventBus;

	/*@Override
	public void setInstitutionFilter(List<InstitutionProxy> values)
	{
		filterPanel.institutionListBox.setAcceptableValues(values);
	}*/

	/*@Override
	public void setSpecialisationFilter(List<QuestionEventProxy> values)
	{
		//filterPanel.specialiationListBox.setAcceptableValues(values);	
	}*/
	
	public QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl() {
		return questionAdvancedSearchSubViewImpl;
	}

	public void setQuestionAdvancedSearchSubViewImpl(
			QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl) {
		this.questionAdvancedSearchSubViewImpl = questionAdvancedSearchSubViewImpl;
	}

	
	public QuestionViewImpl(EventBus eventBus,Boolean questionAddRightsFlag, Boolean printBtnFlag) {
		this.eventBus = eventBus;
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<QuestionProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);

		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	/*Double newWidth =splitLayoutPanel.getWidgetSize(scrollpanel);
               	Cookies.setCookie(McAppConstant.QUESTION_VIEW_WIDTH, String.valueOf(newWidth));*/
               	delegate.splitLayoutPanelResized();
            }
        };  
        
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);

		Log.info("Question Save Event Register");
		QuestionSaveEvent.register(this.eventBus, new QuestionSaveEvent.Handler() {
			
			@Override
			public void onSaveClicked(QuestionSaveEvent event) {
				Log.info("Question Save Event Clicked");
				filterPanel.getShowNew().setValue(true);
				filterPanel.getCreativeWork().setValue(true);
				setFieldForSearchBox();
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		filterPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				Log.info("onClose() of serch box is called");
				// Taking previous search criteria.
				List<String> oldSearchValues = new ArrayList<String>(searchField);
				setFieldForSearchBox();
				if(oldSearchValues.equals(searchField)){
					Log.info("No changes is make by user for search");
					return;
				}else{
					Log.info("Changes is make by user for search criteria");
					// Calling perform search as changes is make by user.
					delegate.performSearch(searchBox.getValue());
				}
				
			}
		});

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				// delegate.performSearch(searchBox.getValue());
				Log.info("serach click");
				
				setFieldForSearchBox();
				
				/*searchField.clear();
				
				if (filterPanel.auther.getValue())
				{	
					searchField.add("author");
					searchField.add(filterPanel.auther.getValue() ? "true" : "false");
					//searchFileds.add(new SearchValue("author", filterPanel.auther.isChecked() ? "true" : "false"));
				}
				
				if (filterPanel.reviewer.getValue())
				{
					searchField.add("reviewer");
					searchField.add(filterPanel.reviewer.getValue()? "true" : "false");
					//searchFileds.add(new SearchValue("reviewer", filterPanel.reviewer.isChecked()? "true" : "false"));
				}
				
				if (filterPanel.questionText.getValue())
				{
					searchField.add("quesitontext");
					searchField.add(filterPanel.questionText.getValue()? "true" : "false");
					//searchFileds.add(new SearchValue("quesitontext", filterPanel.questionText.isChecked()? "true" : "false"));
				}
				
				if (filterPanel.instructionText.getValue())
				{
					searchField.add("instruction");
					searchField.add(filterPanel.instructionText.getValue()? "true" : "false");
					//searchFileds.add(new SearchValue("instruction", filterPanel.instructionText.isChecked()? "true" : "false"));
				}
				
				if (filterPanel.keywordText.getValue())
				{
					searchField.add("keyword");
					searchField.add(filterPanel.keywordText.getValue()? "true" : "false");
					//searchFileds.add(new SearchValue("keyword", filterPanel.keywordText.isChecked()? "true" : "false"));
				}
				
				if (filterPanel.showNew.getValue())
				{
					searchField.add("showNew");
					searchField.add(filterPanel.showNew.getValue() ? "true" : "false");
				}
				
				if (filterPanel.institutionListBox.getValue() != null)
				{
					searchField.add("institution");
					searchField.add(filterPanel.institutionListBox.getValue().getId().toString());
					//searchFileds.add(new SearchValue("institution", filterPanel.institutionListBox.getValue().getId().toString()));
				}
				
				if (filterPanel.specialiationListBox.getValue() != null)
				{
					searchField.add("specialiation");
					searchField.add(filterPanel.specialiationListBox.getValue().getId().toString());
					//searchFileds.add(new SearchValue("specialiation", filterPanel.specialiationListBox.getValue().getId().toString()));
				}
				
				if (filterPanel.status.getValue() != null)
				{
					searchField.add("status");
					searchField.add(filterPanel.status.getValue().toString());
					//searchFileds.add(new SearchValue("status", filterPanel.status.getValue().toString()));
				}
				
				if (filterPanel.createStartDate.getValue() != null)
				{
					searchField.add("createdDateFrom");
					searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.createStartDate.getValue()));
					//searchFileds.add(new SearchValue("createdDateFrom", filterPanel.createStartDate.getValue().toString()));
				}
				
				if (filterPanel.createEndDate.getValue() != null)
				{
					searchField.add("createdDateTo");
					searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.createEndDate.getValue()));
					//searchFileds.add(new SearchValue("createdDateTo", filterPanel.createEndDate.getValue().toString()));
				}
				
				if (filterPanel.usedMcStartDate.getValue() != null)
				{
					searchField.add("usedMcFrom");
					searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.usedMcStartDate.getValue()));
					//searchFileds.add(new SearchValue("usedMcFrom", filterPanel.usedMcStartDate.getValue().toString()));
				}
				
				if (filterPanel.usedMcEndDate.getValue() != null)
				{
					searchField.add("usedMcTo");
					searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.usedMcEndDate.getValue()));
					//searchFileds.add(new SearchValue("usedMcTo", filterPanel.usedMcEndDate.getValue().toString()));
				}
				
				delegate.performSearch(searchBox.getValue());*/
				
				table.setVisibleRange(0, McAppConstant.TABLE_PAGE_SIZE);
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style","position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		newQuestion.setText(constants.newQuestion());
		printPdf.setText(constants.print());
		
		if (printBtnFlag == false)
			printPdf.removeFromParent();		
		
		if (questionAddRightsFlag == false){
			newQuestion.removeFromParent();
			//Document.get().getElementById("queAddBtnDiv").removeFromParent();
		}	
		
		init();

		//setting widget width from cookie.
        //setWidgetWidth();
		
	}
	
	@UiHandler("printPdf")
	public void printPdfClicked(ClickEvent event)
	{
		Widget eventSource = (Widget) event.getSource();
		delegate.printPdfClicked(eventSource.getAbsoluteLeft(), eventSource.getAbsoluteTop());
	}
	
	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.QUESTION_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollpanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}
	
	@UiHandler("filterButton")
	public void filterButtonHover(MouseOverEvent event) {
		// System.out.println("Mouse over");
		Log.info("filter panel call");
		showFilterPanel((Widget) event.getSource());
	}
	
	@Override 
	public Map<String,Object> getSearchFiledValue()
	{
		return serachField;
	}

	private void showFilterPanel(Widget eventSource) {
		int x = eventSource.getAbsoluteLeft();
		int y = eventSource.getAbsoluteTop();

		filterPanel.setPopupPosition(x, y);
		filterPanel.show();
		//filterPanel.setSize("415px", "235px");
		// Log.info(filterPanel.getSpecialisationBox().getValue());

	}

	private void init() {

		columnName.put(constants.id(), "id");
		columnNameorder.add(constants.id());
		paths.add("id");
		table.addColumn(new TextColumn<QuestionProxy>() {

			Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

				public String render(java.lang.Long obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getId());
			}
		}, constants.id(),true);
		
		columnName.put(constants.questionShortName(), "questionShortName");
		columnNameorder.add(constants.questionShortName());
		paths.add("questionShortName");
		table.addColumn(new TextColumn<QuestionProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getQuestionShortName()==null?"":object.getQuestionShortName());
			}
		},constants.questionShortName(),true );
				
		columnName.put(constants.questionText(), "questionText");
		columnNameorder.add(constants.questionText());
		paths.add("questionText");
		table.addColumn(new Column<QuestionProxy, QuestionProxy>(
				new QuestionTextCell()) {
			@Override
			public QuestionProxy getValue(QuestionProxy object) {
				return object == null ? null : object;
			}
		}, constants.questionText(),true);

		columnName.put(constants.questionType(), "questionType");
		columnNameorder.add(constants.questionType());
		paths.add("questionType");
		table.addColumn(new TextColumn<QuestionProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getQuestionType()==null?"":object.getQuestionType().getShortName());
			}
		},constants.questionType(),true );
		
		columnName.put(constants.status(), "status");
		columnNameorder.add(constants.status());
		paths.add("status");
		table.addColumn(new TextColumn<QuestionProxy>() {

			EnumRenderer<Status> renderer = new EnumRenderer<Status>();

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getStatus());
			}
		},constants.status(),true );
		
		// Alternative columns
		columnName.put(constants.auther(), "autor");
		columnNameorder.add(constants.auther());
		paths.add("autor");
		table.addColumn(new TextColumn<QuestionProxy>() {

			Renderer<String> renderer = new AbstractRenderer<String>(){

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getAutor().getName());
			}
		},constants.auther(),false);
		
		path=getPath();
		addMouseDownHandler();
		addColumnSortHandler();
		setFieldForSearchBox();
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
				table.addColumn(new Column<QuestionProxy, QuestionProxy>(
						new QuestionTextCell()) {
					{
						this.setSortable(true);
					}
					@Override
					public QuestionProxy getValue(QuestionProxy object) {
						return object == null ? null : object;
					}
				}, constants.questionText(),false);
				
			}else {
				table.addColumn(new TextColumn<QuestionProxy>() {
	
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
					public String getValue(QuestionProxy object) {
	
						if(object !=null ){
							if (tempColumnHeader == constants.id()) {
								return renderer.render(object.getId()!=null?String.valueOf(object.getId()):"");
								
							} else if (tempColumnHeader == constants.questionShortName()) {
								return renderer.render(object.getQuestionShortName()!=null?object.getQuestionShortName():"");
								
							} /*else if (tempColumnHeader == constants.questionText()) {
								return renderer.render(object.getQuestionText()!=null?object.getQuestionText():"");
								
							}*/ else if (tempColumnHeader == constants.questionType()) {
								return renderer.render(object.getQuestionType().getShortName()!=null?object.getQuestionType().getShortName():"");
								
							} else if (tempColumnHeader == constants.status()) {
								return renderer.render(object.getStatus()!=null?object.getStatus().name():"");
								
							} else if (tempColumnHeader == constants.auther()) {
								return renderer.render(object.getAutor()!=null?object.getAutor().getName():"");
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

				/*if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}
				}
				else
				{
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();
					
				}*/

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
		
					Column<QuestionProxy, String> col = (Column<QuestionProxy, String>) event.getColumn();
					
					
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
	public QuickSearchBox getSerachBox()
	{
		return searchBox;
	}

	/*@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}*/

	/*@Inject
	public QuestionViewImpl(McAppRequestFactory requests,
			PlaceController placeController) {
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;

	}*/

	/*@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub

	}*/

	@Override
	public AdvanceCellTable<QuestionProxy> getTable() {

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
		// TODO Auto-generated method stub
		return detailsPanel;
	}

	private static class QuestionTextCell extends AbstractCell<QuestionProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				QuestionProxy value, SafeHtmlBuilder sb) {
			// Always do a null check on the value. Cell widgets can pass null
			// to cells
			// if the underlying data contains a null, or if the data arrives
			// out of order.
			

			if (value == null) {
				return;
			}
			String text = SafeHtmlUtils.fromString(new HTML(value.getQuestionText()).getText()).asString();
			
			String beginn = "<div title='"+ text +"' style=\"white-space:normal;";
			String end = "</div>";
			
			if (Status.DEACTIVATED.equals(value.getStatus()) == false && Status.ACTIVE.equals(value.getStatus()) == false)
			{
				if (!value.getIsAcceptedAdmin()) {
					beginn += "color:red; ";
				}
				if (!value.getIsAcceptedRewiever()) {
					beginn += "font-style:italic; ";
				}
				//if (!value.getIsActive()) {
				if (!Status.ACTIVE.equals(value.getStatus())) {
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
	public List<String> getSearchValue() {
		return searchField;
	}
	
	private void setFieldForSearchBox()
	{
		searchField.clear();
		
		/*if (filterPanel.auther.getValue())
		{	
			searchField.add("author");
			searchField.add(filterPanel.auther.getValue() ? "true" : "false");
			//searchFileds.add(new SearchValue("author", filterPanel.auther.isChecked() ? "true" : "false"));
		}
		
		if (filterPanel.reviewer.getValue())
		{
			searchField.add("reviewer");
			searchField.add(filterPanel.reviewer.getValue()? "true" : "false");
			//searchFileds.add(new SearchValue("reviewer", filterPanel.reviewer.isChecked()? "true" : "false"));
		}*/
		
		searchField.add("questionShortName");
		searchField.add("true");
		
		if (filterPanel.getQuestionText().getValue())
		{
			searchField.add("quesitontext");
			searchField.add(filterPanel.questionText.getValue()? "true" : "false");
			//searchFileds.add(new SearchValue("quesitontext", filterPanel.questionText.isChecked()? "true" : "false"));
		}
		
		if (filterPanel.getInstructionText().getValue())
		{
			searchField.add("instruction");
			searchField.add(filterPanel.instructionText.getValue()? "true" : "false");
			//searchFileds.add(new SearchValue("instruction", filterPanel.instructionText.isChecked()? "true" : "false"));
		}
		
		if (filterPanel.getKeywordText().getValue())
		{
			searchField.add("keyword");
			searchField.add(filterPanel.keywordText.getValue()? "true" : "false");
			//searchFileds.add(new SearchValue("keyword", filterPanel.keywordText.isChecked()? "true" : "false"));
		}
		
		if (filterPanel.getShowNew().getValue())
		{
			searchField.add("showNew");
			searchField.add(filterPanel.showNew.getValue() ? "true" : "false");
		}
		
		if (filterPanel.getCreativeWork().getValue())
		{
			searchField.add("showCreativeWork");
			searchField.add(filterPanel.creativeWork.getValue() ? "true" : "false");
		}
		
		/*if (filterPanel.institutionListBox.getValue() != null)
		{
			searchField.add("institution");
			searchField.add(filterPanel.institutionListBox.getValue().getId().toString());
			//searchFileds.add(new SearchValue("institution", filterPanel.institutionListBox.getValue().getId().toString()));
		}*/
		
		/*if (filterPanel.specialiationListBox.getValue() != null)
		{
			searchField.add("specialiation");
			searchField.add(filterPanel.specialiationListBox.getValue().getId().toString());
			//searchFileds.add(new SearchValue("specialiation", filterPanel.specialiationListBox.getValue().getId().toString()));
		}
		
		if (filterPanel.status.getValue() != null)
		{
			searchField.add("status");
			searchField.add(filterPanel.status.getValue().toString());
			//searchFileds.add(new SearchValue("status", filterPanel.status.getValue().toString()));
		}
		
		if (filterPanel.createStartDate.getValue() != null)
		{
			searchField.add("createdDateFrom");
			searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.createStartDate.getValue()));
			//searchFileds.add(new SearchValue("createdDateFrom", filterPanel.createStartDate.getValue().toString()));
		}
		
		if (filterPanel.createEndDate.getValue() != null)
		{
			searchField.add("createdDateTo");
			searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.createEndDate.getValue()));
			//searchFileds.add(new SearchValue("createdDateTo", filterPanel.createEndDate.getValue().toString()));
		}
		
		if (filterPanel.usedMcStartDate.getValue() != null)
		{
			searchField.add("usedMcFrom");
			searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.usedMcStartDate.getValue()));
			//searchFileds.add(new SearchValue("usedMcFrom", filterPanel.usedMcStartDate.getValue().toString()));
		}
		
		if (filterPanel.usedMcEndDate.getValue() != null)
		{
			searchField.add("usedMcTo");
			searchField.add(DateTimeFormat.getFormat("dd-MM-yyyy").format(filterPanel.usedMcEndDate.getValue()));
			//searchFileds.add(new SearchValue("usedMcTo", filterPanel.usedMcEndDate.getValue().toString()));
		}*/
		
		/*table.setVisibleRange(0, McAppConstant.TABLE_PAGE_SIZE);
		delegate.performSearch(searchBox.getValue());*/
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
	
	public QuestionFilterViewImpl getFilterPanel() {
		return filterPanel;
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
	@Override
	public ScrollPanel getScrollDetailPanel() {
		return scrollDetailPanel;
	}
}
