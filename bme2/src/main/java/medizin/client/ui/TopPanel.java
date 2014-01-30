/**
 * 
 */
package medizin.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import medizin.client.McAppShell;
import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.style.resources.UiStyles;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.AppLoader;
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
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;

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
    
    private static List<InstitutionProxy> institutionalList=null;
    
    public static List<InstitutionProxy> getInstitutionalList() {
		return institutionalList;
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
    UiStyles uiStyles;
    
    @UiField
    Label languageLbl;
    
    @UiField
    Label recordViewLbl;
    
    @UiField (provided = true)
	ValueListBox<String> recordViewListBox= new ValueListBox<String>(new AbstractRenderer<String>() {
		@Override
		public String render(String object) {
			return object;
		}
	});

    @UiHandler ("loggedUser")
    public void loginUser(ValueChangeEvent<PersonProxy> event){
    	if (event.getValue() != null){
    		AppLoader.setNoLoader();
    		requests.personRequest().loginPerson().using(loggedUser.getValue()).fire();
    		}
    }
    @UiHandler ("institutionListBox")
    public void setInstitution(ValueChangeEvent<InstitutionProxy> event){
    	AppLoader.setNoLoader();
    	requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
    	McAppNav.checkAdminRights(requests,true);
    }
    
    public ValueListBox<InstitutionProxy> getInstitutionListBox() {
		return institutionListBox;
	}

	public ValueListBox<PersonProxy> getLoggedUser() {
		return loggedUser;
		
	}
	
	private final McAppRequestFactory requests;
	//private final PlaceController placeController;

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
        //this.placeController = placeController;
        
		initWidget(uiBinder.createAndBindUi(this));
		
		loggedUser.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
		
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				setInstitutionValue(requests, event.getValue());
			}	
		});
		AppLoader.setNoLoader();
		requests.personRequest().myGetLoggedPerson().fire(new BMEReceiver<PersonProxy>() {

			@Override
			public void onSuccess(PersonProxy response) {
				setInstitutionValue(requests, response);
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
		
		recordViewListBox.setValue(" ");
		recordViewListBox.setAcceptableValues(Arrays.asList("5","10","20","30","50","100","ALL"));
		checkCookies();
		recordViewLbl.setText(constants.tableSize());
		recordViewListBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				AppLoader.setNoLoader();
				requests.getEventBus().fireEvent(new RecordChangeEvent(recordViewListBox.getValue()));	
			}
		});
        uiStyles.uiCss().ensureInjected();
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
	
	private void setInstitutionValue(final McAppRequestFactory requests, PersonProxy userLoggedIn) {
		if(userLoggedIn!=null)
		{	
			
			List<InstitutionProxy> temp = new ArrayList<InstitutionProxy>();
			institutionListBox.setValue(null);
			institutionListBox.setAcceptableValues(temp);
			
			Log.info("Overall Admin :" + userLoggedIn.getIsAdmin());
			
			if (userLoggedIn.getIsAdmin())
			{
				AppLoader.setNoLoader();
				requests.institutionRequest().findAllInstitutions().fire(new BMEReceiver<List<InstitutionProxy>>() {

					@Override
					public void onSuccess(List<InstitutionProxy> response) {
						if (response.size() > 0)
						{
							Log.info("ADMIN IS SELECTED");
							institutionListBox.setValue(response.get(0));
							institutionListBox.setAcceptableValues(response);
							AppLoader.setNoLoader();
							TopPanel.this.requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
							McAppNav.checkAdminRights(requests,true);
							
							institutionalList=response;
						}
						else
						{
							McAppNav.checkAdminRights(requests,true);
							ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.noInstitutionaccssMessage());
						}
					}
				});
			}
			else
			{
				AppLoader.setNoLoader();	
				TopPanel.this.requests.userAccessRightsRequest().findInstituionFromQuestionAccessByPerson(userLoggedIn.getId()).fire(new BMEReceiver<List<InstitutionProxy>>() {

					@Override
					public void onSuccess(List<InstitutionProxy> response) {
						
						
						if(response==null){
							ConfirmationDialogBox.showOkDialogBox(constants.error(),constants.institutionAccessError());
						}		
						
						if (response!=null && response.size() > 0)
						{
							
							institutionListBox.setValue(response.get(0));
							institutionListBox.setAcceptableValues(response);
							AppLoader.setNoLoader();
							TopPanel.this.requests.institutionRequest().mySetCurrentInstitution().using(institutionListBox.getValue()).fire();
							McAppNav.checkAdminRights(requests,true);
							institutionalList=response;
						}
						else
						{
							AppLoader.setNoLoader();
							requests.institutionRequest().fillCurrentInstitutionNull().fire();
							McAppNav.checkAdminRights(requests,true);
							ConfirmationDialogBox.showOkDialogBox(constants.information(),constants.noInstitutionaccssMessage());	
						}
					}
				});
			}
		}
		else
		{
			AppLoader.setNoLoader();
			requests.institutionRequest().fillCurrentInstitutionNull().fire();
			List<InstitutionProxy> temp = new ArrayList<InstitutionProxy>();
			institutionListBox.setValue(null);
			institutionListBox.setAcceptableValues(temp);
			McAppNav.checkAdminRights(requests,false);
		}
	}	

	public void checkCookies()
	{
		String temp = Cookies.getCookie("user");
		
		if (temp == null)
		{
			Log.info("Value is null");
		}	
		else			
		{			
			if (temp.matches("\\d+"))
			{
				McAppConstant.TABLE_PAGE_SIZE = Integer.parseInt(temp);
				recordViewListBox.setValue(temp);
			}
		}
	}
}
