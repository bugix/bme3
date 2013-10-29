package medizin.server.utils.docx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import medizin.server.domain.Answer;
import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.MatrixValidity;
import medizin.server.domain.Person;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionResource;
import medizin.server.domain.QuestionType;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public final class DocxPaperMHTML {

	private static final String EXTENSION_DOCX = ".docx";
	private static final Logger log = Logger.getLogger(DocxPaperMHTML.class);
	private final ByteArrayOutputStream os;
	private final Integer assessment;
	private String fileName = "Paper.docx";
	private List<QuestionVO> questionVOs = Lists.newArrayList();
	private final boolean isVersionA;
	private final List<String> imagePath = Lists.newArrayList();
	private final Map<String,BufferedImage> equationMap = Maps.newHashMap();
	private final boolean printAllQuestions;
	
	public DocxPaperMHTML(ByteArrayOutputStream os, Integer assessment, boolean isVersionA, boolean printAllQuestions, Person loggedPerson) {
		this.os = os;
		this.assessment = assessment;
		this.isVersionA = isVersionA;
		this.printAllQuestions = printAllQuestions;
		fileName = PaperUtils.getDocumentName(assessment.longValue(), PaperUtils.getVersionString(isVersionA), loggedPerson.getShidId(), EXTENSION_DOCX);
		log.info("fileName : " + fileName);
		log.info("isVersionA : " + isVersionA);
		log.info("printAllQuestions : " + printAllQuestions);
		
	}

	public String getFileName() {
		return fileName;
	}

	public void createWordFile() {
		
		questionVOs = PaperUtils.get_data(assessment.longValue(),isVersionA,printAllQuestions);
		
		for (QuestionVO questionVO : questionVOs) {
		
			AssesmentQuestion assesmentQuestion = questionVO.getAssesmentQuestion();
			log.info("assesmentQuestion : " + Objects.toStringHelper(AssesmentQuestion.class).add("questionId", assesmentQuestion.getQuestion().getId())
					.add("questionText", assesmentQuestion.getQuestion().getQuestionText())
					.add("AdminAccepted", assesmentQuestion.getIsAssQuestionAcceptedAdmin())
					.add("ForcedByAdmin", assesmentQuestion.getIsForcedByAdmin())
					.add("AssQuestionAcceptedRewiever", assesmentQuestion.getIsAssQuestionAcceptedRewiever())
					.add("AssQuestionAdminProposal", assesmentQuestion.getIsAssQuestionAdminProposal())
					.toString()
					);
		}
		
		
		try {
			final String htmlPage = addQuestionAnswerToDocument();
			
			final ByteArrayOutputStream htmlFile = createMHTMLContent(htmlPage);
	        
	        createDocxFile(htmlFile);
			
		} catch (InvalidFormatException e) {
			log.error("Error while  creating docx ",e);
			fileName = "Error.docx"; 
		} catch (Docx4JException e) {
			log.error("Error while  creating docx ",e);
			fileName = "Error.docx";
		} catch (MessagingException e) {
			log.error("Error while  creating docx ",e);
			fileName = "Error.docx";
		} catch (IOException e) {
			log.error("Error while  creating docx ",e);
			fileName = "Error.docx";
		}
	}

	private void createDocxFile(final ByteArrayOutputStream htmlFile) throws InvalidFormatException, Docx4JException {
		
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.mht"));
		afiPart.setBinaryData(htmlFile.toByteArray());
		afiPart.setContentType(new ContentType("message/rfc822"));
		Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);
		 
		// .. the bit in document body
		CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
		ac.setId(altChunkRel.getId() );
		wordMLPackage.getMainDocumentPart().addObject(ac);
		
		// .. content type
		wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
		SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
		saver.save(os);
	}

	private ByteArrayOutputStream createMHTMLContent(final String htmlPage) throws MessagingException, IOException {
		
		Properties props = new Properties();
		Session session = Session.getInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		MimeMultipart mpart = new MimeMultipart("related");
		
		mpart.addBodyPart(bodyPart(new StringSource("text/html", "index.html", htmlPage)));
		message.setContent(mpart);
		
		addEquationImageToMessage(message, mpart);
		
		addImageToMessage(message, mpart);
		
		// the subject is displayed as the window title in the browser
		message.setSubject("MHTML example");
		// one can set the URL of the original page:
		message.addHeader("Content-Location", "index.html");

		// Save to example.mhtml
		ByteArrayOutputStream htmlFile = new ByteArrayOutputStream();
		message.writeTo(htmlFile);
		htmlFile.close();
		
		return htmlFile;
	}

	private void addImageToMessage(MimeMessage message, MimeMultipart mpart) {
		for (String path : imagePath) {
			try {
				mpart.addBodyPart(bodyPart(new FileDataSource(path)));
		        message.setContent(mpart);		
			} catch (Exception e) {
				log.error("Error in add image",e);
			}	
		}
	}

	private void addEquationImageToMessage(MimeMessage message, MimeMultipart mpart) throws IOException, MessagingException {
		for (Entry<String, BufferedImage> equationImage : equationMap.entrySet()) {
			ByteArrayDataSource ds = new ByteArrayDataSource(getByteArrayForBufferedImage(equationImage.getValue()), "Application/octet-stream");
			ds.setName(equationImage.getKey());
			mpart.addBodyPart(bodyPart(ds));
		    message.setContent(mpart);
		}
	}

	private String addQuestionAnswerToDocument() throws Docx4JException {
		Document document = Jsoup.parse("<!doctype html><html><head></head><body></body></html>");
		org.jsoup.nodes.Element ul = document.body().appendElement("ul");
		ul.attr("style", "list-style-type:upper-roman");
		for (QuestionVO questionVO : questionVOs) {
			addQuestionNext(ul,questionVO);
		}
		return document.html();
	}

	private byte[] getByteArrayForBufferedImage(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	private final Pattern patternBegin = Pattern.compile("(?<=\\\\)\\[");
	private final Pattern patternEnd = Pattern.compile("(?<=\\\\)\\]");

	private String replaceEquationWithImageTag(String string) {

		List<String> foundList = Lists.newArrayList();
		Matcher matcher = patternBegin.matcher(string);

		// Check all occurance
		while (matcher.find()) {
			log.info("Start index: " + matcher.start());
			log.info("End index: " + matcher.end() + " ");
			log.info("Group : " + matcher.group());
			log.info("Orginal : " + string);
			
			try {
				String subString = string.substring(matcher.start());
				
				Matcher matcher2 = patternEnd.matcher(subString);
				if(matcher2.find()) {
					try {
						log.info("final : " + subString.substring(1,matcher2.start()-1));
						foundList.add(subString.substring(1,matcher2.start()-1));	
					}catch (Exception e) {
						log.error("Error IN sub String for pattern end",e);
					}
				}
			}catch (Exception e) {
				log.error("Error in sub String  for pattern start",e);
			}
		}

		log.info("No match found");

		String newString = string;
		for (String fnd : foundList) {
			final String fileName = UUID.randomUUID().toString();  
			final BufferedImage bufferedImage = PaperUtils.generateImageFromLaTex(fnd);
			newString = newString.replace("\\[" + fnd + "\\]", "<img src=\""+fileName+"\"/>");
			equationMap.put(fileName, bufferedImage);
		}
		log.info("New String  :" + newString);
		
		return newString;
	}
	
	private void addQuestionNext(Element questionUL, QuestionVO questionVO) throws Docx4JException{
		
		Element li = questionUL.appendElement("li");
		
		createTextParaWithEquation(li,removeLastBr(questionVO.getAssesmentQuestion().getQuestion().getQuestionText()));
		
		QuestionType type = questionVO.getAssesmentQuestion().getQuestion().getQuestionType() ;
		Question question = questionVO.getAssesmentQuestion().getQuestion();
		
		addImageToQuestion(li,type,question);	
		
		addAnswer(questionVO.getAnswerToAssQuestions(), li, question);
	}

	private void addAnswer(List<AnswerToAssQuestion> answerToAssQuestions, Element li, Question question) {
		
		if(answerToAssQuestions != null && answerToAssQuestions.size() > 0) {
		
			if(QuestionTypes.Matrix.equals(question.getQuestionType().getQuestionType())) {
				createAnswerMatrix(li,answerToAssQuestions);
			}else {
				Element answerUL = li.appendElement("ul");
				answerUL.attr("style", "list-style-type:lower-alpha");
				for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
					Element answerLI = answerUL.appendElement("li");
					createTextParaWithEquation(answerLI, removeLastBr(answerToAssQuestion.getAnswers().getAnswerText()));
					addImageToAnswer(answerLI, answerToAssQuestion.getAnswers());
				}
			}
		}
	}

	private void createAnswerMatrix(Element li, List<AnswerToAssQuestion> answerToAssQuestions) {
		Element table = li.appendElement("table");
		table.attr("border", "1");
	    List<Long> answerList = PaperUtils.getAnswerIdList(answerToAssQuestions);
	    
	    List<MatrixValidity> matrixValidities = MatrixValidity.findallMatrixValidityForAnswers(answerList);
	    Set<String> yAnswers = PaperUtils.getAllYAnswersText(matrixValidities);
	    Set<String> xAnswers = PaperUtils.getAllXAnswersText(matrixValidities);
	    
	     
	    {
	    	Element tr = table.appendElement("tr");
	    	
		    addTd(tr, "");
		    for (String yAnswer : yAnswers) {
		    	addTd(tr, yAnswer);
		    }				
	    }
	    
	    for (String xAnswer : xAnswers) {
	    	Element tr1 = table.appendElement("tr");
	    	addTd(tr1, xAnswer);
	    	for (int i = 0; i < yAnswers.size(); i++) {
	    		addTd(tr1, "");
			}
	    }		
	}

	private void addTd(Element tr, String text) {
		Element td = tr.appendElement("td");
		td.append(text);
	}
	
	private void addImageToAnswer(Element li, Answer answer) {
		if(StringUtils.isNotBlank(answer.getMediaPath())) {
			li.append(addImage(SharedConstant.getUploadBaseDIRPath() + "/" + answer.getMediaPath()));
		}
	}

	private void addImageToQuestion(Element li, QuestionType type, Question question) {
		
		if(type != null)  {
			if(QuestionTypes.Textual.equals(type.getQuestionType()) || QuestionTypes.Sort.equals(type.getQuestionType()) || QuestionTypes.LongText.equals(type.getQuestionType())) {
				if((type.getQueHaveImage() != null && type.getQueHaveImage())) {
					for (QuestionResource resource : question.getQuestionResources()) {
						if(MultimediaType.Image.equals(resource.getType())) {
							li.append(addImage(SharedConstant.getUploadBaseDIRPath() + "/" + resource.getPath()));	
						}	
					}	
				}
			} 
		}
	}

	private String addImage(String path) {
		
		if(new File(path).exists()) {
			imagePath.add(path);
			return "<img src=\""+ FilenameUtils.getName(path) + "\"/>";	
		} else {
			return "<p> File "+ FilenameUtils.getName(path) + " does not exists.</p>";
		}
		
	}

	private String removeLastBr(String questionText) {
		String text = StringUtils.removeEnd(questionText, "<br>");
		return StringUtils.removeEnd(text, "<br/>"); 
	}

	private void createTextParaWithEquation(Element li,String paragraphText) {
		
		try {
			paragraphText = replaceEquationWithImageTag(paragraphText);	
		}catch (Exception e) {
			log.error("Error ",e);
		}
		
		li.html(li.html() + "<p>" + paragraphText + "</p>");
	}
	
	 static BodyPart bodyPart(DataSource ds) throws MessagingException
	    {
	        MimeBodyPart body = new MimeBodyPart();
	        DataHandler dh = new DataHandler(ds);
	        body.setDisposition("inline");
	        body.setDataHandler(dh);
	        body.setFileName(dh.getName());
	        // the URL of the file; we set it simply to its name
	        body.addHeader("Content-Location", dh.getName());
	        return body;
	    }

	    /**
	     * A simple in-memory implementation of {@link DataSource}.
	     */
	    static final class StringSource implements DataSource
	    {
	        private final String contentType;
	        private final String name;
	        private final byte[] data;
	        public StringSource(String contentType, String name, String data)
	        {
	            this.contentType = contentType;
	            this.data = data.getBytes();
	            this.name = name;
	        }
	        public String getContentType()
	        {
	            return contentType;
	        }
	        public OutputStream getOutputStream() throws IOException
	        {
	            throw new IOException();
	        }
	        public InputStream getInputStream() throws IOException
	        {
	            return new ByteArrayInputStream(data);
	        }
	        public String getName()
	        {
	            return name;
	        }
	    }
}
