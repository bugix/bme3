package medizin.client.ui.view;

import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.image.ImageAltTextViewer;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.image.polygon.ImagePolygonViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.util.ClientUtility;
import medizin.client.util.ImageWidthHeight;
import medizin.client.util.MathJaxs;
import medizin.client.util.Point;
import medizin.client.util.PolygonPath;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AcceptAnswerSubViewImpl extends Composite implements AcceptAnswerSubView  {

	private static AcceptAnswerViewImplUiBinder uiBinder = GWT.create(AcceptAnswerViewImplUiBinder.class);

	interface AcceptAnswerViewImplUiBinder extends UiBinder<Widget, AcceptAnswerSubViewImpl> {}
	
	/*@UiField
	CellTable<AnswerProxy> table;*/

	@UiField(provided = true)
	CellTable<AnswerProxy> table;
		
    @UiField(provided = true)
	public MySimplePager pager;
	
	@Override
	public AbstractHasData<AnswerProxy> getTable(){
		return table;
	}

	private  Delegate delegate;
	private QuestionProxy questionProxy;

	private SingleSelectionModel<AnswerProxy> selectionModel;
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	private DialogBox dialogBox;
	
	private Boolean flag = false;
	
	private AcceptAnswerSubView acceptAnswerSubView = null;
	
	@UiField
	Label headerText;
	
	@UiField
	IconButton viewHtmlText;
	
	@UiField
	IconButton acceptAllAnswer;

	private Boolean forcedActive;
	
	public AcceptAnswerSubViewImpl(Boolean flag, Boolean forcedActive, Boolean acceptAllAnswerFlag) {
		
		this.flag = flag;
		this.forcedActive = forcedActive;
		
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AnswerProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		ProvidesKey<AnswerProxy> keyProvider = ((AbstractHasData<AnswerProxy>) table).getKeyProvider();
        selectionModel = new SingleSelectionModel<AnswerProxy>(keyProvider);
        table.setSelectionModel(selectionModel);  
       
        table.addCellPreviewHandler(new Handler<AnswerProxy>() {

			@Override
			public void onCellPreview(CellPreviewEvent<AnswerProxy> event) {
				
				boolean isClicked="click".equals(event.getNativeEvent().getType());
				if(isClicked){
					if(event.getColumn()==1){
						
						final AnswerProxy selectedObject = selectionModel.getSelectedObject();
						
						if (selectionModel.isSelected(selectedObject))
							selectionModel.setSelected(selectedObject, false);
						
						if (selectedObject != null) {	
							
							if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.Imgkey) || questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.ShowInImage) || questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.MCQ))
							{					
							 	dialogBox = new DialogBox();
							 	dialogBox.setTitle(constants.imageViewer());
							 	dialogBox.setText(constants.imageViewer());
							 	
							 	final VerticalPanel vp = new VerticalPanel();
								 
								if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.Imgkey))
								{	
									final List<Point> points = Point.getPoints(Lists.newArrayList(selectedObject.getPoints()));
									
									
									if(questionProxy.getQuestionResources() != null && questionProxy.getQuestionResources().isEmpty() == false) {
										QuestionResourceProxy questionResourceProxy = Lists.newArrayList(questionProxy.getQuestionResources()).get(0);
									
										final Integer imageHeight = questionResourceProxy.getImageHeight();
										final Integer imageWidth = questionResourceProxy.getImageWidth();
										final String path = questionResourceProxy.getPath();
										
										if (imageWidth != null && imageHeight != null)
										{
											ImageRectangleViewer viewer = new ImageRectangleViewer(path, imageWidth, imageHeight, points, false);
											vp.add(viewer);
										}
										else
										{
											ClientUtility.getImageWidthHeight(path, new ImageWidthHeight() {
												
												@Override
												public void apply(Integer width, Integer height) {
													ImageRectangleViewer viewer = new ImageRectangleViewer(path, width, height, points, false);
													vp.add(viewer);
												}
											});
										}
									}else {
										Log.error("Question resources are null imagekey");
										ImageAltTextViewer viewer = new ImageAltTextViewer();
										vp.add(viewer);
									}
								}
								else if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.ShowInImage))
								{
									if(questionProxy.getQuestionResources() != null && questionProxy.getQuestionResources().isEmpty() == false) {
										QuestionResourceProxy questionResourceProxy = Lists.newArrayList(questionProxy.getQuestionResources()).get(0);
										final Integer imageHeight = questionResourceProxy.getImageHeight();
										final Integer imageWidth = questionResourceProxy.getImageWidth();
										final String path = questionResourceProxy.getPath();
										List<PolygonPath> polygonPathList = PolygonPath.getPolygonPaths(Lists.newArrayList(selectedObject.getPoints()));
										ImagePolygonViewer imgPolygonViewer = new ImagePolygonViewer(path,  imageWidth, imageHeight, polygonPathList, false);
										vp.add(imgPolygonViewer);
									}else {
										Log.error("Question resources are null show in image");
										ImageAltTextViewer viewer = new ImageAltTextViewer();
										vp.add(viewer);
									}
									
								}
								else if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.MCQ))
								{
									if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Image))
									{
										final ImageViewer imgViewer = new ImageViewer();
										
										imgViewer.setUrl("",selectedObject.getMediaPath(), null, null, questionProxy.getQuestionType().getQuestionType());
										/*ClientUtility.getImageWidthHeight(selectedObject.getMediaPath(), new ImageWidthHeight() {
											
											@Override
											public void apply(Integer width, Integer height) {
												imgViewer.setUrl(selectedObject.getMediaPath(), width, height, questionProxy.getQuestionType().getQuestionType());											
											}
										});*/
										
										vp.add(imgViewer);
									}
									
									if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Sound))
									{
										final AudioViewer audioViewer = new AudioViewer(selectedObject.getMediaPath());
										vp.add(audioViewer);
										
										dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
											
											@Override
											public void onClose(CloseEvent<PopupPanel> event) {
												audioViewer.closed();
											}
										});
									}
									
									if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Video))
									{
										VideoViewer videoViewer = new VideoViewer(selectedObject.getMediaPath());
										vp.add(videoViewer);
									}
								}
															
								dialogBox.setWidget(vp);	
								dialogBox.setGlassEnabled(true);
								dialogBox.setAutoHideEnabled(true);
								dialogBox.getElement().getStyle().setZIndex(5);
								dialogBox.center();	
								dialogBox.show();
							}
						}
					}
				}	
			}
		});
        
        acceptAllAnswer.setText(constants.acceptAllAnswer());
        
        if (acceptAllAnswerFlag == false)
        	acceptAllAnswer.removeFromParent();
        
        /*selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
        	
			public void onSelectionChange(SelectionChangeEvent event) {
				AnswerProxy selectedObject = selectionModel.getSelectedObject();
				
				if (selectionModel.isSelected(selectedObject))
					selectionModel.setSelected(selectedObject, false);
				
				if (selectedObject != null) {	
					
					if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.Imgkey) || questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.ShowInImage) || questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.MCQ))
					{					
					 	dialogBox = new DialogBox();
					 	
					 	VerticalPanel vp = new VerticalPanel();
						 
						if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.Imgkey))
						{	
							List<Point> points = Point.getPoints(Lists.newArrayList(selectedObject.getPoints()));
							
							ImageRectangleViewer viewer = new ImageRectangleViewer(questionProxy.getPicturePath(), questionProxy.getQuestionType().getImageWidth(), questionProxy.getQuestionType().getImageHeight(), points, false);
							
							vp.add(viewer);
						}
						else if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.ShowInImage))
						{
							List<PolygonPath> polygonPathList = PolygonPath.getPolygonPaths(Lists.newArrayList(selectedObject.getPoints()));
							
							ImagePolygonViewer imgPolygonViewer = new ImagePolygonViewer(questionProxy.getPicturePath(), questionProxy.getQuestionType().getImageWidth(), questionProxy.getQuestionType().getImageHeight(), polygonPathList, false);
							
							vp.add(imgPolygonViewer);
						}
						else if (questionProxy.getQuestionType().getQuestionType().equals(QuestionTypes.MCQ))
						{
							if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Image))
							{
								ImageViewer imgViewer = new ImageViewer();
								imgViewer.setUrl(selectedObject.getMediaPath(), questionProxy.getQuestionType().getImageWidth(), questionProxy.getQuestionType().getImageHeight(), questionProxy.getQuestionType().getQuestionType());
								vp.add(imgViewer);
							}
							
							if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Sound))
							{
								final AudioViewer audioViewer = new AudioViewer(selectedObject.getMediaPath());
								vp.add(audioViewer);
								
								dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {
									
									@Override
									public void onClose(CloseEvent<PopupPanel> event) {
										audioViewer.closed();
									}
								});
							}
							
							if (questionProxy.getQuestionType().getMultimediaType().equals(MultimediaType.Video))
							{
								VideoViewer videoViewer = new VideoViewer(selectedObject.getMediaPath());
								vp.add(videoViewer);
							}
						}
													
						dialogBox.setWidget(vp);	
						dialogBox.setGlassEnabled(true);
						dialogBox.setAutoHideEnabled(true);
						dialogBox.getElement().getStyle().setZIndex(5);
						dialogBox.center();	
						dialogBox.show();
						
						dialogBox.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
							
							@Override
							public void setPosition(int offsetWidth, int offsetHeight) {
								int left = ((Window.getClientWidth() - offsetWidth) / 2) >> 0;
                                int top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
                                
                                dialogBox.setPopupPosition(left, top);
							}
						});
											
					}
					//showDetails(selectedObject);
				}
			}
		});   */     
        
		init(flag, forcedActive);

	}

	private DeclineEmailPopupDelagate delegatePopup;


	private void init(Boolean flag, Boolean forcedActive) {

		table.addColumn(new Column<AnswerProxy, AnswerProxy>(new ValidityTextCell()) {
    	      @Override
    	      public AnswerProxy getValue(AnswerProxy object) {
    	        return object;
    	      }
    	    },  new Header<String>(new ValidityHeader()){

    			@Override
    			public String getValue() {
    				
    				return "hallo";
    			}});
        
        
        
		table.addColumn(new Column<AnswerProxy, AnswerProxy>(new AnswerTextCell()) {
  	      @Override
  	      public AnswerProxy getValue(AnswerProxy object) {
  	        return object;
  	      }
  	    },  constants.answerText());
        
		table.addColumnStyleName(0, "iconColumn");
		table.addColumnStyleName(1, "questionTextColumn");

		if (flag)
		{
			addColumn(new ActionCell<AnswerProxy>( McAppConstant.DECLINE_ICON, new ActionCell.Delegate<AnswerProxy>() {

				public void execute(final AnswerProxy answerProxy) {
	            	/*DeclineEmailPopup popup = new DeclineEmailPopup(answerProxy);
	            	popup.setDelegate(delegatePopup);*/
//	            	Log.debug("hinterPupup");
	             // delegate.rejectClicked(answerProxy, "verweigert");
					
					ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.rejectAnswerMsg(), new ConfirmDialogBoxYesNoButtonEventHandler() {
						
						@Override
						public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
							delegate.rejectClicked(answerProxy);
						}
						
						@Override
						public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
						}
					});				
	            }
	          }), constants.reject(), new GetValue<AnswerProxy>() {
	        public AnswerProxy getValue(AnswerProxy answerProxy) {
	          return answerProxy;
	        }
	      }, null);

			addColumn(new ActionCell<AnswerProxy>( McAppConstant.ACCEPT_ICON, new ActionCell.Delegate<AnswerProxy>() {
			    public void execute(AnswerProxy answerProxy) {
			     	delegate.acceptClicked(answerProxy, acceptAnswerSubView);
			    }
			  }), constants.accept(), new GetValue<AnswerProxy>() {
			public AnswerProxy getValue(AnswerProxy answerProxy) {
			  return answerProxy;
			}
			}, null);
	    	
	    	
	    	//table.addColumnStyleName(1, "questionTextColumn");
	    	table.addColumnStyleName(2, "deleteColumn");
		}
				
		if (forcedActive)
		{
			addColumn(new ActionCell<AnswerProxy>(McAppConstant.ACCEPT_ICON,
					new ActionCell.Delegate<AnswerProxy>() {
						public void execute(AnswerProxy answerProxy) {
							delegate.forcedAcceptClicked(answerProxy,acceptAnswerSubView);
						}
					}), constants.forceAccept(), new GetValue<AnswerProxy>() {
				public AnswerProxy getValue(AnswerProxy answerProxy) {
					return answerProxy;
				}
			}, null);
	    	
	    	
	    	//table.addColumnStyleName(1, "questionTextColumn");
	    	table.addColumnStyleName(2, "deleteColumn");
		}
	}

	  /**
	   * Add a column with a header.
	   *
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	  private <C> void addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<AnswerProxy, C> fieldUpdater) {
	    Column<AnswerProxy, C> column = new Column<AnswerProxy, C>(cell) {
	      @Override
	      public C getValue(AnswerProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
//	    if (cell instanceof AbstractEditableCell<?, ?>) {
//	      editableCells.add((AbstractEditableCell<?, ?>) cell);
//	    }
	    table.addColumn(column, headerText);
	  }

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(AnswerProxy contact);
	  }


	@Override
	public void setDelegatePopup(DeclineEmailPopupDelagate delegate) {
		this.delegatePopup = delegate;
		
	}

/*@UiField
DivElement questionText;*/
	
	@UiField
	DisclosurePanel questionDisclosurePanel;

	@UiHandler("acceptAllAnswer")
	public void acceptAllAnswerClicked(ClickEvent event)
	{
		questionDisclosurePanel.setOpen(!questionDisclosurePanel.isOpen());
		if (questionProxy != null)
		{
			delegate.acceptAllAnswerClicked(questionProxy);
		}
	}
	
	@UiHandler("viewHtmlText")
	public void viewHtmlTextClicked(ClickEvent event)
	{
		questionDisclosurePanel.setOpen(!questionDisclosurePanel.isOpen());
		
		QuestionTextViewDialogBoxImpl dialogBox = new QuestionTextViewDialogBoxImpl();
		
		if (questionProxy != null)
		{
			MathJaxs.delayRenderLatexResult(RootPanel.getBodyElement());
			dialogBox.setHtmlText(questionProxy.getQuestionText());
			dialogBox.show();
			dialogBox.setWidth(dialogBox.getOffsetWidth() + "px");
			dialogBox.setLeftTopPosition((viewHtmlText.getElement().getAbsoluteLeft()-dialogBox.getOffsetWidth()+30), (viewHtmlText.getAbsoluteTop()+20));
		}
	}


	@Override
	public void setProxy(final QuestionProxy questionProxy) {
		this.questionProxy = questionProxy;
		
		if (questionProxy.getAnswers() != null)
			table.setRowCount(questionProxy.getAnswers().size(), true);
		else if (table.getRowCount() > 0)
			table.setRowCount((table.getRowCount() - 1), true);
		
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			   
			public void onRangeChange(RangeChangeEvent event) {
				
				delegate.onRangeChanged(questionProxy, table);
			}
		});
		delegate.onRangeChanged(questionProxy, table);
		
		//questionText.setInnerHTML(questionProxy.getQuestionText());
		
		//new SafeHtmlBuilder().appendHtmlConstant(questionProxy.getQuestionText());
		
		if (flag || forcedActive)
		{
			//headerText.setText(new HTML(ClientUtility.removeMathJax(questionProxy.getQuestionText())).getText());
			headerText.setText(new HTML(ClientUtility.removeMathJax(questionProxy.getQuestionText())).getText());

			headerText.getElement().getStyle().setMarginLeft(5, Unit.PX);
			viewHtmlText.getElement().getStyle().setMargin(1, Unit.PX);
			viewHtmlText.setVisible(true);
		}
		else
		{
			headerText.setText("");
			headerText.setHeight("23px");
			viewHtmlText.setVisible(false);
		}
					
	}


	  private static class AnswerTextCell extends AbstractCell<AnswerProxy> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					AnswerProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			        return;
			      }
			      String beginn = "<div style=\"";
			      String end = "</div>";
			      if(!value.getIsAnswerAcceptedAdmin())
			      {
			    	  beginn += "color:red; ";		    	  
			      }
			      if(!value.getIsAnswerAcceptedReviewWahrer()){
			    	  beginn += "font-style:italic; ";			    	  
			      }
			      //if(!value.getIsAnswerActive()){
			      if(!Status.ACTIVE.equals(value.getStatus())){
			    	  beginn += "text-decoration: line-through; ";		    	  
			      }

			      beginn += "\">";
			      sb.appendHtmlConstant(beginn);
			      
			      if (value.getAnswerText().equals(""))
			      {
			    	  String name = "";
			    	  if (value.getQuestion() != null && value.getQuestion().getQuestionType() != null && value.getMediaPath() != null)
			    		  name = ClientUtility.getFileName(value.getMediaPath(), value.getQuestion().getQuestionType().getMultimediaType());
			    	 
			    	  sb.appendHtmlConstant(name);
			      }
			      else
			      {
			    	  sb.appendHtmlConstant(value.getAnswerText());
			      }
			      
			      sb.appendHtmlConstant(end);
				
			}
		  }

	  
	  private static class ValidityTextCell extends AbstractCell<AnswerProxy> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					AnswerProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-help\"></span>");
			        return;
			      }
			      String validityIcon = "";
			      switch (value.getValidity()) {
					case Wahr: ; validityIcon += "<span class=\"ui-icon ui-icon-plus\"></span>";
							     break;
							
					case Falsch: validityIcon += "<span class=\"ui-icon ui-icon-minus\"></span>";
							     break;
							     
					/*case Weil:	 validityIcon += "<span class=\"ui-icon ui-icon-refresh\"></span>";	
								break;*/
							     
					default:	  validityIcon += "<span class=\"ui-icon ui-icon-help\"></span>";	
							     break;
					}

			      
			      sb.appendHtmlConstant(validityIcon);
			      
				
			}
		  }
	  
	  private static class ValidityHeader extends AbstractCell<String> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-help\"></span>");
			
			      
				
			}
		  }


	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	public DisclosurePanel getQuestionDisclosurePanel() {
		return questionDisclosurePanel;
	}

	public void setQuestionDisclosurePanel(DisclosurePanel questionDisclosurePanel) {
		this.questionDisclosurePanel = questionDisclosurePanel;
	}

	public AcceptAnswerSubView getAcceptAnswerSubView() {
		return acceptAnswerSubView;
	}

	public void setAcceptAnswerSubView(AcceptAnswerSubView acceptAnswerSubView) {
		this.acceptAnswerSubView = acceptAnswerSubView;
	}
	
}
