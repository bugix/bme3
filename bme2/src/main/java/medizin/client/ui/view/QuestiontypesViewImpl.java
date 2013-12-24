package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.AdvanceCellTable;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestiontypesViewImpl extends Composite implements QuestiontypesView, RecordChangeHandler  {

	private static QuestiontypesViewImplUiBinder uiBinder = GWT
			.create(QuestiontypesViewImplUiBinder.class);

	interface QuestiontypesViewImplUiBinder extends
			UiBinder<Widget, QuestiontypesViewImpl> {
	}

	private Presenter presenter;
	 public BmeConstants constants = GWT.create(BmeConstants.class);
	 private Map<String, String> columnName=new HashMap<String, String>();
	 private List<String> columnNameorder = new ArrayList<String>();
	 private List<String> path = new ArrayList<String>();
	 private int x;
	 private int y;	
	 private String columnHeader;
	 
	 @UiField (provided = true)
	 QuickSearchBox searchBox;
	 
	public QuestiontypesViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<QuestionTypeProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.QUESTIONTYPES_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				Log.info("Searchbox is visited");
				table.setVisibleRange(0, McAppConstant.TABLE_PAGE_SIZE);
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		//splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), McAppConstant.SPLIT_PANEL_MINWIDTH);
		//splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), McAppConstant.SPLIT_PANEL_MINWIDTH);
		//setting widget width from cookie.
        setWidgetWidth();
	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.QUESTIONTYPES_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}
	
//    @Override
//    public RequestFactoryEditorDriver<medizin.client.managed.request.QuestionTypeProxy, QuestiontypesViewImpl> createEditorDriver() {
//        RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesViewImpl> driver = GWT.create(Driver.class);
//        driver.initialize(this);
//        return driver;
//    }
//    
//    interface Driver extends RequestFactoryEditorDriver<QuestionTypeProxy, QuestiontypesViewImpl> {
//    }





	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	protected List<String> paths=new ArrayList<String>() ;
	
	@UiField(provided=true)
	AdvanceCellTable<QuestionTypeProxy> table;
	
	@UiField(provided = true)
	public MySimplePager pager;
	
	
	@UiField
	IconButton newQuestiontype;

    @UiHandler("newQuestiontype")
    public void onDeleteClicked(ClickEvent e) {
        delegate.newClicked();
    }
    
	private Delegate delegate;
	

	
	@Override
	public AdvanceCellTable<QuestionTypeProxy> getTable() {
		
		return table;
	}
	@Override
	public String[] getPaths() {
		// TODO Auto-generated method stub
		return paths.toArray(new String[paths.size()]);
	}
	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	public void init() {
		
		/*ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
		
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(McAppConstant.ANIMATION_TIME);*/	
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		newQuestiontype.setText(constants.newQuestionType());

		columnName.put(constants.name(), "shortName");
		columnNameorder.add(constants.name());
		paths.add("shortName");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

			{
				this.setSortable(true);
			}
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
                //return renderer.render(object.getQuestionTypeName());
            	return renderer.render(object == null ? null : object.getShortName()!=null?object.getShortName():"");
            	//return renderer.render(object.getShortName());
            }
        }, constants.name(),true);
        
        columnName.put(constants.type(), "questionType");
		columnNameorder.add(constants.type());
        paths.add("questionType");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

			{
				this.setSortable(true);
			}
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
                //return renderer.render(object.getQuestionTypeName());
            	return renderer.render(object == null ? null : object.getQuestionType()!=null?object.getQuestionType().toString():"");
            	//return renderer.render(object.getShortName());
            }
        }, constants.type(),true);
       
       
        /*paths.add("questionType");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

			{
				this.setSortable(true);
			}
            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
                //return renderer.render(object.getTrueAnswers());
            	if (object.getSumTrueAnswer() != null)
            		return object.getSumTrueAnswer().toString();
            	else 
            		return "1";
            	return renderer.render(object.getq getQuestionType()!=null?object.getQuestionType():"");
            	//return "1";
            }
        }, constants.type());*/
        
        columnName.put(constants.answer(), "answer");
		columnNameorder.add(constants.answer());
        paths.add("answer");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

                public String render(java.lang.Integer obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
              //  return renderer.render(object.getFalseAnswers());
            	/*if (object.getSumFalseAnswer().toString() != null)
            		return object.getSumFalseAnswer().toString();
            	else 
            		return "2";*/
            	
            	return AnswerValue.getValue(object == null ? null : object);
            }
        }, constants.answer(),true);
        
        columnName.put(constants.description(), "description");
		columnNameorder.add(constants.description());
		paths.add("description");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

			{
				this.setSortable(true);
			}
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
            	return renderer.render(object == null ? null : object.getDescription()!=null?object.getDescription().toString():"");
            }
        }, constants.description(),false);

        // commented as not to include at this time.
        /*columnName.put(constants.institutionLbl(), "institution");
		columnNameorder.add(constants.institutionLbl());
		paths.add("institution");
        table.addColumn(new TextColumn<QuestionTypeProxy>() {

			{
				this.setSortable(true);
			}
            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

            @Override
            public String getValue(QuestionTypeProxy object) {
            	String result="";
            	if(object !=null){
            		if(object.getInstitution()!=null){
            			result = object.getInstitution().getInstitutionName(); 
            			return renderer.render(result);
            		}
            	}else{
            		return renderer.render("");
            	}
            	return renderer.render("");
            }
        }, constants.institutionLbl(),false);*/
        
        path=getPath();
        addMouseDownHandler();
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
				
				
			

			table.addColumn(new TextColumn<QuestionTypeProxy>() {

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
				public String getValue(QuestionTypeProxy object) {

					if(object !=null) {
	
						if (tempColumnHeader == constants.name()) {
							return renderer.render(object.getShortName()!=null?object.getShortName():"");
							
						} else if (tempColumnHeader == constants.type()) {
							return renderer.render(object.getQuestionType().name()!=null?object.getQuestionType().name():"");
							
						} else if (tempColumnHeader == constants.answer()) {
							return renderer.render(object!=null ? AnswerValue.getValue(object):"");
						} else if (tempColumnHeader == constants.description()) {
							
							return renderer.render(object.getDescription()!=null?object.getDescription():"");
						}/* else if (tempColumnHeader == constants.institutionLbl()) {
							
							return renderer.render(object.getInstitution().getInstitutionName()!=null?object.getInstitution().getInstitutionName():"");
						} */
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
		table.addLastColumn();
}
	private void addMouseDownHandler() {
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				Log.info("mouse down");

				x = event.getClientX();
				y = event.getClientY();

				delegate.setXandYOfTablePopyp(x-10,y);
				
				/*if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x-10, y);
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
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}
	
	@UiField
	SimplePanel slidingPanel;
	@Override
	public SimplePanel getDetailsPanel() {
		
		return slidingPanel;
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

	public Map<String, String> getColumnName() {
		return columnName;
	}

	public List<String> getColumnNameorder() {
		return columnNameorder;
	}

public List<String> getPath(){
	return this.paths;
}

public List<String> getCurrentRows(){
	return this.path;
}
}
