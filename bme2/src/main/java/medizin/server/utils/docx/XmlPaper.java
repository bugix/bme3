package medizin.server.utils.docx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import medizin.server.domain.Answer;
import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.Assesment;
import medizin.server.domain.MatrixValidity;
import medizin.server.domain.Person;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionResource;
import medizin.server.domain.QuestionType;
import medizin.server.domain.Student;
import medizin.server.domain.StudentToAssesment;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class XmlPaper {
		
	private static final Logger log = Logger.getLogger(XmlPaper.class);
	private static final String EXTENSION_XML = ".xml";
	private static final String EXTENSION_ZIP = ".zip";
	private final ByteArrayOutputStream os;
	private final ByteArrayOutputStream xmlOS = new ByteArrayOutputStream();
	private final Integer assessment;
	private String zipfileName = "Paper.zip";
	private String xmlfileName = "Paper.xml";
	private List<QuestionVO> questionVOs = Lists.newArrayList();
	private final Set<String> mediaPath = Sets.newHashSet();
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public XmlPaper(ByteArrayOutputStream os, Integer assessment, Person loggedPerson) {
		this.os = os;
		this.assessment = assessment;
		zipfileName = PaperUtils.getDocumentName(assessment.longValue(), null,loggedPerson.getShidId(), EXTENSION_ZIP);
		xmlfileName = PaperUtils.getDocumentName(assessment.longValue(), null,loggedPerson.getShidId(), EXTENSION_XML);
		log.info("fileName : " + zipfileName);
		log.info("fileName : " + xmlfileName);
	}
	
	public String getFileName() {
		return zipfileName;
	}
	
	private String getXMLFileName() {
		return xmlfileName;
	}
	
	public void createXMLFile() {
	
		questionVOs = PaperUtils.get_data(assessment.longValue(),true,false);
		
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            
            Element rootElement = doc.createElementNS("", ROOT);
            doc.appendChild(rootElement);
            
            {//for assessment
            	Assesment assessmentObject = Assesment.findAssesment(assessment.longValue());
            	Element assessmentRootElement = doc.createElement(ASSESSMENT_ROOT);
            	rootElement.appendChild(assessmentRootElement);
	            
	            createAssessment(doc, assessmentRootElement ,assessmentObject);
            }
            
            { // for question
	            Element questionsElement = doc.createElement(QUESTION_ROOT);
	            rootElement.appendChild(questionsElement);
	            
	            // append child elements to root element
	            for (QuestionVO question : questionVOs) {   
	              	questionsElement.appendChild(addQuestion(doc,question));	
				}
            }
            
            {//for student
            	List<Student> students = StudentToAssesment.findStudentsByAssesment(assessment.longValue(), Boolean.TRUE);
            	Element studentRootElement = doc.createElement(STUDENT_ROOT);
	            rootElement.appendChild(studentRootElement);
	            
	            createStudents(doc, studentRootElement , students);
            }
            // output DOM XML to console 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(xmlOS);
            transformer.transform(source, console);
 
            log.info("\nXML DOM Created Successfully..");
 
            createZipFile(mediaPath, os ,xmlOS);
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
	}

	private Node addQuestion(Document doc, QuestionVO question) {
		Element questionElement = doc.createElement(QUESTION_ELEMENT);
        questionElement.setAttribute(ID_ATTRIBUTE, question.getAssesmentQuestion().getId().toString());
        questionElement.setAttribute(QUESTION_ID_ATTRIBUTE, question.getAssesmentQuestion().getQuestion().getId().toString());
        questionElement.appendChild(getQuestionType(doc, question.getAssesmentQuestion().getQuestion().getQuestionType()));
        addToElement(doc, questionElement, TEXT_ELEMENT, question.getAssesmentQuestion().getQuestion().getQuestionText());
        addQuestionEvent(doc,question,questionElement);
        Element questionResources = addQuestionResources(doc,question);
        if(questionResources != null) {
        	questionElement.appendChild(questionResources);
        }
        if(QuestionTypes.Drawing.equals(question.getAssesmentQuestion().getQuestion().getQuestionType().getQuestionType()) ==  false) {
        	questionElement.appendChild(getAnswers(doc, question.getAnswerToAssQuestions(),question.getAssesmentQuestion().getQuestion()));	
        }
        
        return questionElement;
	}

	private void addQuestionEvent(Document doc, QuestionVO question, Element questionElement) {
		
		Element questionEventElement = doc.createElement(QUESTION_EVENT_NAME);
		questionEventElement.appendChild(doc.createCDATASection(StringUtils.defaultString(question.getAssesmentQuestion().getQuestion().getQuestEvent().getEventName(), EMPTY_VALUE)));
		questionEventElement.setAttribute(ID_ATTRIBUTE, question.getAssesmentQuestion().getQuestion().getQuestEvent().getId().toString());
		questionElement.appendChild(questionEventElement);
		
		
		
	}

	private Element addQuestionResources(Document doc, QuestionVO question) {
		Set<QuestionResource> questionResources = question.getAssesmentQuestion().getQuestion().getQuestionResources();
		if(questionResources != null && questionResources.isEmpty() == false) {
			Element questionResource = doc.createElement(QUESTION_RESOURCES);
			for (QuestionResource resource : questionResources) {
				questionResource.appendChild(addResourceElement(doc, resource));
			}
			return questionResource;
		}
		return null;
	}

	private Element addResourceElement(Document doc,  QuestionResource resource) {
		Element questionMedia = doc.createElement(QUESTION_MEDIA);
		questionMedia.setAttribute(ID_ATTRIBUTE, resource.getId().toString());
		addToElement(doc, questionMedia, QUESTION_MEDIA_PATH, resource.getPath());
		addToElement(doc, questionMedia, QUESTION_MEDIA_NAME, resource.getName());
		addToElement(doc, questionMedia, QUESTION_MEDIA_SEQUENCE, resource.getSequenceNumber());
		addToElement(doc, questionMedia, QUESTION_MEDIA_TYPE, resource.getType().toString().toLowerCase()).setAttribute(ID_ATTRIBUTE, String.valueOf(resource.getType().ordinal()));
		
		if(resource.getType().equals(MultimediaType.Image) && resource.getImageHeight() != null && resource.getImageWidth() != null) {
			addToElement(doc, questionMedia, QUESTION_MEDIA_WIDTH, resource.getImageWidth());
			addToElement(doc, questionMedia, QUESTION_MEDIA_HEIGHT, resource.getImageHeight());
		}
		
		mediaPath.add(resource.getPath());
		return questionMedia;
	}

	private Node getQuestionType(Document doc, QuestionType questionType) {
		Element questionTypeElement = doc.createElement(QUESTION_TYPE_ELEMENT);
		questionTypeElement.setAttribute(ID_ATTRIBUTE, questionType.getId().toString());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_NAME_ELEMENT, questionType.getQuestionType().name().toLowerCase()).setAttribute(ID_ATTRIBUTE, String.valueOf(questionType.getQuestionType().ordinal()));
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SHORT_NAME,questionType.getShortName());   
		addToElement(doc, questionTypeElement, QUESTION_TYPE_LONG_NAME,questionType.getLongName());     
		addToElement(doc, questionTypeElement, QUESTION_TYPE_DESCRIPTION,questionType.getDescription());
		
		if(QuestionTypes.Textual.equals(questionType.getQuestionType())) {
			textualQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.Sort.equals(questionType.getQuestionType())) {
			sortQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.Matrix.equals(questionType.getQuestionType())) {
			matrixQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.MCQ.equals(questionType.getQuestionType())) {
			mcqQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.Imgkey.equals(questionType.getQuestionType())) {
			imageKeyQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.ShowInImage.equals(questionType.getQuestionType())) {
			showInImageQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.LongText.equals(questionType.getQuestionType())) {
			longTextQuestionType(doc, questionTypeElement,questionType);
		} else if(QuestionTypes.Drawing.equals(questionType.getQuestionType())) {
			drawingQuestionType(doc, questionTypeElement,questionType);
		}
		
		return questionTypeElement;
	}

	private void drawingQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());			
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_IMAGE,questionType.getQueHaveImage());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_AUDIO,questionType.getQueHaveSound());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_VIDEO,questionType.getQueHaveVideo());
		
	}

	private void longTextQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_ALLOW_RICH_TEXT,questionType.getRichText());          
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MIN_LENGTH,questionType.getMinLength());              			
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_LENGTH,questionType.getMaxLength());              
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MIN_WORD_LENGTH,questionType.getMinWordCount());      
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_WORD_LENGTH,questionType.getMaxWordCount());      
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());			
		addToElement(doc, questionTypeElement, QUESTION_TYPE_KEYWORD_HIGHLIGHT,questionType.getKeywordHighlight());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_IMAGE,questionType.getQueHaveImage());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_AUDIO,questionType.getQueHaveSound());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_VIDEO,questionType.getQueHaveVideo());
	}

	private void showInImageQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());
	}

	private void imageKeyQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());         
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_ANSWER_LENGTH,questionType.getAnswerLength());             			
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SHORT_ANSWER_LENGTH,questionType.getLengthShortAnswer());      
		addToElement(doc, questionTypeElement, QUESTION_TYPE_KEYWORD_COUNT,questionType.getKeywordCount());                 
		addToElement(doc, questionTypeElement, QUESTION_TYPE_AUTOCOMPLETE,questionType.getShowAutocomplete());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_IS_DICTIONARY_KEYWORD, questionType.getIsDictionaryKeyword());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_ALLOW_TYPING, questionType.getAllowTyping());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MIN_AUTOCOMPLETE_LETTER, questionType.getMinAutoCompleteLetter());
		addToElement(doc, questionTypeElement, QUESTION_TYPE_ACCEPT_NON_KEYWORD, questionType.getAcceptNonKeyword());
	}

	private void mcqQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_COLUMNS,questionType.getColumns());                                        
		addToElement(doc, questionTypeElement, QUESTION_TYPE_ALLOW_RICH_TEXT,questionType.getRichText());                               	
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());                     
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MULTIMEDIA_TYPE,questionType.getMultimediaType().toString().toLowerCase()).setAttribute(ID_ATTRIBUTE, String.valueOf(questionType.getMultimediaType().ordinal()));
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SELECTION_TYPE,questionType.getSelectionType().toString().toLowerCase()).setAttribute(ID_ATTRIBUTE, String.valueOf(questionType.getSelectionType().ordinal()));;  
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_BYTES,questionType.getMaxBytes());                                     
	}

	private void matrixQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength());                     
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_ANSWER_LENGTH,questionType.getAnswerLength());                        	
		addToElement(doc, questionTypeElement, QUESTION_TYPE_ALLOW_ONE_TO_ONE,questionType.getAllowOneToOneAss());
	}

	private void sortQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SUM_ANSWER,questionType.getSumAnswer());               
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SUM_TRUE_ANSWER,questionType.getSumTrueAnswer());      	
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SUM_FALSE_ANSWER,questionType.getSumFalseAnswer());    
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_QUESTION_LENGTH,questionType.getQuestionLength()); 
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_ANSWER_LENGTH,questionType.getAnswerLength());     
		addToElement(doc, questionTypeElement, QUESTION_TYPE_MAX_ANSWER_DIFFERENCE,questionType.getDiffBetAnswer());	
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_IMAGE,questionType.getQueHaveImage());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_AUDIO,questionType.getQueHaveSound());            
		addToElement(doc, questionTypeElement, QUESTION_TYPE_HAVE_VIDEO,questionType.getQueHaveVideo());		
	}
	
	private void textualQuestionType(Document doc, Element questionTypeElement, QuestionType questionType) {
		sortQuestionType(doc, questionTypeElement, questionType);
		addToElement(doc, questionTypeElement, QUESTION_TYPE_SHOW_FILTER_DIALOG,questionType.getShowFilterDialog());
	}

	private Node getAnswers(Document doc, List<AnswerToAssQuestion> answerToAssQuestions, Question question) {
		Element answersElement = doc.createElement(ANSWERS_ELEMENT);
		if(question.getQuestionType().getQuestionType().equals(QuestionTypes.Matrix)) {
			addMatrixAnswer(doc,answersElement, answerToAssQuestions);
		}else {
			for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
				answersElement.appendChild(getAnswer(doc,answerToAssQuestion));
			}	
		}
        return answersElement;
	}

	private void addMatrixAnswer(Document doc, Element answersElement, List<AnswerToAssQuestion> answerToAssQuestions) {
		List<Long> answerList = PaperUtils.getAnswerIdList(answerToAssQuestions);
	    List<MatrixValidity> matrixValidities = MatrixValidity.findallMatrixValidityForAnswers(answerList);
	    Map<Long,Long> answerToAssMap = getAnswerToAnsAssID(answerToAssQuestions);
	    
	    for (MatrixValidity matrixValidity : matrixValidities) {
	    	Element matrixElement = doc.createElement(ANSWER_MATRIX);
	    	
	    	Element answerXElement = doc.createElement(MATRIX_ANSWERX);
	    	if(answerToAssMap.containsKey(matrixValidity.getAnswerX().getId())) {
	    		answerXElement.setAttribute(ID_ATTRIBUTE, answerToAssMap.get(matrixValidity.getAnswerX().getId()).toString());	
	    	}
	    	answerXElement.setAttribute(ANSWER_ID_ATTRIBUTE, matrixValidity.getAnswerX().getId().toString());
	    	addToElement(doc, answerXElement, TEXT_ELEMENT, matrixValidity.getAnswerX().getAnswerText());
	    	matrixElement.appendChild(answerXElement);
	    	
	    	Element answerYElement = doc.createElement(MATRIX_ANSWERY);
	    	
	    	if(answerToAssMap.containsKey(matrixValidity.getAnswerY().getId())) {
	    		answerYElement.setAttribute(ID_ATTRIBUTE, answerToAssMap.get(matrixValidity.getAnswerY().getId()).toString());	
	    	}
	    	
	    	answerYElement.setAttribute(ANSWER_ID_ATTRIBUTE, matrixValidity.getAnswerY().getId().toString());
	    	addToElement(doc, answerYElement, TEXT_ELEMENT, matrixValidity.getAnswerY().getAnswerText());
	    	matrixElement.appendChild(answerYElement);
	    	addToElement(doc, matrixElement, ANSWER_VALIDITY, matrixValidity.getValidity());
	    	
	    	matrixElement.setAttribute(ID_ATTRIBUTE, matrixValidity.getId().toString());
	    	answersElement.appendChild(matrixElement);
		}
	}

	private Map<Long, Long> getAnswerToAnsAssID(List<AnswerToAssQuestion> answerToAssQuestions) {
		Map<Long,Long> answerToAssMap = Maps.newHashMap();
		for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
			answerToAssMap.put(answerToAssQuestion.getAnswers().getId(), answerToAssQuestion.getId());
		}
		return answerToAssMap;
	}

	private Node getAnswer(Document doc, AnswerToAssQuestion answerToAssQuestion) {
		Element answerElement = doc.createElement(ANSWER_ELEMENT);
        Answer answer = answerToAssQuestion.getAnswers();
        answerElement.setAttribute(ID_ATTRIBUTE, answerToAssQuestion.getId().toString());
        answerElement.setAttribute(ANSWER_ID_ATTRIBUTE, answer.getId().toString());
		addToElement(doc, answerElement, TEXT_ELEMENT, answer.getAnswerText());
        
        if(answer.getValidity() != null) {
        	addToElement(doc, answerElement, ANSWER_VALIDITY,answer.getValidity());
        }
        
        if(answer.getMediaPath() != null) {
        	addToElement(doc, answerElement, ANSWER_MEDIA_PATH,answer.getMediaPath());
        	mediaPath.add(answer.getMediaPath());
        }
        
        if(answer.getPoints() != null) {
        	addToElement(doc, answerElement, ANSWER_POINTS,answer.getPoints());
        }
        
        if(answer.getAdditionalKeywords() != null) {
        	addToElement(doc, answerElement, ANSWER_ADDITIONAL_KEYWORDS,answer.getAdditionalKeywords());
        }
        
        if(answer.getSequenceNumber() != null) {
        	addToElement(doc, answerElement, ANSWER_SEQUENCE,answer.getSequenceNumber());
        }
        
        
		return answerElement;
	}
	
	private void createZipFile(Set<String> fileNameList, OutputStream os, ByteArrayOutputStream xmlstream) {

		try {
			ZipOutputStream zipOut = new ZipOutputStream(os);
			zipOut.putNextEntry(new ZipEntry(getXMLFileName()));
			zipOut.write(xmlstream.toByteArray());
			zipOut.closeEntry();
			
			for (String fileName : fileNameList) {

				byte[] buf = new byte[1024];
				int len;
				File file = new File(SharedConstant.getUploadBaseDIRPath() + fileName);
				FileInputStream in = null;
				if (file.exists()) {
					in = new FileInputStream(file);
					zipOut.putNextEntry(new ZipEntry(fileName));
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					in.close();
				}
				zipOut.closeEntry();
			}
			zipOut.close();

			log.info("Done...");

		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private void createStudents(Document doc, Element studentRootElement, List<Student> students) {
		log.info("in create students");
		
		for (Student student : students) {
			Element studentElement = doc.createElement(STUDENT_ELEMENT);
			studentRootElement.appendChild(studentElement);
			
			studentElement.setAttribute(ID_ATTRIBUTE,String.valueOf(student.getId()));
			addToElement(doc,studentElement,STUDENT_ID_ELEMENT,student.getStudentId());
			addToElement(doc,studentElement,STUDENT_PRENAME_ELEMENT,student.getPreName());
			addToElement(doc,studentElement,STUDENT_NAME_ELEMENT,student.getName());
			addToElement(doc,studentElement,STUDENT_EMAIL_ELEMENT,student.getEmail());
			
			if(student.getGender() != null) {
				addToElement(doc,studentElement,STUDENT_GENDER_ELEMENT,student.getGender().toString().toLowerCase()).setAttribute(ID_ATTRIBUTE, String.valueOf(student.getGender().ordinal()));	
			} else {
				addToElement(doc,studentElement,STUDENT_GENDER_ELEMENT,EMPTY_VALUE);
			}
			
			addToElement(doc,studentElement,STUDENT_STREET_ELEMENT,student.getStreet());
			addToElement(doc,studentElement,STUDENT_CITY_ELEMENT,student.getCity());
		}
		
	}

	private Element addToElement(Document doc, Element parentElement, String key, String value) {
		Element element = doc.createElement(key);
		element.appendChild(doc.createCDATASection(StringUtils.defaultString(value, EMPTY_VALUE)));
		parentElement.appendChild(element);
		return element;
	}

	private Element addToElement(Document doc, Element parentElement, String key, Long value) {
		Element element = doc.createElement(key);
		element.setTextContent(value != null ? value.toString() : EMPTY_VALUE);
		parentElement.appendChild(element);
		return element;
	}

	private Element addToElement(Document doc, Element parentElement, String key, Integer value) {
		Element element = doc.createElement(key);
		element.setTextContent(value != null ? value.toString() : EMPTY_VALUE);
		parentElement.appendChild(element);
		return element;
	}
	
	private Element addToElement(Document doc, Element parentElement, String key, Double value) {
		Element element = doc.createElement(key);
		element.setTextContent(value != null ? value.toString() : EMPTY_VALUE);
		parentElement.appendChild(element);
		return element;
	}
	
	private Element addToElement(Document doc, Element parentElement, String key, Date value) {
		Element element = doc.createElement(key);
		element.setTextContent(value != null ? FORMAT.format(value) : EMPTY_VALUE);
		parentElement.appendChild(element);
		return element;
	}
	
	private Element addToElement(Document doc, Element parentElement, String key, Boolean value) {
		Element element = doc.createElement(key);
		element.setTextContent(value != null ? value.toString() : EMPTY_VALUE);
		parentElement.appendChild(element);
		return element;
	}
	
	private Element addToElement(Document doc, Element parentElement, String key, Validity value) {
		if(value != null && Validity.Wahr.equals(value)== true) {
			return addToElement(doc, parentElement, key, true);
		} else if (value != null && Validity.Falsch.equals(value)== true){
			return addToElement(doc, parentElement, key, false);
		} else {
			return addToElement(doc, parentElement, key, EMPTY_VALUE);	
		}
	}
	
	private void createAssessment(Document doc, Element assessmentRootElement, Assesment assessment) {
		log.info("in create assessment");
		
		assessmentRootElement.setAttribute(ID_ATTRIBUTE, assessment.getId().toString());
		addToElement(doc, assessmentRootElement, ASSESSMENT_NAME_ELEMENT, assessment.getName());
		addToElement(doc, assessmentRootElement, ASSESSMENT_LOGO_ELEMENT, assessment.getLogo());
		addToElement(doc, assessmentRootElement, ASSESSMENT_PLACE_ELEMENT, assessment.getPlace());
		addToElement(doc, assessmentRootElement, ASSESSMENT_MC_ELEMENT, assessment.getMc().getMcName()).setAttribute(ID_ATTRIBUTE, String.valueOf(assessment.getMc().getId()));
		addToElement(doc, assessmentRootElement, ASSESSMENT_VERSION_ELEMENT, assessment.getAssesmentVersion());
		addToElement(doc, assessmentRootElement, ASSESSMENT_DATE_OF_ASSESSMENT_ELEMENT, assessment.getDateOfAssesment());
		addToElement(doc, assessmentRootElement, ASSESSMENT_PERCENT_SAME_QUESTION_ELEMENT, assessment.getPercentSameQuestion());
		
		if(assessment.getRepeFor() != null) {
			addToElement(doc, assessmentRootElement, ASSESSMENT_REPEAT_FOR_ID_ELEMENT, assessment.getRepeFor().getId());	
		} else {
			addToElement(doc, assessmentRootElement, ASSESSMENT_REPEAT_FOR_ID_ELEMENT, EMPTY_VALUE);
		}
		addToElement(doc, assessmentRootElement, ASSESSMENT_INSTITUTION_ID_ELEMENT, assessment.getInstitution().getId());
		
	}

	private static final String ANSWER_ELEMENT = "answer";
	private static final String ANSWERS_ELEMENT = "answers";
	private static final String QUESTION_ELEMENT = "question";
	private static final String TEXT_ELEMENT = "text";
	private static final String ID_ATTRIBUTE = "id";
	private static final String QUESTION_ROOT = "questions";
	private static final String QUESTION_ID_ATTRIBUTE = "questionid";
	private static final String QUESTION_TYPE_ELEMENT ="type";
	private static final String QUESTION_TYPE_NAME_ELEMENT = "typename";
	private static final String QUESTION_TYPE_SHORT_NAME = "shortname";
	private static final String QUESTION_TYPE_LONG_NAME = "longname";
	private static final String QUESTION_TYPE_DESCRIPTION = "description";
	private static final String QUESTION_TYPE_ALLOW_RICH_TEXT = "allowrichtext";
	private static final String QUESTION_TYPE_MIN_LENGTH = "minlength";
	private static final String QUESTION_TYPE_MAX_LENGTH = "maxlength";
	private static final String QUESTION_TYPE_MIN_WORD_LENGTH = "minwordlength";
	private static final String QUESTION_TYPE_MAX_WORD_LENGTH = "maxwordlength";
	private static final String QUESTION_TYPE_MAX_QUESTION_LENGTH = "questionlength";
	private static final String QUESTION_TYPE_KEYWORD_HIGHLIGHT = "keywordhighlight";
	private static final String QUESTION_TYPE_MAX_ANSWER_LENGTH = "answerlength";
	private static final String QUESTION_TYPE_SHORT_ANSWER_LENGTH = "shortanswerlength";
	private static final String QUESTION_TYPE_KEYWORD_COUNT = "keywordcount";
	private static final String QUESTION_TYPE_AUTOCOMPLETE = "autocomplete";
	private static final String QUESTION_TYPE_COLUMNS = "columns";
	private static final String QUESTION_TYPE_MULTIMEDIA_TYPE = "multimediatype";
	private static final String QUESTION_TYPE_SELECTION_TYPE = "selectiontype";
	private static final String QUESTION_TYPE_MAX_BYTES = "maxbytes";
	private static final String QUESTION_TYPE_ALLOW_ONE_TO_ONE = "onetoone";
	private static final String QUESTION_TYPE_SUM_ANSWER = "sumanswer";
	private static final String QUESTION_TYPE_SUM_TRUE_ANSWER = "sumtrueanswer";
	private static final String QUESTION_TYPE_SUM_FALSE_ANSWER = "sumfalseanswer";
	private static final String QUESTION_TYPE_MAX_ANSWER_DIFFERENCE = "maxanswerdifference";
	private static final String QUESTION_TYPE_HAVE_IMAGE = "haveimage";
	private static final String QUESTION_TYPE_HAVE_AUDIO = "haveaudio";
	private static final String QUESTION_TYPE_HAVE_VIDEO = "havevideo";
	private static final String QUESTION_TYPE_SHOW_FILTER_DIALOG = "showfilterdialog";
	private static final String QUESTION_RESOURCES = "resources";
	private static final String QUESTION_MEDIA = "media";
	private static final String QUESTION_MEDIA_PATH = "path";
	private static final String QUESTION_MEDIA_NAME = "name";
	private static final String QUESTION_MEDIA_HEIGHT = "height";
	private static final String QUESTION_MEDIA_WIDTH = "width";
	private static final String QUESTION_MEDIA_SEQUENCE = "sequence";
	private static final String QUESTION_MEDIA_TYPE = "type";
	private static final String ANSWER_MEDIA_PATH = "path";
	private static final String ANSWER_POINTS = "points";
	private static final String ANSWER_VALIDITY = "validity";
	private static final String ANSWER_ADDITIONAL_KEYWORDS = "additionalkeywords";
	private static final String ANSWER_SEQUENCE = "sequence";
	private static final String ANSWER_MATRIX = "matrix";
	private static final String MATRIX_ANSWERX = "answerx";
	private static final String MATRIX_ANSWERY = "answery";
	private static final String ASSESSMENT_ROOT = "exam";
	private static final String STUDENT_ROOT = "students";
	private static final String STUDENT_ELEMENT = "student";
	private static final String STUDENT_CITY_ELEMENT = "city";
	private static final String STUDENT_STREET_ELEMENT = "street";
	private static final String STUDENT_ID_ELEMENT = "studentid";
	private static final String STUDENT_EMAIL_ELEMENT = "email";
	private static final String STUDENT_PRENAME_ELEMENT = "prename";
	private static final String STUDENT_NAME_ELEMENT = "name";
	private static final String STUDENT_GENDER_ELEMENT = "gender";
	private static final String ASSESSMENT_INSTITUTION_ID_ELEMENT = "institution";
	private static final String ASSESSMENT_PERCENT_SAME_QUESTION_ELEMENT = "percentsamequestion";
	private static final String ASSESSMENT_REPEAT_FOR_ID_ELEMENT = "repeatfor";
	private static final String ASSESSMENT_MC_ELEMENT = "mc";
	private static final String ASSESSMENT_VERSION_ELEMENT = "version";
	private static final String ASSESSMENT_LOGO_ELEMENT = "logo";
	private static final String ASSESSMENT_PLACE_ELEMENT = "venue";
	private static final String ASSESSMENT_DATE_OF_ASSESSMENT_ELEMENT = "dateofassessment";
	private static final String ASSESSMENT_NAME_ELEMENT = "name";
	private static final String ROOT = "assessment";
	private static final String EMPTY_VALUE = "";
	private static final String ANSWER_ID_ATTRIBUTE = "answerId";
	private static final String QUESTION_TYPE_IS_DICTIONARY_KEYWORD = "isdictionarykeyword";
	private static final String QUESTION_TYPE_ALLOW_TYPING = "allowtyping";
	private static final String QUESTION_TYPE_MIN_AUTOCOMPLETE_LETTER = "minautocompleteletters";
	private static final String QUESTION_TYPE_ACCEPT_NON_KEYWORD = "acceptnonkeyword";
	private static final String QUESTION_EVENT_NAME = "specialization";
}
