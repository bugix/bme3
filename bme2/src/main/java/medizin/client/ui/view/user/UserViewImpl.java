package medizin.client.ui.view.user;

import java.util.HashSet;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserViewImpl extends Composite implements UserView  {

	private static UserViewImplUiBinder uiBinder = GWT
			.create(UserViewImplUiBinder.class);

	interface UserViewImplUiBinder extends
			UiBinder<Widget, UserViewImpl> {
	}
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;

	private Presenter presenter;

	public UserViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		init();

	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	
	   @UiField
	    CellTable<PersonProxy> table;
	   @UiField
	   Button addInstitution;
	   
	   public BmeConstants constants = GWT.create(BmeConstants.class);
	   
	   @UiHandler ("addInstitution")
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
	                return renderer.render(object.getName());
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
	                return renderer.render(object.getPrename());
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
	                return renderer.render(object.getEmail());
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


}
