package medizin.client.ui.view.user;

import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.TabPanelHelper;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
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
	
	private BmeConstants constants = GWT.create(BmeConstants.class);

	private Presenter presenter;

	@UiField
	TabPanel userDetailPanel;
	
	@UiField
	TabPanel userAccessDetailPanel;
	
	@Override
    public TabPanel getUserAccessDetailPanel() {
		return userAccessDetailPanel;
	}

	@Override
	public void setUserAccessDetailPanel(TabPanel userAccessDetailPanel) {
		this.userAccessDetailPanel = userAccessDetailPanel;
	}

    @UiField
    IconButton edit;

    @UiField
    IconButton delete;

    private Delegate delegate;  

    @UiField
    Label name;

    @UiField
    Label prename;
    
    @UiField
    Label email;

    @UiField
    Label alternativEmail;

    @UiField
    Label phoneNumber;

    @UiField
    Label isAdmin;
  
    @UiField
    Label isDoctor;
    
    @UiField
    Label doctorLbl;

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
        name.setText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        prename.setText(proxy.getPrename() == null ? "" : String.valueOf(proxy.getPrename()));
        email.setText(proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail()));
        alternativEmail.setText(proxy.getAlternativEmail() == null ? "" : String.valueOf(proxy.getAlternativEmail()));
        phoneNumber.setText(proxy.getPhoneNumber() == null ? "" : String.valueOf(proxy.getPhoneNumber()));
        isAdmin.setText(proxy.getIsAdmin() == null ? "" : (proxy.getIsAdmin()? "ja" : "nein"));
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
    }

   	public UserDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	
		userDetailPanel.selectTab(0);
		userDetailPanel.getTabBar().setTabText(0, "Users");
		TabPanelHelper.moveTabBarToBottom(userDetailPanel);
		
		userAccessDetailPanel.selectTab(0);
		
		userAccessDetailPanel.getTabBar().setTabText(0, constants.institutionAccess());
		userAccessDetailPanel.getTabBar().setTabText(1, constants.eventAccess());
		userAccessDetailPanel.getTabBar().setTabText(2, constants.questionAccess());
	}


	@Override
	public void setName(String helloName) {		
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
		return questionAccessView;
	}

	public InstituteAccessViewImpl getInstituteAccessView() {
		return instituteAccessView;
	}

	public void setInstituteAccessView(InstituteAccessViewImpl instituteAccessView) {
		this.instituteAccessView = instituteAccessView;
	}
}
