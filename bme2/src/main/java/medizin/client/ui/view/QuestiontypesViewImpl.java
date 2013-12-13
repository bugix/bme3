package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

	 @UiField (provided = true)
	 QuickSearchBox searchBox;
	 
	public QuestiontypesViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<QuestionTypeProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
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
	CellTable<QuestionTypeProxy> table;
	
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
	public CellTable<QuestionTypeProxy> getTable() {
		
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
        }, constants.name());
        
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
        }, constants.type());
       
       
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
        }, constants.answer());
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


	
}
