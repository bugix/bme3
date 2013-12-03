package medizin.server.utils.importdata;

import static com.google.common.base.Objects.toStringHelper;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import medizin.shared.Status;
import medizin.shared.Validity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class XMLData {

	private static Logger log = Logger.getLogger(XMLData.class);
	private int index;
	private String questionText;
	private String answerA;
	private String answerB;
	private String answerC;
	private String answerD;
	private String answerE;
	private Set<String> comments = Sets.newHashSet();
	private String author;
	private Set<String> keywords = Sets.newHashSet();
	private Status questionStatus;
	private String questionType1;
	private String questionType2;
	private String questionShortText;
	private String questionEvent;
	private List<String> dateOfAssessment = Lists.newArrayList();
	private List<String> MC = Lists.newArrayList();
	private String questionMC;
	private String reviewer;
	private Date questionDateAdded;
	private List<String> questionDifficultyList = Lists.newArrayList();
	private List<String> questionSelectivityList = Lists.newArrayList();
	private Date questionDateChanged;
	private List<AssessmentPojo> assessmentPojoList = Lists.newArrayList();
	private Validity answerAValidity;
	private Validity answerBValidity;
	private Validity answerCValidity;
	private Validity answerDValidity;
	private Validity answerEValidity;

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswerA() {
		return answerA;
	}

	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	public String getAnswerB() {
		return answerB;
	}

	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	public String getAnswerC() {
		return answerC;
	}

	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	public String getAnswerD() {
		return answerD;
	}

	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	public String getAnswerE() {
		return answerE;
	}

	public void setAnswerE(String answerE) {
		this.answerE = answerE;
	}

	public Set<String> getComments() {
		return comments;
	}

	public void addComment(String comment) {
		comments.add(comment);
	}

	public void addCommentSet(Set<String> commentSet) {
		comments.addAll(commentSet);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if (isBlank(author)) {
			this.author = "unknown author";
		} else {
			this.author = author;
		}
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	public void addKeywordSet(Set<String> keywordSet) {
		keywords.addAll(keywordSet);
	}

	public void setQuestionStatus(String isQuestionNotActive) {
		if (isNotBlank(isQuestionNotActive) && isQuestionNotActive.trim().equalsIgnoreCase("ja")) {
			this.questionStatus = Status.DEACTIVATED;
		} else {
			this.questionStatus = Status.ACTIVE;
		}
	}
	
	public Status getQuestionStatus() {
		return questionStatus;
	}
	
	public String getQuestionType1() {
		return questionType1;
	}

	public void setQuestionType1(String questionType1) {
		this.questionType1 = questionType1;
	}

	public String getQuestionType2() {
		return questionType2;
	}

	public void setQuestionType2(String questionType2) {
		this.questionType2 = questionType2;
	}

	public String getQuestionShortText() {
		return questionShortText;
	}

	public void setQuestionShortText(String questionShortText) {
		if (StringUtils.isNotBlank(questionShortText)) {
			this.questionShortText = "ID Nummer alt =" + questionShortText;
		} else {
			this.questionShortText = "";
		}

	}

	public String getQuestionEvent() {
		return questionEvent;
	}

	public void setQuestionEvent(String questionEvent) {
		if (isBlank(questionEvent)) {
			this.questionEvent = "unknown question event";
		} else {
			this.questionEvent = questionEvent;
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setAnswersValidity(String answersValidity) {
		this.answerAValidity = getAnswerValidity(answersValidity, "A",0);
		this.answerBValidity = getAnswerValidity(answersValidity, "B",1);
		this.answerCValidity = getAnswerValidity(answersValidity, "C",2);
		this.answerDValidity = getAnswerValidity(answersValidity, "D",3);
		this.answerEValidity = getAnswerValidity(answersValidity, "E",4);
		
		log.info("Solution string : " + answersValidity);
		log.info("Answer A Validity : " + this.answerAValidity);
		log.info("Answer B Validity : " + this.answerBValidity);
		log.info("Answer C Validity : " + this.answerCValidity);
		log.info("Answer D Validity : " + this.answerDValidity);
		log.info("Answer E Validity : " + this.answerEValidity);
	}

	public Validity getAnswerAValidity() {
		return answerAValidity;
	}
	
	public Validity getAnswerBValidity() {
		return answerBValidity;
	}
	
	public Validity getAnswerCValidity() {
		return answerCValidity;
	}
	
	public Validity getAnswerDValidity() {
		return answerDValidity;
	}
	
	public Validity getAnswerEValidity() {
		return answerEValidity;
	}
	
	public List<String> getDateOfAssessment() {
		return dateOfAssessment;
	}

	public void addDateOfAssessmentList(List<String> dateOfAssessmentList) {
		dateOfAssessment.addAll(dateOfAssessmentList);
	}

	public List<String> getMC() {
		return MC;
	}

	public void addMCs(List<String> mcList) {
		MC.addAll(mcList);
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		if (isBlank(reviewer)) {
			this.reviewer = "unknown reviewer";
		} else {
			this.reviewer = reviewer;
		}
	}

	public Date getQuestionDateAdded() {
		return questionDateAdded;
	}

	public void setQuestionDateAdded(String questionDateAdded) {

		if (isNotBlank(questionDateAdded)) {
			int year = Integer.parseInt(questionDateAdded, 10);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
			this.questionDateAdded = calendar.getTime();
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
			this.questionDateAdded = calendar.getTime();
		}
		
		log.info("question date Added : " + questionDateAdded + ", " + this.questionDateAdded);

	}

	public List<String> getQuestionDifficultys() {
		return questionDifficultyList;
	}

	public void setQuestionDifficultyList(List<String> questionDifficultys) {
		this.questionDifficultyList.addAll(questionDifficultys);
	}

	public void setQuestionSelectivityList(List<String> questionSelectivityList) {
		this.questionSelectivityList = questionSelectivityList;
	}

	public List<String> getQuestionSelectivityList() {
		return questionSelectivityList;
	}

	public Date getQuestionDateChanged() {
		return questionDateChanged;
	}

	public void setQuestionDateChanged(String questionDateChanged) {
		this.questionDateChanged = getDate(questionDateChanged);
		log.info("question date changed : " + questionDateChanged + ", " + this.questionDateChanged);
	}

	public String getQuestionMC() {
		return questionMC;
	}

	public void setQuestionMC(String questionMC) {
		if (isBlank(questionMC)) {
			this.questionMC = "MC 6.1";
		} else {
			this.questionMC = getMCValue(questionMC);
		}
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("questionText", questionText)
				.add("answerA", answerA)
				.add("answerB", answerB)
				.add("answerC", answerC)
				.add("answerD", answerD)
				.add("answerE", answerE)
				.add("comments", comments)
				.add("author", author)
				.add("keywords", keywords)
				.add("isQuestionActive", questionStatus)
				.add("questionType1", questionType1)
				.add("questionType2", questionType2)
				.add("previousQuestionID", questionShortText)
				.add("questionEvent", questionEvent)
				.add("answersAValidity", answerAValidity)
				.add("answersBValidity", answerBValidity)
				.add("answersCValidity", answerCValidity)
				.add("answersDValidity", answerDValidity)
				.add("answersEValidity", answerEValidity)
				.add("dateOfAssessment", dateOfAssessment)
				.add("MC", MC)
				.add("reviewer", reviewer)
				.add("questionDateAdded", questionDateAdded)
				.add("questionDifficultys", questionDifficultyList)
				.add("questionDateChanged", questionDateChanged)
				.add("assessmentPojoList", assessmentPojoList)
				.toString();
	}

	public void createAssessmentInfo() {
		for (int index = 0; index < 12; index++) {
			AssessmentPojo assessmentPojo = new AssessmentPojo();
			assessmentPojo.assessmentName = "imported";//"Assesment " + dateOfAssessment.get(index);
			assessmentPojo.assessmentdDate = getDate(dateOfAssessment.get(index));
			assessmentPojo.mc = getMCValue(MC.get(index));
			assessmentPojo.questionDifficulty = getDoubleValue(questionDifficultyList.get(index));
			assessmentPojo.questionSelectivity = getDoubleValue(questionSelectivityList.get(index));
			
			if(assessmentPojo.assessmentdDate != null || StringUtils.isNotBlank(dateOfAssessment.get(index))) 
				log.info("Assessment Date 	  : " + dateOfAssessment.get(index) + " , " + assessmentPojo.assessmentdDate);	
			
			
			if(isNotBlank(MC.get(index)) || isNotBlank(assessmentPojo.mc)) 
				log.info("MC 			  	  : " + MC.get(index) + " , " + assessmentPojo.mc);	
			
			
			if(isNotBlank(questionDifficultyList.get(index)) || assessmentPojo.questionDifficulty != null) 
				log.info("questionDifficulty  : " +questionDifficultyList.get(index) + " , " + assessmentPojo.questionDifficulty);
			
			if(isNotBlank(questionSelectivityList.get(index)) || assessmentPojo.questionSelectivity != null)
				log.info("questionSelectivity : " +questionSelectivityList.get(index) + " , " + assessmentPojo.questionSelectivity);
			
			this.assessmentPojoList.add(assessmentPojo);
		}
		log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	private Double getDoubleValue(String value) {

		if (StringUtils.isNotBlank(value)) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				log.error("Double value : " + value);
			}

		}
		return null;
	}

	public static String getMCValue(String value) {
		if (NumberUtils.isNumber(value)) {
			int mcInt = Integer.parseInt(value, 10);
			String mcString;
			switch (mcInt) {
			case 11:
				mcString = "MC 1.1";
				break;
			case 12:
				mcString = "MC 1.2";
				break;
			case 21:
				mcString = "MC 2.1";
				break;
			case 22:
				mcString = "MC 2.2";
				break;
			case 31:
				mcString = "MC 3.1";
				break;
			case 32:
				mcString = "MC 3.2";
				break;
			case 41:
				mcString = "MC 4.1";
				break;
			case 42:
				mcString = "MC 4.2";
				break;
			case 51:
				mcString = "MC 5.1";
				break;
			case 52:
				mcString = "MC 5.2";
				break;
			case 61:
				mcString = "MC 6.1";
				break;
			case 62:
				mcString = "MC 6.2";
				break;

			default:
				mcString = null;
				break;
			}
			return mcString;
		}
		return null;
	}

	public static Date getDate(String value) {
		String[] monthYear = StringUtils.split(value, ".");

		if (monthYear != null && monthYear.length == 2) {
			int month = Integer.parseInt(monthYear[0], 10);
			int year = Integer.parseInt(monthYear[1], 10) + 2000;
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, 1, 0, 0, 0);
			return calendar.getTime();
		} else {
			return null;
		}
	}

	private Validity getAnswerValidity(String answersValidity, String answerLetter, int index) {

		String questionType = (getQuestionType2() + " " + getQuestionType1()).trim();
		if(questionType.equalsIgnoreCase("K prim") ||questionType.startsWith("Kprim")) {
			 String bracketContent = answersValidity.substring(answersValidity.indexOf("(") + 1);
			 if(bracketContent.length() > index && bracketContent.charAt(index) == '+') {
				   return Validity.Wahr;
			 } else {
				   return Validity.Falsch;
			 }
		} else if (startsWithIgnoreCase(answersValidity, "A") == true || startsWithIgnoreCase(answersValidity, "B") == true || startsWithIgnoreCase(answersValidity, "C") == true || startsWithIgnoreCase(answersValidity, "D") == true || startsWithIgnoreCase(answersValidity, "E") == true) {
			if(startsWithIgnoreCase(answersValidity, answerLetter) == true) {
				return Validity.Wahr;	
			}else {
				return Validity.Falsch;
			}
			
		} else if(contains(answersValidity, "(")){
			 String bracketContent = answersValidity.substring(answersValidity.indexOf("(") + 1);
			 if(bracketContent.length() > index && bracketContent.charAt(index) == '+') {
				   return Validity.Wahr;
			 } else {
				   return Validity.Falsch;
			 }
		}

		return Validity.Falsch;
	}

	public List<AssessmentPojo> getAssessmentPojoList() {
		return assessmentPojoList;
	}

	class AssessmentPojo {
		private String assessmentName;
		private Date assessmentdDate;
		private String mc;
		private Double questionDifficulty;
		private Double questionSelectivity;

		public String getAssessmentName() {
			return assessmentName;
		}

		public Date getAssessmentdDate() {
			return assessmentdDate;
		}

		public String getMc() {
			return mc;
		}

		public Double getQuestionDifficulty() {
			return questionDifficulty;
		}

		public Double getQuestionSelectivity() {
			return questionSelectivity;
		}

		@Override
		public String toString() {
			return toStringHelper(this).add("assessmentdDate", assessmentdDate).add("mc", mc).add("questionDifficulty", questionDifficulty).add("questionSelectivity", questionSelectivity).toString();
		}
	}
}
