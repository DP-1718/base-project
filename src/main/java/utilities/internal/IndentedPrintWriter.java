package utilities.internal;

import java.io.Writer;

public class IndentedPrintWriter extends java.io.PrintWriter {
	private boolean newLine;
	private String singleIndent = "    ";
	private String currentIndent = "";

	public IndentedPrintWriter(final Writer pOut, final String indent) {
		super(pOut);
		this.singleIndent = indent;
	}

	public void indent() {
		this.currentIndent += this.singleIndent;
	}

	public void unindent() {
		if (this.currentIndent.isEmpty()) {
			return;
		}
		this.currentIndent = this.currentIndent.substring(0, this.currentIndent.length() - this.singleIndent.length());
	}

	@Override
	public void print(String pString) {
		// indent when printing at the start of a new line
		if (this.newLine) {
			super.print(this.currentIndent);
			this.newLine = false;
		}

		// strip the last new line symbol (if there is one)
		final boolean endsWithNewLine = pString.endsWith("\n");
		if (endsWithNewLine) {
			pString = pString.substring(0, pString.length() - 1);
		}

		// print the text (add indent after new-lines)
		pString = pString.replaceAll("\n", "\n" + this.currentIndent);
		super.print(pString);

		// finally add the stripped new-line symbol.
		if (endsWithNewLine) {
			this.println();
		}
	}

	@Override
	public void println() {
		super.println();
		this.newLine = true;
	}
}