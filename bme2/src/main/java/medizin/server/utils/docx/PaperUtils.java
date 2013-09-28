package medizin.server.utils.docx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.Assesment;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.MatrixValidity;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionType;
import medizin.server.domain.QuestionTypeCountPerExam;
import medizin.shared.utils.SharedConstant;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import sun.misc.BASE64Encoder;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings("restriction")
public final class PaperUtils {

	private static final Logger log = Logger.getLogger(PaperUtils.class);
	
	public static List<QuestionVO> get_data(final Long assessmentID, final boolean isVersionA,final boolean printAllQuestions) {
		
		List<QuestionVO> questionVOs = Lists.newArrayList();
		
		List<QuestionTypeCountPerExam> countPerExams = QuestionTypeCountPerExam.findQuestionTypesCountSortedByAssesmentNonRoo(assessmentID);
		
		for (QuestionTypeCountPerExam questionTypeCountPerExam : countPerExams) {
			Set<QuestionType> questionTypes = questionTypeCountPerExam.getQuestionTypesAssigned();
			
			List<Long> questionTypeIds = Lists.newArrayList(FluentIterable.from(questionTypes).transform(new Function<QuestionType, Long>() {

				@Override
				public Long apply(QuestionType input) {
					return input.getId();
				}	
			}).iterator());
			
			List<QuestionEvent> questionEvents =  QuestionEvent.findAllQuestionEventsByQuestionTypeAndAssesmentID(assessmentID, questionTypeIds);
			
			for (QuestionEvent questionEvent : questionEvents) {
				List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssesmentQuestionsByQuestionEventAssIdQuestType(questionEvent.getId(), assessmentID, questionTypeIds,isVersionA,printAllQuestions);
				
				for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
					
					log.info("assesmentQuestion : " + Objects.toStringHelper(AssesmentQuestion.class).add("questionId", assesmentQuestion.getQuestion().getId())
							.add("questionText", assesmentQuestion.getQuestion().getQuestionText())
							.add("AdminAccepted", assesmentQuestion.getIsAssQuestionAcceptedAdmin())
							.add("ForcedByAdmin", assesmentQuestion.getIsForcedByAdmin())
							.add("AssQuestionAcceptedRewiever", assesmentQuestion.getIsAssQuestionAcceptedRewiever())
							.add("AssQuestionAdminProposal", assesmentQuestion.getIsAssQuestionAdminProposal())
							.toString()
							);
					
					
					
					List<AnswerToAssQuestion>  answerToAssQuestions = AnswerToAssQuestion.findAnswerToAssQuestionByAssesmentQuestion(assesmentQuestion.getId());
					
					for (AnswerToAssQuestion answerToAssQuestion : answerToAssQuestions) {
						log.info("Answer : " + Objects.toStringHelper(AnswerToAssQuestion.class)
								.add("AnswerText", answerToAssQuestion.getAnswers().getAnswerText())
								.add("SortOrder", answerToAssQuestion.getSortOrder())
								.toString()
								);
					}
					
					questionVOs.add(new QuestionVO(assesmentQuestion, answerToAssQuestions));
					
				}
				
			}
		}
		
		return questionVOs;
	}
	
	public static Set<String> getAllYAnswersText(List<MatrixValidity> matrixValidities) {
		
		Set<String> answers = Sets.newHashSet();
		
		for (MatrixValidity matrixValidity : matrixValidities) {
			answers.add(matrixValidity.getAnswerY().getAnswerText());
		}
		
		return answers;
	}
	
	public static Set<String> getAllXAnswersText(List<MatrixValidity> matrixValidities) {
		
		Set<String> answers = Sets.newHashSet();
		
		for (MatrixValidity matrixValidity : matrixValidities) {
			answers.add(matrixValidity.getAnswerX().getAnswerText());
		}
		
		return answers;
	}
	
	public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
	
	public static BufferedImage generateImageFromLaTex(String convertToTex) {
		TeXFormula formula = new TeXFormula(convertToTex);
		return (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_TEXT,40, Color.BLACK, Color.WHITE);
	}

	public static BufferedImage generateImageFromPath(String path,int width) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		int newWidth = width;
		int newHeight = (width * image.getHeight())/image.getWidth();
		
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, newWidth , newHeight, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage; 
	}

	public static String writeToFile(BufferedImage generateImageFromLaTex, String ext) throws IOException {
		String fileName = UUID.randomUUID() + "." + ext;
		String path = SharedConstant.UPLOAD_MEDIA_PATH + fileName;
		ImageIO.write(generateImageFromLaTex, ext, new File(path));
		return path;
	}
	
	public static String getDocumentName(Long assessmentID,String version,String shibId, String extension) {
		Assesment assesmentObj = Assesment.findAssesment(assessmentID);
		final StringBuilder nameBuilder = new StringBuilder(assesmentObj.getName());
		if(version != null) {
			nameBuilder.append("_").append(version);
		}
		return nameBuilder.append("_").append(shibId).append(extension).toString().replaceAll(" ", "_") ;
	}

	public static String getVersionString(boolean isVersionA) {
		return isVersionA == true ? "A" : "B";
	}
	
	public static List<Long> getAnswerIdList(List<AnswerToAssQuestion> answerToAssQuestions) {
		return Lists.newArrayList(FluentIterable.from(answerToAssQuestions).transform(new Function<AnswerToAssQuestion, Long>() {

			@Override
			public Long apply(AnswerToAssQuestion input) {
				if(input != null && input.getAnswers() != null) {
					return input.getAnswers().getId();
				}
				return null;
			}
		}).filter(Predicates.notNull()).iterator());
	}
}
