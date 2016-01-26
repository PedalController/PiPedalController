package br.com.srmourasilva.pipedalcontroller.componentview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.drawer.DisplayGraphics;
import com.pi4j.component.display.drawer.DisplayGraphics.ColorType;
import com.pi4j.component.display.impl.AWTDisplayComponent;

@SuppressWarnings("serial")
public class CurrentPatchComponentView extends Component {
	private int number;
	private String name;

	public CurrentPatchComponentView() {
		this.number = 0;
		this.name = "";
	}

	public void setPatch(int number, String name) {
		this.number = number;
		this.name = name;
	}

	@Override
	public void paint(Graphics graphics) {
		DisplayGraphics dg = (DisplayGraphics) graphics;
		
		Font originalFont = dg.getFont();
		Color originalColor = dg.getColor();

		dg = paintPatchNumber(dg);
		dg = paintPatchName(dg);
		
		dg.setFont(originalFont);
		dg.setColor(originalColor);
	}

	private DisplayGraphics paintPatchNumber(DisplayGraphics dg) {
		String patchNumber = ((char) (65 + this.number/10)) + "" + this.number%10;

		int width = dg.getDisplay().getWidth();
		int height = dg.getDisplay().getHeight()/3 * 2;
		int border = 2;
		
		int fontSize = height - 2*border;

		dg.setColor(Color.BLACK);
		dg.fillRect(
			0,
			0,
			dg.getDisplay().getWidth(),
			height
		);
		
		Font font = new Font(Font.DIALOG, Font.BOLD , fontSize);
		dg.setFont(font);
		
		FontMetrics fontMetrics = dg.getFontMetrics();
		int numberX = width/2 - fontMetrics.stringWidth(patchNumber)/2;
		int numberY = fontSize - border*2;

		dg.setColor(Color.WHITE);
		dg.drawString(patchNumber, numberX, numberY);

		return dg;
	}
	

	private DisplayGraphics paintPatchName(DisplayGraphics dg) {
		int border = 2;
		int width  = dg.getDisplay().getWidth(); 
		int height = dg.getDisplay().getHeight()/3;
		
		int base = dg.getDisplay().getHeight();
		
		int fontSize = height - border;

		Font font = new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, fontSize);
		dg.setFont(font);
		
		int x = (width - dg.getFontMetrics().stringWidth(name))/2;
		int y = base - border;

		dg.setColor(Color.BLACK);
		dg.drawString(this.name, x, y);

		return dg;
	}
	
	public static void main(String[] args) {
		CurrentPatchComponentView view = new CurrentPatchComponentView();
		view.setPatch(01, "Shows EFX");
		
		//Display display = new AWTDisplayComponent(168, 96);
		Display display = new AWTDisplayComponent(84, 48, false);
		DisplayGraphics graphics = new DisplayGraphics(display, Color.WHITE, ColorType.BINARY);
		
		view.paint(graphics);
		graphics.dispose();
	}
}
