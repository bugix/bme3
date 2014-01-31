package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.QuestionResourceProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.view.renderer.EnumRenderer;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.resource.audio.AudioViewer;
import medizin.client.ui.widget.resource.image.ImageAltTextViewer;
import medizin.client.ui.widget.resource.image.ImageViewer;
import medizin.client.ui.widget.resource.image.rectangle.ImageRectangleViewer;
import medizin.client.ui.widget.resource.video.VideoViewer;
import medizin.client.util.ClientUtility;
import medizin.client.util.ImageWidthHeight;
import medizin.client.util.Point;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnswerListViewImpl extends Composite implements  AnswerListView {

	private static AnswerViewImplUiBinder uiBinder = GWT.create(AnswerViewImplUiBinder.class);

	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	interface AnswerViewImplUiBinder extends UiBinder<Widget, AnswerListViewImpl> {}
	
	@UiField
	Label headerText;

	@UiField
    IconButton newAnswer;
	
	@UiField
	IconButton showAll;

	@UiField(provided = true)
	CellTable<AnswerProxy> tableAnswer;

	@UiField(provided = true)
	public MySimplePager pager;

	@UiField
	HorizontalPanel headerHP;
	
	public BmeConstants constant =GWT.create(BmeConstants.class);
    private Delegate delegate;
    protected Set<String> paths = new HashSet<String>();
    private List<AbstractEditableCell<?, ?>> editableCells;

	public AnswerListViewImpl(boolean addAnswerRights, boolean isEditable, QuestionTypes questionTypes) {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		tableAnswer = new CellTable<AnswerProxy>(McAppConstant.TABLE_PAGE_SIZE,tableResources);
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources,true, McAppConstant.TABLE_JUMP_SIZE, true);

		initWidget(uiBinder.createAndBindUi(this));
		
		if(addAnswerRights == false) {
			newAnswer.removeFromParent();
		}
		
		init(isEditable,questionTypes);
		
		headerText.setText("");
		headerText.setHeight("23px");
		showAll.setVisible(false);
		headerHP.setCellHorizontalAlignment(showAll, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	@UiHandler("newAnswer")
	void addEventClicked(ClickEvent event) {
		Log.info("event");
		delegate.addNewAnswerClicked();
	}

	@UiHandler("showAll")
	void showAllClicked(ClickEvent event) {
		
		event.preventDefault();
		event.stopPropagation();
		Log.info("event");
		newAnswer.setVisible(true);
		showAll.setVisible(false);
		delegate.showAllClicked();
	}

	
	public void init(boolean isEditable, QuestionTypes questionTypes) {
    	Log.debug("Im AnswerListView.init() ");
    	
    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
    	int columnIndex = 0;
		tableAnswer.addColumn(new Column<AnswerProxy, AnswerProxy>(new ValidityTextCell()) {
			@Override
			public AnswerProxy getValue(AnswerProxy object) {
				return object;
			}
		}, new Header<String>(new ValidityHeader()) {

			@Override
			public String getValue() {

				return "hallo";
			}
		});
        
		tableAnswer.addColumnStyleName(columnIndex, "iconColumn");
		tableAnswer.setColumnWidth(columnIndex, "20px");
		columnIndex++;
		
		tableAnswer.addColumn(new Column<AnswerProxy, AnswerProxy>(new AnswerTextCell()) {
			@Override
			public AnswerProxy getValue(AnswerProxy object) {
				return object;
			}
		}, constant.answerText());
    	//tableAnswer.addColumnStyleName(1, "questionTextColumn");
    	tableAnswer.getColumn(columnIndex).setCellStyleNames("answerTextColumn");
    	columnIndex++;
    	
        if(Objects.equal(questionTypes, QuestionTypes.MCQ)) {
			ActionCell.Delegate<AnswerProxy> resourceDelegate = new ActionCell.Delegate<AnswerProxy>() {

				@Override
				public void execute(AnswerProxy answer) {
					openResourceDialog(answer);
				}
			};
			tableAnswer.addColumn(new Column<AnswerProxy, AnswerProxy>(new MCAQAnswerCell(McAppConstant.HELP_ICON, resourceDelegate)) {
				@Override
				public AnswerProxy getValue(AnswerProxy object) {
					return object;
				}
			}, constant.media());
			
			tableAnswer.addColumnStyleName(columnIndex, "iconColumn");
			tableAnswer.setColumnWidth(columnIndex, "20px");
			columnIndex++;
        }
        
        if(Objects.equal(questionTypes, QuestionTypes.Imgkey)) {
			ActionCell.Delegate<AnswerProxy> resourceDelegate = new ActionCell.Delegate<AnswerProxy>() {

				@Override
				public void execute(AnswerProxy answer) {
					openImageKeyDialog(answer);
				}
			};
			tableAnswer.addColumn(new Column<AnswerProxy, AnswerProxy>(new ImageKeyAnswerCell(McAppConstant.HELP_ICON, resourceDelegate)) {
				@Override
				public AnswerProxy getValue(AnswerProxy object) {
					return object;
				}
			}, constant.media());
			
			tableAnswer.addColumnStyleName(columnIndex, "iconColumn");
			tableAnswer.setColumnWidth(columnIndex, "20px");
			columnIndex++;
        }
        
        if(isEditable == true) {
        
			addColumn(new EditIconCell(McAppConstant.EDIT_ICON, new ActionCell.Delegate<AnswerProxy>() {
				public void execute(AnswerProxy answer) {
					delegate.editAnswerClicked(answer);
				}
			}), constants.edit(), new GetValue<AnswerProxy>() {
				public AnswerProxy getValue(AnswerProxy contact) {
					return contact;
				}
			}, null);
            
			tableAnswer.addColumnStyleName(columnIndex, "iconColumn");
			tableAnswer.setColumnWidth(columnIndex, "20px");
			columnIndex++;
			
			addColumn(new DeleteIconCell(McAppConstant.DELETE_ICON, new ActionCell.Delegate<AnswerProxy>() {
				public void execute(final AnswerProxy answer) {
					ConfirmationDialogBox.showYesNoDialogBox(constant.warning(), constant.deleteAnswerConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
						
						@Override
						public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
							delegate.deleteAnswerClicked(answer);
						}
						
						@Override
						public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {}
					});
				}
			}), constants.delete(), new GetValue<AnswerProxy>() {
				public AnswerProxy getValue(AnswerProxy contact) {
					return contact;
				}
			}, null);
        	
        	tableAnswer.addColumnStyleName(columnIndex, "iconColumn");
        	tableAnswer.setColumnWidth(columnIndex, "20px");
        	columnIndex++;
        }

    	tableAnswer.addColumn(new TextColumn<AnswerProxy>() {

			EnumRenderer<Status> renderer = new EnumRenderer<Status>();

			@Override
			public String getValue(AnswerProxy object) {
				return renderer.render(object == null ? null : object.getStatus());
			}
		},constants.status());
    	
    	tableAnswer.setColumnWidth(columnIndex, "150px");
    	columnIndex++;
    }
    
	private void openImageKeyDialog(AnswerProxy answer) {
		DialogBox dialogBox = new DialogBox();
	 	dialogBox.setTitle(constants.imageViewer());
	 	dialogBox.setText(constants.imageViewer());
	 	
	 	final VerticalPanel vp = new VerticalPanel();
	 	QuestionProxy questionProxy= answer.getQuestion();
		final List<Point> points = Point.getPoints(Lists.newArrayList(answer.getPoints()));
			
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
		dialogBox.setWidget(vp);	
		dialogBox.setGlassEnabled(true);
		dialogBox.setAutoHideEnabled(true);
		dialogBox.getElement().getStyle().setZIndex(5);
		dialogBox.center();	
		dialogBox.show();
	}

	private void openResourceDialog(AnswerProxy answer) {
		if(answer != null && answer.getQuestion() != null && answer.getQuestion().getQuestionType() != null && answer.getQuestion().getQuestionType().getMultimediaType() != null) {
			switch (answer.getQuestion().getQuestionType().getMultimediaType()) {
			case Image: {
				final ImageViewer viewer = new ImageViewer();
				viewer.setUrl("",answer.getMediaPath(), answer.getQuestion().getQuestionType().getQuestionType());
				Function<Boolean,Void> closeViewer = new Function<Boolean,Void>(){

					@Override
					public Void apply(Boolean input) {
						if(input != null && input.equals(true)) {
							viewer.closed();	
						}
						return null;
					}
				};
				createDialogBox(constants.mediaViewer(),viewer,closeViewer);
				
				break;
			}
			case Sound: {
				final AudioViewer viewer = new AudioViewer(answer.getMediaPath());
				Function<Boolean,Void> closeViewer = new Function<Boolean,Void>(){

					@Override
					public Void apply(Boolean input) {
						if(input != null && input.equals(true)) {
							viewer.closed();	
						}
						return null;
					}
				};
				createDialogBox(constants.mediaViewer(),viewer,closeViewer);
				break;
			}
			case Video: {
				final VideoViewer viewer = new VideoViewer(answer.getMediaPath());
				Function<Boolean,Void> closeViewer = new Function<Boolean,Void>(){

					@Override
					public Void apply(Boolean input) {
						if(input != null && input.equals(true)) {
							viewer.closed();	
						}
						return null;
					}
				};
				createDialogBox(constants.mediaViewer(),viewer,closeViewer);
				break;
			}
			default:
				break;
			}
		}
	}
	
	@Override
	public CellTable<AnswerProxy> getTable() {
		return tableAnswer;
	}
	
	@Override
	public String[] getPaths() {
        return paths.toArray(new String[paths.size()]);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	  /**
	   * Add a column with a header.
	   *
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	  private <C> void addColumn(Cell<C> cell, String headerText, final GetValue<C> getter, FieldUpdater<AnswerProxy, C> fieldUpdater) {
	    Column<AnswerProxy, C> column = new Column<AnswerProxy, C>(cell) {
	      @Override
	      public C getValue(AnswerProxy object) {
	        return getter.getValue(object);
	      }
	    };
	    column.setFieldUpdater(fieldUpdater);
	    if (cell instanceof AbstractEditableCell<?, ?>) {
	      editableCells.add((AbstractEditableCell<?, ?>) cell);
	    }
	    tableAnswer.addColumn(column, headerText);
	  }

	  /**
	   * Get a cell value from a record.
	   *
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(AnswerProxy contact);
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
			      
			      if (Status.ACTIVE.equals(value.getStatus()) == false)
			      {
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
			      }
			      
			      if (value.getQuestion() != null && Status.DEACTIVATED.equals(value.getQuestion().getStatus()))
			      {
			    	  beginn = "<div style=\"";
			      }

			      beginn += "\">";
			      sb.appendHtmlConstant(beginn);
			      sb.appendHtmlConstant(getAnswerValue(value));
			      sb.appendHtmlConstant(end);
				
			}
		  }

	  
	  private static String getAnswerValue(AnswerProxy value) {
		  
		  if(value != null && value.getQuestion() != null && value.getQuestion().getQuestionType() != null && value.getQuestion().getQuestionType().getQuestionType() != null) {
			  
			  if(Objects.equal(value.getQuestion().getQuestionType().getQuestionType(), QuestionTypes.MCQ)) {
				  return ClientUtility.getFileName(value.getMediaPath(), value.getQuestion().getQuestionType().getMultimediaType());  
			  }
			  
			  if(Objects.equal(value.getQuestion().getQuestionType().getQuestionType(), QuestionTypes.Imgkey)) {
				  return value.getAdditionalKeywords();  
			  }

		  } 
		  
		  if(value != null && value.getAnswerText() != null) {
			  return value.getAnswerText();
		  }
			
		  return "";
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
					case Wahr: ; validityIcon += "<span class=\"ui-icon ui-icon-check\"></span>";
							     break;
							
					case Falsch: validityIcon += "<span class=\"ui-icon ui-icon-close\"></span>";
							     break;
							     
					/*case Weil:	 validityIcon += "<span class=\"ui-icon ui-icon-refresh\"></span>";	
								break;*/
							     
					default:	  validityIcon += "<span class=\"ui-icon ui-icon-help\"></span>";	
							     break;
					}

			      
			      sb.appendHtmlConstant(validityIcon);
			      
				
			}
		  }
	  
	  private class MCAQAnswerCell extends ActionCell<AnswerProxy> {
		  public MCAQAnswerCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<AnswerProxy> delegate) {
			  super(message, delegate);
		  }

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, AnswerProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-help\" style=\"margin:auto\"></span>");
			        return;
			      }
			      String validityIcon = "";
			      
			      if(value != null && value.getQuestion() != null && value.getQuestion().getQuestionType() != null && value.getQuestion().getQuestionType().getQuestionType() != null && value.getQuestion().getQuestionType().getMultimediaType() != null) {
			    	  switch (value.getQuestion().getQuestionType().getMultimediaType()) {
						case Image:  
						{
							validityIcon += "<span class=\"ui-icon ui-icon-image\" style=\"margin:auto\"></span>";
						    break;
						}
								
						case Sound :
						{
							validityIcon += "<span class=\"ui-icon ui-icon-signal-diag\" style=\"margin:auto\"></span>";
							break;
							
						}
								 
						case Video : 
						{
							validityIcon += "<span class=\"ui-icon ui-icon-video\" style=\"margin:auto\"></span>";
							break;
						}
								     
						default:
						{
							validityIcon += "<span class=\"ui-icon ui-icon-help\" style=\"margin:auto\"></span>";
						     break;
						}
						
						}
			      }
			      sb.appendHtmlConstant(validityIcon);
			}
	  }
	  private class ImageKeyAnswerCell extends ActionCell<AnswerProxy> {
		  public ImageKeyAnswerCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<AnswerProxy> delegate) {
			  super(message, delegate);
		  }

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, AnswerProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-help\" style=\"margin:auto\"></span>");
			        return;
			      }
			      String validityIcon = "<span class=\"ui-icon ui-icon-image\" style=\"margin:auto\"></span>";
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
	  
	  private static class EditIconCell extends ActionCell<AnswerProxy>{
		
		  public EditIconCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<AnswerProxy> delegate) {
			super(message, delegate);
		 }

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				AnswerProxy value, SafeHtmlBuilder sb) {
			if (value == null)
				return;
			
			if (Status.ACTIVE.equals(value.getStatus()) || Status.NEW.equals(value.getStatus())){
				sb.append(McAppConstant.EDIT_ICON);
			}
		}
		  
	  }
	  
	  private static class DeleteIconCell extends ActionCell<AnswerProxy>{
			
		  public DeleteIconCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<AnswerProxy> delegate) {
			super(message, delegate);
		 }

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				AnswerProxy value, SafeHtmlBuilder sb) {
			if (value == null)
				return;
			
			if (Status.ACTIVE.equals(value.getStatus()) || Status.NEW.equals(value.getStatus())){
				sb.append(McAppConstant.DELETE_ICON);
			}
		}
		  
	  }

	public IconButton getNewAnswer() {
		return newAnswer;
	}
	public void setNewAnswer(IconButton newAnswer) {
		this.newAnswer = newAnswer;
	}

	  
	private void createDialogBox(String title,Widget widget, final Function<Boolean, Void> function) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.ensureDebugId("cwDialogBox");
		dialogBox.setText(title);

		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("100%");
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);

		// Add an image viewer to the dialog
		
		dialogContents.add(widget);
		dialogContents.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_CENTER);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button(constants.close());
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				function.apply(true);
			}
		});
		
		dialogContents.add(closeButton);
		dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);

		dialogBox.setGlassEnabled(true);
		dialogBox.center();
        dialogBox.show();
	}

	public void showAllBtn() {
		showAll.setVisible(true);
	}

	public void hideAddAnswer() {
		newAnswer.setVisible(false);
	}
}
