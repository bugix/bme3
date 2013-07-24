package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.shared.Status;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MatrixAnswerListViewImpl extends Composite implements MatrixAnswerListView {

	private static MatrixAnswerListViewImplUiBinder uiBinder = GWT
			.create(MatrixAnswerListViewImplUiBinder.class);

	interface MatrixAnswerListViewImplUiBinder extends
			UiBinder<Widget, MatrixAnswerListViewImpl> {
	}

	public MatrixAnswerListViewImpl(boolean isEditable) {
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		tableAnswer = new CellTable<MatrixValidityProxy>(McAppConstant.TABLE_PAGE_SIZE,
				tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, McAppConstant.TABLE_JUMP_SIZE, true);

		initWidget(uiBinder.createAndBindUi(this));
		init(isEditable);
	}


	public BmeConstants constant =GWT.create(BmeConstants.class);
			
    private Delegate delegate;
    

	@UiField
    IconButton newAnswer;


	@UiField(provided = true)
	CellTable<MatrixValidityProxy> tableAnswer;

	@UiField(provided = true)
	public SimplePager pager;
	
	@UiHandler("newAnswer")
	void addEventClicked(ClickEvent event) {
		Log.info("event");
		delegate.addMatrixNewAnswerClicked();
	}


	private String name;
    protected Set<String> paths = new HashSet<String>();

    public void init(boolean isEditable) {
    	
    	Log.debug("Im AnswerListView.init() ");
    	
    	editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

    	
//        paths.add("dateAdded");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getDateAdded());
//            }
//        }, "Date Added");
//        paths.add("dateChanged");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.util.Date> renderer = new DateTimeFormatRenderer(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT));
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getDateChanged());
//            }
//        }, "Date Changed");
//        paths.add("rewiewer");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<medizin.client.managed.request.PersonProxy> renderer = medizin.client.ui.view.roo.PersonProxyRenderer.instance();
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getRewiewer());
//            }
//        }, "Rewiewer");
//        paths.add("autor");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<medizin.client.managed.request.PersonProxy> renderer = medizin.client.ui.view.roo.PersonProxyRenderer.instance();
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getAutor());
//            }
//        }, "Autor");
    	
//        paths.add("validity");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<medizin.client.shared.Validity> renderer = new AbstractRenderer<medizin.client.shared.Validity>() {
//
//                public String render(medizin.client.shared.Validity obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getValidity());
//            }
//        }, "Validity");
        
 
        tableAnswer.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new ValidityTextCell()) {
    	      @Override
    	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
    	        return object;
    	      }
    	    },  new Header<String>(new ValidityHeader()){

    			@Override
    			public String getValue() {
    				
    				return "hallo";
    			}});
        
        
        
        tableAnswer.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new AnswerXTextCell()) {
  	      @Override
  	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
  	        return object;
  	      }
  	    }, constant.answerX());
        
        tableAnswer.addColumn(new Column<MatrixValidityProxy, MatrixValidityProxy>(new AnswerYTextCell()) {
    	      @Override
    	      public MatrixValidityProxy getValue(MatrixValidityProxy object) {
    	        return object;
    	      }
    	}, constant.answerY());
        
//        paths.add("answerText");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//            	
//                return renderer.render(object.getAnswerText());
//            }
//        }, "Answer Text");
//        paths.add("isAnswerActive");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//                public String render(java.lang.Boolean obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getIsAnswerActive());
//            }
//        }, "Is Answer Active");
//        paths.add("isPicture");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//                public String render(java.lang.Boolean obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getIsPicture());
//            }
//        }, "Is Picture");
//        paths.add("isAnswerAcceptedReviewWahrer");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//                public String render(java.lang.Boolean obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getIsAnswerAcceptedReviewWahrer());
//            }
//        }, "Is Answer Accepted Review Wahrer");
//        paths.add("isAnswerAcceptedAutor");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//                public String render(java.lang.Boolean obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getIsAnswerAcceptedAutor());
//            }
//        }, "Is Answer Accepted Autor");
//        paths.add("isAnswerAcceptedAdmin");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.Boolean> renderer = new AbstractRenderer<java.lang.Boolean>() {
//
//                public String render(java.lang.Boolean obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getIsAnswerAcceptedAdmin());
//            }
//        }, "Is Answer Accepted Admin");

//        paths.add("picturePath");
//        
//        
//        
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//                public String render(java.lang.String obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getPicturePath());
//            }
//        }, "Picture Path");
//        paths.add("question");
//        tableAnswer.addColumn(new TextColumn<AnswerProxy>() {
//
//            Renderer<medizin.client.managed.request.QuestionProxy> renderer = medizin.client.ui.view.roo.QuestionProxyRenderer.instance();
//
//            @Override
//            public String getValue(AnswerProxy object) {
//                return renderer.render(object.getQuestion());
//            }
//        }, "Question");
        
        if(isEditable == true) {
        	addColumn(new EditIconCell(McAppConstant.EDIT_ICON, new ActionCell.Delegate<MatrixValidityProxy>() 
        			{
    		    		public void execute(MatrixValidityProxy matrixValidity) {
    		    			delegate.editMatrixValidityClicked(matrixValidity);
    		    	            
    		    		}
        	          
        			}), constant.edit(), new GetValue<MatrixValidityProxy>() {
        	        
        		public MatrixValidityProxy getValue(MatrixValidityProxy contact) {
        			return contact;
        		}
        	}, null);
        	
        	addColumn(new DeleteIconCell(McAppConstant.DELETE_ICON, new ActionCell.Delegate<MatrixValidityProxy>() 
        			{
    		    		public void execute(MatrixValidityProxy matrixValidity) {
    		    			delegate.deleteMatrixValidityClicked(matrixValidity);
    		    		}
        			}), constant.delete(), new GetValue<MatrixValidityProxy>() {
        		public MatrixValidityProxy getValue(MatrixValidityProxy contact) {
        			return contact;
        		}
        	}, null);
        	tableAnswer.addColumnStyleName(3, "iconColumn");
        	tableAnswer.addColumnStyleName(4, "iconColumn");
        }
    	
    	tableAnswer.addColumnStyleName(0, "iconColumn");
    	/*tableAnswer.addColumnStyleName(1, "questionTextColumn");
    	tableAnswer.addColumnStyleName(2, "questionTextColumn");*/
    	


    }
	@Override
	public CellTable<MatrixValidityProxy> getTable() {

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
	  private <C> void addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<MatrixValidityProxy, C> fieldUpdater) {
	    Column<MatrixValidityProxy, C> column = new Column<MatrixValidityProxy, C>(cell) {
	      @Override
	      public C getValue(MatrixValidityProxy object) {
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
	    C getValue(MatrixValidityProxy contact);
	  }
	  
	  private List<AbstractEditableCell<?, ?>> editableCells;


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
			      //if(!value.getAnswerX().getIsAnswerActive()){
			      if(!Status.ACTIVE.equals(value.getAnswerX().getStatus())){
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
			      //if(!value.getAnswerY().getIsAnswerActive()){
			      if(!Status.ACTIVE.equals(value.getAnswerY().getStatus())){
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
	  
	  private static class ValidityHeader extends AbstractCell<String> {


			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					String value, SafeHtmlBuilder sb) {
			      // Always do a null check on the value. Cell widgets can pass null to cells
			      // if the underlying data contains a null, or if the data arrives out of order.
			      
			    	  sb.appendHtmlConstant("<span class=\"ui-icon ui-icon-help\"></span>");
			
			      
				
			}
		  }

	  private static class EditIconCell extends ActionCell<MatrixValidityProxy>{
			
		  public EditIconCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<MatrixValidityProxy> delegate) {
			super(message, delegate);
		 }

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				MatrixValidityProxy value, SafeHtmlBuilder sb) {
			if (Status.ACTIVE.equals(value.getAnswerX().getStatus()) || Status.NEW.equals(value.getAnswerX().getStatus()) || Status.ACTIVE.equals(value.getAnswerY().getStatus()) || Status.NEW.equals(value.getAnswerY().getStatus())){
				sb.append(McAppConstant.EDIT_ICON);
			}
		}
		  
	  }
	  
	  private static class DeleteIconCell extends ActionCell<MatrixValidityProxy>{
			
		  public DeleteIconCell(SafeHtml message, com.google.gwt.cell.client.ActionCell.Delegate<MatrixValidityProxy> delegate) {
			super(message, delegate);
		 }

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				MatrixValidityProxy value, SafeHtmlBuilder sb) {
			if (Status.ACTIVE.equals(value.getAnswerX().getStatus()) || Status.NEW.equals(value.getAnswerX().getStatus()) || Status.ACTIVE.equals(value.getAnswerY().getStatus()) || Status.NEW.equals(value.getAnswerY().getStatus())){
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

}
