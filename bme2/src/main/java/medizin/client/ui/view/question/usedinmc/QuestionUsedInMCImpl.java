package medizin.client.ui.view.question.usedinmc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.events.RecordChangeEvent;
import medizin.client.events.RecordChangeHandler;
import medizin.client.proxy.AssesmentProxy;
import medizin.client.proxy.AssesmentQuestionProxy;
import medizin.client.style.resources.MyCellTableResources;
import medizin.client.style.resources.MySimplePagerResources;
import medizin.client.ui.McAppConstant;
import medizin.shared.i18n.BmeConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;

public class QuestionUsedInMCImpl extends Composite implements RecordChangeHandler,QuestionUsedInMC {

	private static QuestionUsedInMCImplUiBinder uiBinder = GWT.create(QuestionUsedInMCImplUiBinder.class);

	interface QuestionUsedInMCImplUiBinder extends UiBinder<Widget, QuestionUsedInMCImpl> {
	}

	private static final DateTimeFormat SHORT_FORMAT = DateTimeFormat.getFormat("dd.MM.yyyy");
	
	private BmeConstants constants = GWT.create(BmeConstants.class);

	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	CellTable<AssesmentQuestionProxy> table;

	private Set<String> paths = new HashSet<String>();

	private Delegate delegate;

	public QuestionUsedInMCImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AssesmentQuestionProxy>(McAppConstant.TABLE_PAGE_SIZE, tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, McAppConstant.TABLE_JUMP_SIZE, true);

		initWidget(uiBinder.createAndBindUi(this));

		addColumns();
	}

	private void addColumns() {
		paths.add("assesment.name");

		TextColumn<AssesmentQuestionProxy> assessmentName = new TextColumn<AssesmentQuestionProxy>() {

			Renderer<AssesmentProxy> renderer = new AbstractRenderer<AssesmentProxy>() {

				public String render(AssesmentProxy obj) {
					return obj == null ? "" : String.valueOf(obj.getName());
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object.getAssesment());
			}
		};

		table.addColumn(assessmentName, constants.assessmentName());
		
		paths.add("assesment.mc");

		TextColumn<AssesmentQuestionProxy> mc = new TextColumn<AssesmentQuestionProxy>() {

			Renderer<AssesmentProxy> renderer = new AbstractRenderer<AssesmentProxy>() {

				public String render(AssesmentProxy obj) {
					return obj == null ? "" : String.valueOf(obj.getMc().getMcName());
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object.getAssesment());
			}
		};

		table.addColumn(mc, constants.tableHeaderMC());
		table.setColumnWidth(1, "7%");

		paths.add("assesment.dateOfAssesment");
		TextColumn<AssesmentQuestionProxy> dateOfAssessment = new TextColumn<AssesmentQuestionProxy>() {

			Renderer<AssesmentProxy> renderer = new AbstractRenderer<AssesmentProxy>() {

				public String render(AssesmentProxy obj) {
					return obj == null ? "" : SHORT_FORMAT.format(obj.getDateOfAssesment());
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object.getAssesment());
			}
		};

		table.addColumn(dateOfAssessment, constants.dateOfAssessment());
		table.setColumnWidth(2, "10%");

		paths.add("schwierigkeit");
		TextColumn<AssesmentQuestionProxy> schwierigkeit = new TextColumn<AssesmentQuestionProxy>() {

			Renderer<Double> renderer = new AbstractRenderer<Double>() {

				public String render(Double obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object.getSchwierigkeit());
			}
		};

		table.addColumn(schwierigkeit, constants.schwierigkeit());
		table.setColumnWidth(3, "10%");

		paths.add("trenschaerfe");
		TextColumn<AssesmentQuestionProxy> trenschaerfe = new TextColumn<AssesmentQuestionProxy>() {

			Renderer<Double> renderer = new AbstractRenderer<Double>() {

				public String render(Double obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AssesmentQuestionProxy object) {
				return renderer.render(object.getTrenschaerfe());
			}
		};

		table.addColumn(trenschaerfe, constants.trenschaerfe());
		table.setColumnWidth(4, "10%");
	}

	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			McAppConstant.TABLE_PAGE_SIZE = pagesize;
		} else {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void setTableRowCount(Integer count) {
		table.setRowCount(count);
	}

	public HandlerRegistration addTableRangeChangeHandler(Handler handler) {
		return table.addRangeChangeHandler(handler);
	}

	public Range getTableVisibleRange() {
		return table.getVisibleRange();
	}

	public void setTableRowData(int start, List<AssesmentQuestionProxy> response) {
		table.setRowData(start,response);
	}

	public CellTable<AssesmentQuestionProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void initUsedInMCView() {
		delegate.initUsedInMCView();
	}
}
