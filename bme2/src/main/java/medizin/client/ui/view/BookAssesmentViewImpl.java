package medizin.client.ui.view;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.place.PlaceBookAssesmentDetails;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

public class BookAssesmentViewImpl extends Composite implements BookAssesmentView  {

	private static BookAssesmentViewImplUiBinder uiBinder = GWT
			.create(BookAssesmentViewImplUiBinder.class);

	private BmeConstants constant = GWT.create(BmeConstants.class);
	
	interface BookAssesmentViewImplUiBinder extends
			UiBinder<Widget, BookAssesmentViewImpl> {
	}

	private Presenter presenter;
	private  Delegate delegate;
	
	public BookAssesmentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		showButton.setText(constant.show());
		tabLayout.setVisible(false);
		setStyles();
		fillDataInYearListBox();
		addClickHandlerOfYearListBox();
	}

	private McAppRequestFactory requests;
	private PlaceController placeController;
	private boolean isFirstTab=true;
	
//	//Tab for Assesements
//	@UiField
//    TabLayoutPanel tabPanel;
// 
//	@UiField
//    CellTable<AssesmentProxy> table;
	
	/*@UiField
	HorizontalPanel tabs;*/
	
	@UiField
	TabBar tabLayout;
	
	@UiField
	SimplePanel content;
	
	@UiField
	HorizontalPanel serchCriteriaContainerPanel;
	
	@UiField
	ListBox yearListBox;
	
	@UiField
	DefaultSuggestBox<AssesmentProxy, EventHandlingValueHolderItem<AssesmentProxy>> assesmentSuggestionBox;
	
	@UiField
	IconButton showButton;
	
	protected Set<String> paths = new HashSet<String>();
	private Integer tabId=0;
	private HashMap<Integer, EntityProxyId<?>> idMap = new HashMap<Integer, EntityProxyId<?>>();

	@UiHandler("showButton")
	public void showButtonClicked(ClickEvent event){
		Log.info("Show button clicked");
		showClicked();
	}

	/*@Inject
	public BookAssesmentViewImpl(McAppRequestFactory requests, PlaceController placeController) {
		initWidget(uiBinder.createAndBindUi(this));
        this.requests = requests;
        this.placeController = placeController;
        showButton.setText(constant.show());
        fillDataInYearListBox();
	}*/

	private void setStyles(){
		yearListBox.addStyleName("bookAssesmentListBox");
		assesmentSuggestionBox.addStyleDependentName("assesmentSuggestionBox");
		showButton.addStyleName("bookAssesmentShowButton");
	}
	public void fillDataInYearListBox() {
		
		int currentYear =Integer.parseInt(DateTimeFormat.getFormat( "d-M-yyyy" ).format( new Date() ).split( "-")[2]); 
		
		for (int year=1990; year <= currentYear; year++) {
			yearListBox.addItem(String.valueOf(year));
		}
	}

	private void addClickHandlerOfYearListBox(){
			yearListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String selectedYear = yearListBox.getValue(yearListBox.getSelectedIndex());
				Log.info("year selected is" + selectedYear);
				delegate.yearSelected(selectedYear);
			}
		});
	}
	private void showClicked(){

		String assesmentSelected =assesmentSuggestionBox.getSelected().getName();
		boolean isexistSameTab=false;
		if(tabLayout.getTabCount() > 0){
			
			for(int count=0;count< tabLayout.getTabCount();count++){
				String name = tabLayout.getTabHTML(count);
				if(name.equalsIgnoreCase(assesmentSelected)){
					isexistSameTab=true;
					break;
				}
			}
			
			if(!isexistSameTab){
				createTab(assesmentSelected,assesmentSuggestionBox.getSelected().stableId() );
				tabLayout.selectTab(tabId-1);
				
			}
		}else{
			createTab(assesmentSelected,assesmentSuggestionBox.getSelected().stableId() );
			addTabHandler();
		}
		
	}
	public void setAssesmentSuggsetBoxValue(List<AssesmentProxy> assesmentProxyList)
	{
		assesmentSuggestionBox.setText("");
		
		DefaultSuggestOracle<AssesmentProxy> suggestOracle1 = (DefaultSuggestOracle<AssesmentProxy>) assesmentSuggestionBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(assesmentProxyList);
		assesmentSuggestionBox.setSuggestOracle(suggestOracle1);
		
		assesmentSuggestionBox.setRenderer(new AbstractRenderer<AssesmentProxy>() {

			@Override
			public String render(AssesmentProxy object) {
				return object == null ? "" : object.getName();					
			}
		});
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
	public void addTabHandler() {
		
		tabLayout.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				presenter.goTo(new PlaceBookAssesmentDetails(idMap.get(event.getItem())));
			}
		});
		
		if(idMap.isEmpty() == false)
		{
			Log.info("TabLayout selection : " + tabLayout.selectTab(0));	
		}
		
	
	}
	@Override
	public void createTab(String name, EntityProxyId<?> id) {

		/*Anchor link = new Anchor(name);
		link.setName(String.valueOf(tabId));
		link.addStyleName("ui-button ui-widget ui-state-default ui-button-text-only");*/
		if(isFirstTab){
			tabLayout.setVisible(true);
			isFirstTab=false;
		}
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		Label nameLbl = new Label(name);
		hp.add(nameLbl);
		IconButton closeBtn = new IconButton("");
		closeBtn.setIcon("close");
		hp.add(closeBtn);
		closeBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int selectedTab =tabLayout.getSelectedTab();
				Log.info("selected tab is" + selectedTab);
				tabLayout.removeTab(selectedTab);
				tabId--;
				if(selectedTab==0){
					if(tabLayout.getTabCount() > 0)
					tabLayout.selectTab(selectedTab +1);
					else
					tabLayout.removeFromParent();	
				}else{
					tabLayout.selectTab(selectedTab - 1);
				}
			}
		});
		idMap.put(tabId, id);
		tabId++;
		tabLayout.addTab(name);
		
		/*link.addClickHandler( new ClickHandler(){  
			   @Override  
			  public void onClick(ClickEvent event) {  
				   
				   Anchor clickedLink = (Anchor) event.getSource();
				   
				   Integer count = tabs.getWidgetCount();
				   for(Integer i = 0;i < count;i++){
					  Widget linkTemp = tabs.getWidget(i);
					  if (linkTemp != clickedLink){
						  linkTemp.setStyleName("ui-button ui-widget ui-state-default ui-button-text-only");
						  
					  }
				   }

				   Integer value = Integer.parseInt(clickedLink.getName());
				   clickedLink.setStyleName("ui-button ui-widget ui-state-default ui-button-text-only ui-state-active");
				   Log.info(idMap.get(value).toString());
				   presenter.goTo(new PlaceBookAssesmentDetails(idMap.get(value))); 
			  
			 }  
			 }); */
		
		
	}
	@Override
	public AcceptsOneWidget getDetailsPanel() {
		
		return content;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public ListBox getYearListBox() {
		return yearListBox;
	}

	public DefaultSuggestBox<AssesmentProxy, EventHandlingValueHolderItem<AssesmentProxy>> getAssesmentSuggestionBox() {
		return assesmentSuggestionBox;
	}

}
