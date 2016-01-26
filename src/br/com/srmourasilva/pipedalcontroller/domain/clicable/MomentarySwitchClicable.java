package br.com.srmourasilva.pipedalcontroller.domain.clicable;

import java.util.Optional;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.impl.GpioMomentarySwitchComponent;

/**
 * A clicable of type MomentarySwitch 
 */
public class MomentarySwitchClicable implements Clicable {

	private GpioMomentarySwitchComponent momentarySwitch;
	private Optional<OnClickListener> listener;

	public MomentarySwitchClicable(GpioMomentarySwitchComponent momentarySwitch) {
		this.listener = Optional.empty();
		this.momentarySwitch = momentarySwitch;
		this.momentarySwitch.addListener(generateListener());
	}

	private SwitchListener generateListener() {
		return event -> {

			boolean clicked = event.getNewState() == SwitchState.ON;

			if (clicked && this.listener.isPresent())
				this.listener.get().onClick(this);

		};
	}

	@Override
	public void setListener(OnClickListener listener) {
		this.listener = Optional.of(listener);
	}
}
