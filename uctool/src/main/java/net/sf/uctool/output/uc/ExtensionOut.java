package net.sf.uctool.output.uc;

public class ExtensionOut {

	private String indent;
	private String number;
	private String content;
	private boolean spaceBefore = false;

	public String getIndent() {
		return indent;
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getSpaceBefore() {
		return spaceBefore;
	}

	public void setSpaceBefore(boolean spaceBefore) {
		this.spaceBefore = spaceBefore;
	}

}
