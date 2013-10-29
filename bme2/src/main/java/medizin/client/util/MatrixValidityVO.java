package medizin.client.util;


import com.google.common.base.Objects;

import medizin.client.proxy.MatrixValidityProxy;
import medizin.shared.Validity;

public class MatrixValidityVO {
	
	private State state = State.NEW;
	private Long id;
	private AnswerVO answerX;
	private AnswerVO answerY;
	private Validity validity = Validity.Falsch;
	private MatrixValidityProxy matrixValidityProxy;
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnswerVO getAnswerX() {
		return answerX;
	}

	public void setAnswerX(AnswerVO answerX) {
		this.answerX = answerX;
		
		if(answerX != null && matrixValidityProxy != null && answerX.getAnswerProxy() != null && matrixValidityProxy.getAnswerX() == null) {
			matrixValidityProxy.setAnswerX(answerX.getAnswerProxy());
		}
	}

	public AnswerVO getAnswerY() {
		return answerY;
	}

	public void setAnswerY(AnswerVO answerY) {
		this.answerY = answerY;
		
		if(answerY != null && matrixValidityProxy != null && answerY.getAnswerProxy() != null && matrixValidityProxy.getAnswerY() == null) {
			matrixValidityProxy.setAnswerY(answerY.getAnswerProxy());
		}
	}

	public void setValidity(Validity validity) {
		this.validity = validity;
	}

	public Validity getValidity() {
		return validity;
	}
	
	public MatrixValidityProxy getMatrixValidityProxy() {
		return matrixValidityProxy;
	}

	public void setMatrixValidityProxy(MatrixValidityProxy matrixValidityProxy) {
		this.matrixValidityProxy = matrixValidityProxy;
	}

	@Override
	public boolean equals(Object obj) {
	
		if(obj == null ) {
			return false;
		}  
	
		if(!(obj instanceof MatrixValidityVO)) {
			return false;
		}
		final MatrixValidityVO vo = (MatrixValidityVO) obj;
		
		return Objects.equal(this.state, vo.state)
				&& Objects.equal(this.answerX, vo.answerX)
				&& Objects.equal(this.answerY, vo.answerY)
				&& Objects.equal(this.id, vo.id)
				&& Objects.equal(this.matrixValidityProxy, vo.matrixValidityProxy);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper("MatrixValidityVO")
				.add("id", id)
				.add("state", state)
				.add("AnswerX", answerX)
				.add("answerY", answerY)
				.add("validity", validity)
				.add("proxy",matrixValidityProxy)
				.toString();
				
	}
}
