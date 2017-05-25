package edu.ifce.sistops;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.concurrent.Semaphore;

public class Bebinho extends Thread {

	private long t1, t2, tb, tc, tempoCorrido = 0;
	private SituacaoBebinho situacao = SituacaoBebinho.NO_BAR;
	private Buteco buteco;
	private String id;
	private boolean expulso;
	private Semaphore n;
	private static Image[] frames = new Image[13];
	private int currframe = 0;
	static {
		try {
			int i = 13;
			while (i-- > 1)
				frames[i] = Loader.INSTANCE.assetImg("cliente" + i + ".png");
			frames[0] = Loader.INSTANCE.assetImg("boneco0.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bebinho(Buteco buteco, long tempoBebendo, long tempoEmCasa, Semaphore n) {
		this.id = this.toString();
		this.tb = tempoBebendo;
		this.tc = tempoEmCasa;
		this.buteco = buteco;
		this.n = n;
	}

	@Override
	public void run() {
		try {
			buteco.log(id + " acabou de chegar no bar");
			n.acquire();
			buteco.entrarButeco(this);
			t1 = System.currentTimeMillis();
			while (!expulso) {
				t2 = System.currentTimeMillis();
				if (t2 - t1 > 1000) {
					step(); // roda uma vez por segundo
					t1 = System.currentTimeMillis();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void step() throws Exception {
		tempoCorrido++;
		currframe++;
		if (currframe == frames.length)
			currframe = 0;
		if (situacao == SituacaoBebinho.NO_BAR) {
			stepBar();
		} else if (situacao == SituacaoBebinho.EM_CASA) {
			stepCasa();
		}
		buteco.repaint();
	}

	private void stepBar() throws Exception {
		buteco.log(id + " esta bebendo");
		if (tempoCorrido >= tb) {
			buteco.log(id + "quer ir pra casa pois nao consegue mais beber");
			n.release();
			buteco.sairButeco(this);
		}
	}

	private void stepCasa() throws Exception {
		buteco.log(id + " esta em casa");
		if (tempoCorrido >= tc) {
			buteco.log(id + " quer uma cadeira pra poder beber");
			situacao = SituacaoBebinho.NA_FILA;
			n.acquire();
			buteco.entrarButeco(this);
		}
	}

	public void beber() {
		tempoCorrido = 0;
		situacao = SituacaoBebinho.NO_BAR;
	}

	public void irPraCasa() {
		tempoCorrido = 0;
		situacao = SituacaoBebinho.EM_CASA;
	}

	public void expulsa() {
		expulso = true;
		this.interrupt();
	}

	public void paint(Graphics2D g2, int i, int j) {
		int a = 10 + 40 * i;
		int b = 50 + 40 * j;
		if (situacao == SituacaoBebinho.EM_CASA)
			a += 500;
		else if (situacao == SituacaoBebinho.NA_FILA)
			a += 200;
		g2.drawImage(frames[currframe], a, b, null);
		g2.drawString("#" + i + " " + situacao.toString(), a - 20, b);
	}
}
