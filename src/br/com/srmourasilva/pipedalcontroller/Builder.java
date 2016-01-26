package br.com.srmourasilva.pipedalcontroller;

import com.pi4j.component.button.impl.GpioButtonComponent;
import com.pi4j.component.light.LED;
import com.pi4j.component.light.impl.GpioLEDComponent;
import com.pi4j.component.switches.impl.GpioMomentarySwitchComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import br.com.srmourasilva.pipedalcontroller.domain.clicable.ButtonClicable;
import br.com.srmourasilva.pipedalcontroller.domain.clicable.MomentarySwitchClicable;

public class Builder {
	private GpioController gpio;

	public Builder(GpioController gpio) {
		this.gpio = gpio;
	}
	
	public MomentarySwitchClicable buildMomentarySwitch(Pin pin) {
		GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
		return new MomentarySwitchClicable(new GpioMomentarySwitchComponent(inputPin));
	}

	public ButtonClicable buildButton(Pin pin) {
		GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
		return new ButtonClicable(new GpioButtonComponent(inputPin));
	}

	public LED buildLed(Pin pin) {
		GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(pin, PinState.LOW);
		return new GpioLEDComponent(outputPin);
	}
}
