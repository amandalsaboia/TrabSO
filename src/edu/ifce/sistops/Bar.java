package edu.ifce.sistops;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class Bar extends JPanel {

	private static final long serialVersionUID = -7065575459579164850L;

	private List<Cliente> clientes = new LinkedList<Cliente>();
	private List<String> logMensagens = new LinkedList<String>();
	private BufferedImage mesa;
	private int x, y, num;
	private int numbebinhos = 0;
	private JTextArea jta = new JTextArea();
	private Semaphore mutex = new Semaphore(1), mutmesa, n;
	// numero de cadeira
	public Bar() throws Exception {
		setLayout(null);
		setSize(800, 600);
		String s = JOptionPane.showInputDialog("Informe o numero de cadeiras");
		num = Integer.parseInt(s);
		this.n = new Semaphore(num);
		this.mutmesa = new Semaphore(num);

		JFrame jf = new JFrame("O bar tem [ " + num + " ] cadeiras");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout(5, 5));
		jf.add(this, BorderLayout.CENTER);
		jf.setSize(800, 650);

		JPanel jp2 = new JPanel();
		jp2.setLayout(new FlowLayout());
		jf.add(jp2, BorderLayout.NORTH);
		JLabel label1 = new JLabel("ID");
		jp2.add(label1);
		final JTextField idd = new JTextField(10);
		jp2.add(idd);

		JLabel label = new JLabel("Tempo no bar");
		jp2.add(label);

		final JTextField tfBar = new JTextField(10);
		jp2.add(tfBar);

		label = new JLabel("Tempo em casa");
		jp2.add(label);

		final JTextField tfCasa = new JTextField(10);
		jp2.add(tfCasa);

		JButton bt = new JButton("Adicionar Cliente");
		jp2.add(bt);
		// bota o log
		JFrame jf1 = new JFrame("Log de Atividades");
		jf1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JScrollPane scroll = new JScrollPane(jta);
		DefaultCaret caret = (DefaultCaret) jta.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		jf1.add(scroll);
		jf1.setSize(800, 480);
		JButton log = new JButton("Ver Log");
		add(log);
		log.setBounds(140, 440, 230, 30);
		log.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jf1.setVisible(true);
			}
		});

		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long id_cliente = Long.parseLong(idd.getText());
				long tempoBebendo = Long.parseLong(tfBar.getText());
				long tempoEmCasa = Long.parseLong(tfCasa.getText());
				Bar.this.addCliente(id_cliente, tempoBebendo, tempoEmCasa);
			}
		});

		jf.setVisible(true);
		mesa = Loader.INSTANCE.assetImg("table.jpg");
		x = 50;
		y = 50;
		jf.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, 800, 600);
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(mesa, x, y, null);
		g.drawString("tem " + numbebinhos + " bebendo", 10, 10);
		int i = clientes.size();
		int j = 0;
		while (i-- > 0) {
			Cliente b = clientes.get(i);
			b.paint(g2, i, j++);
			if (j > 3)
				j = 0;
		}
	}

	public void addCliente(long id_cliente, long tempoBebendo, long tempoEmCasa) {
		Cliente b = new Cliente(this, id_cliente, tempoBebendo, tempoEmCasa, n);
		clientes.add(b);
		b.start();
	}

	public void removeCliente(Cliente b) {
		clientes.remove(b);
		b.expulsa();
	}

	// TODO se um cliente chega e todas as cadeiras estiverem ocupadas,
	// significa que todos os clientes sentados estaÌƒo jantando juntos e o
	// cliente que chegou devera esperar (bloqueado) ate que todas as
	// cadeiras sejam desocupadas para so entao se sentar.
	public void entrarButeco(Cliente b) throws Exception {
		mutex.acquire();
		b.beber();
		numbebinhos++;
		mutex.release();			
		if(numbebinhos<num)
			mutmesa.acquire();
	}

	public void sairButeco(Cliente b) throws Exception {
		mutex.acquire();
		b.irPraCasa();
		numbebinhos--;
		mutex.release();
		if(numbebinhos==0){
			int i = num;
			while(i-->0)
				mutmesa.release();					
		}
			
	}

	public void log(String string) {
		logMensagens.add(string);
		if (logMensagens.size() > 1000) {
			while (logMensagens.size() > 1000)
				logMensagens.removeAll(logMensagens);
			jta.setText("");
		}
		jta.append("\n" + string);
	}
}
