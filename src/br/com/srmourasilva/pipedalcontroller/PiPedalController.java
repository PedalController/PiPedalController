package br.com.srmourasilva.pipedalcontroller;

import javax.sound.midi.MidiUnavailableException;

import com.pi4j.component.display.Display;
import com.pi4j.component.display.impl.PCD8544DisplayComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import br.com.srmourasilva.architecture.exception.DeviceNotFoundException;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.controller.PedalControllerFactory;
import br.com.srmourasilva.pipedalcontroller.domain.PhysicalEffect;
import br.com.srmourasilva.pipedalcontroller.domain.PhysicalPedalController;
import br.com.srmourasilva.pipedalcontroller.domain.clicable.Clicable;

/**
 * Simple pedal controller by buttons or switchs in a
 * Raspberry Pi
 */
public class PiPedalController {

	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		Builder builder = new Builder(gpio);

		DisplayGenerator displayGenerator = new DisplayGenerator(gpio);

		Display display1     = displayGenerator.generate(RaspiPin.GPIO_07, RaspiPin.GPIO_15);
		Display displayPatch = displayGenerator.generate(RaspiPin.GPIO_12, RaspiPin.GPIO_13);

		PhysicalEffect footswitch1 = new PhysicalEffect(
			0,
			//builder.buildButton(RaspiPin.GPIO_00, gpio),
			builder.buildMomentarySwitch(RaspiPin.GPIO_23),
			builder.buildLed(RaspiPin.GPIO_27),
			display1
		);

		PhysicalEffect footswitch2 = new PhysicalEffect(
			1,
			builder.buildMomentarySwitch(RaspiPin.GPIO_24),
			builder.buildLed(RaspiPin.GPIO_28)
		);

		PhysicalEffect footswitch3 = new PhysicalEffect(
			2,
			builder.buildMomentarySwitch(RaspiPin.GPIO_25),
			builder.buildLed(RaspiPin.GPIO_29)
		);


		Clicable next   = builder.buildMomentarySwitch(RaspiPin.GPIO_16);
		Clicable before = builder.buildMomentarySwitch(RaspiPin.GPIO_01);
		
		PedalController pedal;
		try {
			pedal = PedalControllerFactory.searchPedal();
		} catch (DeviceNotFoundException e1) {
			System.out.println("Pedal not found! You connected any?");
			e1.printStackTrace();
			return;
		}

		PhysicalPedalController multistomp = new PhysicalPedalController(pedal);
		multistomp.vinculePedalEffects(footswitch1, footswitch2, footswitch3);

		multistomp.vinculeNext(next);
		multistomp.vinculeBefore(before);
		
		multistomp.vinculeDisplayPatch(displayPatch);


		try {
			multistomp.start();
		} catch (MidiUnavailableException e) {
			System.out.println("This Pedal has been used by other process program");
		}
	}
	
	public static class DisplayGenerator {
		private GpioController gpio;
		private GpioPinDigitalOutput DC;
		private GpioPinDigitalOutput DIN;
		private GpioPinDigitalOutput CLK;

		public DisplayGenerator(GpioController gpio) {
			this.gpio = gpio;

			this.DC  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
			this.DIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
			this.CLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
		}

		public Display generate(Pin displayControllerPin, Pin resetPin) {
			GpioPinDigitalOutput SCE = gpio.provisionDigitalOutputPin(displayControllerPin, PinState.LOW);
			GpioPinDigitalOutput RST = gpio.provisionDigitalOutputPin(resetPin, PinState.LOW);

			return new PCD8544DisplayComponent(
				DIN,
				CLK,
				DC,
				RST,
				SCE,
				(byte) 60,
				false
			);
		}
	}
}
