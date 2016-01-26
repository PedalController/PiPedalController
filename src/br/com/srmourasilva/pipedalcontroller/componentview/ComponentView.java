package br.com.srmourasilva.pipedalcontroller.componentview;

import com.pi4j.component.display.drawer.DisplayGraphics;

public interface ComponentView {
	/**
	 * Claims the view (re)draw all elements in display
	 */
	void paint(DisplayGraphics graphics);
}
