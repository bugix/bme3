package medizin.client.ui.view.user;

import medizin.client.place.PlaceUserDetails;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.DoctorProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.request.PersonRequest;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UserEditViewImpl extends Composite implements UserEditView  {

	private static UserEditViewImplUiBinder uiBinder = GWT
			.create(UserEditViewImplUiBinder.class);

	interface UserEditViewImplUiBinder extends
			UiBinder<Widget, UserEditViewImpl> {
	}

	private Presenter presenter;
	@UiField
	Element  editTitle;
	@UiField
	Element  createTitle;
	
    @UiField
    Button cancel;

    @UiField
    Button save;

    private Delegate delegate;
	
    @UiField
    TextBox name;

    @UiField
    TextBox prename;

    @UiField
    TextBox email;

    @UiField
    TextBox alternativEmail;

    @UiField
    TextBox phoneNumber;

    @UiField
    CheckBox isAdmin;
    
    @UiField
    CheckBox isAccepted;
    
    @UiField
    CheckBox isDoctor;    
    
    @UiField 
    DefaultSuggestBox<DoctorProxy, EventHandlingValueHolderItem<DoctorProxy>> doctorSuggestBox;
     
	private PersonProxy proxy;

	private McAppRequestFactory requests;

    @UiHandler("cancel")
    void onCancel(ClickEvent event) {
        delegate.cancelClicked();
    }

    @UiHandler("save")
    void onSave(ClickEvent event) {
    	Log.info("SAVE CLICKED");
        delegate.saveClicked();
    }


//    @UiField
//    SpanElement displayRenderer;



	public UserEditViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		isDoctor.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (isDoctor.getValue())
					Document.get().getElementById("doctorDisplay").getStyle().clearDisplay();
				else
					Document.get().getElementById("doctorDisplay").getStyle().setDisplay(Display.NONE);
			}
		});
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@Override
	public void setValue(PersonProxy person) {
		this.proxy = person;
		
		name.setText(person.getName() == null ? "" : person.getName());
		prename.setText(person.getPrename() == null ? "" : person.getPrename());
		email.setText(person.getEmail() == null ? "" : person.getEmail());
		alternativEmail.setText(person.getAlternativEmail() == null ? "" : person.getAlternativEmail());
		phoneNumber.setText(person.getPhoneNumber() == null ? "" : person.getPhoneNumber());
		isAdmin.setValue(proxy.getIsAdmin() == null ? Boolean.FALSE : person.getIsAdmin());
		isAccepted.setValue(proxy.getIsAccepted() == null ? Boolean.FALSE : person.getIsAccepted());
		isDoctor.setValue(proxy.getIsDoctor() == null ? Boolean.FALSE : person.getIsDoctor());
		
		Document.get().getElementById("doctorDisplay").getStyle().clearDisplay();
		if (person.getIsDoctor() != null && person.getIsDoctor())
		{
			if (proxy.getDoctor() != null)
				doctorSuggestBox.setSelected(proxy.getDoctor());
		}
	}

	public PersonProxy getProxy() {
		return proxy;
	}

	public void setProxy(PersonProxy proxy) {
		this.proxy = proxy;
	}

	public TextBox getName() {
		return name;
	}

	public TextBox getPrename() {
		return prename;
	}

	public TextBox getEmail() {
		return email;
	}

	public TextBox getAlternativEmail() {
		return alternativEmail;
	}

	public TextBox getPhoneNumber() {
		return phoneNumber;
	}

	public CheckBox getIsAdmin() {
		return isAdmin;
	}

	public CheckBox getIsAccepted() {
		return isAccepted;
	}

	public CheckBox getIsDoctor() {
		return isDoctor;
	}

	@Override
	public void disableAdminField(Boolean flag) {
		Document.get().getElementById("doctorDisplay").getStyle().setDisplay(Display.NONE);
		if (!flag)
		{
			Document.get().getElementById("isAdmin").getStyle().setDisplay(Display.NONE);
		}
		else
		{
			Document.get().getElementById("isAdmin").getStyle().clearDisplay();
		}
	}

	public DefaultSuggestBox<DoctorProxy, EventHandlingValueHolderItem<DoctorProxy>> getDoctorSuggestBox() {
		return doctorSuggestBox;
	}

	public void setDoctorSuggestBox(
			DefaultSuggestBox<DoctorProxy, EventHandlingValueHolderItem<DoctorProxy>> doctorSuggestBox) {
		this.doctorSuggestBox = doctorSuggestBox;
	}
	
	

}
