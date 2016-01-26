package br.com.srmourasilva.pipedalcontroller.view;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.drawer.DisplayGraphics;
import com.pi4j.component.display.drawer.DisplayGraphics.ColorType;

import br.com.srmourasilva.pipedalcontroller.componentview.CurrentPatchComponentView;

public class CurrentPatchDisplayView {
	private Display display;
	private DisplayGraphics graphics;
	private CurrentPatchComponentView view;

	public CurrentPatchDisplayView(Display display) {
		this.display = display;
		this.graphics = new DisplayGraphics(display, WhiteBlackDisplay.BLACK, ColorType.BINARY);
		this.view = new CurrentPatchComponentView();
	}
	
	public void setPatch(int number, String name) {
		graphics.clear();
		view.setPatch(number, name);
		view.paint(graphics);
		graphics.dispose();
	}
}
