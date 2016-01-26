package br.com.srmourasilva.pipedalcontroller.domain.clicable;

import java.util.Optional;

import com.pi4j.component.button.ButtonStateChangeListener;
import com.pi4j.component.button.impl.GpioButtonComponent;

/**
 * A clicable of type MomentarySwitch 
 */
public class ButtonClicable implements Clicable {

	private GpioButtonComponent button;
	private Optional<OnClickListener> listener;

	public ButtonClicable(GpioButtonComponent button) {
		this.button = button;
		this.button.addListener(generateListener());
	}

	private ButtonStateChangeListener generateListener() {
		return event -> {
			if (this.listener.isPresent())
				this.listener.get().onClick(this);
		};
	}

	@Override
	public void setListener(OnClickListener listener) {
		this.listener = Optional.of(listener);
	}
}
