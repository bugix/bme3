package medizin.client.ui;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
/**
 * Constants that can be used throughout the application.
 * @author masterthesis
 *
 */
public class McAppConstant {
	public static final String CONTENT_NOT_UNIQUE = "Die Institution mit diesem Namen besteht bereits.";
	public static final String INSTITUTION_IS_REFERENCED = "Die Institution hat noch Themenblöcke. Sie kann nicht gelöscht werden, solange die Themenblöcke dieser Institution zugeordnet sind.";
	public static final String ERROR_WHILE_DELETE = "Fehlen beim löschen: ";
	public static final String ERROR_WHILE_DELETE_VIOLATION = "Fehlen beim löschen, Violation: ";
	public static final String ERROR_WHILE_CREATE = "Fehlen beim löschen: ";
	public static final String ERROR_WHILE_CREATE_VIOLATION = "Fehlen beim löschen, Violation: ";
	public static final String ERROR_WHILE_UPDATE = "Fehlen beim Bearbeiten: ";
	public static final String ERROR_WHILE_UPDATE_VIOLATION = "Fehlen beim Bearbeiten, Violation: ";
	public static final String EVENT_IS_REFERENCED = "Der Themenbereich ist noch in Prüfungsfragen Referenziert und kann nicht gelöscht werden.";
	public static final String ACTIVITY_MAY_STOP = "Sie haben ungespeicherte Änderungen, möchten Sie wirklich die aktuelle Aktion abbrechen?";
	public static final String QUESTION_TAB_PROPOSAL = "vorgeschlagene Fragen";
	public static final String QUESTION_TAB_ASSEMENTQUESTION = "bestehende Prüfungsfragen";
	public static final String QUESTION_TAB_NEWQUESTION = "neue Prüfungsfragen";
	public static final String DO_NOT_SAVE_CHANGES = "Sollen alle Änderungen verworfen werden?";
	public static final String QUESTION_TYPE = "Fragetyp";
	public static final String QUESTION_EVENT = "Themenbereich";
	public static final String QUESTION_TEXT = "Fragentext";
	public static final String ANSWER_TEXT = "Antworttext";
	public static final SafeHtml ACCEPT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-check\"></span>").toSafeHtml();
	public static final SafeHtml DECLINE_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-closethick\"></span>").toSafeHtml();
	public static final SafeHtml DELETE_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-trash\" style=\"margin:auto\"></span>").toSafeHtml();
	public static final SafeHtml EDIT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-pencil\" style=\"margin:auto\"></span>").toSafeHtml();
	
	public static final SafeHtml RIGHT_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-check\"></span>").toSafeHtml();
	public static final SafeHtml WRONG_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-close\"></span>").toSafeHtml();

	public static final SafeHtml HELP_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-help\"></span>").toSafeHtml();
	
	//public static final SafeHtml DELETE_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-closethick\"></span>").toSafeHtml();
	public static final SafeHtml DOWN_ICON =  new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-carat-1-n\"></span>").toSafeHtml();
	public static final SafeHtml UP_ICON =  new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-carat-1-s\"></span>").toSafeHtml();
	public static int TABLE_PAGE_SIZE = 15;
	public static final int TABLE_JUMP_SIZE = 30;
	public static final int ENTRY_TIMEOUT_MS = 700;
	
	public static final String RESEND_TO_REVIEW_KEY = "resendToReview";
	public static final String LAST_SELECTED_QUESTION_REVIEWER = "lastSelectedQuestionReviewer";
	public static final String LAST_SELECTED_ANSWER_REVIEWER = "lastSelectedAnswerReviewer";
}
