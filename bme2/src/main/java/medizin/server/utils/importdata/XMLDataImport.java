package medizin.server.utils.importdata;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.server.domain.Answer;
import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.Assesment;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.Institution;
import medizin.server.domain.Keyword;
import medizin.server.domain.Mc;
import medizin.server.domain.Person;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionType;
import medizin.server.utils.importdata.XMLData.AssessmentPojo;
import medizin.shared.QuestionTypes;
import medizin.shared.Status;
import medizin.shared.Validity;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class XMLDataImport {

	private static Logger log = Logger.getLogger(XMLDataImport.class);
	private final XMLParser parser;
	private final List<XMLData> xmlDataList = Lists.newArrayList();
	private final Date defaultDateOfQuestionAdded;
	private static final Function<Element, String> ELEMENT_TO_STRING = new Function<Element, String>() {

		@Override
		public String apply(Element input) {
			return input.getText();
		}
	};

	public XMLDataImport(File xmlFile) throws JDOMException, IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
		defaultDateOfQuestionAdded = calendar.getTime();
		parser = new XMLParser(xmlFile);
	}

	public void importData() {
		System.out.println("Import started");
		xmlToPojo();
		pojoToDomain();
		System.out.println("Import done");
	}

	private void pojoToDomain() {
		Institution institution = Institution.findInstitutionByName("Humanmedizin");
		
		if(institution == null) {
			institution = new Institution();
			institution.setInstitutionName("Humanmedizin");
			institution.persist();
			institution.flush();
			institution = Institution.findInstitution(institution.getId());
		}
		
		log.info("instition created");

		for (XMLData data : xmlDataList) {
			/*if (StringUtils.isNotBlank(data.getQuestionType1()) && data.getQuestionType1().equalsIgnoreCase("E") == false) {*/
			log.error("Record index : " + data.getIndex());
			if(data.getIndex() >= 6000) {
				if(data.getIndex() >= 8000) {
					break;
				}
				if(isNotBlank(data.getQuestionText())) {
					QuestionEvent questionEvent = getQuestionEvent(data.getQuestionEvent().trim(), institution);
					QuestionType questionType = getQuestionType((data.getQuestionType2() + " " + data.getQuestionType1()).trim(), institution);
					Person author = getPerson(data.getAuthor().trim());
					Person reviewer = getPerson(data.getReviewer().trim());
					Question question = getQuestion(data, questionEvent, questionType, author, reviewer);
					List<Answer> answers = getAnswers(data, question, author, reviewer);
					
					for (AssessmentPojo pojo : data.getAssessmentPojoList()) {
						if(pojo.getAssessmentdDate() != null) {
							Assesment assesment = getAssessment(institution, pojo);
							question = updateDateOfQuestion(question,assesment);
							AssesmentQuestion assesmentQuestion = getAssessmentQuestion(pojo,question,assesment,author,reviewer);
							getAssessmentAnswers(assesmentQuestion,answers);
						}
					} 
				} else {
					log.info("Question text is blank for " + data.getIndex());
					log.info("Question Type is blank for " + data.getIndex());
					log.info("Question : "+ data.getQuestionText() +" , Question Type : "+data.getQuestionType2() + " " + data.getQuestionType1());
				}
			}
			
			
			/*}*/
		}
	}

	
	private Question updateDateOfQuestion(Question question, Assesment assesment) {
		if(defaultDateOfQuestionAdded.equals(question.getDateAdded())) {
			question.setDateAdded(assesment.getDateOfAssesment());
			question.persist();
			return Question.findQuestion(question.getId());
		}
		return question;
	}

	private void getAssessmentAnswers(AssesmentQuestion assesmentQuestion, List<Answer> answers) {
		for (int i = 0; i < answers.size(); i++) {
			Answer answer = answers.get(i);
			AnswerToAssQuestion answerToAssQuestion = new AnswerToAssQuestion();
			answerToAssQuestion.setAnswers(answer);
			answerToAssQuestion.setAssesmentQuestion(assesmentQuestion);
			answerToAssQuestion.setSortOrder(i);
			answerToAssQuestion.persist();
			answerToAssQuestion.flush();
			log.info("AnswerToAssQuestion created");
		}
	}

	private AssesmentQuestion getAssessmentQuestion(AssessmentPojo pojo, Question question, Assesment assesment, Person author, Person reviewer) {
		AssesmentQuestion assesmentQuestion = new AssesmentQuestion();
		
		assesmentQuestion.setOrderAversion(0);
		assesmentQuestion.setOrderBversion(0);
		assesmentQuestion.setTrenschaerfe(pojo.getQuestionSelectivity());
		assesmentQuestion.setSchwierigkeit(pojo.getQuestionDifficulty());
		assesmentQuestion.setIsAssQuestionAcceptedAdmin(true);
		assesmentQuestion.setIsAssQuestionAcceptedAutor(true);
		assesmentQuestion.setIsAssQuestionAcceptedRewiever(true);
		assesmentQuestion.setIsAssQuestionAdminProposal(false);
		assesmentQuestion.setIsForcedByAdmin(false);
		assesmentQuestion.setQuestion(question);
		assesmentQuestion.setDateAdded(assesment.getDateOfAssesment());
		assesmentQuestion.setDateChanged(null);
		assesmentQuestion.setAutor(author);
		assesmentQuestion.setRewiewer(reviewer);
		assesmentQuestion.setAssesment(assesment);
		assesmentQuestion.persist();
		assesmentQuestion.flush();
		log.info("AssesmentQuestion created");
		assesmentQuestion = AssesmentQuestion.findAssesmentQuestion(assesmentQuestion.getId());
		return assesmentQuestion;
	}

	private Assesment getAssessment(Institution institution, AssessmentPojo pojo) {
		Mc mc = getMc(pojo.getMc(),  "MC 6.1");
		Assesment assesment = Assesment.findAssessmentByDateAndMC(pojo.getAssessmentdDate(),mc);
		
		if(assesment == null) {
			assesment = new Assesment();
			assesment.setName(pojo.getAssessmentName());
			assesment.setRememberBeforeClosing(0);
			assesment.setDateOfAssesment(pojo.getAssessmentdDate());
			assesment.setDateOpen(pojo.getAssessmentdDate());
			assesment.setDateClosed(pojo.getAssessmentdDate());
			assesment.setPlace("Lernzentrum Medizin");
			assesment.setLogo("no_logo");
			assesment.setIsClosed(false);
			assesment.setIsClosed(false);
			assesment.setAssesmentVersion(1);
			assesment.setMc(mc);
			assesment.setRepeFor(null);
			assesment.setPercentSameQuestion(50);
			assesment.setInstitution(institution);
			assesment.persist();
			assesment.flush();
			log.info("Assesment created");
			assesment = Assesment.findAssesment(assesment.getId());
		}
	
		return assesment;
	}


	private List<Answer> getAnswers(XMLData data, Question question, Person author, Person reviewer) {

		List<Answer> answers = Lists.newArrayList();

		{
			Answer answerA = getAnswer(data.getAnswerA(), data.getAnswerAValidity(), question, author, reviewer);
			if (answerA != null) {
				answers.add(answerA);
			}
		}
		{
			Answer answerB = getAnswer(data.getAnswerB(), data.getAnswerBValidity(), question, author, reviewer);
			if (answerB != null) {
				answers.add(answerB);
			}
		}

		{
			Answer answerC = getAnswer(data.getAnswerC(), data.getAnswerCValidity(), question, author, reviewer);
			if (answerC != null) {
				answers.add(answerC);
			}
		}

		{
			Answer answerD = getAnswer(data.getAnswerD(), data.getAnswerDValidity(), question, author, reviewer);
			if (answerD != null) {
				answers.add(answerD);
			}
		}

		{
			Answer answerE = getAnswer(data.getAnswerE(), data.getAnswerEValidity(), question, author, reviewer);
			if (answerE != null) {
				answers.add(answerE);
			}
		}

		return answers;
	}
	
	private Answer getAnswer(String answerText, Validity answerValidity, Question question, Person author, Person reviewer) {

		if (isBlank(answerText)) {
			return null;
		}
		Answer answer = Answer.findAnswersByAnswerTextQuestionValidityAuthorAndReviewer(answerText,question,answerValidity,author,reviewer);
		
		if(answer == null) {
			answer = new Answer();
			answer.setAnswerText(answerText);
			answer.setQuestion(question);
			answer.setValidity(answerValidity);
			answer.setRewiewer(reviewer);
			answer.setAutor(author);
			answer.setComment("");
			answer.setCreatedBy(answer.getAutor());
			answer.setDateAdded(question.getDateAdded());
			answer.setIsMedia(false);
			answer.setIsAnswerAcceptedAdmin(true);
			answer.setIsAnswerAcceptedAutor(true);
			answer.setIsAnswerAcceptedReviewWahrer(true);
			answer.setMediaPath(null);
			answer.setSubmitToReviewComitee(false);
			answer.setPoints(null);
			answer.setAdditionalKeywords(null);
			answer.setSequenceNumber(null);
			answer.setStatus(Status.ACTIVE);
			answer.setIsForcedActive(false);
			answer.setModifiedBy(null);
		} else {
			log.info("@@@Answer Date added :  " + answer.getDateAdded() + " : new " + question.getDateAdded());
			log.info("@@@Answer Date changed :  " + answer.getDateChanged() + " : new " + question.getDateChanged());
		}
		
		answer.setDateChanged(question.getDateChanged());
		answer.persist();
		answer.flush();
		log.info("Answer created");
		answer = Answer.findAnswer(answer.getId());
		return answer;
	}

	private Question getQuestion(XMLData data, QuestionEvent questionEvent, QuestionType questionType, Person author, Person reviewer) {
		Mc questionMc = getMc(data.getQuestionMC(), "MC 6.1");

		Question question = Question.findQuestionByQuestionTextQuestionEventQuestionTypeRewiewerAndAutor(data.getQuestionText(),questionEvent,questionType,reviewer,author);
		if(question == null) {
			question = new Question();
			question.setQuestionText(data.getQuestionText());
			question.setQuestEvent(questionEvent);
			question.setQuestionType(questionType);
			question.setAutor(author);
			question.setRewiewer(reviewer);
			question.setDateAdded(data.getQuestionDateAdded());
			question.setCreatedBy(question.getAutor());
			question.setQuestionVersion(0);
			question.setQuestionSubVersion(0);
			question.setIsAcceptedAdmin(true);
			question.setIsAcceptedAuthor(true);
			question.setIsAcceptedRewiever(true);
			question.setIsForcedActive(false);
			question.setSubmitToReviewComitee(false);
			question.setPreviousVersion(null);
			question.setIsReadOnly(false);
			question.setModifiedBy(null);
		} else {
			log.info("~~~previousStatus : " + question.getStatus() + " new Status : " + data.getQuestionStatus());
			log.info("@@@ previous date add : " + question.getDateAdded() + " new date added " + data.getQuestionDateAdded());
			log.info("@@@ previous date changed : " + question.getDateChanged() + " new date changed " + data.getQuestionDateChanged());
			
			question.setQuestionSubVersion(question.getQuestionSubVersion()+1);
		}
		
		
		question.setQuestionShortName(getQuestionShortName(question.getQuestionShortName(),data.getQuestionShortText()));
		question.setKeywords(getQuestionKeywords(question.getKeywords(),getKeywords(data.getKeywords())));
		question.setComment(getQuestionComment(question.getComment(),data.getComments()));
		question.setStatus(data.getQuestionStatus());
		question.setMcs(getQuestionMCs(question.getMcs(),questionMc));
		question.setDateChanged(data.getQuestionDateChanged());
		question.persist();
		question.flush();
		
		log.info("Question created");
		question = Question.findQuestion(question.getId());
		return question;
	}

	private String getQuestionShortName(String previousQuestionShortName, String currentQuestionShortName) {
		String questionShortName = 	currentQuestionShortName;
		if(isNotBlank(previousQuestionShortName)) {
			questionShortName = previousQuestionShortName + " " + currentQuestionShortName;
		}
		return questionShortName;
	}

	private Set<Mc> getQuestionMCs(Set<Mc> previousMCs, Mc currentMc) {
		Set<Mc> newMcs = Sets.newHashSet();
		
		if(previousMCs != null) {
			newMcs.addAll(previousMCs);
		}
		if(currentMc != null) {
			newMcs.add(currentMc);
		}else {
			log.error("!!!!!!!MC is null");
		}
		
		return newMcs;
	}

	private Set<Keyword> getQuestionKeywords(Set<Keyword> previousKeywords, Set<Keyword> currentKeywords) {
		Set<Keyword> newKeywords = Sets.newHashSet();
		
		if(previousKeywords != null) {
			newKeywords.addAll(previousKeywords);
		}
		
		if(currentKeywords != null) {
			newKeywords.addAll(currentKeywords);
		}
		
		return newKeywords;
	}

	private Person getPerson(String personName) {

		ArrayList<String> nameList = Lists.newArrayList(Splitter.on(" ").split(personName));
		String name = nameList.get(0);
		String prename = nameList.size() > 1 ? nameList.get(1) : nameList.get(0);

		Person person = Person.findPersonByNameAndPrename(name, prename);

		if (person == null) {
			person = new Person();
			person.setName(name);
			person.setPrename(prename);
			person.setEmail(name.replace(" ", "_") + "@" + prename.replace(" ", "_") + ".ch");
			person.setAlternativEmail(null);
			person.setIsAccepted(true);
			person.setIsAdmin(false);
			person.setPhoneNumber("061 611 11 11");
			person.persist();
			person.flush();
			log.info("Person created");
			person = Person.findPerson(person.getId());
		}

		return person;
	}

	private String getQuestionComment(String previousComment, Set<String> comments) {
		String commentString = Joiner.on(", ").skipNulls().join(comments);
		String newComment = "";
		if(StringUtils.isNotBlank(previousComment)) {
			newComment = previousComment + " " + commentString;
		} else {
			newComment = commentString;
		}
		
		return newComment;
	}

	private Set<Keyword> getKeywords(Set<String> keywords) {
		Set<Keyword> keywordset = Sets.newHashSet();

		for (String currentKeyword : keywords) {

			if (isNotBlank(currentKeyword)) {
				
				if(currentKeyword.length() > 45) {
					currentKeyword = substring(currentKeyword, 0, 40) + "...";
				}
				
				Keyword keyword = Keyword.findKeywordByString(currentKeyword);
				if (keyword == null) {
					keyword = new Keyword();
					keyword.setName(currentKeyword);
					keyword.persist();
				}
				keywordset.add(keyword);
			}
		}
		return keywordset;
	}

	private Mc getMc(String mcValue, String defaultMcValue) {
		if (isBlank(mcValue)) {
			mcValue = defaultMcValue;
		}

		return Mc.findByName(mcValue);
	}

	private QuestionType getQuestionType(String questionTypeName, Institution institution) {

		QuestionType questionType = QuestionType.findQuestionTypeByShortName(questionTypeName, institution);

		if (questionType == null) {
			QuestionType type = new QuestionType();
			type.setShortName(questionTypeName);
			type.setLongName(questionTypeName);
			type.setDescription("");
			type.setInstitution(institution);
			type.setQuestionType(QuestionTypes.Textual);
			type.setSumAnswer(SharedConstant.INFINITE_VALUE);
			type.setSumFalseAnswer(0);
			if (questionTypeName.equals("A pos") || questionTypeName.equals("A neg")) {
				type.setSumTrueAnswer(1);
			} else {
				type.setSumTrueAnswer(0);
			}
			type.setQuestionLength(9000);
			type.setAnswerLength(5000);
			type.setDiffBetAnswer(100.0);
			type.setQueHaveImage(false);
			type.setQueHaveSound(false);
			type.setQueHaveVideo(false);
			type.persist();
			type.flush();
			log.info("QuestionType created");
			questionType = QuestionType.findQuestionType(type.getId());
		}
		return questionType;
	}

	private QuestionEvent getQuestionEvent(String questionEvent, Institution institution) {
		QuestionEvent event = QuestionEvent.findQuestionEventByName(questionEvent);
		if (event == null) {
			event = new QuestionEvent();
			event.setEventName(questionEvent);
			event.setInstitution(institution);
			event.persist();
			event.flush();
			log.info("QuestionEvent created");
			event = QuestionEvent.findQuestionEvent(event.getId());
		}
		return event;
	}

	private void xmlToPojo() {
		List<Element> data = parser.getData();
		int index = 0;
		for (Element element : data) {

			/*if() {*/
				XMLData xmlData = new XMLData();
				xmlData.setIndex(index);
				xmlData.setQuestionText(getSingleValue(element, "Frage"));
				xmlData.setAnswerA(getSingleValue(element, "A Frage"));
				xmlData.setAnswerB(getSingleValue(element, "B Frage"));
				xmlData.setAnswerC(getSingleValue(element, "C Frage"));
				xmlData.setAnswerD(getSingleValue(element, "D Frage"));
				xmlData.setAnswerE(getSingleValue(element, "E Frage"));
//				xmlData.addComment(getSingleCommentValue(element, "AAE Fach"));
//				xmlData.addComment(getSingleCommentValue(element, "AAE No"));
//				xmlData.addComment(getSingleCommentValue(element, "AAE Themenblock"));
				xmlData.addComment(getSingleCommentValue(element, "Abbildung", "has Abbildung"));
				xmlData.setAuthor(getSingleValue(element, "Autor"));
				xmlData.addComment(getSingleCommentValue(element, "Besonderes"));
				xmlData.setQuestionDateAdded(getSingleValue(element, "erfasst: Studienjahr"));
				xmlData.addKeyword(getSingleValue(element, "Fach"));
				xmlData.setQuestionStatus(getSingleValue(element, "Frage gesperrt?"));
				xmlData.setQuestionType1(getSingleValue(element, "Fragen_Typ"));
				xmlData.setQuestionType2(getSingleValue(element, "FragenTyp II"));
				xmlData.addKeyword(getSingleValue(element, "Gebiet"));
				xmlData.addKeywordSet(getMultiValues(element, "Gebiet/Untergebiet"));
				xmlData.setQuestionShortText(getSingleValue(element, "ID_Nr"));
				xmlData.addKeywordSet(getMultiValues(element, "Keywords"));
				xmlData.addKeyword(getSingleValue(element, "Lehrveranstaltung"));
				xmlData.setQuestionEvent(getSingleValue(element, "Themenblock"));
				xmlData.setAnswersValidity(getSingleValue(element, "Lösung"));
				xmlData.addComment(getSCLOItemsValues(element, "LV_Nr")); //TODO Need to change this 
				xmlData.addDateOfAssessmentList(getMultiListValues(element, "MC"));
				xmlData.addMCs(getMultiListValues(element, "MCQ"));
				xmlData.setQuestionMC(getSingleValue(element, "MCQ2"));
				xmlData.addComment(getSingleCommentValue(element, "Original"));
//				xmlData.addComment(getSingleCommentValue(element, "PathoRefNr")); 
				xmlData.setReviewer(getSingleValue(element, "Reviewer"));
//				xmlData.setQuestionDateAdded(getSingleValue(element, "Revision"));
				xmlData.setQuestionDifficultyList(getMultiListValues(element, "Schwierigkeit"));
				xmlData.setQuestionSelectivityList(getMultiListValues(element, "Trennschärfe"));
				xmlData.addComment(getSingleCommentValue(element, "Sonderzeichen", "!!!Sonderzeichen"));
//				xmlData.setQuestionSpecialCharFlag(getSingleBooleanValue(element,"Sonderzeichen"));
				xmlData.setQuestionDateChanged(getSingleValue(element, "Status: seit wann"));
				xmlData.addComment(getSingleCommentValue(element, "Status: welcher"));
				xmlData.addComment(getSingleCommentValue(element, "Thema"));
//				xmlData.addComment(getSingleCommentValue(element,"Themenblock"));
				xmlData.createAssessmentInfo();
				xmlDataList.add(xmlData);
			/*}*/
			
			index++;
		}
	}

	private String getSCLOItemsValues(Element element, String key) {
		String scloItem = "SCLO item = ";
		List<String> values = getMultiListValues(element, key);
		String finalValue = Joiner.on(",").skipNulls().join(values);

		if (StringUtils.isNotBlank(finalValue)) {
			return scloItem + finalValue;
		}

		return "";
	}

	private String getSingleCommentValue(Element element, String key, String defaultKey) {
		String value = getSingleValue(element, key);
		if (StringUtils.isNotBlank(value)) {
			return defaultKey + " = " + value;
		}
		return "";
	}

	private String getSingleCommentValue(Element element, String key) {
		return getSingleCommentValue(element, key, key);
	}

	private List<String> getMultiListValues(Element element, String key) {
		return Lists.newArrayList(getMultiIterablesValues(element, key));
	}

	private Set<String> getMultiValues(Element element, String key) {
		return Sets.newHashSet(getMultiIterablesValues(element, key));
	}

	private Iterable<String> getMultiIterablesValues(Element element, String key) {
		FluentIterable<String> iterable = FluentIterable.from(parser.getField(element, key)).transform(ELEMENT_TO_STRING);
		List<String> list = Lists.newArrayList(iterable);
		//log.info(String.format("%50s : %s", key,Joiner.on(", ").join(list)));
		return iterable;
	}

	private Long getSingleLongValue(Element element, String key) {
		String value = getSingleValue(element, key);
		if (isNumber(value)) {
			return Long.parseLong(value);
		}
		return null;
	}

	private boolean getSingleBooleanValue(Element element, String key) {
		String value = getSingleValue(element, key);
		if (isNotBlank(value) && value.trim().equalsIgnoreCase("ja")) {
			return false;
		}
		return true;
	}

	private String getSingleValue(Element element, String key) {
		String text = parser.getField(element, key).get(0).getText();
		//log.info(String.format("%50s : %s", key,text));
		return text;
	}
}
