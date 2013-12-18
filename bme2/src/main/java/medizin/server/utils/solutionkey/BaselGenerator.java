package medizin.server.utils.solutionkey;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.Assesment;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.QuestionType;
import medizin.shared.KprimValidityEnum;
import medizin.shared.Validity;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Ranges;


public class BaselGenerator {
	private static Logger log = Logger.getLogger(BaselGenerator.class);
	
	private final ByteArrayOutputStream os;
	private final PrintWriter out;
	private final String fileName = "Basel.txt";
	private final Long assessmentId;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
	private int single;
	private int multi;
	private Character[] singleList = new Character[150];
	private Character[] multiList = new Character[50];
	private List<Integer> aScrambling; // 150 values
	private List<Integer> kprimScrambling; // 50 values
	private List<Integer> pickScrambling; // 50 values
	private List<Integer> aEliminations = Lists.newArrayListWithExpectedSize(150);
	private List<Integer> kprimEliminations = Lists.newArrayListWithExpectedSize(50);
	private List<Character> pickEliminations = Lists.newArrayListWithExpectedSize(50);
	private List<Character> aContains = Lists.newArrayListWithExpectedSize(150);
	private List<Character> kprimContains = Lists.newArrayListWithExpectedSize(50);
	private List<Character> pickContains = Lists.newArrayListWithExpectedSize(50);
	
	
	public BaselGenerator(Long assessmentId) {
		this.assessmentId = assessmentId;
		os = new ByteArrayOutputStream();
		out = new PrintWriter(os);
		Arrays.fill(singleList, '-');
		Arrays.fill(multiList, '-');
		aScrambling = Lists.newArrayList(Ranges.closed(1, 150).asSet((DiscreteDomains.integers())));
		kprimScrambling = Lists.newArrayList(Ranges.closed(1, 50).asSet((DiscreteDomains.integers())));
		pickScrambling = Lists.newArrayList(Ranges.closed(1, 50).asSet((DiscreteDomains.integers())));
		
		aEliminations = Lists.newArrayList(Collections.nCopies(150, 0));
		kprimEliminations = Lists.newArrayList(Collections.nCopies(50, 0));
		pickEliminations = Lists.newArrayList(Collections.nCopies(50, '-'));
		aContains = Lists.newArrayList(Collections.nCopies(150, '-'));
		kprimContains = Lists.newArrayList(Collections.nCopies(50, '-'));
		pickContains = Lists.newArrayList(Collections.nCopies(50, '-'));
		
	}
	
	public byte[] getBytes() {
		return os.toByteArray();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void generate() {
		getCount();
		Assesment assesment = Assesment.findAssesment(assessmentId);
		out.print(assesment.getName());
		out.print(", ");
		out.println(dateFormat.format(assesment.getDateOfAssesment()));
		out.println("Itemzahl (Typ A-K, Kprim, Pick N, R)");
		out.println(single);
		out.println(multi);
		out.println("");
		out.println("");
		out.println("Notengrenzen");
		for(int i=8;i<=17;i++) {
			out.println("");
		}
		out.println("Loesungen Einfachwahlfragen Typ A, B, E, K");
		out.println(Joiner.on("").join(singleList));
		out.println("Loesungen Mehrfachwahlfragen Typ Kprim");
		out.println(Joiner.on("").join(multiList));
		out.println("Anzahl Auswahlantworten Typ Pick N / R");
		for(int i=23;i<=72;i++) {
			out.println("");
		}
		out.println("Loesungen Typ Pick N / R");
		for(int i=74;i<=123;i++) {
			out.println("");
		}
		out.println("Scrambling Einfachwahlfragen Typ A, B, E, K");
		for (Integer integer : aScrambling) {
			out.println(integer);
		}
		out.println("Scrambling Mehrfachwahlfragen Typ Kprim");
		for(Integer integer : kprimScrambling) {
			out.println(integer);
		}
		out.println("Scrambling Typ Pick N / R");
		for(Integer integer : pickScrambling) {
			out.println(integer);
		}
		out.println("Eliminationen Einfachwahlfragen Typ A, B, E, K"); //TODO need to implement logic
		for (Integer value : aEliminations) {
			if(value <= 0) {
				out.println("-");
			} else {
				out.println(value);	
			}
			
		}
		out.println("Eliminationen Mehrfachwahlfragen Typ Kprim");//TODO need to implement logic
		for (Integer value : kprimEliminations) {
			if(value <= 0) {
				out.println("-");
			} else {
				out.println(value);	
			}
		}
		out.println("Eliminationen Typ Pick N / R");
		for (Character character : pickEliminations) {
			out.println(character);
		}
		out.println("Teilpruefungen Einfachwahlfragen Typ A, B, E, K");
		for (Character character : aContains) {
			out.println(character);
		}
		out.println("Teilpruefungen Mehrfachwahlfragen Typ Kprim");
		for (Character character : kprimContains) {
			out.println(character);
		}
		out.println("Teilpruefungen Typ Pick N / R");
		for (Character character : pickContains) {
			out.println(character);
		}
		out.flush();
		out.close();
	}
	
	
	public void getCount() {
		single = 0;
		multi = 0;
		List<AssesmentQuestion> assesmentQuestions = AssesmentQuestion.findAssementQuestionForAssementBookByQuestionTypeAndQuestionEvent(this.assessmentId);
		for (AssesmentQuestion assesmentQuestion : assesmentQuestions) {
			QuestionType questionType = assesmentQuestion.getQuestion().getQuestionType();
			List<AnswerToAssQuestion> answersToAssQuestion = sort(assesmentQuestion.getAnswersToAssQuestion());
			if(questionType.getSumTrueAnswer() == 1) {
				// single choice (A+ or A-)
				singleList[single] = getCorrectAnswerIndex(answersToAssQuestion);
				
				if(assesmentQuestion.getEliminateQuestion() != null && assesmentQuestion.getEliminateQuestion() == true) {
					aEliminations.set(single, single+1);	
				}
				
				// order B
				aContains.set(single,'1');
				
				single++;
			} else {
				if (multi < 50) {
					
					// Multiple choice (k prim)
					multiList[multi] = getCorrectAnswerEnumChar(answersToAssQuestion);
					if(assesmentQuestion.getEliminateQuestion() != null && assesmentQuestion.getEliminateQuestion() == true) {
						kprimEliminations.set(multi, multi+1);	
					}
					
					// order B				
					kprimContains.set(multi,'1');	
					
					multi++;
				} else {
					log.error("Multi question list more then 50");
				}
				
			}
		}
		
		int index = 0;
		for (int i = single; i > 0; i--) {
			aScrambling.set(index, i);
			index++;
		}
		
		index = 0;
		for (int i = multi; i > 0; i--) {
			kprimScrambling.set(index, i);
			index++;
		}
	}
	
	private final Ordering<AnswerToAssQuestion> orderingOnSortOrder = Ordering.natural().onResultOf(onSortOrder());
	private final Ordering<AnswerToAssQuestion> orderingOnId = Ordering.natural().onResultOf(onId());
	private final Ordering<AnswerToAssQuestion> orderingOnSortOrderAndId = orderingOnSortOrder.compound(orderingOnId);
	
	private List<AnswerToAssQuestion> sort(Set<AnswerToAssQuestion> answersToAssQuestion) {
		return orderingOnSortOrderAndId.sortedCopy(answersToAssQuestion);
	}

	private Function<AnswerToAssQuestion, Long> onId() {
		return new Function<AnswerToAssQuestion, Long>() {
			
			@Override
			public Long apply(AnswerToAssQuestion input) {
				return input.getId();
			}
		};
	}

	private Function<AnswerToAssQuestion, Integer> onSortOrder() {
		return new Function<AnswerToAssQuestion, Integer>() {

			@Override
			public Integer apply(AnswerToAssQuestion input) {
				return input.getSortOrder();
			}
		};
	}

	private char getCorrectAnswerEnumChar(List<AnswerToAssQuestion> answersToAssQuestion) {
		if (answersToAssQuestion.size() == 4) {
			for (KprimValidityEnum kprimEnum : KprimValidityEnum.values()) {
				List<Validity> allValidity = kprimEnum.getAllValidity();
				boolean flag = true;
				for (int i = 0; i < 4; i++) {
					if (allValidity.get(i).equals(answersToAssQuestion.get(i).getAnswers().getValidity()) == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					return kprimEnum.name().charAt(0);
				}
			}
		} else {
			log.error("Answer to ass question size is not 4");
		}

		return '0';
	}

	private char getCorrectAnswerIndex(List<AnswerToAssQuestion> answersToAssQuestion) {
		
		for (AnswerToAssQuestion answerToAssQuestion : answersToAssQuestion) {
			if(Validity.Wahr.equals(answerToAssQuestion.getAnswers().getValidity())) {
				return String.valueOf(answerToAssQuestion.getSortOrder()+1).charAt(0); 
			}
		}
		
		return '0';
	}
}
