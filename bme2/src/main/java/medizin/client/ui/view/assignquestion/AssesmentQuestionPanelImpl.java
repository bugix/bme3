package medizin.client.ui.view.assignquestion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.MyCellTableNoHilightResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.sendmail.SendMailPopupViewImpl;
import medizin.client.util.ClientUtility;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;

public class AssesmentQuestionPanelImpl extends Composite implements AssesmentQuestionPanel {

	private static AssesmentQuestionPanelImplUiBinder uiBinder = GWT
			.create(AssesmentQuestionPanelImplUiBinder.class);

	interface AssesmentQuestionPanelImplUiBinder extends
			UiBinder<Widget, AssesmentQuestionPanelImpl> {
	}
	
	private BmeConstants constants=GWT.create(BmeConstants.class);
	
	private Delegate delegate;
	
	private static int pageSize=3;
	
	public static int getPageSize() {
		return pageSize;
	}

	private SendMailPopupViewImpl sendMailPopupViewImpl=null;
	
	private List<QuestionTypeCountPerExamProxy> questionTypeCountPerExams;
	
	public List<QuestionTypeCountPerExamProxy> getQuestionTypeCountPerExams() {
		return questionTypeCountPerExams;
	}

	public void setQuestionTypeCountPerExams(
			List<QuestionTypeCountPerExamProxy> questionTypeCountPerExams) {
		this.questionTypeCountPerExams = questionTypeCountPerExams;
	}

	public SendMailPopupViewImpl getSendMailPopupViewImpl() {
		return sendMailPopupViewImpl;
	}

/*	@UiField
	VerticalPanel questionTypeVP;
	
	public VerticalPanel getQuestionTypeVP() {
		return questionTypeVP;
	}
*/
	@UiField
	VerticalPanel assesmentQuestionDisplayPanel;
	
	@UiField
	Button sendMail;
	
	@UiField
	ScrollPanel assesmentQuestionScrollPanel;
	
    @UiField(provided = true)
    CellTable<QuestionTypeCountProxy> table;
	
    // Open All Questions button to open all the assigned question panel.
    @UiField
    Button openAllQuestionsBtn;
    
    // Close All Questions button to close all the assigned question panel.
    @UiField
    Button closeAllQuestionsBtn;
    
    //Open All Questions Button click handler.
    @UiHandler("openAllQuestionsBtn")
    public void openAllButtonClicked(ClickEvent event){
    	Log.info("open All Questions Button Clicked Total widget of view is : " + assesmentQuestionDisplayPanel.getWidgetCount());
    	try{
    		for(int widgetCount=0;widgetCount < assesmentQuestionDisplayPanel.getWidgetCount(); widgetCount++){
    			Widget widget = assesmentQuestionDisplayPanel.getWidget(widgetCount);
    			if(widget!=null){
    				((AssesmentQuestionViewImpl)widget).open();
    			}
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		Log.info("Exception occured while opening All Questions Panels");
		}
    }
    //Close All Questions Button click handler.
    @UiHandler("closeAllQuestionsBtn")
    public void closeAllButtonClicked(ClickEvent event){
    	Log.info("Close All Questions Button Clicked Total widget of view is : " + assesmentQuestionDisplayPanel.getWidgetCount());
    	try{
    		for(int widgetCount=0;widgetCount < assesmentQuestionDisplayPanel.getWidgetCount(); widgetCount++){
    			Widget widget = assesmentQuestionDisplayPanel.getWidget(widgetCount);
    			if(widget!=null){
    				((AssesmentQuestionViewImpl)widget).close();
    			}
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		Log.info("Exception occured while closing All Questions panels");
		}
    }
    
    public CellTable<QuestionTypeCountProxy> getTable() {
		return table;
	}

	@UiField(provided = true)
	public MySimplePager pager;
	
	public Button getSendMail() {
		return sendMail;
	}

	@UiField(provided = true)
	ValueListBox<PersonProxy> authorListBox=new ValueListBox<PersonProxy>(new AbstractRenderer<PersonProxy>() {

		@Override
		public String render(PersonProxy object) {
			// TODO Auto-generated method stub
			if(object!=null)
				return object.getPrename() +" "+ object.getName();
			else
			{
				return "";
			}
		}
	});
	
	public ValueListBox<PersonProxy> getAuthorListBox() {
		return authorListBox;
	}
	
	protected Set<String> paths = new HashSet<String>();
	
	private Map<String, QuestionTypeCountProxy> questionTypeModelMap=new HashMap<String, QuestionTypeCountProxy>();

	
	public Map<String, QuestionTypeCountProxy> getQuestionTypeModelMap() {
		return questionTypeModelMap;
	}

	public AssesmentQuestionPanelImpl() {
		
		CellTable.Resources tableResources = GWT
				.create(MyCellTableNoHilightResources.class);
		table = new CellTable<QuestionTypeCountProxy>(pageSize,
				tableResources);

		MySimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,
				true, pageSize*2, true);

		
		initWidget(uiBinder.createAndBindUi(this));
//		assesmentQuestionDisplayPanel.setBorderWidth(1);
//		assesmentQuestionDisplayPanel.setHeight("100px");
//		assesmentQuestionDisplayPanel.setWidth("100px");
		openAllQuestionsBtn.setText(constants.openAll());
		closeAllQuestionsBtn.setText(constants.closeAll());
		
		assesmentQuestionDisplayPanel.setSpacing(5);
		sendMail.setText(constants.sendMail());
		authorListBox.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				delegate.authorValueChanged(event.getValue());
				
			}
		});
		 
		assesmentQuestionScrollPanel.setHeight(((int)(Window.getClientHeight()*0.65))+"px");
		
		
		table.addColumn(new TextColumn<QuestionTypeCountProxy>() {

			@Override
			public String getValue(QuestionTypeCountProxy object) {
				return (ClientUtility.getSignedNumber(object.getCount()) + " / " + object.getTotalQuestionAllocated().toString());
			}
		},"Count");
	   
	   
      paths.add("count");
      
		table.addColumn(new TextColumn<QuestionTypeCountProxy>() {

			@Override
			public String getValue(QuestionTypeCountProxy object) {
				
				String questionTypeStr="";
				
				Set<QuestionTypeProxy> questionTypes=object.getQuestionTypeCountPerExamProxy().getQuestionTypesAssigned();
				int k=0;
				for(QuestionTypeProxy questionType:questionTypes)
				{
					if(k==0)
					questionTypeStr=questionTypeStr+questionType.getShortName();
					else
					{
						questionTypeStr=questionTypeStr+","+questionType.getShortName();

					}
					String key="";
					if(questionTypeModelMap.get("q"+questionType.getId()+"s"+object.getQuestionSumPerPersonProxy().getQuestionEvent().getId())==null)
					questionTypeModelMap.put(key, object);
					
					k++;	
				}
				
				
				questionTypeStr=questionTypeStr+"("+object.getQuestionSumPerPersonProxy().getQuestionEvent().getEventName()+")";

				
				return questionTypeStr;
			}
		},"Question Type");
	   
	   
      paths.add("questionType");
      
      table.addRangeChangeHandler(new Handler() {
		
		@Override
		public void onRangeChange(RangeChangeEvent event) {
			int start=event.getNewRange().getStart();
			int end=start+event.getNewRange().getLength();
			delegate.initTopElemnt(start,end);
		}
	});
      
//
		
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	@Override
	public void addAssesmentQuestion(AssesmentQuestionView question) {
		assesmentQuestionDisplayPanel.add(question);	
	}
	


	@Override
	public void removeAll() {
		assesmentQuestionDisplayPanel.clear();		
	}

	@Override
	public VerticalPanel getAssesmentQuestionDisplayPanel() {
		
		return assesmentQuestionDisplayPanel;
	}
	
	@UiHandler("sendMail")
	public void showSendMailPopup(ClickEvent event)
	{
		if(sendMailPopupViewImpl==null)
		{
			sendMailPopupViewImpl=new SendMailPopupViewImpl(getSendMailParamsMap());
			
			sendMailPopupViewImpl.addSendClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {					
					delegate.sendMail(sendMailPopupViewImpl.getMessageContent());					
				}
			});
			
			sendMailPopupViewImpl.addRestoreClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					delegate.loadTemplate();
				}
			});		
		}
		delegate.loadTemplate();
	}
	
	private Map<String, String> getSendMailParamsMap() {
		Map<String, String> paramMap = Maps.newHashMap();
		
		paramMap.put(constants.mailVarAssesment(), constants.mailAssesment());
		paramMap.put(constants.mailVarTo(), constants.mailToName());
		paramMap.put(constants.mailVarFrom(), constants.mailFromName());
		paramMap.put(constants.mailVarStartDate(), constants.mailStartDate());
		paramMap.put(constants.mailVarClosedDate(), constants.mailClosedDate());
		paramMap.put(constants.mailVarMC(), constants.mailMC());
		paramMap.put(constants.mailVarProposedCount(), constants.mailProposedCount());
		paramMap.put(constants.mailVarTotalCount(), constants.mailTotalCount());
		paramMap.put(constants.mailVarLoopStart(), constants.mailLoopStart());
		paramMap.put(constants.mailVarLoopEnd(), constants.mailLoopEnd());
		paramMap.put(constants.mailVarAllocatedCount(), constants.mailAllocatedCount());
		paramMap.put(constants.mailVarQuestionType(), constants.mailQuestionType());
		paramMap.put(constants.mailVarSpecialization(), constants.mailSpecialization());
		paramMap.put(constants.mailVarTotalRemaining(), constants.mailTotalRemaining());
		paramMap.put(constants.mailVarTotalRemainingCount(), constants.mailTotalRemainingCount());
		return paramMap;
	}
	
	public void displayMail(String response)
	{
		if (sendMailPopupViewImpl != null)
		{
			sendMailPopupViewImpl.setMessageContent(response);
			if (sendMailPopupViewImpl.isShowing() == false)
				sendMailPopupViewImpl.center();
		}
		else
		{
			Log.error("PopupImpl is null");
		}
		
	}

}
