package medizin.client.ui.widget.resource.dndview.vo;

import medizin.shared.MultimediaType;

public class QuestionResourceClient {

	private String path;

	private Integer sequenceNumber;

	private MultimediaType type;
	
	private State state;
	
	private Long id;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public MultimediaType getType() {
		return type;
	}

	public void setType(MultimediaType type) {
		this.type = type;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setId(Long id) {
		this.id = id;
	} 
	
	public Long getId() {
		return this.id;
	}
	

}
