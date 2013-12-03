package medizin.client.ui.view.question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.HtmlToggleButton;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.dialogbox.receiver.ReceiverDialog;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.client.util.AnswerVO;
import medizin.client.util.ClientUtility;
import medizin.client.util.Matrix;
import medizin.client.util.MatrixValidityVO;
import medizin.client.util.State;
import medizin.shared.Validity;
import medizin.shared.i18n.BmeConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MatrixAnswerViewImpl extends DialogBox implements MatrixAnswerView {

	private static MatrixAnswerViewImplUiBinder uiBinder = GWT.create(MatrixAnswerViewImplUiBinder.class);

	private BmeConstants constants = GWT.create(BmeConstants.class);

	interface MatrixAnswerViewImplUiBinder extends
			UiBinder<Widget, MatrixAnswerViewImpl> {
	}

	private Delegate delegate;

	@UiField(provided = true)
	FlexTable matrix = new FlexTable();

	@UiField
	public CheckBox submitToReviewComitee;

	@UiField
	public TextArea comment;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> author;

	@UiField
	@Ignore
	public DefaultSuggestBox<PersonProxy, EventHandlingValueHolderItem<PersonProxy>> rewiewer;

	@UiField
	IconButton save;

	@UiField
	IconButton closeButton;

	private final IconButton addAnswerX;
	private final IconButton addAnswerY;
	private final QuestionProxy questionProxy;
	private final Matrix<MatrixValidityVO> matrixList = new Matrix<MatrixValidityVO>();

	private List<MatrixValidityProxy> currentMatrixValidityProxy;

	public MatrixAnswerViewImpl(QuestionProxy questionProxy) {
		this.questionProxy = questionProxy;
		setWidget(uiBinder.createAndBindUi(this));
		//matrix.setWidth("100%");
		//matrix.setHeight("200px");
		//matrix.setCellSpacing(5);
		//matrix.setCellPadding(3);
		matrix.setStyleName("matrix-table", true);
		
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setTitle(constants.answerDialogBoxTitle());
		setText(constants.answerDialogBoxTitle());

		// Add a button that will add more rows to the table

		addAnswerX = new IconButton(constants.addAnswerY(), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAnswerX(null,true,true);
			}

		});
		addAnswerX.setIcon("plusthick");
		addAnswerX.addStyleName("matrix-answerX-btn");
		//addAnswerX.getElement().getStyle().setMarginLeft(10, Unit.PX);

		addAnswerY = new IconButton(constants.addAnswerX(), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAnswerY(null,true,true);
			}
		});

		addAnswerY.setIcon("plusthick");
		addAnswerY.addStyleName("matrix-answerY-btn");
		//addAnswerY.addStyleName("rowRotate90");
		//addAnswerY.getElement().getStyle().setPaddingTop(0, Unit.PX);
				
		matrix.setWidget(0, 1, addAnswerY);
		matrix.setWidget(1, 0, addAnswerX);

		submitToReviewComitee.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (submitToReviewComitee.getValue()) {
					rewiewer.setEnabled(false);
				} else {
					rewiewer.setEnabled(true);
				}
			}
		});

	}

	@UiHandler("closeButton")
	public void onCloseButtonClick(ClickEvent event) {
		hide();
		delegate.closedMatrixValidityView();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	private void addAnswerX(final MatrixValidityVO vo, boolean isEdit, boolean isDelete) {
		final IconButton editX = new IconButton();
		editX.setIcon("pencil");
		final IconButton deleteX = new IconButton();
		deleteX.setIcon("trash");
		final IconButton saveX = new IconButton();
		saveX.setIcon("disk");
		final TextBox textBox = new TextBox();
		textBox.addStyleName("matrix-textbox-x");
		final Label label = new Label();
		label.addStyleName("matrix-label-x");
			
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(2);
		hp.addStyleName("matrix-hp-text-x");
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hp.add(saveX);
		hp.add(editX);
		hp.add(deleteX);
		hp.add(textBox);
		hp.add(label);
				
		final int currentRow = matrix.getRowCount() - 1;
		final int totalColumn = matrix.getCellCount(0);
		
		Log.info("Current Row in answerX add : " + currentRow);
		Log.info("Total Column in answerX add : " + totalColumn);
		
		matrix.setWidget(currentRow, 0, hp);
		matrix.setWidget(currentRow + 1, 0, addAnswerX);

		final AnswerVO answerVO;
		
		if(vo != null) {
			//add exiting answer
			answerVO = vo.getAnswerX();
			
			label.setText(vo.getAnswerX().getAnswer());
			label.setTitle(vo.getAnswerX().getAnswer());
			textBox.setText(vo.getAnswerX().getAnswer());
			
			label.setVisible(true);
			textBox.setVisible(false);
			saveX.setVisible(false);
		} else {
			//add for new answer
			answerVO = new AnswerVO();
			answerVO.setState(State.NEW);	
			
			label.setVisible(false);
			editX.setVisible(false);
			textBox.setFocus(true);
			
			addAnswerX.setVisible(false);
			addAnswerY.setVisible(false);
		}
		
		saveX.addClickHandler(new TextSaveClickedHandler(answerVO, textBox, label,editX,saveX, new Function<Boolean, Void>() {

			@Override
			public Void apply(Boolean input) {
				if(vo == null) {
					// for first case (*,1) 
					if(totalColumn - 1 == 1 && matrixList.get(currentRow, 1) == null) {
						final MatrixValidityVO matrixValidityVO = new MatrixValidityVO();
						matrixValidityVO.setAnswerX(answerVO);
						matrixList.set(currentRow, 1, matrixValidityVO);
					}
					
					for (int columnIndex = 1; columnIndex < (totalColumn - 1); columnIndex++) {
						
						if(matrix.isCellPresent(0, columnIndex)  == true && matrix.getHTML(0, columnIndex).trim().isEmpty() == false) {
							final MatrixValidityVO matrixValidityVO;
							if (matrixList.exists(currentRow, columnIndex)) {
								matrixValidityVO = matrixList.get(currentRow, columnIndex);
							} else {
								matrixValidityVO = new MatrixValidityVO();
								matrixList.set(currentRow, columnIndex, matrixValidityVO);
							}
							matrixValidityVO.setAnswerX(answerVO);
							
							if(matrixValidityVO.getAnswerY() == null && matrixList.get(1, columnIndex) != null) {
									MatrixValidityVO otherVO =  matrixList.get(1, columnIndex);				
									matrixValidityVO.setAnswerY(otherVO.getAnswerY());
							}

							matrixValidityVO.setValidity(Validity.Falsch);
							
							addValidityBtnOn(currentRow, columnIndex, matrixValidityVO);
						}
						/*delegate.saveMatrixValidityValue(matrixValidityVO,Validity.Falsch, new Function<MatrixValidityProxy, Void>() {

							@Override
							public Void apply(MatrixValidityProxy input) {
								matrixValidityVO.setMatrixValidityProxy(input);
								return null;
							}
						});*/

					}
				} else {
					//do nothing
				}
				return null;
			}
		}));

		editX.addClickHandler(new TextEditClickHandler(answerVO, textBox, label,editX,saveX));
		deleteX.addClickHandler(new DeleteClickedHandler(answerVO,true));
		
		if(isEdit == false) {
			editX.removeFromParent();
			saveX.removeFromParent();
		}
		
		if(isDelete == false) {
			deleteX.removeFromParent();
		}
	}

	private void addAnswerY(final MatrixValidityVO vo, boolean isEdit, boolean isDelete) {
		final IconButton editY = new IconButton();
		editY.setIcon("pencil");
		final IconButton deleteY = new IconButton();
		deleteY.setIcon("trash");
		final IconButton saveY = new IconButton();
		saveY.setIcon("disk");
		final TextBox textBox = new TextBox();
		final Label label = new Label();
		label.setVisible(false);
		//label.addStyleName("rowRotate90");
		//textBox.addStyleName("rowRotate90");
		textBox.addStyleName("matrix-textbox-y");
		label.addStyleName("matrix-label-y");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setSpacing(2);
		vp.addStyleName("matrix-hp-text-y");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.add(textBox);
		hp.add(label);

		HorizontalPanel buttonHP = new HorizontalPanel();
		buttonHP.setSpacing(1);
		buttonHP.add(editY);
		buttonHP.add(saveY);
		buttonHP.add(deleteY);
		
		vp.add(buttonHP);
		vp.add(hp);		
				
		saveY.getElement().getParentElement().addClassName("matrix-icon-y");
		deleteY.getElement().getParentElement().addClassName("matrix-icon-y");
		editY.getElement().getParentElement().addClassName("matrix-icon-y");
		
		final int totalRows = matrix.getRowCount();
		final int currentColumn = matrix.getCellCount(0) - 1;
		
		matrix.setWidget(0, currentColumn, vp);
		matrix.setWidget(0, currentColumn + 1, addAnswerY);

		final AnswerVO answerVO;
		
		if(vo != null) {
			answerVO = vo.getAnswerY();
			
			label.setVisible(true);
			textBox.setVisible(false);
			saveY.setVisible(false);
			
			label.setText(vo.getAnswerY().getAnswer());
			label.setTitle(vo.getAnswerY().getAnswer());
			textBox.setText(vo.getAnswerY().getAnswer());
		}else {
			editY.setVisible(false);
			textBox.setFocus(true);
			addAnswerX.setVisible(false);
			addAnswerY.setVisible(false);

			answerVO = new AnswerVO();
			answerVO.setState(State.NEW);
		}
		
		saveY.addClickHandler(new TextSaveClickedHandler(answerVO, textBox, label,editY,saveY, new Function<Boolean, Void>() {
			
			@Override
			public Void apply(Boolean input) {
				
				if(vo == null) {
					// for first case (1,*) 
					if(totalRows - 1 == 1 && matrixList.get(1, currentColumn) == null) {
						final MatrixValidityVO matrixValidityVO = new MatrixValidityVO();
						matrixValidityVO.setAnswerY(answerVO);
						matrixList.set(1, currentColumn, matrixValidityVO);
					}

					for (int rowIndex = 1; rowIndex < (totalRows - 1); rowIndex++) {
						if(matrix.isCellPresent(rowIndex, 0) && matrix.getHTML(rowIndex, 0).trim().isEmpty() == false) {
							final MatrixValidityVO matrixValidityVO;
							if (matrixList.exists(rowIndex, currentColumn)) {
								matrixValidityVO = matrixList.get(rowIndex, currentColumn);
							} else {
								matrixValidityVO = new MatrixValidityVO();
								matrixList.set(rowIndex, currentColumn, matrixValidityVO);
							}
							matrixValidityVO.setAnswerY(answerVO);
							
							if(matrixValidityVO.getAnswerX() == null && matrixList.get(rowIndex,1) != null) {
								MatrixValidityVO otherVO =  matrixList.get(rowIndex, 1);
								matrixValidityVO.setAnswerX(otherVO.getAnswerX());
							}
										
							matrixValidityVO.setValidity(Validity.Falsch);
							
							addValidityBtnOn(rowIndex, currentColumn, matrixValidityVO);

						}
												
						/*delegate.saveMatrixValidityValue(matrixValidityVO,Validity.Falsch, new Function<MatrixValidityProxy, Void>() {

							@Override
							public Void apply(MatrixValidityProxy input) {
								matrixValidityVO.setMatrixValidityProxy(input);
								return null;
							}
						});*/
					}
				}else {
					//do nothing
				}
				
				return null;
			}
		}));
		
		editY.addClickHandler(new TextEditClickHandler(answerVO, textBox, label,editY,saveY));
		deleteY.addClickHandler(new DeleteClickedHandler(answerVO,false));
		
		if(isEdit == false) {
			editY.removeFromParent();
			saveY.removeFromParent();
		}
		
		if(isDelete == false) {
			deleteY.removeFromParent();
		}
	}

	@Override
	public void setAutherPickerValues(Collection<PersonProxy> values,PersonProxy logedUser,boolean isAdminOrInstitutionalAdmin) {

		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) author.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		author.setSuggestOracle(suggestOracle1);
		author.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				if (object != null) {
					return object.getName() + " " + object.getPrename();
				} else {
					return "";
				}
			}
		});
		// change {
		if (isAdminOrInstitutionalAdmin == false) {
			author.setSelected(logedUser);
			author.setEnabled(false);
		}else {
			author.setSelected(questionProxy.getAutor());
		}
		author.setWidth(150);
	}

	@Override
	public void setRewiewerPickerValues(List<PersonProxy> values) {
		DefaultSuggestOracle<PersonProxy> suggestOracle1 = (DefaultSuggestOracle<PersonProxy>) rewiewer.getSuggestOracle();
		suggestOracle1.setPossiblilities((List<PersonProxy>) values);
		
		rewiewer.setSuggestOracle(suggestOracle1);
		rewiewer.setRenderer(new AbstractRenderer<PersonProxy>() {

			@Override
			public String render(PersonProxy object) {
				if (object != null) {
					return object.getName() + " " + object.getPrename();
				} else {
					return "";
				}
			}
		});
		
		rewiewer.setWidth(150);
		
		PersonProxy lastSelectedReviwer = ClientUtility.getAnswerReviwerPersonProxyFromCookie(values);
		if (lastSelectedReviwer != null)
			rewiewer.setSelected(lastSelectedReviwer);
	}

	@Override
	public void display() {
		center();
		show();
	}

	@UiHandler("save")
	public void onSaveButtonClick(ClickEvent event) {
		Log.info("IN save");
		//Log.info("Size " + matrixList.size());
		
		for (MatrixValidityVO vo : matrixList) {
			Log.info("Vo : " + vo);
		}
		
		if(questionProxy!= null && questionProxy.getQuestionType() != null ) {
			
//			StringBuilder errorString = new StringBuilder();
			
			
			if(validationOfFields(true,null) == true) {
                //delegate.saveMatrixAnswer(currentMatrixValidityProxy,matrixList, author.getSelected(), rewiewer.getSelected(), submitToReviewComitee.getValue(), comment.getText());
				delegate.saveAllTheValuesToAnswerAndMatrixAnswer(currentMatrixValidityProxy,matrixList, author.getSelected(), rewiewer.getSelected(), submitToReviewComitee.getValue(), comment.getText());
				hide();
			}
		}
		
		
	}
	
	private boolean validationOfFields(boolean addMatrixValidation, String answerText) {

		//comment.removeStyleName("higlight_onViolation");
		rewiewer.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		author.getTextField().advancedTextBox.removeStyleName("higlight_onViolation");
		submitToReviewComitee.removeStyleName("higlight_onViolation");
		
		ArrayList<String> messages = Lists.newArrayList();
		boolean flag = true;
		
		if(submitToReviewComitee.getValue())
		{
			rewiewer.setSelected(null);
		}
		else if(rewiewer.getSelected() != null)
		{
			submitToReviewComitee.setValue(false);
		}else {
			//ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.selectReviewerOrComitee());
			//return false;
			flag = false;
			messages.add(constants.selectReviewerOrComitee());
			rewiewer.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
			submitToReviewComitee.addStyleName("higlight_onViolation");
		}
		
		if(author.getSelected() == null) {
			flag = false;
			//errorString.append(constants.authorMayNotBeNull()).append("<br />");
			messages.add(constants.authorMayNotBeNull());
			author.getTextField().advancedTextBox.addStyleName("higlight_onViolation");
		}
		
		/*if(comment.getText() == null || comment.getText().isEmpty()) {
			flag = false;
			//errorString.append(constants.commentMayNotBeNull()).append("<br />");
			messages.add(constants.commentMayNotBeNull());
			comment.addStyleName("higlight_onViolation");
		}*/
		
		// Validate answer Max Length
		if(answerText != null && questionProxy.getQuestionType().getAnswerLength() != null && answerText.length() > questionProxy.getQuestionType().getAnswerLength()) {
			flag = false;
			messages.add(constants.answerTextMaxLength());
		}
		
		if(addMatrixValidation == true) {
			int size = FluentIterable.from(matrixList).filter(new Predicate<MatrixValidityVO>() {

				@Override
				public boolean apply(MatrixValidityVO input) {
					return input.getAnswerX() == null || input.getAnswerY() == null;
				}
			}).size();
			
			if(size > 0) {
				flag = false;
				messages.add(constants.errorInMatrixList());
				//ConfirmationDialogBox.showOkDialogBox(constants.error(), constants.errorInMatrixList());
				//return;
			}
			
			if(questionProxy.getQuestionType().getAllowOneToOneAss() != null && questionProxy.getQuestionType().getAllowOneToOneAss().equals(true)) {
								
				Set<Cell<Integer,Integer,MatrixValidityVO>> setOfTrueValidity = matrixList.filter(new Predicate<MatrixValidityVO>() {

					@Override
					public boolean apply(MatrixValidityVO input) {
						return input.getValidity() != null && Validity.Wahr.equals(input.getValidity());
					}
				});
		
				for (Cell<Integer, Integer, MatrixValidityVO> cell : setOfTrueValidity) {
					
					if(cell.getRowKey() != null && cell.getColumnKey() != null) {
						int count = 0;
						
						Log.info("Current Row : " + cell.getRowKey() + " current Column : " + cell.getColumnKey());
						
						for(Cell<Integer, Integer, MatrixValidityVO> cell2 : setOfTrueValidity) {
							
							if(cell.getRowKey().equals(cell2.getRowKey()) || cell.getColumnKey().equals(cell2.getColumnKey())) {
								count += 1;
								Log.info("Row : " + cell2.getRowKey() + " Column : " + cell2.getColumnKey());
							}
						}
						
						if(count > 1) {
							Log.info("For this row and column there are " + count);
							flag = false;
							messages.add(constants.errorInMatrixOneToOneAss());
							break;
						}
					}
				}
			}

		}
				
		if(flag == false) {
			ReceiverDialog.showMessageDialog(constants.pleaseEnterWarning(),messages);
		}
		return flag;
	}

	private class TextSaveClickedHandler implements ClickHandler {
		
		private final AnswerVO answer;
		private final TextBox textBox;
		private final Label label;
		private final IconButton edit;
		private final IconButton save;
		private final Function<Boolean, Void> function;
		
		public TextSaveClickedHandler(AnswerVO answer, TextBox textBox, Label label,IconButton edit,IconButton save, Function<Boolean,Void> function) {
			this.answer = answer;
			this.textBox = textBox;
			this.label = label;
			this.edit = edit;
			this.save = save;
			this.function = function;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String text = textBox.getText();
			if (text != null && text.isEmpty() == false) {
	
				boolean flag = true;//validationOfFields(false,text);
				
				if(flag == true) {

					/*String points = null; 
					String mediaPath = null;
					String additionalKeywords = null; 
					Integer sequenceNumber =null;
					Validity validity = Validity.Falsch; 
					
					if(answer.getAnswerProxy() != null && answer.getAnswerProxy().getValidity() != null) {
						validity = answer.getAnswerProxy().getValidity();
					}
					
                    delegate.saveAnswerProxy(answer.getAnswerProxy(), text, author.getSelected(), rewiewer.getSelected(), submitToReviewComitee.getValue(), comment.getText(), validity, points, mediaPath, additionalKeywords,sequenceNumber, new Function<AnswerProxy,Void>() {

						@Override
						public Void apply(AnswerProxy input) {
						*/	
							addAnswerX.setVisible(true);
							addAnswerY.setVisible(true);
							
							//answer.setAnswerProxy(input);
							textBox.setVisible(false);
							
							label.setVisible(true);	
							label.setText(text);
							label.setTitle(text);
							
							answer.setAnswer(text);
							
							save.setVisible(false);
							edit.setVisible(true);
							function.apply(true);
							
						//	return null;
						//}
					//});
				}
			}
		}
	}
	private class ButtonClickHandler implements ClickHandler {
		
		private final MatrixValidityVO mValidtyVO;
		private final HtmlToggleButton button;
		
		public ButtonClickHandler(MatrixValidityVO mValidtyVO, HtmlToggleButton button) {
			this.mValidtyVO = mValidtyVO;
			this.button = button;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			//Validity validity;
			if (button.isDown()) {
				Log.info("value true");
				//validity = Validity.Wahr;
				mValidtyVO.setValidity(Validity.Wahr);
			} else {
				Log.info("value false");
				//validity = Validity.Falsch;
				mValidtyVO.setValidity(Validity.Falsch);
			}
			/*delegate.saveMatrixValidityValue(mValidtyVO, validity, new Function<MatrixValidityProxy, Void>() {
				
				@Override
				public Void apply(MatrixValidityProxy input) {
					mValidtyVO.setMatrixValidityProxy(input);
					return null;
				}
			});*/
		}
	}
	
	private class TextEditClickHandler implements ClickHandler {
		
		//private final AnswerVO answer;
		private final TextBox textBox;
		private final Label label;
		private final IconButton edit;
		private final IconButton save;
		
		public TextEditClickHandler(AnswerVO answer, TextBox textBox, Label label, IconButton edit, IconButton save) {
			//this.answer = answer;
			this.textBox = textBox;
			this.label = label;
			this.edit = edit;
			this.save = save;
		}
	
		@Override
		public void onClick(ClickEvent event) {
			String text = label.getText();
			addAnswerX.setVisible(false);
			addAnswerY.setVisible(false);
			
			textBox.setText(text);
			label.setVisible(false);
			textBox.setVisible(true);
			edit.setVisible(false);
			save.setVisible(true);
			textBox.setFocus(true);
		}
	}
	
	private class DeleteClickedHandler implements ClickHandler {

		private final AnswerVO answer;
		private final boolean isAnswerX;
		public DeleteClickedHandler(AnswerVO answerVO,boolean isAnswerX) {
			this.answer = answerVO;
			this.isAnswerX = isAnswerX;
		}

		@Override
		public void onClick(ClickEvent event) {
			Log.info("Delete clicked");
			ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.deleteMatrixAnswerConfirmation(), new ConfirmDialogBoxYesNoButtonEventHandler() {
				
				@Override
				public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
					addAnswerX.setVisible(true);
					addAnswerY.setVisible(true);
					
					FluentIterable<MatrixValidityVO> fluentIterable = FluentIterable.from(matrixList);
					final FluentIterable<MatrixValidityVO> foundAnswers;
					if(isAnswerX == true) {
						foundAnswers = fluentIterable.filter(new AnswerXPredicate(answer.getAnswerProxy()));
					}else {
						foundAnswers = fluentIterable.filter(new AnswerYPredicate(answer.getAnswerProxy()));
					}
					
					if(foundAnswers.isEmpty() == false) {
                                         
						delegate.deletedSelectedAnswer(answer.getAnswerProxy(),isAnswerX,new Function<Boolean, Void>() {

							@Override
							public Void apply(Boolean input) {
								
								if(input == true) {
									//delete the full answer
									clearFullMatrix();
								}else {
									if(isAnswerX ==  true) {
										removeTableRow(foundAnswers);
									}else {
										removeTableCols(foundAnswers);
									}
								}
								return null;
							}
						});
					} else {
						final FluentIterable<MatrixValidityVO> foundAnswersWithoutIds;
						if(isAnswerX == true) {
							foundAnswersWithoutIds = fluentIterable.filter(new AnswerXWithoutIdsPredicate(answer));
						}else {
							foundAnswersWithoutIds = fluentIterable.filter(new AnswerYWithoutIdsPredicate(answer));
						}
						
						if(foundAnswersWithoutIds.isEmpty() == false) {
							if(isAnswerX ==  true) {
								removeTableRow(foundAnswersWithoutIds);
							}else {
								removeTableCols(foundAnswersWithoutIds);
							}
						} else {
							if(isAnswerX ==  true) {
								matrix.removeRow(matrix.getRowCount() - 2);
							}else {
								matrix.removeCell(0, matrix.getCellCount(0) -2);
							}
						}
					}
				}

				private void removeTableCols(final FluentIterable<MatrixValidityVO> foundAnswersWithoutIds) {
					int firstRow = matrixList.getRowForObject(foundAnswersWithoutIds.get(0));
					int currentColumn = matrixList.getColumnForObject(foundAnswersWithoutIds.get(0));
					int totalRows = matrix.getRowCount() -2; // removed answerx button row
					if(firstRow > -1 && currentColumn > -1) {
						for(int i=totalRows;i>=0;i--) {
							matrix.setHTML(i, currentColumn, "");
							//matrix.removeCell(i, currentColumn);
							matrixList.removeCell(i,currentColumn);	
						}
						Log.info("in column remove : Row count  After : " + matrixList.getRows());
						if(matrixList.getRows() == 0) {
							clearFullMatrix();
						}
					}else {
						Log.error("error in first row or current column " + firstRow + " : " + currentColumn);
					}
				}
				
				private void removeTableRow(final FluentIterable<MatrixValidityVO> foundAnswers) {
					int currentRow = matrixList.getRowForObject(foundAnswers.get(0));
					int totalCols = matrixList.getColumns(currentRow);
					if(currentRow > -1) {
						//matrix.removeRow(currentRow);
						for(int i=0;i<totalCols;i++) {
							matrix.removeCell(currentRow, 0);	
						}
						Log.info("Row count Before : " + matrixList.getRows());
						matrixList.removeRow(currentRow);
						Log.info("Row count After : " + matrixList.getRows());
						if(matrixList.getRows() == 0) {
							clearFullMatrix();
						}
					}else {
						Log.error("cannot find row for matrix vo : " + currentRow);
					}
				}
				
				@Override
				public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
					Log.info("No clicked");
				}
			});
		}
		
	}

	private void clearFullMatrix() {
		matrix.clear(true);
		matrix.removeAllRows();
		matrixList.clear();
		
		// create new matrix with addAnswerX and addAnswerY
		matrix.setWidget(0, 1, addAnswerY);
		matrix.setWidget(1, 0, addAnswerX);
	}
	
	@Override
	public void setValues(List<MatrixValidityProxy> response, boolean isNew, boolean isEdit, boolean isDelete) {
		
		this.currentMatrixValidityProxy = response;
		FluentIterable<MatrixValidityVO> fluentIterable = FluentIterable.from(matrixList);
		
		for (MatrixValidityProxy matrixValidityProxy : response) {
			
			Optional<MatrixValidityVO> optional1 = fluentIterable.firstMatch(new MatrixValidityPredicate(matrixValidityProxy));
			
			MatrixValidityVO matrixValidityVO = new MatrixValidityVO();		
			matrixValidityVO.setId(matrixValidityProxy.getId());
			matrixValidityVO.setState(State.CREATED);
			matrixValidityVO.setValidity(matrixValidityProxy.getValidity());
			matrixValidityVO.setMatrixValidityProxy(matrixValidityProxy);
				
			if(optional1.isPresent() == false)  {
				
				Optional<MatrixValidityVO> optionalAnswerX = fluentIterable.firstMatch(new AnswerXPredicate(matrixValidityProxy.getAnswerX()));
				Optional<MatrixValidityVO> optionalAnswerY = fluentIterable.firstMatch(new AnswerYPredicate(matrixValidityProxy.getAnswerY()));
				
				if(optionalAnswerX.isPresent() == true && optionalAnswerY.isPresent() == false) {
					// add new column
									
					int currentRow = matrixList.getRowForObject(optionalAnswerX.get());
					
					AnswerVO answerY = new AnswerVO();
					answerY.setAnswerProxy(matrixValidityProxy.getAnswerY());
					answerY.setAnswer(matrixValidityProxy.getAnswerY().getAnswerText());
					answerY.setId(matrixValidityProxy.getAnswerY().getId());
					answerY.setState(State.CREATED);
					
					matrixValidityVO.setAnswerX(optionalAnswerX.get().getAnswerX());
					matrixValidityVO.setAnswerY(answerY);
					
					matrixList.addColumn(currentRow, 1, matrixValidityVO);
					int currentColumn = matrixList.getColumnForObject(matrixValidityVO);
					
					addValidityBtnOn(currentRow,currentColumn,matrixValidityVO);
					//addAnswerYTextAndLabel(matrixValidityVO);
					addAnswerY(matrixValidityVO,isEdit,isDelete);
				}
				
				if(optionalAnswerX.isPresent() == false && optionalAnswerY.isPresent() == true){
					// add new row
					int currentColumn = matrixList.getColumnForObject(optionalAnswerY.get());
					AnswerVO answerX = new AnswerVO();
					answerX.setAnswerProxy(matrixValidityProxy.getAnswerX());	
					answerX.setAnswer(matrixValidityProxy.getAnswerX().getAnswerText());
					answerX.setId(matrixValidityProxy.getAnswerX().getId());
					answerX.setState(State.CREATED);
					
					matrixValidityVO.setAnswerX(answerX);
					matrixValidityVO.setAnswerY(optionalAnswerY.get().getAnswerY());
					matrixList.addRow(1,currentColumn,matrixValidityVO);
					int currentRow = matrixList.getRowForObject(matrixValidityVO);
					addValidityBtnOn(currentRow, currentColumn, matrixValidityVO);
					//addAnswerXTextAndLabel(matrixValidityVO);
					addAnswerX(matrixValidityVO,isEdit,isDelete);
				}
				
				// not answer is added; 
				if(optionalAnswerX.isPresent() == false && optionalAnswerY.isPresent() == false) {
					AnswerVO answerX = new AnswerVO();
					AnswerVO answerY = new AnswerVO();

					answerX.setAnswerProxy(matrixValidityProxy.getAnswerX());
					answerX.setAnswer(matrixValidityProxy.getAnswerX().getAnswerText());
					answerX.setId(matrixValidityProxy.getAnswerX().getId());
					answerX.setState(State.CREATED);
					
					answerY.setAnswerProxy(matrixValidityProxy.getAnswerY());
					answerY.setAnswer(matrixValidityProxy.getAnswerY().getAnswerText());
					answerY.setId(matrixValidityProxy.getAnswerY().getId());
					answerY.setState(State.CREATED);
					
					matrixValidityVO.setAnswerX(answerX);
					matrixValidityVO.setAnswerY(answerY);
					
					matrixList.add(1,1,matrixValidityVO);	
					int currentRow = matrixList.getRowForObject(matrixValidityVO);
					int currentColumn = matrixList.getColumnForObject(matrixValidityVO);
					
					addValidityBtnOn(currentRow, currentColumn, matrixValidityVO);
					//addAnswerXTextAndLabel(matrixValidityVO);
					//addAnswerYTextAndLabel(matrixValidityVO);
					addAnswerX(matrixValidityVO,isEdit,isDelete);
					addAnswerY(matrixValidityVO,isEdit,isDelete);
				}
				
				if(optionalAnswerX.isPresent() == true && optionalAnswerY.isPresent() == true) {
					int currentRow = matrixList.getRowForObject(optionalAnswerX.get());
					int currentColumn = matrixList.getColumnForObject(optionalAnswerY.get());
					matrixValidityVO.setAnswerX(optionalAnswerX.get().getAnswerX());
					matrixValidityVO.setAnswerY(optionalAnswerY.get().getAnswerY());
					matrixList.set(currentRow,currentColumn,matrixValidityVO);
					addValidityBtnOn(currentRow, currentColumn, matrixValidityVO);
				}
				
			}else {
				Log.info("MatrixValidityProxy already added");
			}
		}
		
		if(response.size() > 0) {
			AnswerProxy proxy = response.get(0).getAnswerX();
			author.setSelected(proxy.getAutor());
			rewiewer.setSelected(proxy.getRewiewer());
			submitToReviewComitee.setValue(proxy.getSubmitToReviewComitee());
			if(proxy.getComment() != null) {
				comment.setText(proxy.getComment());
			}
		}
		
		if(isNew == false) {
			addAnswerX.removeFromParent();
			addAnswerY.removeFromParent();
		}
	}
	
	
	/*private void addAnswerXTextAndLabel(MatrixValidityVO vo) {
		
		final IconButton editX = new IconButton();
		editX.setIcon("pencil");
		final IconButton deleteX = new IconButton();
		deleteX.setIcon("trash");
		final IconButton saveX = new IconButton();
		saveX.setIcon("disk");
		final TextBox textBoxX = new TextBox();
		final Label labelX = new Label();
		textBoxX.addStyleName("matrix-textbox-x");
		labelX.addStyleName("matrix-label-x");
		HorizontalPanel hpX = new HorizontalPanel();
		hpX.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpX.addStyleName("matrix-hp-text-x");
		hpX.setSpacing(2);
		
		hpX.add(saveX);
		hpX.add(editX);
		hpX.add(deleteX);
		hpX.add(textBoxX);
		hpX.add(labelX);
		
		labelX.setVisible(true);
		textBoxX.setVisible(false);
		saveX.setVisible(false);
		labelX.setText(vo.getAnswerX().getAnswer());
		labelX.setTitle(vo.getAnswerX().getAnswer());
		textBoxX.setText(vo.getAnswerX().getAnswer());
		
		labelX.setWidth("100px");
		labelX.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		labelX.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		labelX.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		
		final int currentRowX = matrix.getRowCount() - 1;
				
		matrix.setWidget(currentRowX, 0, hpX);
		matrix.setWidget(currentRowX + 1, 0, addAnswerX);
		
		saveX.addClickHandler(new TextSaveClickedHandler(vo.getAnswerX(), textBoxX, labelX,editX,saveX, new Function<Boolean, Void>() {

			@Override
			public Void apply(Boolean input) {
				Log.info("persist done for answerX");
				return null;
			}
		}));
		editX.addClickHandler(new TextEditClickHandler(vo.getAnswerX(), textBoxX, labelX,editX,saveX));
		deleteX.addClickHandler(new DeleteClickedHandler(vo.getAnswerX(),true));
		
	}

	private void addAnswerYTextAndLabel(MatrixValidityVO vo) {
		final IconButton editY = new IconButton();
		editY.setIcon("pencil");
		final IconButton deleteY = new IconButton();
		deleteY.setIcon("trash");
		final IconButton saveY = new IconButton();
		saveY.setIcon("disk");
		final TextBox textBoxY = new TextBox();
		final Label labelY = new Label();
		//labelY.addStyleName("rowRotate90");
		//textBoxY.addStyleName("rowRotate90");
		textBoxY.addStyleName("textbox-y");
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel buttonHP = new HorizontalPanel();
		
		vp.addStyleName("matrix-hp-text-y");
		vp.setSpacing(2);
		
		buttonHP.add(editY);
		buttonHP.add(saveY);
		buttonHP.add(deleteY);
		
		vp.add(buttonHP);
		vp.add(hp);
		
		hp.add(textBoxY);
		hp.add(labelY);
		labelY.setVisible(true);
		textBoxY.setVisible(false);
		saveY.setVisible(false);
		labelY.setText(vo.getAnswerY().getAnswer());
		labelY.setTitle(vo.getAnswerY().getAnswer());
		textBoxY.setText(vo.getAnswerY().getAnswer());
		
		editY.getElement().getParentElement().addClassName("matrix-icon-y");
		deleteY.getElement().getParentElement().addClassName("matrix-icon-y");
		saveY.getElement().getParentElement().addClassName("matrix-icon-y");
		
		int widgetAddColumnY = matrix.getCellCount(0) - 1;
		matrix.setWidget(0, widgetAddColumnY, vp);
		matrix.setWidget(0, widgetAddColumnY + 1, addAnswerY);
		
		saveY.addClickHandler(new TextSaveClickedHandler(vo.getAnswerY(), textBoxY, labelY,editY,saveY, new Function<Boolean, Void>() {
			
			@Override
			public Void apply(Boolean input) {
				Log.info("Persist done for answer Y");
				return null;
			}
		}));
		editY.addClickHandler(new TextEditClickHandler(vo.getAnswerY(), textBoxY, labelY, editY, saveY));
		deleteY.addClickHandler(new DeleteClickedHandler(vo.getAnswerY(),false));
		
	}*/

	private void addValidityBtnOn(int currentRow, int currentColumn,final MatrixValidityVO vo) {
		if(currentColumn > -1 && currentRow > -1) {
			
			int totalRows = matrix.getRowCount() -1; // removed button x row 
			int totalColumns = matrix.getCellCount(currentRow);
			
			if(totalRows > currentRow && totalColumns > currentColumn && matrix.getWidget(currentRow, currentColumn) != null) {
				HtmlToggleButton buttonX = (HtmlToggleButton) matrix.getWidget(currentRow, currentColumn);
				Log.info("button is " + buttonX);					
			}else {
				final HtmlToggleButton button = new HtmlToggleButton(McAppConstant.WRONG_ICON, McAppConstant.RIGHT_ICON);
				//button.setWidth("13px");
				//button.getElement().getStyle().setBorderWidth(0, Unit.PX);
				button.addStyleName("matrix-validity-btn");
				matrix.setWidget(currentRow, currentColumn, button);
				button.setDown(Validity.Wahr.equals(vo.getValidity())); // for true down
				button.addClickHandler(new ButtonClickHandler(vo, button));
				button.getElement().getParentElement().addClassName("matrix-validity-btn-td");
			}
		}else {
			Log.info("error in getcolumnforobject method");
		}
		
	}

	

	private class AnswerXPredicate implements Predicate<MatrixValidityVO> {

		private final AnswerProxy answerX;
		
		public AnswerXPredicate(final AnswerProxy answerX) {
			this.answerX = answerX;
		}

		@Override
		public boolean apply(MatrixValidityVO input) {
			
			if(answerX == null) {
				return false;
			}
			if(input != null && input.getAnswerX() != null && input.getAnswerX().getAnswerProxy() != null) {
				return input.getAnswerX().getAnswerProxy().getId().equals(answerX.getId());
			}
			return false;
		}
		
	}
	
	private class AnswerYPredicate implements Predicate<MatrixValidityVO> {
		private final AnswerProxy answerY;
		
		public AnswerYPredicate(final AnswerProxy answerY) {
			this.answerY = answerY;
		}
		
		@Override
		public boolean apply(MatrixValidityVO input) {
			
			if(answerY == null) {
				return false;
			}
			
			if(input != null && input.getAnswerY() != null && input.getAnswerY().getAnswerProxy() != null) {
				return input.getAnswerY().getAnswerProxy().getId().equals(answerY.getId());
			}
			return false;
		}
		
	}

	private class AnswerXWithoutIdsPredicate implements Predicate<MatrixValidityVO> {

		private final AnswerVO answerX;
		
		public AnswerXWithoutIdsPredicate(final AnswerVO answerX) {
			this.answerX = answerX;
		}

		@Override
		public boolean apply(MatrixValidityVO input) {
			
			if(answerX == null) {
				return false;
			}
			if(input != null && input.getAnswerX() != null) {
				return input.getAnswerX().equals(answerX);
			}
			return false;
		}
		
	}
	
	private class AnswerYWithoutIdsPredicate implements Predicate<MatrixValidityVO> {
		private final AnswerVO answerY;
		
		public AnswerYWithoutIdsPredicate(final AnswerVO answerY) {
			this.answerY = answerY;
		}
		
		@Override
		public boolean apply(MatrixValidityVO input) {
			
			if(answerY == null) {
				return false;
			}
			
			if(input != null && input.getAnswerY() != null) {
				return input.getAnswerY().equals(answerY);
			}
			return false;
		}
		
	}

	private class MatrixValidityPredicate implements Predicate<MatrixValidityVO> {
		private final MatrixValidityProxy matrixValidityProxy;
		
		public MatrixValidityPredicate(final MatrixValidityProxy matrixValidityProxy) {
			this.matrixValidityProxy = matrixValidityProxy;
		}
		
		@Override
		public boolean apply(MatrixValidityVO input) {
			
			if(input != null && input.getMatrixValidityProxy() != null) {
				return input.getMatrixValidityProxy().getId().equals(matrixValidityProxy.getId());
			}
			return false;
		}
		
	}
	
}
