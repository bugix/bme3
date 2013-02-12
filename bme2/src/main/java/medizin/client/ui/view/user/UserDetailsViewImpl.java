package medizin.client.ui.view.user;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.PersonProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UserDetailsViewImpl extends Composite implements UserDetailsView  {

	private static UserDetailsViewImplUiBinder uiBinder = GWT
			.create(UserDetailsViewImplUiBinder.class);

	interface UserDetailsViewImplUiBinder extends
			UiBinder<Widget, UserDetailsViewImpl> {
	}
	


	private Presenter presenter;

	
    @UiField
    HasClickHandlers edit;

    @UiField
    HasClickHandlers delete;

    private Delegate delegate;
	
//    @UiField
//    SpanElement id;
//
//    @UiField
//    SpanElement version;

    @UiField
    Label name;

    @UiField
    Label prename;
    
    @UiField
    SpanElement header;

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

//    @UiField
//    SpanElement displayRenderer;

    public void setValue(PersonProxy proxy) {
        this.proxy = proxy;
       
//        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
//        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        header.setInnerText((proxy.getName() == null ? "" : String.valueOf(proxy.getName()) )+" "+ (proxy.getPrename() == null ? "" : String.valueOf(proxy.getPrename())));
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

	public UserDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

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
