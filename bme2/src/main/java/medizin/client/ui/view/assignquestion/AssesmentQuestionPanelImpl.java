package medizin.client.ui.view.assignquestion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionTypeCountPerExamProxy;
import medizin.client.proxy.QuestionTypeProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.shared.i18n.BmeConstants;

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
import com.google.gwt.user.cellview.client.SimplePager;
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
	
	private static int pageSize=5;
	
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
	
    
    public CellTable<QuestionTypeCountProxy> getTable() {
		return table;
	}

	@UiField(provided = true)
	public SimplePager pager;
	
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
				.create(MyCellTableResources.class);
		table = new CellTable<QuestionTypeCountProxy>(pageSize,
				tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, pageSize*2, true);

		
		initWidget(uiBinder.createAndBindUi(this));
//		assesmentQuestionDisplayPanel.setBorderWidth(1);
//		assesmentQuestionDisplayPanel.setHeight("100px");
//		assesmentQuestionDisplayPanel.setWidth("100px");
		assesmentQuestionDisplayPanel.setSpacing(5);
		sendMail.setText(constants.sendMail());
		authorListBox.addValueChangeHandler(new ValueChangeHandler<PersonProxy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PersonProxy> event) {
				delegate.authorValueChanged(event.getValue());
				
			}
		});
		 
		assesmentQuestionScrollPanel.setHeight(((int)(Window.getClientHeight()*0.73))+"px");
		
		
		table.addColumn(new TextColumn<QuestionTypeCountProxy>() {

			@Override
			public String getValue(QuestionTypeCountProxy object) {
				return object.getCount().toString();
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
			sendMailPopupViewImpl=new SendMailPopupViewImpl();
			
			sendMailPopupViewImpl.setAnimationEnabled(true);
			sendMailPopupViewImpl.center();
			delegate.loadTemplate();
			
			sendMailPopupViewImpl.getSendMailButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					delegate.sendMail(sendMailPopupViewImpl.getMessageContent());
					
				}
			});
		}
		sendMailPopupViewImpl.show();
		
	}
	
	

}
