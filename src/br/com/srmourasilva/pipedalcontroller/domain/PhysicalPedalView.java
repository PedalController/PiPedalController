package br.com.srmourasilva.pipedalcontroller.domain;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.pi4j.component.display.Display;

import br.com.srmourasilva.pipedalcontroller.view.CurrentPatchDisplayView;

public class PhysicalPedalView {

	private Map<Integer, PhysicalEffect> effects = new HashMap<>();
	private Optional<CurrentPatchDisplayView> currentPatchView = Optional.empty();

	public void add(PhysicalEffect effect) {
		effects.put(effect.getPosition(), effect);
	}

	public void updateCurrentPatchDisplay(int number, String name) {
		if (!currentPatchView.isPresent())
			return;

		currentPatchView.get().setPatch(number, name);
	}

	public void active(int position) {
		Optional<PhysicalEffect> physicalEffect = getEffect(position);

		if (physicalEffect.isPresent())
			physicalEffect.get().activeLed();
	}

	public void disable(int position) {
		Optional<PhysicalEffect> physicalEffect = getEffect(position);

		if (physicalEffect.isPresent())
			physicalEffect.get().disableLed();
	}
	
	public void vinculeDisplayPatch(Display display) {
		CurrentPatchDisplayView view = new CurrentPatchDisplayView(display);
		this.currentPatchView = Optional.of(view);
	}

	private Optional<PhysicalEffect> getEffect(int position) {
		PhysicalEffect physicalEffect = effects.get(position);

		return physicalEffect == null ? Optional.empty() : Optional.of(physicalEffect);
	}
	
	// FIXME - Fazer a mesma coisa feita para com o CurrentPatchDisplayView 
	public void updateEffectType(int position, String pedalName) {
		Optional<PhysicalEffect> effect = getEffect(position);
		if (!effect.isPresent())
			return;

		String imageName = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "images" + File.separator;
		imageName += pedalName + ".bmp";

		try {
			Image image = ImageIO.read(new File(imageName));
			effect.get().updateDisplay(image);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
