import java.io.*;
import java.net.*;
import java.util.Stack;

public class AtvMulticastServer1 {
	public static void main(String[] args) {
		MulticastSocket inSocket = null;
		DatagramPacket inPacket = null;
		byte[] inBuf = new byte[256];

		DatagramSocket outSocket = null;
		DatagramPacket outPacket = null;
		byte[] outBuf;
		final int PORT = 8888;

		try {

			inSocket = new MulticastSocket(8888);
			InetAddress address = InetAddress.getByName("224.2.2.3");
			inSocket.joinGroup(address);

			inPacket = new DatagramPacket(inBuf, inBuf.length);
			inSocket.receive(inPacket);

			String msg = new String(inBuf, 0, inPacket.getLength());
			if (inSocket != null) {
				try {
					outSocket = new DatagramSocket();
					String resposta;
					resposta = transformar(msg).toString();
					System.out.println("resposta=" + resposta);

					outBuf = resposta.getBytes();
					System.out.println("converteu ok");
					outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);

					long inicio = System.currentTimeMillis();
					while (System.currentTimeMillis() - inicio < 2000) {

						outSocket.send(outPacket);

					}

				} catch (IOException ie) {
					System.out.println(ie);
				}
			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	public static Double transformar(String exp) {
		Stack<Double> numeros = new Stack<>();
		Stack<Character> operadores = new Stack<>();

		char[] aux = exp.toCharArray();
		for (int i = 0; i < aux.length; i++) {
			char ch = aux[i];
			if (ch == ' ') {
				continue;
			}

			else if (Character.isDigit(ch)) {
				StringBuffer buff = new StringBuffer();
				while ((i < aux.length) && (Character.isDigit(aux[i]))) {
					buff.append(aux[i++]);
				}
				i--;
				numeros.push(Double.parseDouble(buff.toString()));
			}

			if (ch == '(') {
				operadores.push(ch);
			} else if (ch == ')') {
				while (operadores.peek() != '(') {
					double output = realizarOperacao(numeros.pop(), numeros.pop(), operadores.pop());
					numeros.push(output);
				}
				operadores.pop();
			} else if (definirOperador(ch)) {
				while (!operadores.isEmpty() && (prioridade(ch) < prioridade(operadores.peek()))) {
					double output = realizarOperacao(numeros.pop(), numeros.pop(), operadores.pop());
					numeros.push(output);
				}
				operadores.push(ch);
			}
		}

		while (!operadores.isEmpty()) {
			double output = realizarOperacao(numeros.pop(), numeros.pop(), operadores.pop());
			numeros.push(output);
		}
		return numeros.peek();
	}

	public static int prioridade(char op) {
		if (op == '*' || op == '/') {
			return 2;
		} else if (op == '+' || op == '-') {
			return 1;
		}
		return -1;
	}

	public static boolean definirOperador(char op) {
		return (op == '+' || op == '-' || op == '*' || op == '/');
	}

	public static double realizarOperacao(Double a, Double b, Character op) {
		switch (op) {
			case '+':
				return a + b;
			case '-':
				return b - a;
			case '*':
				return a * b;
			case '/':
				return b / a;
		}
		return 0;
	}

}