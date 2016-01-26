package br.com.srmourasilva.pipedalcontroller.domain.clicable;

public interface Clicable {
	
	public interface OnClickListener {
		void onClick(Clicable clicable);
	};

	void setListener(OnClickListener listener);
}
