package medizin.client.ui.widget.matrix;

import java.util.List;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.shared.Validity;
import medizin.client.ui.McAppConstant;
import medizin.client.util.Matrix;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MatrixAnswerViewer extends Composite {

	private static MatrixAnswerViewerUiBinder uiBinder = GWT
			.create(MatrixAnswerViewerUiBinder.class);

	interface MatrixAnswerViewerUiBinder extends
			UiBinder<Widget, MatrixAnswerViewer> {
	}

	@UiField(provided = true)
	FlexTable matrix = new FlexTable();
	
	private final List<MatrixValidityProxy> matrixValidityList;
	private final Matrix<MatrixValidityProxy> matrixList = new Matrix<MatrixValidityProxy>();
	
	public MatrixAnswerViewer(List<MatrixValidityProxy> matrixValidityList) {
		initWidget(uiBinder.createAndBindUi(this));
		
		matrix.setWidth("100%");
		matrix.setCellSpacing(5);
		matrix.setCellPadding(3);
		matrix.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		
		this.matrixValidityList = matrixValidityList;
		
		setValues();
	}

	private void setValues() {
		
		FluentIterable<MatrixValidityProxy> fluentIterable = FluentIterable.from(matrixList);
		
		for (MatrixValidityProxy matrixValidityProxy : matrixValidityList) {
			
			Optional<MatrixValidityProxy> optional1 = fluentIterable.firstMatch(new MatrixValidityPredicate(matrixValidityProxy));
				
			if(optional1.isPresent() == false)  {
				
				Optional<MatrixValidityProxy> optionalAnswerX = fluentIterable.firstMatch(new AnswerXPredicate(matrixValidityProxy.getAnswerX()));
				Optional<MatrixValidityProxy> optionalAnswerY = fluentIterable.firstMatch(new AnswerYPredicate(matrixValidityProxy.getAnswerY()));
				
				if(optionalAnswerX.isPresent() == true && optionalAnswerY.isPresent() == false) {
					// add new column
					int currentRow = matrixList.getRowForObject(optionalAnswerX.get());
					
					matrixList.addColumn(currentRow, 1, matrixValidityProxy);
					int currentColumn = matrixList.getColumnForObject(matrixValidityProxy);
					addValidityBtnOn(currentRow,currentColumn,matrixValidityProxy);
					addAnswerYTextAndLabel(matrixValidityProxy);
				}
				
				if(optionalAnswerX.isPresent() == false && optionalAnswerY.isPresent() == true){
					// add new row
					int currentColumn = matrixList.getColumnForObject(optionalAnswerY.get());
					
					matrixList.addRow(1,currentColumn,matrixValidityProxy);
					int currentRow = matrixList.getRowForObject(matrixValidityProxy);
					addValidityBtnOn(currentRow, currentColumn, matrixValidityProxy);
					addAnswerXTextAndLabel(currentRow,matrixValidityProxy);
				}
				
				// not answer is added; 
				if(optionalAnswerX.isPresent() == false && optionalAnswerY.isPresent() == false) {
										
					matrixList.add(1,1,matrixValidityProxy);	
					int currentRow = matrixList.getRowForObject(matrixValidityProxy);
					int currentColumn = matrixList.getColumnForObject(matrixValidityProxy);
					
					addValidityBtnOn(currentRow, currentColumn, matrixValidityProxy);
					addAnswerXTextAndLabel(currentRow,matrixValidityProxy);
					addAnswerYTextAndLabel(matrixValidityProxy);
				}
				
				if(optionalAnswerX.isPresent() == true && optionalAnswerY.isPresent() == true) {
					int currentRow = matrixList.getRowForObject(optionalAnswerX.get());
					int currentColumn = matrixList.getColumnForObject(optionalAnswerY.get());
					
					matrixList.set(currentRow,currentColumn,matrixValidityProxy);
					addValidityBtnOn(currentRow, currentColumn, matrixValidityProxy);
				}
				
			}else {
				Log.info("MatrixValidityProxy already added");
			}
		}
	}

	private void addAnswerXTextAndLabel(int currentRow, MatrixValidityProxy matrixValidity) {
		final Label labelX = new Label();
		
		HorizontalPanel hpX = new HorizontalPanel();
		
		labelX.setWidth("100px");
		labelX.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		labelX.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		labelX.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		
		hpX.add(labelX);
		
		labelX.setVisible(true);		
		labelX.setText(matrixValidity.getAnswerX().getAnswerText());
		labelX.setTitle(matrixValidity.getAnswerX().getAnswerText());
		
		matrix.addCell(currentRow);
		matrix.setWidget(currentRow, 0, hpX);
	}

	private void addAnswerYTextAndLabel(MatrixValidityProxy matrixValidity) {

		final Label labelY = new Label();
		labelY.addStyleName("rowRotate90");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(labelY);
		labelY.setVisible(true);
		labelY.setText(matrixValidity.getAnswerY().getAnswerText());
		labelY.setTitle(matrixValidity.getAnswerY().getAnswerText());
		
		int widgetAddColumnY = matrix.getCellCount(0) == 0 ? 1 : matrix.getCellCount(0);
		matrix.setWidget(0, widgetAddColumnY, hp);
	
	}

	private void addValidityBtnOn(int currentRow, int currentColumn, MatrixValidityProxy matrixValidity) {

		if(currentColumn > -1 && currentRow > -1) {
			
			int totalRows = 0;
			int totalColumns = 0;
			
			if(matrix.getRowCount() > 0) {
				totalRows = matrix.getRowCount() -1; // removed button x row
				if(currentRow < totalRows) {
					totalColumns = matrix.getCellCount(currentRow) ;
				}else {
					totalColumns = 0;
				}
			}
			
			if(totalRows > currentRow && totalColumns > currentColumn && matrix.getWidget(currentRow, currentColumn) != null) {
				HTML value = (HTML) matrix.getWidget(currentRow, currentColumn);
				Log.info("HTML is " + value);					
			}else {
				SafeHtml value;
				if(Validity.Wahr.equals(matrixValidity.getValidity())) {
					value = McAppConstant.RIGHT_ICON;
				}else {
					value = McAppConstant.WRONG_ICON;
				}
				HTML html = new HTML(value);
				//html.getElement().getStyle().setPaddingLeft(8, Unit.PX);
				matrix.setWidget(currentRow, currentColumn, html);
				
			}
		}else {
			Log.info("error in getcolumnforobject method");
		}
	}
	
	private class MatrixValidityPredicate implements Predicate<MatrixValidityProxy> {
		private final MatrixValidityProxy matrixValidityProxy;
		
		public MatrixValidityPredicate(final MatrixValidityProxy matrixValidityProxy) {
			this.matrixValidityProxy = matrixValidityProxy;
		}
		
		@Override
		public boolean apply(MatrixValidityProxy input) {
			
			if(input != null) {
				return input.getId().equals(matrixValidityProxy.getId());
			}
			return false;
		}
		
	}
	
	private class AnswerXPredicate implements Predicate<MatrixValidityProxy> {

		private final AnswerProxy answerX;
		
		public AnswerXPredicate(final AnswerProxy answerX) {
			this.answerX = answerX;
		}

		@Override
		public boolean apply(MatrixValidityProxy input) {
			
			if(answerX == null) {
				return false;
			}
			if(input != null && input.getAnswerX() != null && input.getAnswerX() != null) {
				return input.getAnswerX().getId().equals(answerX.getId());
			}
			return false;
		}
		
	}
	
	private class AnswerYPredicate implements Predicate<MatrixValidityProxy> {
		private final AnswerProxy answerY;
		
		public AnswerYPredicate(final AnswerProxy answerY) {
			this.answerY = answerY;
		}
		
		@Override
		public boolean apply(MatrixValidityProxy input) {
			
			if(answerY == null) {
				return false;
			}
			
			if(input != null && input.getAnswerY() != null && input.getAnswerY() != null) {
				return input.getAnswerY().getId().equals(answerY.getId());
			}
			return false;
		}
		
	}
}
