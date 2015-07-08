package signup;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
class HintTextField extends JTextField implements FocusListener, KeyListener {
	
	private static final Color HINT_COLOR = Color.LIGHT_GRAY;
	
	private boolean hintSet = true;
	private String hint;
	private Color color = Color.BLACK;
	private int maxLength;
	
	
	// Constructors
	public HintTextField(String hint, int columns, int maxLength) {
		this.hint = hint;
		this.maxLength = maxLength;
		setColumns(columns);
		setHint(hasFocus());
		addFocusListener(this);
		addKeyListener(this);
	}
	public HintTextField(String hint, int columns, int maxLength, Font font, Color color) {
		this(hint, columns, maxLength);
		setFont(font);
		this.color = color;
	}
	
	
	// Set Hint
	private void setHint(boolean hasFocus) {
		if (hasFocus) {
			if (hintSet) {
				setText("");
			}
			setForeground(color);
		} else {
			if (getText().equals("")) {
				setForeground(HINT_COLOR);
				setText(hint);
				hintSet = true;
			} else {
				hintSet = false;
			}
		}
	}
	
	// Clear
	public void clear() {
		setText("");
		setHint(false);
	}
	
	// Get Written Text
	public String getWrittenText() {
		if (hasFocus() || !hintSet) {
			return getText();
		}
		return "";
	}
	
	
	@Override
	public void focusGained(FocusEvent event) {
		setHint(true);
	}

	@Override
	public void focusLost(FocusEvent event) {
		setHint(false);
	}
	
	
	@Override
	public void keyTyped(KeyEvent event) {
		if (getText().length() == maxLength && getSelectionStart() == getSelectionEnd()) {
			event.consume();
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}

}
