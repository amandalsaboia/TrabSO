package edu.ifce.sistops;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buteco {

	private int numcadeiras;
	private Semaphore cadeiras;
	private Semaphore mutex = new Semaphore(1);
	private List<Bebinho> clientes = new LinkedList<Bebinho>();

	public Buteco(int numcadeiras) {
		cadeiras = new Semaphore(numcadeiras);
		this.numcadeiras = numcadeiras;
	}

	public void addCliente(long tempoBebendo, long tempoEmCasa) {
		clientes.add(new Bebinho(this, tempoBebendo, tempoEmCasa));
	}

	public void entrarButeco(Bebinho b) {

	}

	private void entrarFila(Bebinho b) {

	}

	private void sentarCadeira(Bebinho b) {

	}

	public void sairButeco(Bebinho b) {

	}
}
