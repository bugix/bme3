package medizin.client.ui.view.question.learningobjective;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.MainQuestionSkillProxy;
import medizin.client.proxy.MinorQuestionSkillProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.process.ApplicationLoadingView;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class QuestionLearningObjectiveSubViewImpl extends Composite implements QuestionLearningObjectiveSubView, RecordChangeHandler {

	private static QuestionLearningObjectiveSubViewUiBinder uiBinder = GWT
			.create(QuestionLearningObjectiveSubViewUiBinder.class);

	interface QuestionLearningObjectiveSubViewUiBinder extends
			UiBinder<Widget, QuestionLearningObjectiveSubViewImpl> {
	}

	Map<String, Widget> mainSkillMap;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	protected List<String> paths = new ArrayList<String>();

	private Delegate delegate;
	
	private final BmeConstants constants = GWT.create(BmeConstants.class);
	
	@UiField (provided = true)
	public CellTable<MainQuestionSkillProxy> majorTable;
	
	@UiField (provided = true)
	public CellTable<MinorQuestionSkillProxy> minorTable;
	
	
	@UiField (provided = true)
	public SimplePager pagerMajor;
	
	@UiField (provided = true)
	public SimplePager pagerMinor;
		
	@UiField
	public IconButton btnAdd;	
	
	@UiField
	ApplicationLoadingView loadingPopup;
	
	@UiField
	ApplicationLoadingView loadingPopupMajor;
	
	@UiField
	ApplicationLoadingView loadingPopupMinor;
	
	public QuestionLearningObjectivePopupView popUpView;
	
	public LearningObjectiveViewImpl learningObjectiveViewImpl;
	
	public DialogBox popup;
	
	public boolean loadingFlag = false;
			
	public QuestionLearningObjectiveSubViewImpl(boolean isDeleteLearningObjective) 
	{
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pagerMajor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		pagerMinor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		majorTable = new CellTable<MainQuestionSkillProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		minorTable = new CellTable<MinorQuestionSkillProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));	
	
		btnAdd.setText(constants.addSkill());
		
		majorTable.addStyleName("skillTable");
		minorTable.addStyleName("skillTable");
		
		initMajorTable(isDeleteLearningObjective);
		
		initMinorTable(isDeleteLearningObjective);				
	}
	
	private void initMajorTable(boolean isDeleteLearningObjective) 
	{
		paths.add("mainClassi");
		TextColumn<MainQuestionSkillProxy> mainClassiCol = new TextColumn<MainQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		
		majorTable.addColumn(mainClassiCol, constants.mainClassi());		
		majorTable.setColumnWidth(mainClassiCol, "130px");
				
		paths.add("classificatonTopic");
		TextColumn<MainQuestionSkillProxy> classificationTopicCol = new TextColumn<MainQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		
		majorTable.addColumn(classificationTopicCol, constants.classiTopic());
		majorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MainQuestionSkillProxy> topicCol = new TextColumn<MainQuestionSkillProxy>() {
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		majorTable.setTableLayoutFixed(true);
		majorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MainQuestionSkillProxy> skillLevelCol = new TextColumn<MainQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MainQuestionSkillProxy object) 
			{
				if (object == null)
					return null;
				else
				{
					if (object.getSkill().getSkillLevel() == null)
						return renderer.render("");
					else
						return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
				}
			}
		};
		
		majorTable.setColumnWidth(skillLevelCol, "75px");		
		majorTable.addColumn(skillLevelCol, constants.skillLevel());		
		//majorTable.addColumnStyleName(2, "topicCol");
		
		if (isDeleteLearningObjective)
			addMajorTableLastColumn();
			
	}

	//SPEC Change
	public void removeMajorTableLastColumn() {
		if(majorTable != null){
			if(majorTable.getColumnCount() > 4){
				majorTable.removeColumn(majorTable.getColumnCount()-1);
			}
		}
	}
	
	//SPEC Change
	public void addMajorTableLastColumn() {
		addColumnMajor(new ActionCell<MainQuestionSkillProxy>(
				McAppConstant.DELETE_ICON, new ActionCell.Delegate<MainQuestionSkillProxy>() {
					public void execute(final MainQuestionSkillProxy mainSkill) {
						ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteMajorQuestionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
							
							@Override
							public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
								delegate.majorDeleteClicked(mainSkill);
							}
							
							@Override
							public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
							}
						});
					}
				}), "", new GetValueMajor<MainQuestionSkillProxy>() {
			public MainQuestionSkillProxy getValue(MainQuestionSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		majorTable.addColumnStyleName(4, "iconColumn");
			
	}
	
	public void initMinorTable(boolean isDeleteLearningObjective)
	{
		paths.add("mainClassi");
		TextColumn<MinorQuestionSkillProxy> mainClassiCol = new TextColumn<MinorQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		minorTable.addColumn(mainClassiCol, constants.mainClassi());
		minorTable.setColumnWidth(mainClassiCol, "130px");
			
		paths.add("classificatonTopic");
		TextColumn<MinorQuestionSkillProxy> classificationTopicCol = new TextColumn<MinorQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		minorTable.addColumn(classificationTopicCol, constants.classiTopic());
		minorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MinorQuestionSkillProxy> topicCol = new TextColumn<MinorQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorQuestionSkillProxy object) 
			{
				return object == null ? null : renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		minorTable.setTableLayoutFixed(true);
		minorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MinorQuestionSkillProxy> skillLevelCol = new TextColumn<MinorQuestionSkillProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MinorQuestionSkillProxy object) 
			{
				if (object == null)
					return null;
				else
				{
					if (object.getSkill().getSkillLevel() == null)
						return renderer.render("");
					else
						return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
				}				
			}
		};
		minorTable.addColumn(skillLevelCol, constants.skillLevel());
		minorTable.setColumnWidth(skillLevelCol, "75px");
		
		if (isDeleteLearningObjective)
			addMinorTableLastColumn();
	}

	//SPEC Change
	public void removeMinorTableLastColumn() {
		if(minorTable != null){
			if(minorTable.getColumnCount() > 4){
				minorTable.removeColumn(minorTable.getColumnCount()-1);
			}
		}
	}
	//SPEC Change
	public void addMinorTableLastColumn() {
		addColumnMinor(new ActionCell<MinorQuestionSkillProxy>(
				McAppConstant.DELETE_ICON, new ActionCell.Delegate<MinorQuestionSkillProxy>() {
					public void execute(final MinorQuestionSkillProxy minorSkill) {
						
						ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteMinorQuestionConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
							
							@Override
							public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
								delegate.minorDeleteClicked(minorSkill);
							}
							
							@Override
							public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
							}
						});
					}
				}), "", new GetValueMinor<MinorQuestionSkillProxy>() {
			public MinorQuestionSkillProxy getValue(MinorQuestionSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		minorTable.addColumnStyleName(4, "iconColumn");
	}
	
	private <C> void addColumnMajor(Cell<C> cell, String headerText,final GetValueMajor<C> getter, FieldUpdater<MainQuestionSkillProxy, C> fieldUpdater) 
	{
		Column<MainQuestionSkillProxy, C> column = new Column<MainQuestionSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MainQuestionSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		majorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMajor<C> {
		C getValue(MainQuestionSkillProxy proxyvalue);
	}
	
	private <C> void addColumnMinor(Cell<C> cell, String headerText,final GetValueMinor<C> getter, FieldUpdater<MinorQuestionSkillProxy, C> fieldUpdater) 
	{
		Column<MinorQuestionSkillProxy, C> column = new Column<MinorQuestionSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MinorQuestionSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		minorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMinor<C> {
		C getValue(MinorQuestionSkillProxy proxyvalue);
	}
	
	@UiHandler("btnAdd")
	public void btnAddClicked(ClickEvent event)
	{		
		VerticalPanel vp = new VerticalPanel();
		
		popup = new DialogBox();
		
		popup.clear();
		
		popup.setGlassEnabled(true);
		
		learningObjectiveViewImpl = new LearningObjectiveViewImpl(true);	
				
		popup.addStyleName("learningObjPopupStyle");
		
		IconButton addMajor = new IconButton();
		IconButton addMinor = new IconButton();
		IconButton clearAll = new IconButton();
		IconButton cancelButton = new IconButton();
		
		addMajor.setText(constants.majorBtnLbl());
		addMajor.setIcon("plusthick");
		addMinor.setText(constants.minorBtnLbl());
		addMinor.setIcon("plusthick");
		clearAll.setText(constants.clearAll());
		clearAll.setIcon("trash");
		cancelButton.setIcon("close");
		cancelButton.addStyleName("learningObjPopupCloseButton");
		
		
		vp.add(cancelButton);
		
		addMajor.addStyleName("learningObjPopupLabel");
		addMinor.addStyleName("learningObjPopupButton");
		
		learningObjectiveViewImpl.getMainClassiLbl().addStyleName("learningObjPopupLabel");
		learningObjectiveViewImpl.getSkillLevelLbl().addStyleName("learningObjPopupLabel");
		
		learningObjectiveViewImpl.getTable().setWidth("1190px");
		learningObjectiveViewImpl.getLearningScrollPanel().addStyleName("learningObjScroll");
		
		learningObjectiveViewImpl.getHpBtnPanel().add(addMajor);		
		learningObjectiveViewImpl.getHpBtnPanel().add(addMinor);
		learningObjectiveViewImpl.getHpBtnPanel().add(clearAll);
		
		vp.add(learningObjectiveViewImpl);
		
		popup.add(vp);
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				learningObjectiveViewImpl = null;
				popup.clear();			
				popup.hide();	
								
				delegate.closeButtonClicked();
			}
		});
		
		addMajor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.addMainClicked();
			}
		});
		
		addMinor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.addMinorClicked();
			}
		});
		
		clearAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.clearAllButtonClicked();
			}
		});
		
		delegate.loadLearningObjectiveData();
		
		popup.center();
	}

	@Override
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public Map getMainSkillMap()
	{
		return this.mainSkillMap;
	}

	public LearningObjectiveViewImpl getLearningObjectiveViewImpl() {
		return learningObjectiveViewImpl;
	}

	public void setLearningObjectiveViewImpl(
			LearningObjectiveViewImpl learningObjectiveViewImpl) {
		this.learningObjectiveViewImpl = learningObjectiveViewImpl;
	}

	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = majorTable.getRowCount();
			McAppConstant.TABLE_PAGE_SIZE = pagesize;
		} else if (event.getRecordValue().matches("\\d+")) {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		majorTable.setPageSize(pagesize);
		minorTable.setPageSize(pagesize);
	}

	@Override
	public void initLearningObjectiveView() {
		delegate.initLearningObjectiveView();
	}

	
	@Override
	public ApplicationLoadingView getLoadingPopup() {
			return loadingPopup;
	}
	
	@Override
	public ApplicationLoadingView getLoadingPopupMajor() {
		return loadingPopupMajor;
	}
	
	@Override
	public ApplicationLoadingView getLoadingPopupMinor() {
		return loadingPopupMinor;
	}
}
