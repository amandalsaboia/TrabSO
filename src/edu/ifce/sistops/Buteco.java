package edu.ifce.sistops;

import java.awt.Graphics2D;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Buteco extends JPanel {

	private List<Bebinho> clientes = new LinkedList<Bebinho>();
	private Semaphore n;
	private static final long     serialVersionUID = -7065575459579164850L;

	public Buteco(int n) throws Exception{
		setLayout(null);
		int N=0;
		String s=JOptionPane.showInputDialog("Informe o número de cadeiras");
		try{
			N=Integer.parseInt(s);
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Quantidade inválida");
		}
		this.n = new Semaphore(n);
		
		JButton addCliente = new JButton("Adicionar cliente");
	}
	 @Override
	  public void paint(Graphics2D g2) {
	    super.paint(g2);
	    g2.drawString(nme + " #" + getId(), x, y - 15);
	    g2.drawString(status + " " + "[" + (numAtendimentos) + "]", x, y);

	  }
	 @Override
	  protected void paintComponent(Graphics g) {
	    g.clearRect(0, 0, 640, 480);
	    Graphics2D g2 = (Graphics2D) g;
	    for (SituacaoBebinho pc : caixasDesenho) {
	      pc.paint(g2);
	    }
	    clientes.removeAll(clientesRemover);
	    clientesRemover.removeAll(clientesRemover);
	    for (ProcessoCliente pc : clientes) {
	      pc.paint(g2);
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
	// significa que todos os clientes sentados estaÌƒo jantando juntos e o 
	// cliente que chegou deveraÌ� esperar (bloqueado) ateÌ� que todas as 
	// cadeiras sejam desocupadas para soÌ� entaÌƒo se sentar.
	public void entrarButeco(Bebinho b) throws Exception {
		b.beber();
	}

	public void sairButeco(Bebinho b) throws Exception {
		b.irPraCasa();
	}
}
