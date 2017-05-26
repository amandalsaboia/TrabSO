package edu.ifce.sistops;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.concurrent.Semaphore;

public class Cliente extends Thread {

	private long t1, t2, tb, tc, tempoCorrido = 0;
//	private SituacaoBebinho situacao = SituacaoBebinho.NO_BAR;
	private SituacaoBebinho situacao = SituacaoBebinho.NA_FILA;//Teste
	private Bar buteco;
	private long id;
	private boolean expulso;
	private boolean impresso_bar = false,impresso_casa = false;
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

	public Cliente(Bar buteco,long id_cliente, long tempoBebendo, long tempoEmCasa, Semaphore n) {
		this.id = id_cliente;
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
			situacao = SituacaoBebinho.NO_BAR;//Teste
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
			if (impresso_bar == false){
				buteco.log(id + " esta bebendo por " + tb + " segundos.");
				impresso_bar = true;
				impresso_casa = false;
			}
			if (tempoCorrido >= tb) {
				buteco.log(id + " quer ir pra casa pois nao consegue mais beber");
				n.release();
				buteco.sairButeco(this);
				buteco.log(id + " saiu do bar..");
			}
		}

	private void stepCasa() throws Exception {
		if (impresso_casa == false){
			buteco.log(id + " esta em casa por " + tc + " segundos.");
			impresso_casa = true;
			impresso_bar = false;
		}
		if (tempoCorrido >= tc) {
			buteco.log(id + " quer uma cadeira pra poder beber");
			buteco.log(id + " entrou na fila para entrar no bar.");
			situacao = SituacaoBebinho.NA_FILA;
			n.acquire();
			if (situacao == SituacaoBebinho.NA_FILA){
				buteco.log(id + " saiu da fila\n" + id + " entrou no bar.");
				impresso_casa = false;
				impresso_bar = false;
			}
			buteco.entrarButeco(this);
		}
	}




	/*private void stepCasa() throws Exception {
		buteco.log(id + " esta em casa");
		if (tempoCorrido >= tc) {
			buteco.log(id + " quer uma cadeira pra poder beber");
			situacao = SituacaoBebinho.NA_FILA;
			n.acquire();
			buteco.entrarButeco(this);
		}
	}*/

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
		int a = 30 + 5 * i;//10
		int b = 80 + 55 * j;//50
		if (situacao == SituacaoBebinho.EM_CASA)
			a += 540;
		else if (situacao == SituacaoBebinho.NA_FILA)
			a += 300;
		g2.drawImage(frames[currframe], a, b, null);
		g2.drawString("#" + id +" " + situacao.toString(), a, b);
	}
}
