package medizin.client.ui.widget.resource.dndview.vo;

import com.google.common.base.Objects;

import medizin.shared.MultimediaType;

public class QuestionResourceClient {

	private String path;

	private Integer sequenceNumber;

	private MultimediaType type;

	private State state;

	private Long id;
	
	private String name;
	
	private Integer height;
	
	private Integer width;

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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public String toString() {

		return Objects.toStringHelper(this)
				.add("id", this.id)
				.add("path", this.path)
				.add("sequenceNumber", this.sequenceNumber)
				.add("type", this.type)
				.add("State", this.state)
				.add("Name", this.name)
				.add("Height", this.height)
				.add("Width", this.width)
				.toString();
	}
}
