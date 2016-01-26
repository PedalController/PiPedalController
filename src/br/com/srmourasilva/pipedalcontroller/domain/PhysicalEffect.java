package br.com.srmourasilva.pipedalcontroller.domain;

import java.awt.Image;
import java.util.Optional;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.WhiteBlackDisplay;
import com.pi4j.component.display.drawer.DisplayGraphics;
import com.pi4j.component.display.drawer.DisplayGraphics.ColorType;
import com.pi4j.component.light.LED;

import br.com.srmourasilva.pipedalcontroller.domain.clicable.Clicable;

public class PhysicalEffect {

	public static interface OnFootswitchClickListener {
		void onClick(int position);
	}

	private int position;
	private Clicable clicable;
	private LED light;
	private Optional<Display> display = Optional.empty();
	private DisplayGraphics graphics;
	
	/**
	 * @param position 0 is the first
	 * @param clicable
	 * @param led
	 * @param display
	 */
	public PhysicalEffect(int position, Clicable clicable, LED led, Display display) {
		this(position, clicable, led);
		this.display = Optional.of(display);
		this.graphics = new DisplayGraphics(display, WhiteBlackDisplay.WHITE, ColorType.BINARY);
	}

	/**
	 * @param position 0 is the first
	 * @param clicable
	 * @param led
	 */
	public PhysicalEffect(int position, Clicable clicable, LED led) {
		this.position = position;
		this.clicable = clicable;
		this.light = led;
	}
	
	public void setOnFootswitchClickListener(OnFootswitchClickListener listener) {
		this.clicable.setListener(clicable -> listener.onClick(this.position));
	}
	
	public int getPosition() {
		return position;
	}

	public void activeLed() {
		this.light.on();
	}
	
	public void disableLed() {
		this.light.off();
	}
	
	public void switchLed() {
		this.light.toggle();
	}
	
	public void updateDisplay(Image image) {
		if (!display.isPresent())
			return;

		graphics.clear();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
	}
}
