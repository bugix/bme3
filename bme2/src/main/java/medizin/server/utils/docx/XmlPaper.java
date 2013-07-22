package medizin.server.utils.docx;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import medizin.server.domain.AnswerToAssQuestion;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

public class XmlPaper {
		
	private static final Logger log = Logger.getLogger(XmlPaper.class);
	private final ByteArrayOutputStream os;
	private final Integer assessment;
	private String fileName = "Paper.xml";
	private List<QuestionVO> questionVOs = Lists.newArrayList();
	
	public XmlPaper(ByteArrayOutputStream os, Integer assessment) {
		this.os = os;
		this.assessment = assessment;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void createXMLFile() {
	
		questionVOs = PaperUtils.get_data(assessment.longValue(),true);
		
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("", "Questions");
            doc.appendChild(mainRootElement);
            
            // append child elements to root element
            for (QuestionVO question : questionVOs) {   
              	mainRootElement.appendChild(addQuestion(doc,question));	
			}
            
            
            // output DOM XML to console 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(os);
            transformer.transform(source, console);
 
            log.info("\nXML DOM Created Successfully..");
 
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
	}

	private Node addQuestion(Document doc, QuestionVO question) {
		Element questionElement = doc.createElement(QUESTION_ELEMENT);
        questionElement.setAttribute(QUESTION_ID, question.getAssesmentQuestion().getQuestion().getId().toString());
        questionElement.appendChild(getCDATAElements(doc, QUESTION_TEXT, question.getAssesmentQuestion().getQuestion().getQuestionText()));
        questionElement.appendChild(getTextElements(doc, QUESTION_TYPE, question.getAssesmentQuestion().getQuestion().getQuestionType().getQuestionType().toString()));
        questionElement.appendChild(getAnswers(doc, question.getAnswerToAssQuestions()));
        return questionElement;
	}

	private Node getAnswers(Document doc, List<AnswerToAssQuestion> answerToAssQuestions) {	
		Element answersElement = doc.createElement(ANSWERS_ELEMENT);
		for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
			answersElement.appendChild(getAnswer(doc,answerToAssQuestion));
		}
		
        return answersElement;
	}

	private Node getAnswer(Document doc, AnswerToAssQuestion answerToAssQuestion) {
		Element answerElement = doc.createElement(ANSWER_ELEMENT);
        answerElement.setAttribute(ANSWER_ID, answerToAssQuestion.getAnswers().getId().toString());
        answerElement.appendChild(getCDATAElements(doc, ANSWER_TEXT, answerToAssQuestion.getAnswers().getAnswerText()));
		return answerElement;
	}

	private Node getTextElements(Document doc, String name, String value) {
		Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
	}
	
	private Node getCDATAElements(Document doc, String name, String value) {
		Element node = doc.createElement(name);
        node.appendChild(doc.createCDATASection(value));
        return node;
	}
	
	private static final String ANSWER_TEXT = "Text";
	private static final String ANSWER_ID = "id";
	private static final String ANSWER_ELEMENT = "Answer";
	private static final String ANSWERS_ELEMENT = "Answers";
	private static final String QUESTION_ELEMENT = "Question";
	private static final String QUESTION_TYPE = "Type";
	private static final String QUESTION_TEXT = "Text";
	private static final String QUESTION_ID = "id";
}
