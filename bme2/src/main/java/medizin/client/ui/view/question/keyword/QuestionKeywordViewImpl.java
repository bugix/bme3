package medizin.client.ui.view.question.keyword;

import java.util.ArrayList;
import java.util.List;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.style.resources.MyCellTableNoHilightResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEvent;
import medizin.client.ui.widget.dialogbox.event.ConfirmDialogBoxYesNoButtonEventHandler;
import medizin.client.ui.widget.pager.MySimplePager;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class QuestionKeywordViewImpl extends Composite implements QuestionKeywordView {

	private static QuestionKeywordViewImplUiBinder uiBinder = GWT.create(QuestionKeywordViewImplUiBinder.class);

	interface QuestionKeywordViewImplUiBinder extends UiBinder<Widget, QuestionKeywordViewImpl> {}
	
	public BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField(provided = true)
	CellTable<KeywordProxy> keywordTable;
	
	@UiField(provided = true)
	MySimplePager keywordTablePager;
	
	@UiField
	DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSuggestBox;
	
	@UiField
	IconButton keywordAddButton;
	
	private List<AbstractEditableCell<?, ?>> editableCells;

	private medizin.client.ui.view.question.keyword.QuestionKeywordView.Delegate delegate;
	
	private QuestionProxy proxy;
	
	public QuestionKeywordViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableNoHilightResources.class);
		keywordTable = new CellTable<KeywordProxy>(5, tableResources);
		
		MySimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		keywordTablePager = new MySimplePager(MySimplePager.TextLocation.RIGHT, pagerResources, true, 10, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}
	@Override
	public void setQuestionProxy(QuestionProxy proxy) {
		this.proxy = proxy;
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@UiHandler("keywordAddButton")
	public void keywordAddButtonClicked(ClickEvent event)
	{
		if (keywordSuggestBox.getText().isEmpty())
		{
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.keywordNullMessage());
		}
		else
		{
			delegate.keywordAddButtonClicked(keywordSuggestBox.getText(), proxy);
		}
	}

	public void initKeyword(boolean isAnswerEditable) {
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		keywordTable.addColumn(new TextColumn<KeywordProxy>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

                public String render(java.lang.String obj) {
                    return obj == null ? "" : String.valueOf(obj);
                }
            };

			@Override
			public String getValue(KeywordProxy object) {
				return renderer.render(object == null ? null : object.getName());
			}
		}, constants.keywords());
		
		if (isAnswerEditable)
		{
			ActionCell.Delegate<KeywordProxy> deleteKeyworddelegate = new ActionCell.Delegate<KeywordProxy>() {
				public void execute(final KeywordProxy keyword) {
					ConfirmationDialogBox.showYesNoDialogBox(constants.warning(), constants.keywordDelMessage(), new ConfirmDialogBoxYesNoButtonEventHandler() {
						
						@Override
						public void onYesButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {
							delegate.deleteKeywordClicked(keyword, proxy);
						}
						
						@Override
						public void onNoButtonClicked(ConfirmDialogBoxYesNoButtonEvent event) {}
					});
				}	
			};
			GetValue<KeywordProxy> getKeywordValue = new GetValue<KeywordProxy>() {
				public KeywordProxy getValue(KeywordProxy keyword) {
					return keyword;
				}
			};
			
			addColumn(new ActionCell<KeywordProxy>(McAppConstant.DELETE_ICON, deleteKeyworddelegate), "", getKeywordValue, null);
	    
			keywordTable.addColumnStyleName(1, "iconColumn");			
		}
		
		keywordTable.addColumnStyleName(0, "questionTextColumn");
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<KeywordProxy, C> fieldUpdater) {
		Column<KeywordProxy, C> column = new Column<KeywordProxy, C>(cell) {
			@Override
			public C getValue(KeywordProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		keywordTable.addColumn(column);
	}
	
	  private static interface GetValue<C> {
	    C getValue(KeywordProxy keyword);
	  }

	public CellTable<KeywordProxy> getKeywordTable() {
		return keywordTable;
	}

	public void setKeywordTable(CellTable<KeywordProxy> keywordTable) {
		this.keywordTable = keywordTable;
	}

	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> getKeywordSuggestBox() {
		return keywordSuggestBox;
	}

	public void setKeywordSuggestBox(DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSuggestBox) {
		this.keywordSuggestBox = keywordSuggestBox;
	}

	public IconButton getKeywordAddButton() {
		return keywordAddButton;
	}

	public void setKeywordAddButton(IconButton keywordAddButton) {
		this.keywordAddButton = keywordAddButton;
	}

	@Override
	public void initKeywordView() {
		delegate.initKeywordView();
	}

}
