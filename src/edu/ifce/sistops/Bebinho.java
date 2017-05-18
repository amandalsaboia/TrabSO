package edu.ifce.sistops;

public class Bebinho extends Thread {

	private long t1, t2, tempoBebendo, tempoEmCasa, tempoCorrido = 0;
	private SituacaoBebinho situacao = SituacaoBebinho.NO_BAR;
	private Buteco buteco;

	public Bebinho(Buteco buteco, long tempoBebendo, long tempoEmCasa) {
		this.tempoBebendo = tempoBebendo;
		this.tempoEmCasa = tempoEmCasa;
		this.buteco = buteco;
	}

	@Override
	public void run() {
		t1 = System.currentTimeMillis();
		while (true) {
			t2 = System.currentTimeMillis();
			if (t2 - t1 > 1000)
				step(); // roda uma vez por segundo
		}
	}

	private void step() {
		tempoCorrido++;
		if (situacao == SituacaoBebinho.NO_BAR) {
			stepBar();
		} else if (situacao == SituacaoBebinho.EM_CASA) {
			stepCasa();
		}
	}

	private void stepBar() {

	}

	private void stepCasa() {

	}
}
