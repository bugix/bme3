package medizin.client.ui.view.user;

import java.util.HashSet;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.PersonProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.QuickSearchBox;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.util.ClientUtility;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserViewImpl extends Composite implements UserView, RecordChangeHandler  {

	private static UserViewImplUiBinder uiBinder = GWT
			.create(UserViewImplUiBinder.class);

	interface UserViewImplUiBinder extends
			UiBinder<Widget, UserViewImpl> {
	}
	
	@UiField(provided=true)
	SplitLayoutPanel splitLayoutPanel;

	@UiField (provided = true)
	QuickSearchBox searchBox;
	
	@UiField
	HTMLPanel mainPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	private Presenter presenter;

	public UserViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<PersonProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);

		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		splitLayoutPanel =new SplitLayoutPanel(){
            @Override
            public void onResize() {
               super.onResize();
               	Double newWidth =splitLayoutPanel.getWidgetSize(scrollPanel);
               	Cookies.setCookie(McAppConstant.USER_VIEW_WIDTH, String.valueOf(newWidth));
            }
        };  
        
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		init();
		//setting widget width from cookie.
        setWidgetWidth();
		
	}

	private void setWidgetWidth() {
		String widgetWidthFromCookie = Cookies.getCookie(McAppConstant.USER_VIEW_WIDTH);
        if(widgetWidthFromCookie !=null){
        	splitLayoutPanel.setWidgetSize(scrollPanel,Double.valueOf(widgetWidthFromCookie));	
        }
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	
	@UiField(provided = true)
	CellTable<PersonProxy> table;

	@UiField(provided = true)
	public MySimplePager pager;

	
	  /* @UiField
	    CellTable<PersonProxy> table;*/
	  
	   @UiField
	   IconButton addUser;
	   
	   public BmeConstants constants = GWT.create(BmeConstants.class);
	   
	   @UiHandler ("addUser")
	   public void onNewClicked(ClickEvent e) {
        delegate.newClicked();
    }

	    protected Set<String> paths = new HashSet<String>();
		private Delegate delegate;

	    public void init() {
//	        paths.add("id");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//	                public String render(java.lang.Long obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getId());
//	            }
//	        }, "Id");
//	        paths.add("version");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//	                public String render(java.lang.Integer obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getVersion());
//	            }
//	        }, "Version");
	        paths.add("name");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getName());
	            }
	        }, constants.name());
	        
	        paths.add("prename");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getPrename());
	            }
	        }, constants.prename());
	        
	        paths.add("email");
	        table.addColumn(new TextColumn<PersonProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(PersonProxy object) {
	                return renderer.render(object == null ? null : object.getEmail());
	            }
	        }, constants.email());
//	        paths.add("alternativEmail");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//	                public String render(java.lang.String obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getAlternativEmail());
//	            }
//	        }, "Alternativ Email");
//	        paths.add("phoneNumber");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//	                public String render(java.lang.String obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getPhoneNumber());
//	            }
//	        }, "Phone Number");
//	        paths.add("isAdmin");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//	                public String render(java.lang.Boolean obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getIsAdmin());
//	            }
//	        }, "Is Admin");
//	        paths.add("isAccepted");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//	                public String render(java.lang.Boolean obj) {
//	                    return obj == null ? "" : String.valueOf(obj);
//	                }
//	            };
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getIsAccepted());
//	            }
//	        }, "Is Accepted");
//	        paths.add("questionAccesses");
//	        table.addColumn(new TextColumn<PersonProxy>() {
//
//	            Renderer<java.util.Set> renderer = medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.QuestionAccessProxyRenderer.instance());
//
//	            @Override
//	            public String getValue(PersonProxy object) {
//	                return renderer.render(object.getQuestionAccesses());
//	            }
//	        }, "Question Accesses");
	        
	      
	    }



		@Override
		public CellTable<PersonProxy> getTable() {
			
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
		SimplePanel slidingPanel;
		@Override
		public SimplePanel getDetailsPanel() {
			
			return slidingPanel;
		}


		@Override
		public SplitLayoutPanel getSplitLayoutPanel() {
			
			return splitLayoutPanel;
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
