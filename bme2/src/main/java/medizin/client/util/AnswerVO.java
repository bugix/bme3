package medizin.client.util;

import medizin.client.proxy.AnswerProxy;

import com.google.common.base.Objects;

public class AnswerVO {

	private String answer;
	private Long id;
	private State state;
	private AnswerProxy answerProxy;
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	public AnswerProxy getAnswerProxy() {
		return answerProxy;
	}
	public void setAnswerProxy(AnswerProxy answerProxy) {
		this.answerProxy = answerProxy;
	}
	@Override
	public boolean equals(Object obj) {
	
		if(obj == null) {
			return false;
		}
		
		if((obj instanceof AnswerVO) == false) {
			return false;
		}
		
		AnswerVO vo = (AnswerVO) obj;
		
		return Objects.equal(this.answer, vo.answer)
				&& Objects.equal(this.id, vo.id)
				&& Objects.equal(this.state, vo.state);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper("AnswerVO")
				.add("id", id)
				.add("answer", answer)
				.add("state",state)
				.toString();
	}
}
