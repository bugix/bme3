package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;

import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.shared.i18n.BmeConstants;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestiontypesViewImpl extends Composite implements QuestiontypesView  {

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
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		//splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), McAppConstant.SPLIT_PANEL_MINWIDTH);
		//splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), McAppConstant.SPLIT_PANEL_MINWIDTH);
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
	public SimplePager pager;
	
	
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
	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	public void init() {
		
		/*ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
		
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(McAppConstant.ANIMATION_TIME);*/	
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

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
            	return renderer.render(object.getShortName()!=null?object.getShortName():"");
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
            	return renderer.render(object.getQuestionType()!=null?object.getQuestionType().toString():"");
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
            	
            	return AnswerValue.getValue(object);
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


	
}
