package medizin.server.utils.questionprint;


import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import medizin.client.util.PolygonPath;
import medizin.server.domain.Answer;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.MatrixValidity;
import medizin.server.domain.Mc;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionResource;
import medizin.server.i18n.GWTI18N;
import medizin.server.utils.ServerConstants;
import medizin.server.utils.docx.PaperUtils;
import medizin.server.utils.imagej.plugin.DrawPolygon;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.Validity;
import medizin.shared.i18n.BmeConstants;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.lowagie.text.Image;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class QuestionPrintPdf {

	private static Logger log = Logger.getLogger(QuestionPrintPdf.class);
	private final ByteArrayOutputStream os;
	private final String questionId;
	private Question question = null;
	private final Pattern patternBegin = Pattern.compile("(?<=\\\\)\\(");
	private final Pattern patternEnd = Pattern.compile("(?<=\\\\)\\)");
	Configuration cfg = new Configuration();
	private final String dirPath = new File(QuestionPrintPdf.class.getResource("QuestionPrintTemplate.ftl").getPath()).getParent();
	
	private static Function<Mc, String> MC_TO_NAME = new Function<Mc, String>() {

		@Override
		public String apply(Mc input) {
			return input.getMcName();
		}
	};	
		
	private static Function<Answer, Long> ANSWER_TO_ID = new Function<Answer, Long>() {

		@Override
		public Long apply(Answer input) {
			if(input != null) {
				return input.getId();
			}
			return null;
		}
	};
	
	private static Comparator<Answer> ANSWER_COMPARATOR = new Comparator<Answer>() {

		@Override
		public int compare(Answer o1, Answer o2) {
			return ComparisonChain.start().compare(o1.getId(), o2.getId()).result();
		}
	};
	private final Boolean questionDetails;
	private final Boolean keyword;
	private final Boolean learningObjective;
	private final Boolean usedInMc;
	private final Boolean answer;
	private final HttpServletRequest request;
	private BmeConstants constants;
	
	public QuestionPrintPdf(HttpServletRequest request, ByteArrayOutputStream os, String questionId, Boolean questionDetails, Boolean keyword, Boolean learningObjective, Boolean usedInMc, Boolean answer, String locale) {
		try
		{
			if (StringUtils.isNotBlank(locale))
				constants = GWTI18N.create(BmeConstants.class, locale);
			else
				constants = GWTI18N.create(BmeConstants.class, "de");
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}	
		this.request = request;
		this.os = os;
		this.questionId = questionId;
		this.questionDetails = questionDetails;
		this.keyword = keyword;
		this.learningObjective = learningObjective;
		this.usedInMc = usedInMc;
		this.answer = answer;
	}
	
	public void writeToPdf()
	{
		try
		{
			if (new File(SharedConstant.UPLOAD_PRINT_MEDIA_PATH).exists() == false)
				new File(SharedConstant.UPLOAD_PRINT_MEDIA_PATH).mkdir();

			List<Long> questionIdList = new ArrayList<Long>();
			
			if ("ALL".equals(questionId))
			{
				HttpSession session = request.getSession();
				if ((session.getAttribute(ServerConstants.QUESTION_PRINT_PDF_KEY)) instanceof List){
					List<Long> queIdList = (ArrayList<Long>) session.getAttribute(ServerConstants.QUESTION_PRINT_PDF_KEY);
					questionIdList.addAll(queIdList);
				}		
			}
			else
			{
				question = Question.findQuestion(Long.parseLong(questionId));
				questionIdList.add(question.getId());
			}
						
			QuestionPrintLabelPojo labelPojo = new QuestionPrintLabelPojo();			
			labelPojo.setQuestionText(constants.questionText());
			labelPojo.setQuestionTypeText(constants.questionType());
			labelPojo.setAuthor(constants.auther());
			labelPojo.setReviewer(constants.reviewer());
			labelPojo.setQuestionEvent(constants.questionEvent());
			labelPojo.setComment(constants.comment());
			labelPojo.setMcs(constants.mcs());
			labelPojo.setCreatedDate(constants.createdDate());
			labelPojo.setModifiedDate(constants.modifiedDate());
			labelPojo.setMedia(constants.media());
			labelPojo.setImageDirPath(SharedConstant.getUploadBaseDIRPath() + "/");
			labelPojo.setKeywords(constants.keyword());
			labelPojo.setLearningObjective(constants.learning());
			labelPojo.setMajorSkill(constants.majorSkill());
			labelPojo.setMinorSkill(constants.minorSkill());
			labelPojo.setMainClassification(constants.mainClassi());
			labelPojo.setClassificationTopic(constants.classiTopic());
			labelPojo.setSkills(constants.topicLbl());
			labelPojo.setSkillLevel(constants.skillLevel());
			labelPojo.setUsedInMc(constants.usedinMC());
			labelPojo.setAssessmentName(constants.assessmentName());
			labelPojo.setMc(constants.mc());
			labelPojo.setDateOfAssessment(constants.dateOfAssessment());
			labelPojo.setSchwierigkeit(constants.schwierigkeit());
			labelPojo.setTrenschaerfe(constants.trenschaerfe());
			labelPojo.setAnswer(constants.answer());
			labelPojo.setAnswerText(constants.answerText());
			labelPojo.setValidity(constants.validity());
			labelPojo.setAdditionalKeyword(constants.additionalKeyword());
			labelPojo.setQuestionDetailFlag(questionDetails);
			labelPojo.setKeywordFlag(keyword);
			labelPojo.setLearningObjectiveFlag(learningObjective);
			labelPojo.setUsedInMcFlag(usedInMc);
			labelPojo.setAnswerFlag(answer);
			
			List<QuestionPrintPojo> questionPojoList = new ArrayList<QuestionPrintPojo>();
			
			for (Long queId : questionIdList)
			{
				Question que = Question.findQuestion(queId);				
				QuestionPrintPojo questionPojo = new QuestionPrintPojo();
				questionPojo.setQuestion(que);
				questionPojo.setQuestionText(replaceWithImageTag(que.getQuestionText()));
				
				if (questionDetails)
				{																			
					String mcs = Joiner.on(", ").join(FluentIterable.from(que.getMcs()).transform(MC_TO_NAME));
					questionPojo.setMcs(mcs);
				}				
				
				if (usedInMc)
				{
					List<AssesmentQuestion> assessmentQueList = AssesmentQuestion.findAllAssesmentQueByQueId(queId);
					questionPojo.setAssessmentQueList(assessmentQueList);
				}
				
				if (answer)
				{
					if (QuestionTypes.Textual.equals(que.getQuestionType().getQuestionType()) || QuestionTypes.LongText.equals(que.getQuestionType().getQuestionType()) || QuestionTypes.Sort.equals(que.getQuestionType().getQuestionType()))
						questionPojo.setAnswerList(createAnswerString(que.getAnswers()));
					else if (QuestionTypes.MCQ.equals(que.getQuestionType().getQuestionType()))
						questionPojo.setAnswerList(createAnswerForMultimedia(que.getAnswers(), que.getQuestionType().getMultimediaType()));
					else if (QuestionTypes.Imgkey.equals(que.getQuestionType().getQuestionType()))
						questionPojo.setAnswerList(createAnswerForImgKey(que.getAnswers(), que.getQuestionResources()));
					else if (QuestionTypes.ShowInImage.equals(que.getQuestionType().getQuestionType()))
						questionPojo.setAnswerList(createAnswerForShowInImage(que.getAnswers(), que.getQuestionResources()));
					else if (QuestionTypes.Matrix.equals(que.getQuestionType().getQuestionType()))
						questionPojo.setMatrixAnswerList(createAnswerForMatrix(que));
				}				
					
				questionPojoList.add(questionPojo);								
			}
			
			FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(new File(dirPath));
			cfg.setTemplateLoader(fileTemplateLoader);
			
			Template template = cfg.getTemplate("QuestionPrintTemplate.ftl");
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("questionPojoList", questionPojoList);
			data.put("questionLabelPojo", labelPojo);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			template.process(data, new OutputStreamWriter(out));
			
			Document document = Jsoup.parse(out.toString());
						
			Elements elements = document.select("font");			
			ListIterator<org.jsoup.nodes.Element> listIterator = elements.listIterator();
			
			while (listIterator.hasNext())
			{
				org.jsoup.nodes.Element ele = listIterator.next();
				String spanStyle = "";
				
				if (ele.hasAttr("size"))
				{
					spanStyle = spanStyle + "font-size:" + ele.attr("size") + "; ";
					ele.removeAttr("size");
				}
				
				if (ele.hasAttr("color"))
				{
					spanStyle = spanStyle + "color:" + ele.attr("color") + "; ";
					ele.removeAttr("color");
				}
				
				if (ele.hasAttr("face"))
				{
					spanStyle = spanStyle + "font-family:" + ele.attr("face") + "; ";
					ele.removeAttr("face");
				}
				ele.attr("style", spanStyle);
			}
		
			elements.tagName("span");
			
			ITextRenderer renderer = new ITextRenderer();			
			renderer.getSharedContext().setReplacedElementFactory(new ProfileImageReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
			String html = document.html();
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(os);			
			os.close();
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}		
	}
	
	private List<List<String>> createAnswerForMatrix(Question que) {
		List<List<String>> matrixAnswerList = new ArrayList<List<String>>();
		
		List<Long> answerList = getAnswerIdList(que.getAnswers());	    
	    List<MatrixValidity> matrixValidities = MatrixValidity.findallMatrixValidityForAnswers(answerList);
	    List<Answer> yAnswers = getAllYAnswers(matrixValidities);
	    List<Answer> xAnswers = getAllXAnswers(matrixValidities);
	     
	    {
	    	List<String> headerRowList = new ArrayList<String>();
	    	headerRowList.add(" ");
		    for (Answer yAnswer : yAnswers) {
		    	headerRowList.add(yAnswer.getAnswerText());
		    }
		    
		    matrixAnswerList.add(headerRowList);
	    }
	    
	    for (Answer xAnswer : xAnswers) {
	    	List<String> rowList = new ArrayList<String>();
	    	rowList.add(xAnswer.getAnswerText());
	    	for (Answer yAnswer : yAnswers) {
    			MatrixValidity validity = getMatrixValidity(matrixValidities,xAnswer.getId(),yAnswer.getId());
    			if (validity != null && Validity.Wahr.equals(validity.getValidity()))
    				rowList.add("True");
    			else if (validity != null && Validity.Falsch.equals(validity.getValidity()))
    				rowList.add("False");
    			else
    				rowList.add(" ");	
    		}
	    	matrixAnswerList.add(rowList);	
	    }		
	    
	    return matrixAnswerList;
	}

	private List<Answer> createAnswerForShowInImage(Set<Answer> answerSet, Set<QuestionResource> questionResources) {
		try
		{
			List<Answer> answers = Lists.newArrayList(answerSet);
			Collections.sort(answers, ANSWER_COMPARATOR);
			
			for (QuestionResource resource : questionResources)
			{
				String imagePath = "file:///" + SharedConstant.getUploadBaseDIRPath() + "/" + resource.getPath();
				for (Answer answer : answers)
				{
					if (answer.getPoints() != null)
					{
						String newUrl = SharedConstant.UPLOAD_PRINT_MEDIA_PATH + UUID.randomUUID() + ".png";
						FileUtils.touch(new File(newUrl));
						String newImagePath = saveImage(imagePath, newUrl, Lists.newArrayList(answer.getPoints()));
						answer.setMediaPath(addImage(newImagePath));
					}										
				}
			}
			
			return answers;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private List<Answer> createAnswerForImgKey(Set<Answer> answerSet, Set<QuestionResource> questionResources) {
		try
		{
			List<Answer> answers = Lists.newArrayList(answerSet);
			Collections.sort(answers, ANSWER_COMPARATOR);
			
			for (QuestionResource resource : questionResources)
			{
				String imagePath = "file:///" + SharedConstant.getUploadBaseDIRPath() + "/" + resource.getPath();
				for (Answer answer : answers)
				{
					if (answer.getPoints() != null)
					{
						List<String> pointList = Lists.newArrayList(Splitter.on(",").split(answer.getPoints()));
						Rectangle rectangle = new Rectangle(Integer.parseInt(pointList.get(0)), Integer.parseInt(pointList.get(1)), 10, 10);
						Polygon convertToPolygon = convertToPolygon(rectangle);
						String newUrl = SharedConstant.UPLOAD_PRINT_MEDIA_PATH + UUID.randomUUID() + ".png";
						FileUtils.touch(new File(newUrl));
						String newImagePath = saveImageForImgKeyAnswer(imagePath, newUrl, Lists.newArrayList(convertToPolygon));
						answer.setAnswerText(addImage(newImagePath));
					}										
				}
			}
			
			return answers;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private List<Answer> createAnswerForMultimedia(Set<Answer> answerSet, MultimediaType multimediaType) {
		List<Answer> answers = Lists.newArrayList(answerSet);
		Collections.sort(answers, ANSWER_COMPARATOR);
		for (Answer answer : answers)
		{
			if (MultimediaType.Image.equals(multimediaType))
				answer.setAnswerText(addImage(SharedConstant.getUploadBaseDIRPath() + "/" + answer.getMediaPath()));
			else 
				answer.setAnswerText(FilenameUtils.getName(answer.getMediaPath()));
		}		
		
		return answers;
	}

	private List<Answer> createAnswerString(Set<Answer> answerSet) {
		List<Answer> answers = Lists.newArrayList(answerSet);
		Collections.sort(answers, ANSWER_COMPARATOR);
		for (Answer answer : answers)
		{
			answer.setAnswerText(replaceWithImageTag(answer.getAnswerText()));			
		}		
		
		return answers;
	}

	private MatrixValidity getMatrixValidity(List<MatrixValidity> matrixValidities, final Long xAnswerId, final Long yAnswerId) {
		return FluentIterable.from(matrixValidities).firstMatch(matrixValidityPredicate(xAnswerId, yAnswerId)).orNull();
	}

	private Predicate<MatrixValidity> matrixValidityPredicate(final Long xAnswerId, final Long yAnswerId) {
		return new Predicate<MatrixValidity>() {

			@Override
			public boolean apply(MatrixValidity input) {
				return Objects.equal(input.getAnswerX().getId(), xAnswerId) && Objects.equal(input.getAnswerY().getId(), yAnswerId);
			}
		};
	}

	public static List<Answer> getAllYAnswers(List<MatrixValidity> matrixValidities)
	{
		List<Answer> answerList = new ArrayList<Answer>();
		
		for (MatrixValidity matrixAns : matrixValidities) {
			if (answerList.contains(matrixAns.getAnswerY()) == false){
				answerList.add(matrixAns.getAnswerY());
			}		
		}
		
		return answerList;
	}
	
	public static List<Answer> getAllXAnswers(List<MatrixValidity> matrixValidities)
	{
		List<Answer> answerList = new ArrayList<Answer>();
		
		for (MatrixValidity matrixAns : matrixValidities) {
			if (answerList.contains(matrixAns.getAnswerX()) == false){
				answerList.add(matrixAns.getAnswerX());
			}		
		}
		
		return answerList;
	}
	
	public static List<Long> getAnswerIdList(Set<Answer> answers) {		
		return Lists.newArrayList(FluentIterable.from(answers).transform(ANSWER_TO_ID).filter(Predicates.notNull()).iterator());
	}
	

	private String addImage(String path) {
		try
		{
			if(new File(path).exists()) {
				return "<img class=\"questionMedia\" src=\""+ path + "\"/>";	
			} else {
				return "<p> File "+ FilenameUtils.getName(path) + " does not exists.</p>";
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
			return "<p> File "+ FilenameUtils.getName(path) + " does not exists.</p>";
		}
		
	}
	
	private String replaceWithImageTag(String string) {

		List<String> foundList = Lists.newArrayList();
		Matcher matcher = patternBegin.matcher(string);

		while (matcher.find()) {
			try {
				String subString = string.substring(matcher.start());
				
				Matcher matcher2 = patternEnd.matcher(subString);
				if(matcher2.find()) {
					try {
						foundList.add(subString.substring(1,matcher2.start()-1));	
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		String newString = string;
		for (String fnd : foundList) {
			newString = newString.replace("\\(" + fnd + "\\)", "<img style=\"margin-bottom:50px\" class=\"questionText\" src=\""+ fnd +"\"/>");
		}
		
		return newString;
	}
	
	public class ProfileImageReplacedElementFactory implements ReplacedElementFactory {

	    private final ReplacedElementFactory superFactory;
		
	    public ProfileImageReplacedElementFactory(ReplacedElementFactory superFactory) {
	        this.superFactory = superFactory;
	    }

	    @Override
	    public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
	            UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {

	    	org.w3c.dom.Element element = blockBox.getElement();
	        if (element == null) {
	            return null;
	        }
	       
	        String nodeName = element.getNodeName();
	        String className = element.getAttribute("class");
	        if ("img".equals(nodeName) && "questionText".equals(className)) {

	            InputStream input = null;
	            try {
	                Image image = Image.getInstance(PaperUtils.imageToBytes(PaperUtils.generateImageFromLaTex(element.getAttribute("src")), "png"));
	                FSImage fsImage = new ITextFSImage(image);
	              
	                cssWidth = (int) image.getWidth() * 15;
	                cssHeight = (int) image.getHeight() * 15;
	                
	                if (fsImage != null) {
	                    if ((cssWidth != -1) || (cssHeight != -1)) {
	                        fsImage.scale(cssWidth, cssHeight);
	                    }
	                    else
	                    {
	                    	fsImage.scale((int)image.getWidth(), (int)image.getHeight());
	                    }
	                    return new ITextImageElement(fsImage);
	                }
	            } catch (Exception e) {
	            	log.error(e.getMessage(), e);
	            } finally {
	                IOUtils.closeQuietly(input);
	            }
	        }
	        else if ("img".equals(nodeName) && "questionMedia".equals(className)) {
	        	InputStream input = null;
	            try {
	            	input = new FileInputStream(element.getAttribute("src"));
	                byte[] bytes = IOUtils.toByteArray(input);
	                Image image = Image.getInstance(bytes);
	                FSImage fsImage = new ITextFSImage(image);
	              
	                cssWidth = (int) image.getWidth() * 9;
	                cssHeight = (int) image.getHeight() * 9;
	                
	                if (fsImage != null) {
	                    if ((cssWidth != -1) || (cssHeight != -1)) {
	                        fsImage.scale(cssWidth, cssHeight);
	                    }
	                    else
	                    {
	                    	fsImage.scale((int)image.getWidth(), (int)image.getHeight());	                    	
	                    }
	                    return new ITextImageElement(fsImage);
	                }
	            } catch (Exception e) {
	            	log.error(e.getMessage(), e);
	            } finally {
	                IOUtils.closeQuietly(input);
	            }
			}
	        
	        return superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
	    }

	    @Override
	    public void reset() {
	        superFactory.reset();
	    }

	    @Override
	    public void remove(org.w3c.dom.Element e) {
	        superFactory.remove(e);
	    }

	    @Override
	    public void setFormSubmissionListener(FormSubmissionListener listener) {
	        superFactory.setFormSubmissionListener(listener);
	    }

	}
	public static String saveImage(String inputURL,String newUrl,List<String> polygonPathsString) {
		// get image
		ImagePlus image;
		/*String inputURL = "file:///" + url;
		String newUrl = "public/images/" + UUID.randomUUID() + ".png";*/
		
		Opener opener = new Opener();
		image = opener.openURL(inputURL);

		// resize image
		DrawPolygon pif = new DrawPolygon();
		pif.setPolygonPaths(convertToPolygon(polygonPathsString));
	
		pif.setup("", image);
		ImageProcessor ip = image.getProcessor();
		pif.run(ip);
		BufferedImage bufferedImage = pif.getImage();
		try {
			ImageIO.write(bufferedImage, "png",FileUtils.getFile(new File(newUrl)));
		} catch (IOException e) {
			log.error("error in saveImage ", e);
		}
		
		return newUrl;
	}
	
	public static String saveImageForImgKeyAnswer(String inputURL,String newUrl, List<Polygon> polygon) {
		// get image
		ImagePlus image;
		/*String inputURL = "file:///" + url;
		String newUrl = "public/images/" + UUID.randomUUID() + ".png";*/
		
		Opener opener = new Opener();
		image = opener.openURL(inputURL);

		// resize image
		DrawPolygon pif = new DrawPolygon();
		pif.setPolygonPaths(polygon);
	
		pif.setup("", image);
		ImageProcessor ip = image.getProcessor();
		pif.run(ip);
		BufferedImage bufferedImage = pif.getImage();
		try {
			ImageIO.write(bufferedImage, "png",FileUtils.getFile(new File(newUrl)));
		} catch (IOException e) {
			log.error("error in saveImage ", e);
		}
		
		return newUrl;
	}
	
	private static List<Polygon> convertToPolygon(List<String> polygonPathsString) {
		List<Polygon> polygons = new ArrayList<Polygon>();
		List<PolygonPath> polygonPaths = convertToPloygonPaths(polygonPathsString);
		
		for (PolygonPath polygonPath : polygonPaths) {
			polygons.add(new Polygon(polygonPath.getAllXPoints(), polygonPath.getAllYPoints(), polygonPath.size()));			
		}
		 
		return polygons;
	}
	
	private static List<PolygonPath> convertToPloygonPaths(List<String> polygonPathsString) {
		List<PolygonPath> polygonPaths = new ArrayList<PolygonPath>();
		
		for (String polygonstring : polygonPathsString) {
			polygonPaths.add(getPolygon(polygonstring));
		}
		
		return polygonPaths;
	}

	private static PolygonPath getPolygon(String polygonstring) {
		PolygonPath  path = new PolygonPath();
		String[] points = StringUtils.split(polygonstring,",");
		for (int i = 0; i < (points.length-1); i+=2) {
			int x = Integer.parseInt(points[i],10);
			int y = Integer.parseInt(points[i+1],10);
			path.addPoint(x,y);
		}
		return path;
	}
	
	private static Polygon convertToPolygon(Rectangle rect) {
		Polygon result = new Polygon(); 
		result.addPoint(rect.x, rect.y); 
		result.addPoint(rect.x + rect.width, rect.y);
		result.addPoint(rect.x + rect.width, rect.y + rect.height); 
		result.addPoint(rect.x, rect.y + rect.height); 
		return result;
	}
}
