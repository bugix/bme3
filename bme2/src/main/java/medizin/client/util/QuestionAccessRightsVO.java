package medizin.client.util;

public class QuestionAccessRightsVO {

	private boolean addRight = false;
	
	private boolean readRight = false;
	
	private boolean writeRight = false;

	public boolean hasAddRight() {
		return addRight;
	}

	public void setAddRight(boolean addRight) {
		this.addRight = addRight;
	}

	public boolean hasReadRight() {
		return readRight;
	}

	public void setReadRight(boolean readRight) {
		this.readRight = readRight;
	}

	public boolean hasWriteRight() {
		return writeRight;
	}

	public void setWriteRight(boolean writeRight) {
		this.writeRight = writeRight;
	}
}
