package medizin.client.ui.view.user;

import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserDetailsViewImpl extends Composite implements UserDetailsView  
{

	private static UserDetailsViewImplUiBinder uiBinder = GWT
			.create(UserDetailsViewImplUiBinder.class);

	interface UserDetailsViewImplUiBinder extends
			UiBinder<Widget, UserDetailsViewImpl> 
	{
	}
	


	private Presenter presenter;

	@UiField
	TabPanel userDetailPanel;
	
	@UiField
	TabPanel userAccessDetailPanel;
	
    @UiField
    IconButton edit;

    @UiField
    IconButton delete;

    private Delegate delegate;
    
    @UiField
	DisclosurePanel userDisclosurePanel;
    
	@UiField
	Image arrow;

	
//    @UiField
//    SpanElement id;
//
//    @UiField
//    SpanElement version;

    @UiField
    Label name;

    @UiField
    Label prename;
    
    /*@UiField
    SpanElement header;*/

    @UiField
    Label email;

    @UiField
    Label alternativEmail;

    @UiField
    Label phoneNumber;

    @UiField
    Label isAdmin;

    @UiField
    Label isAccepted;
    
    @UiField
    Label isDoctor;
    
    @UiField
    Label doctorLbl;

//    @UiField
//    SpanElement questionAccesses;
    
    @UiField
    EventAccessViewImpl eventAccessView;
    
    @UiField
    QuestionAccessViewImpl questionAccessView;
    
    @UiField
    InstituteAccessViewImpl instituteAccessView;
    
    
	@Override
    public EventAccessViewImpl getEventAccessView(){
    	return eventAccessView;
    }

    PersonProxy proxy;
    
    @UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}
    
    
    @UiHandler("edit")
    public void onEditClicked(ClickEvent e) {
        delegate.editClicked();
    }

    @UiField
    SpanElement displayRenderer;

    public void setValue(PersonProxy proxy) {
        this.proxy = proxy;
        
        displayRenderer.setInnerText( (this.proxy.getPrename()==null?"":this.proxy.getPrename()) +" "+ (this.proxy.getName()==null?"":this.proxy.getName()));
       
//        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
//        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
  //      header.setInnerText((proxy.getName() == null ? "" : String.valueOf(proxy.getName()) )+" "+ (proxy.getPrename() == null ? "" : String.valueOf(proxy.getPrename())));
        name.setText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        prename.setText(proxy.getPrename() == null ? "" : String.valueOf(proxy.getPrename()));
        email.setText(proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail()));
        alternativEmail.setText(proxy.getAlternativEmail() == null ? "" : String.valueOf(proxy.getAlternativEmail()));
        phoneNumber.setText(proxy.getPhoneNumber() == null ? "" : String.valueOf(proxy.getPhoneNumber()));
        isAdmin.setText(proxy.getIsAdmin() == null ? "" : (proxy.getIsAdmin()? "ja" : "nein"));
        isAccepted.setText(proxy.getIsAccepted() == null ? "" : (proxy.getIsAccepted()? "ja" : "nein"));
        isDoctor.setText(proxy.getIsDoctor() == null ? "" : proxy.getIsDoctor().toString());
        
        if (proxy.getIsDoctor() != null && proxy.getIsDoctor())
        {
        	Document.get().getElementById("doctorDisplay").getStyle().clearDisplay();
        	doctorLbl.setText(proxy.getDoctor() == null ? "" : proxy.getDoctor().getName());
        }
        else
        {
        	Document.get().getElementById("doctorDisplay").getStyle().setDisplay(Display.NONE);
        }
//        Log.info(proxy.getQuestionAccesses().toString());       
//        questionAccesses.setInnerText(proxy.getQuestionAccesses() == null ? "" : medizin.client.ui.view.roo.CollectionRenderer.of(medizin.client.ui.view.roo.QuestionAccessProxyRenderer.instance()).render(proxy.getQuestionAccesses()));
//        displayRenderer.setInnerText(medizin.client.ui.view.roo.PersonProxyRenderer.instance().render(proxy));
    }

    @UiHandler("arrow")
	void handleClick(ClickEvent e) {
		if (userDisclosurePanel.isOpen()) {
			userDisclosurePanel.setOpen(false);
			///bme2/src/main/java/medizin/client/ui/view/user/UserDetailsViewImpl.ui.xml
			///bme2/src/main/java/medizin/client/style/theme/public/gwt/unibas/images/arrowdownselect.png
			
			//arrow.setUrl("/application/gwt/unibas/images/arrowdownselect.png");// set
			arrow.setUrl("/application/gwt/unibas/images/down.png");// set
																				// url
																				// of
																				// up
															// image

		} else {
			userDisclosurePanel.setOpen(true);
			arrow.setUrl("/application/gwt/unibas/images/up.png");// set
																				// url
																				// of
																				// down
																				// image
		}

	}
    
	public UserDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		userDisclosurePanel.setOpen(true);
		userDisclosurePanel.setContent(userDetailPanel);
		userDisclosurePanel.setStyleName("");

		userDetailPanel.selectTab(0);
		userDetailPanel.getTabBar().setTabText(0, "Users");
		TabPanelHelper.moveTabBarToBottom(userDetailPanel);
		
		userAccessDetailPanel.selectTab(0);
		
		userAccessDetailPanel.getTabBar().setTabText(0, "Institute Access");
		userAccessDetailPanel.getTabBar().setTabText(1, "Event Access");
		userAccessDetailPanel.getTabBar().setTabText(2, "Question Access");
		
		
		//userAccessDetailPanel.getTabBar().setTabEnabled(0, false);
		
		
		
		

	}


	@Override
	public void setName(String helloName) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@Override
	public QuestionAccessViewImpl getQuestionAccessView() {
		// TODO Auto-generated method stub
		return questionAccessView;
	}

	public InstituteAccessViewImpl getInstituteAccessView() {
		return instituteAccessView;
	}

	public void setInstituteAccessView(InstituteAccessViewImpl instituteAccessView) {
		this.instituteAccessView = instituteAccessView;
	}

	


}
