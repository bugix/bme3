package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.QuestionSaveEvent;
import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.question.criteria.QuestionAdvancedSearchSubViewImpl;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionViewImpl extends Composite implements QuestionView, RecordChangeHandler {

	private static QuestionViewImplUiBinder uiBinder = GWT
			.create(QuestionViewImplUiBinder.class);

	interface QuestionViewImplUiBinder extends
			UiBinder<Widget, QuestionViewImpl> {
	}

	private Presenter presenter;
	protected Set<String> paths = new HashSet<String>();
	private Delegate delegate;
	private McAppRequestFactory requests;
	private PlaceController placeController;
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
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
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField(provided = true)
	QuickSearchBox searchBox;

	QuestionFilterViewImpl filterPanel = new QuestionFilterViewImpl();

	@UiField(provided = true)
	CellTable<QuestionProxy> table;

	@UiField(provided = true)
	public MySimplePager pager;

	@UiField
	public IconButton filterButton;

	@UiField
	IconButton newQuestion;
	
	@UiField
	QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl;

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

	@Override
	public void setSpecialisationFilter(List<QuestionEventProxy> values)
	{
		//filterPanel.specialiationListBox.setAcceptableValues(values);	
	}
	
	public QuestionAdvancedSearchSubViewImpl getQuestionAdvancedSearchSubViewImpl() {
		return questionAdvancedSearchSubViewImpl;
	}

	public void setQuestionAdvancedSearchSubViewImpl(
			QuestionAdvancedSearchSubViewImpl questionAdvancedSearchSubViewImpl) {
		this.questionAdvancedSearchSubViewImpl = questionAdvancedSearchSubViewImpl;
	}

	
	public QuestionViewImpl(EventBus eventBus,Boolean flag) {
		this.eventBus = eventBus;
		
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new CellTable<QuestionProxy>(McAppConstant.TABLE_PAGE_SIZE,
				tableResources);

		MySimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,
				true, McAppConstant.TABLE_JUMP_SIZE, true);

		Log.info("Question Save Event Register");
		QuestionSaveEvent.register(this.eventBus, new QuestionSaveEvent.Handler() {
			
			@Override
			public void onSaveClicked(QuestionSaveEvent event) {
				Log.info("Question Save Event Clicked");
				filterPanel.getShowNew().setValue(true);
				setFieldForSearchBox();
			}
		});
		
		filterPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				setFieldForSearchBox();
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
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
				"position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		newQuestion.setText(constants.newQuestion());
		
		if (flag)
			newQuestion.setVisible(true);
		else
			newQuestion.setVisible(false);
		
		init();

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
		}, constants.id());
		
		paths.add("shortName");
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
		},constants.questionShortName() );
		
		
		/*
		 * 
		 * table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getStudyYear()==null)
				{
					return " ";
				}
				else
				{
					String s=" "+object.getStudyYear()+"."+OsceEditActivity.semester.getSemester();
					if(object.getIsRepeOsce()==true)
					{
						s=s+" rape";
					}
				//String s=""+object.getStudyYear().ordinal();
				return renderer.render(s);
				}
			}
		}, constants.osce());
		 */

		// paths.add("dateAdded");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.util.Date> renderer = new
		// DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getDateAdded());
		// }
		// }, "Date Added");
		// paths.add("dateChanged");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.util.Date> renderer = new
		// DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getDateChanged());
		// }
		// }, "Date Changed");
		// paths.add("rewiewer");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.PersonProxy> renderer =
		// medizin.client.ui.view.roo.PersonProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getRewiewer());
		// }
		// }, "Rewiewer");
		// paths.add("autor");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.PersonProxy> renderer =
		// medizin.client.ui.view.roo.PersonProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getAutor());
		// }
		// }, "Autor");
		// paths.add("questionText");
		//
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.String> renderer = new
		// AbstractRenderer<java.lang.String>() {
		//
		// public String render(java.lang.String obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return object.getQuestionText();
		// }
		// }, McAppConstant.QUESTION_TEXT);
		// paths.add("picturePath");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.String> renderer = new
		// AbstractRenderer<java.lang.String>() {
		//
		// public String render(java.lang.String obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getPicturePath());
		// }
		// }, "Picture Path");
		// paths.add("questionVersion");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.Double> renderer = new
		// AbstractRenderer<java.lang.Double>() {
		//
		// public String render(java.lang.Double obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getQuestionVersion());
		// }
		// }, "Question Version");
		// paths.add("isAcceptedRewiever");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.Boolean> renderer = new
		// AbstractRenderer<java.lang.Boolean>() {
		//
		// public String render(java.lang.Boolean obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getIsAcceptedRewiever());
		// }
		// }, "Is Accepted Rewiever");
		// paths.add("isAcceptedAdmin");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.Boolean> renderer = new
		// AbstractRenderer<java.lang.Boolean>() {
		//
		// public String render(java.lang.Boolean obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getIsAcceptedAdmin());
		// }
		// }, "Is Accepted Admin");
		// paths.add("isActive");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.lang.Boolean> renderer = new
		// AbstractRenderer<java.lang.Boolean>() {
		//
		// public String render(java.lang.Boolean obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getIsActive());
		// }
		// }, "Is Active");
		// paths.add("previousVersion");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.QuestionProxy> renderer =
		// medizin.client.ui.view.roo.QuestionProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getPreviousVersion());
		// }
		// }, "Previous Version");
		// paths.add("keywords");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.util.Set> renderer =
		// medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.KeywordProxyRenderer.instance());
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getKeywords());
		// }
		// }, "Keywords");
		// paths.add("questEvent");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.QuestionEventProxy> renderer
		// = medizin.client.ui.view.roo.QuestionEventProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getQuestEvent());
		// }
		// }, McAppConstant.QUESTION_EVENT);
		// paths.add("comment");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.CommentProxy> renderer =
		// medizin.client.ui.view.roo.CommentProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getComment());
		// }
		// }, "Comment");
		// paths.add("questionType");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<medizin.client.managed.request.QuestionTypeProxy> renderer =
		// medizin.client.ui.view.roo.QuestionTypeProxyRenderer.instance();
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getQuestionType());
		// }
		// }, McAppConstant.QUESTION_TYPE );
		// paths.add("mcs");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.util.Set> renderer =
		// medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.McProxyRenderer.instance());
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getMcs());
		// }
		// }, "Mcs");
		// paths.add("answers");
		// table.addColumn(new TextColumn<QuestionProxy>() {
		//
		// Renderer<java.util.Set> renderer =
		// medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.AnswerProxyRenderer.instance());
		//
		// @Override
		// public String getValue(QuestionProxy object) {
		// return renderer.render(object.getAnswers());
		// }
		// }, "Answers");
		table.addColumn(new Column<QuestionProxy, QuestionProxy>(
				new QuestionTextCell()) {
			@Override
			public QuestionProxy getValue(QuestionProxy object) {
				return object == null ? null : object;
			}
		}, constants.questionText());

		/*table.addColumn(new Column<QuestionProxy, QuestionProxy>(
				new GridTreeCell()) {
			@Override
			public QuestionProxy getValue(QuestionProxy object) {
				return object;
			}
		}, "Grid");*/

		
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
		},constants.questionType() );
		paths.add("status");
		table.addColumn(new TextColumn<QuestionProxy>() {

			EnumRenderer<Status> renderer = new EnumRenderer<Status>();

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object == null ? null : object.getStatus());
			}
		},constants.status() );
		
		/*
		table.addColumn(new Column<QuestionProxy, TreeItem>(null) {

			@Override
			public TreeItem getValue(QuestionProxy object) {
				// TODO Auto-generated method stub
				
				TreeItem department = new TreeItem("Department");
				
				TreeItem employee1 = new TreeItem("Robert");
			      TreeItem employee2 = new TreeItem("Joe");
			      TreeItem employee3 = new TreeItem("Chris");
			      department.addItem(employee1);
			      department.addItem(employee2);
			      department.addItem(employee3);
			      
			      return department;
			}
		}, "check");
		*/
		
		
		
	}

	@Override
	public QuickSearchBox getSerachBox()
	{
		return searchBox;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	/*@Inject
	public QuestionViewImpl(McAppRequestFactory requests,
			PlaceController placeController) {
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;

	}*/

	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub

	}

	@Override
	public CellTable<QuestionProxy> getTable() {

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
			String beginn = "<div style=\"white-space:normal;";
			String end = "</div>";
			
			if (Status.ACTIVE.equals(value.getStatus()) == false)
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
			sb.appendHtmlConstant(value.getQuestionText());
			sb.appendHtmlConstant(end);

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
		
		delegate.performSearch(searchBox.getValue());
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
}
