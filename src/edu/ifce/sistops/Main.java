package edu.ifce.sistops;

public class Main {
	public static void main(String[] args) {
		Buteco b = new Buteco(3);
		b.addCliente(3, 4);
		b.addCliente(1, 5);
		b.addCliente(10, 5);
		b.addCliente(3, 2);
		b.addCliente(6, 3);
		b.addCliente(9, 4);
	}
}
