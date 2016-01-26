package br.com.srmourasilva.pipedalcontroller.domain;

import javax.sound.midi.MidiUnavailableException;

import com.pi4j.component.display.Display;

import br.com.srmourasilva.domain.OnMultistompListener;
import br.com.srmourasilva.domain.message.CommonCause;
import br.com.srmourasilva.domain.message.Messages;
import br.com.srmourasilva.domain.message.Messages.Message;
import br.com.srmourasilva.multistomp.controller.PedalController;
import br.com.srmourasilva.multistomp.zoom.gseries.ZoomGSeriesMessages;
import br.com.srmourasilva.pipedalcontroller.domain.clicable.Clicable;

public class PhysicalPedalController implements OnMultistompListener {

	private PedalController pedal;
	private PhysicalPedalView pedalboard;

	public PhysicalPedalController(PedalController pedal) {
		this.pedal = pedal;
		this.pedal.addListener(this);		
		this.pedalboard = new PhysicalPedalView();
	}

	public void vinculePedalEffects(PhysicalEffect ... effects) {
		for (PhysicalEffect physicalEffect : effects) {
			pedalboard.add(physicalEffect);
			physicalEffect.setOnFootswitchClickListener(effectPosition -> {
				System.out.println("Effect Position clicked: " + effectPosition);
				this.pedal.toogleEffect(effectPosition);
			});
		}
	}

	public void vinculeNext(Clicable next) {
		next.setListener(clicable -> pedal.nextPatch());
	}

	public void vinculeBefore(Clicable next) {
		next.setListener(clicable -> pedal.beforePatch());
	}
	

	public void vinculeDisplayPatch(Display display) {
		pedalboard.vinculeDisplayPatch(display);
	}

	public void start() throws MidiUnavailableException {
		pedal.on();
		pedal.send(ZoomGSeriesMessages.REQUEST_CURRENT_PATCH_NUMBER());
	}

	//////////////////////////////////////////

	@Override
	public void onChange(Messages messages) {
		System.out.println(messages);

		messages.getBy(CommonCause.EFFECT_ACTIVE).forEach(message -> updateEffect(message, CommonCause.EFFECT_ACTIVE));
		messages.getBy(CommonCause.EFFECT_DISABLE).forEach(message -> updateEffect(message, CommonCause.EFFECT_DISABLE));

		messages.getBy(CommonCause.TO_PATCH).forEach(message -> setPatch(message));
		messages.getBy(CommonCause.PATCH_NAME).forEach(message -> updateTitle((String) message.details().value));
		
		messages.getBy(CommonCause.EFFECT_TYPE).forEach(message -> updateEffect(message, CommonCause.EFFECT_TYPE));
	}

	private void updateEffect(Message message, CommonCause cause) {
		int patch  = message.details().patch;
		int effect = message.details().effect;

		boolean otherPatch = patch != pedal.multistomp().getIdCurrentPatch();
		if (otherPatch)
			return;

		if (cause == CommonCause.EFFECT_ACTIVE)
			pedalboard.active(effect);

		else if (cause == CommonCause.EFFECT_DISABLE)
			pedalboard.disable(effect);

		else if (cause == CommonCause.EFFECT_TYPE)
			pedalboard.updateEffectType(effect, pedal.multistomp().currentPatch().effects().get(effect).getName());
	}

	private void updateTitle(String title) {
		int index = pedal.multistomp().currentPatch().getId();

		pedalboard.updateCurrentPatchDisplay(index, title);
	}

	private void setPatch(Message message) {
		int idPatch = message.details().patch;

		pedal.send(ZoomGSeriesMessages.REQUEST_SPECIFIC_PATCH_DETAILS(idPatch));
	}
}
