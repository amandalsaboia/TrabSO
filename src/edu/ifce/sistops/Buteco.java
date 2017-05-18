package edu.ifce.sistops;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buteco {

	private List<Bebinho> clientes = new LinkedList<Bebinho>();
	private Semaphore n;

	public Buteco(int n) {
		this.n = new Semaphore(n);
	}

	public void addCliente(long tempoBebendo, long tempoEmCasa) {
		Bebinho b = new Bebinho(this, tempoBebendo, tempoEmCasa, n);
		clientes.add(b);
		b.start();
	}
	
	public void removeCliente(Bebinho b){
		clientes.remove(b);
		b.expulsa();
	}

	
	// TODO se um cliente chega e todas as cadeiras estiverem ocupadas, 
	// significa que todos os clientes sentados estão jantando juntos e o 
	// cliente que chegou deverá esperar (bloqueado) até que todas as 
	// cadeiras sejam desocupadas para só então se sentar.
	public void entrarButeco(Bebinho b) throws Exception {
		b.beber();
	}

	public void sairButeco(Bebinho b) throws Exception {
		b.irPraCasa();
	}
}
