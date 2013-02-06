package medizin.client.ui.view.question;

import java.util.HashSet;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class QuestionViewImpl extends Composite implements QuestionView {

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
	public SimplePager pager;

	@UiField
	public IconButton filterButton;

	@UiField
	IconButton newQuestion;

	@UiHandler(value = { "newQuestion" })
	public void newButtonClicked(ClickEvent e) {
		delegate.newClicked();
	}


	public QuestionViewImpl() {
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new CellTable<QuestionProxy>(McAppConstant.TABLE_PAGE_SIZE,
				tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, McAppConstant.TABLE_JUMP_SIZE, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				// delegate.performSearch(searchBox.getValue());
				Log.info("serach click");
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
				"position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		init();

	}

	@UiHandler("filterButton")
	public void filterButtonHover(MouseOverEvent event) {
		// System.out.println("Mouse over");
		Log.info("filter panel call");
		showFilterPanel((Widget) event.getSource());
	}

	private void showFilterPanel(Widget eventSource) {
		int x = eventSource.getAbsoluteLeft();
		int y = eventSource.getAbsoluteTop();

		filterPanel.setPopupPosition(x, y);
		filterPanel.show();
		filterPanel.setSize("350px", "210px");
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
				return renderer.render(object.getId());
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
				return renderer.render(object.getQuestionShortName()==null?"":object.getQuestionShortName());
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
				return object;
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
				return renderer.render(object.getQuestionType()==null?"":object.getQuestionType().getShortName());
			}
		},constants.questionType() );
		paths.add("status");
		table.addColumn(new TextColumn<QuestionProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(QuestionProxy object) {
				return renderer.render(object.getStatus()==null?"":object.getStatus().toString());
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@Inject
	public QuestionViewImpl(McAppRequestFactory requests,
			PlaceController placeController) {
		initWidget(uiBinder.createAndBindUi(this));
		this.requests = requests;
		this.placeController = placeController;

	}

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
			String beginn = "<div style=\"";
			String end = "</div>";
			if (!value.getIsAcceptedAdmin()) {
				beginn += "color:red; ";
			}
			if (!value.getIsAcceptedRewiever()) {
				beginn += "font-style:italic; ";
			}
			if (!value.getIsActive()) {
				beginn += "text-decoration: line-through; ";
			}

			beginn += "\">";
			sb.appendHtmlConstant(beginn);
			sb.appendHtmlConstant(value.getQuestionText());
			sb.appendHtmlConstant(end);

		}
	}
	
	private static class GridTreeCell extends AbstractCell<QuestionProxy> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				QuestionProxy value,  SafeHtmlBuilder sb) {
			// Always do a null check on the value. Cell widgets can pass null
			// to cells
			// if the underlying data contains a null, or if the data arrives
			// out of order.
			
			TreeItem department = new TreeItem("Department");
			
			TreeItem employee1 = new TreeItem("Robert");
		      TreeItem employee2 = new TreeItem("Joe");
		      TreeItem employee3 = new TreeItem("Chris");
		      
		      department.addItem(employee1);
		      department.addItem(employee2);
		      department.addItem(employee3);
		      
		      
			if (value == null) {
				return;
			}
			String beginn = "<div style=\"";
			String end = "</div>";
			if (!value.getIsAcceptedAdmin()) {
				beginn += "color:red; ";
			}
			if (!value.getIsAcceptedRewiever()) {
				beginn += "font-style:italic; ";
			}
			if (!value.getIsActive()) {
				beginn += "text-decoration: line-through; ";
			}

			beginn += "\">";
			
			sb.appendHtmlConstant(department.getElement().getInnerHTML());
			Log.info("html value--"+department.getElement().getInnerHTML());
			/*sb.appendHtmlConstant(beginn);
			sb.appendHtmlConstant(value.getQuestionText());
			sb.appendHtmlConstant(end);*/

		}
	}


}
