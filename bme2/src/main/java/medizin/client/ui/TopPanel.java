/**
 * 
 */
package medizin.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import medizin.client.McAppShell;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.shared.Locale;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * The TopPanel contains the users login information and a list box to select the institution. 
 *
 */
public class TopPanel extends Composite {

	private static TopPanelUiBinder uiBinder = GWT
			.create(TopPanelUiBinder.class);

	interface TopPanelUiBinder extends UiBinder<Widget, TopPanel> {
	}
    private static TopPanel instance;
    public static TopPanel instance(McAppRequestFactory requests, PlaceController placeController) {
        if (instance == null) {
            instance = new TopPanel(requests, placeController);
        }
//		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();

        return instance;
    }
    
    private McAppShell shell;
    
    public McAppShell getShell() {
		return shell;
	}
	public void setShell(McAppShell shell) {
		this.shell = shell;
	}

	@UiField(provided = true)
    ValueListBox<InstitutionProxy> institutionListBox = new ValueListBox<InstitutionProxy>(medizin.client.ui.view.roo.InstitutionProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.InstitutionProxy>());

    @UiField(provided = true)
    ValueListBox<PersonProxy> loggedUser = new ValueListBox<PersonProxy>(medizin.client.ui.view.roo.PersonProxyRenderer.instance(), new EntityProxyKeyProvider<medizin.client.proxy.PersonProxy>());

    @UiField (provided = true)
	ValueListBox<Locale> languageListBox = new ValueListBox<Locale>(new AbstractRenderer<Locale>() {
		// Note: this is not an EnumRenderer bc. translations of language names would be futile.
		@Override
		public String render(Locale locale) {
			if (locale != null)
				return locale.getLanguageName();
			return "null";
		}
	});
    
   // private List<InstitutionProxy> temp = new ArrayList<InstitutionProxy>();
    
    @UiField
    Label languageLbl;

    @UiHandler ("loggedUser")
    public void loginUser(ValueChangeEvent<PersonProxy> event){
    	requests.personRequest().loginPerson().using(loggedUser.getValue()).fire();
    }
    @UiHandler ("institutionListBox")
    public void setInstitution(ValueChangeEvent<InstitutionProxy> event){
    	requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
    	McAppNav.checkAdminRights(requests);
    }
    
    public ValueListBox<InstitutionProxy> getInstitutionListBox() {
		return institutionListBox;
	}

	public ValueListBox<PersonProxy> getLoggedUser() {
		return loggedUser;
		
	}
	
	private McAppRequestFactory requests;
	private PlaceController placeController;

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	
	public BmeConstants constants = GWT.create(BmeConstants.class);
	
	@Inject
	public TopPanel(final McAppRequestFactory requests, final PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
		initWidget(uiBinder.createAndBindUi(this));
		
		loggedUser.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {

			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				
				List<InstitutionProxy> temp = new ArrayList<InstitutionProxy>();
				institutionListBox.setValue(null);
				institutionListBox.setAcceptableValues(temp);
				
				Log.info("Overall Admin :" + event.getValue().getIsAdmin());
				
				if (event.getValue().getIsAdmin())
				{
					
					requests.institutionRequest().findAllInstitutions().fire(new Receiver<List<InstitutionProxy>>() {

						@Override
						public void onSuccess(List<InstitutionProxy> response) {
							if (response.size() > 0)
							{
								Log.info("ADMIN IS SELECTED");
								institutionListBox.setValue(response.get(0));
								institutionListBox.setAcceptableValues(response);
								TopPanel.this.requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
								McAppNav.checkAdminRights(requests);
							}
							else
							{
								McAppNav.checkAdminRights(requests);
								ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.noInstitutionaccssMessage());
							}
						}
					});
				}
				else
				{
					
					TopPanel.this.requests.questionAccessRequest().findInstituionFromQuestionAccessByPerson(event.getValue().getId()).fire(new Receiver<List<InstitutionProxy>>() {

						@Override
						public void onSuccess(List<InstitutionProxy> response) {
							
							
							if(response==null){
								ConfirmationDialogBox.showOkDialogBox(constants.error(),constants.institutionAccessError());
							}		
							
							if (response!=null && response.size() > 0)
							{
								
								institutionListBox.setValue(response.get(0));
								institutionListBox.setAcceptableValues(response);
								TopPanel.this.requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
								McAppNav.checkAdminRights(requests);
							}
							else
							{
								requests.institutionRequest().fillCurrentInstitutionNull().fire();
								McAppNav.checkAdminRights(requests);
								ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.noInstitutionaccssMessage());	
							}
						}
					});
				}
			}
		});
		
		Locale[] locales = Locale.values();
		String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
		
		for (Locale locale: locales) {
			if (currentLocale.equals(locale.toString())) {
				languageListBox.setValue(locale);
			}
		}
		languageListBox.setAcceptableValues(Arrays.asList(locales));
		languageLbl.setText(constants.selectLang());
		
		languageListBox.addValueChangeHandler(new ValueChangeHandler<Locale>() {

			@Override
			public void onValueChange(ValueChangeEvent<Locale> event) {
				changeLanguage(languageListBox.getValue());
			}
		});
	}
	
	public void changeLanguage(Locale locale)
	{
		int indexOfHash;
		String newLocaleString;
		String url = Location.getHref();
		
		url = url.replaceAll("locale=[a-z]{2,2}", "locale=" + locale.toString());
		if (url.indexOf("locale") < 0) {
			if (url.indexOf("?") > -1) {
				newLocaleString = "&locale=" + locale.toString();
			} else {
				newLocaleString = "?locale=" + locale.toString();
			}
			
			if ((indexOfHash = url.indexOf("#")) > -1) {
				url = url.substring(0, indexOfHash) + newLocaleString + url.substring(indexOfHash);
			} else {
				url = url + newLocaleString;
			}
		}
		Window.open(url, "_self", "");
	}

}
