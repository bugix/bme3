package medizin.client.activites;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import medizin.client.events.RecordChangeEvent;
import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.AnswerListViewImpl;
import medizin.client.ui.view.question.usedinmc.QuestionUsedInMC;
import medizin.client.ui.widget.process.AppLoader;

public class PartActivityQuestionUsedInMC implements QuestionUsedInMC.Delegate {
	
	private final McAppRequestFactory requests;
	private final QuestionUsedInMC view;
	private QuestionProxy question;
	private AssesmentQuestionProxy assesmentQuestionProxy;
	private HandlerRegistration answerRangeChangeHandler;
	private CellTable<AnswerProxy> answerTable;
	private AnswerListViewImpl answerListView;
	
	public PartActivityQuestionUsedInMC(McAppRequestFactory requests, QuestionUsedInMC view){
		this.requests = requests;
		this.view = view;
		this.view.setDelegate(this);
	}
	
	public void setQuestionProxy(QuestionProxy questionProxy) {
		this.question = questionProxy;
	}
	
	public void setAnswerTable(CellTable<AnswerProxy> answerTable) {
		this.answerTable = answerTable;
	}
	
	public void setAnswerListView(AnswerListViewImpl answerListView) {
		this.answerListView = answerListView;
	}
	
	public void initUsedInMCView() {
		RecordChangeEvent.register(requests.getEventBus(), view);
		if (question != null)
		{
			AppLoader.setNoLoader();
			requests.assesmentQuestionRequest().countAllAssesmentQuestionByQuestion(question.getId()).fire(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(Integer response) {
					view.setTableRowCount(response);
					onUsedInMcTableRangeChanged();
				}
			});
					
			view.addTableRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onUsedInMcTableRangeChanged();
				}

				
			});
			
			ProvidesKey<AssesmentQuestionProxy> keyProvider = ((AbstractHasData<AssesmentQuestionProxy>) view.getTable()).getKeyProvider();
			final SingleSelectionModel<AssesmentQuestionProxy> selectionModel = new SingleSelectionModel<AssesmentQuestionProxy>(keyProvider);
			
			view.getTable().setSelectionModel(selectionModel);		

			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							AssesmentQuestionProxy selectedObject = selectionModel.getSelectedObject();
							if (selectedObject != null) {
								Log.debug(selectedObject.getId() + " selected!");
								showUsedInMcAnswerDetails(selectedObject);
							}
						}
					});

			
		}
	}
	
	protected void showUsedInMcAnswerDetails(AssesmentQuestionProxy assesmentQuestionProxy) {
		
		if(assesmentQuestionProxy != null) {
			this.assesmentQuestionProxy = assesmentQuestionProxy;
			
			if (answerRangeChangeHandler!=null){
				answerRangeChangeHandler.removeHandler();
				answerRangeChangeHandler=null;
			}
			AppLoader.setNoLoader();
			requests.answerToAssQuestionRequest().countAllAnswerToAssQuestion(assesmentQuestionProxy.getId()).fire(new BMEReceiver<Long>() {

				@Override
				public void onSuccess(Long response) {
					answerTable.setRowCount(response.intValue(), true);

					onUsedInMcAnswerTableRangeChanged();
				}
			});
			
			
			answerRangeChangeHandler =  answerTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
				public void onRangeChange(RangeChangeEvent event) {
					PartActivityQuestionUsedInMC.this.onUsedInMcAnswerTableRangeChanged();
				}
			});
			answerListView.hideAddAnswer();
			answerListView.showAllBtn();
			
		}
		
	}

	private void onUsedInMcAnswerTableRangeChanged() {
		
		final Range range = answerTable.getVisibleRange();
		AppLoader.setNoLoader();
		requests.answerToAssQuestionRequest().findAllAnswerToAssQuestion(this.assesmentQuestionProxy.getId(), range.getStart(), range.getLength()).with("question","rewiewer","autor","question.questionType").fire( new BMEReceiver<List<AnswerProxy>>(){


			@Override
			public void onSuccess(List<AnswerProxy> response) {
				answerTable.setRowData(range.getStart(), response);
			}
		});
		
	}
	
	private void onUsedInMcTableRangeChanged() {
		if (question != null)
		{
			final Range range = view.getTableVisibleRange();
			AppLoader.setNoLoader();
			requests.assesmentQuestionRequest().findAllAssesmentQuestionByQuestion(question.getId(), range.getStart(), range.getLength()).with(view.getPaths()).fire(new BMEReceiver<List<AssesmentQuestionProxy>>() {

				@Override
				public void onSuccess(List<AssesmentQuestionProxy> response) {
					view.setTableRowData(range.getStart(), response);
				}
			});
		}
		
	}
}
