package medizin.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.DeclineEmailPopupDelagate;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.matrix.MatrixAnswerViewer;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AcceptMatrixAnswerSubViewImpl extends Composite implements AcceptMatrixAnswerSubView {

	private static AcceptMatrixAnswerSubViewImplUiBinder uiBinder = GWT
			.create(AcceptMatrixAnswerSubViewImplUiBinder.class);

	interface AcceptMatrixAnswerSubViewImplUiBinder extends
			UiBinder<Widget, AcceptMatrixAnswerSubViewImpl> {
	}

	@UiField(provided = true)
	CellTable<MatrixValidityProxy> table;
		
    @UiField(provided = true)
	public SimplePager pager;
	
	@Override
	public AbstractHasData<MatrixValidityProxy> getTable(){
		return table;
	}

	private  Delegate delegate;
	private QuestionProxy questionProxy;

	private SingleSelectionModel<MatrixValidityProxy> selectionModel;
	
	private List<MatrixValidityProxy> matrixAnswerList = new ArrayList<MatrixValidityProxy>();
	
	private BmeConstants constants = GWT.create(BmeConstants.class);
	
	DialogBox dialogBox;
	
	public AcceptMatrixAnswerSubViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<MatrixValidityProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		ProvidesKey<MatrixValidityProxy> keyProvider = ((AbstractHasData<MatrixValidityProxy>) table).getKeyProvider();
        selectionModel = new SingleSelectionModel<MatrixValidityProxy>(keyProvider);
        table.setSelectionModel(selectionModel);  
       
        /*selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				MatrixValidityProxy selectedObject = selectionModel.getSelectedObject();
				
				if (selectionModel.isSelected(selectedObject))
					selectionModel.setSelected(selectedObject, false);
				
				if (selectedObject != null) {
					DialogBox dialogBox = new DialogBox();
					
					dialogBox.setWidth("200px");
					dialogBox.setHeight("200px");
				 	VerticalPanel vp = new VerticalPanel();
				 	vp.setWidth("98%");
				 	vp.setHeight("100%");
				 	MatrixAnswerViewer viewer = new MatrixAnswerViewer(matrixAnswerList);
				 	vp.add(viewer);				 	
				 	dialogBox.setWidget(vp);	
					dialogBox.setGlassEnabled(true);
					dialogBox.setAutoHideEnabled(true);
					dialogBox.getElement().getStyle().setZIndex(5);
					dialogBox.center();	
					dialogBox.show();
				}
			}
		});*/
		
        
        table.addCellPreviewHandler(new Handler<MatrixValidityProxy>() {

			@Override
			public void onCellPreview(CellPreviewEvent<MatrixValidityProxy> event) {
				
				boolean isClicked="click".equals(event.getNativeEvent().getType());
				if(isClicked){
					if(event.getColumn()==1 || event.getColumn()==2){
						MatrixValidityProxy selectedObject = selectionModel.getSelectedObject();
						
						if (selectionModel.isSelected(selectedObject))
							selectionModel.setSelected(selectedObject, false);
						
						if (selectedObject != null) {
							
							DialogBox dialogBox = new DialogBox();
							
							dialogBox.setWidth("200px");
							dialogBox.setHeight("200px");
						 	VerticalPanel vp = new VerticalPanel();
						 	vp.setWidth("98%");
						 	vp.setHeight("100%");
						 	MatrixAnswerViewer viewer = new MatrixAnswerViewer(matrixAnswerList);
						 	vp.add(viewer);				 	
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
		});
        
		init();

	}

	private DeclineEmailPopupDelagate delegatePopup;


	private void init() {

		table.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new ValidityTextCell()) {
    	      @Override
    	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
    	        return object;
    	      }
    	    },  new Header<String>(new ValidityHeader()){

    			@Override
    			public String getValue() {
    				
    				return "hallo";
    			}});
        
        
        
		table.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new AnswerXTextCell()) {
  	      @Override
  	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
  	        return object;
  	      }
  	    },  constants.answerText());
		
		table.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new AnswerYTextCell()) {
	  	      @Override
	  	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
	  	        return object;
	  	      }
	  	    },  constants.answerText());
        

		addColumn(new ActionCell<MatrixValidityProxy>( McAppConstant.DECLINE_ICON, new ActionCell.Delegate<MatrixValidityProxy>() {
            

			public void execute(final MatrixValidityProxy answerProxy) {
            	/*DeclineEmailPopup popup = new DeclineEmailPopup(answerProxy);
            	popup.setDelegate(delegatePopup);*/
//            	Log.debug("hinterPupup");
             // delegate.rejectClicked(answerProxy, "verweigert");
				
				ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.rejectAnswerMsg(), new ConfirmDialogBoxYesNoButtonEventHandler() {
					
					@Override
					public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
						delegate.matrixRejectClicked(questionProxy);
					}
					
					@Override
					public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
					}
				});				
            }
          }), constants.reject(), new GetValue<MatrixValidityProxy>() {
        public MatrixValidityProxy getValue(MatrixValidityProxy answerProxy) {
          return answerProxy;
        }
      }, null);

		addColumn(new ActionCell<MatrixValidityProxy>( McAppConstant.ACCEPT_ICON, new ActionCell.Delegate<MatrixValidityProxy>() {
		    public void execute(MatrixValidityProxy answerProxy) {
		      delegate.matrixAcceptClicked(questionProxy);
		    }
		  }), constants.accept(), new GetValue<MatrixValidityProxy>() {
		public MatrixValidityProxy getValue(MatrixValidityProxy answerProxy) {
		  return answerProxy;
		}
		}, null);
    	
    	table.addColumnStyleName(0, "deleteColumn");
    	table.addColumnStyleName(3, "deleteColumn");
    	table.addColumnStyleName(4, "deleteColumn");
		
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
	      final GetValue<C> getter, FieldUpdater<MatrixValidityProxy, C> fieldUpdater) {
	    Column<MatrixValidityProxy, C> column = new Column<MatrixValidityProxy, C>(cell) {
	      @Override
	      public C getValue(MatrixValidityProxy object) {
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
	    C getValue(MatrixValidityProxy contact);
	  }


	@Override
	public void setDelegatePopup(DeclineEmailPopupDelagate delegate) {
		this.delegatePopup = delegate;
		
	}

/*@UiField
DivElement questionText;*/
	
	@UiField
	DisclosurePanel questionDisclosurePanel;


	@Override
	public void setProxy(final QuestionProxy questionProxy) {
		this.questionProxy = questionProxy;
		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			   
			public void onRangeChange(RangeChangeEvent event) {
				
				delegate.onMatrixRangeChanged(questionProxy, table, AcceptMatrixAnswerSubViewImpl.this);
			}
		});
		delegate.onMatrixRangeChanged(questionProxy, table, AcceptMatrixAnswerSubViewImpl.this);
		
		//questionText.setInnerHTML(questionProxy.getQuestionText());
		questionDisclosurePanel.getHeaderTextAccessor().setText(questionProxy.getQuestionText());
		
	}


	 private static class AnswerXTextCell extends AbstractCell<MatrixValidityProxy> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					MatrixValidityProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			        return;
			      }
			      String beginn = "<div style=\"";
			      String end = "</div>";
			      if(!value.getAnswerX().getIsAnswerAcceptedAdmin())
			      {
			    	  beginn += "color:red; ";		    	  
			      }
			      if(!value.getAnswerX().getIsAnswerAcceptedReviewWahrer()){
			    	  beginn += "font-style:italic; ";			    	  
			      }
			      if(!value.getAnswerX().getIsAnswerActive()){
			    	  beginn += "text-decoration: line-through; ";		    	  
			      }

			      beginn += "\">";
			      sb.appendHtmlConstant(beginn);
			      sb.appendHtmlConstant(value.getAnswerX().getAnswerText());
			      sb.appendHtmlConstant(end);
				
			}
		  }

	  private static class AnswerYTextCell extends AbstractCell<MatrixValidityProxy> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					MatrixValidityProxy value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      if (value == null) {
			        return;
			      }
			      String beginn = "<div style=\"";
			      String end = "</div>";
			      if(!value.getAnswerY().getIsAnswerAcceptedAdmin())
			      {
			    	  beginn += "color:red; ";		    	  
			      }
			      if(!value.getAnswerY().getIsAnswerAcceptedReviewWahrer()){
			    	  beginn += "font-style:italic; ";			    	  
			      }
			      if(!value.getAnswerY().getIsAnswerActive()){
			    	  beginn += "text-decoration: line-through; ";		    	  
			      }

			      beginn += "\">";
			      sb.appendHtmlConstant(beginn);
			      sb.appendHtmlConstant(value.getAnswerY().getAnswerText());
			      sb.appendHtmlConstant(end);
				
			}
		  }

	  
	  private static class ValidityTextCell extends AbstractCell<MatrixValidityProxy> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					MatrixValidityProxy value, SafeHtmlBuilder sb) {
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

	public List<MatrixValidityProxy> getMatrixAnswerList() {
		return matrixAnswerList;
	}

	public void setMatrixAnswerList(List<MatrixValidityProxy> matrixAnswerList) {
		this.matrixAnswerList = matrixAnswerList;
	}
	
	

}
