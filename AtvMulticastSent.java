import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AtvMulticastSent {
	public static void main(String[] args) {
		DatagramSocket outSocket = null;
		DatagramPacket outPacket = null;
		byte[] outBuf;

		MulticastSocket inSocket = null;
		DatagramPacket inPacket = null;
		byte[] inBuf = new byte[256];
		final int PORT = 8888;

		try {
			outSocket = new DatagramSocket();
			Scanner input = new Scanner(System.in);
			System.out.println("Digite a expressão matemática:");
			String msg=input.next();;
			//msg padrão 3+5-4/3*4
			String resultado = "";

			boolean cond = true;
			while (cond) {

				outBuf = msg.getBytes();
				InetAddress address = InetAddress.getByName("224.2.2.3");

				outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
				outSocket.send(outPacket);

				System.out.println("Servidor enviou " + msg);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}

				// recebimento

				inSocket = new MulticastSocket(8888);
				inSocket.joinGroup(address);

				inPacket = new DatagramPacket(inBuf, inBuf.length);
				inSocket.setSoTimeout(1000);

				try {
					inSocket.receive(inPacket);
					if (inSocket != null) {
						cond = false;
					}
				} catch (SocketTimeoutException ste) {
					System.out.println("sem resposta");
					continue;
				}

				resultado = new String(inBuf, 0, inPacket.getLength());
				String servidor=inPacket.getAddress().toString();
				//numera a máquina de acordo com o último número do ip, a maquina 1 termina com 2 e a outra com 4
				char numIp=servidor.charAt(servidor.length()-1);
				int numServ=0;
				if(numIp=='2')
					numServ=1;
				else if(numIp=='4')
					numServ=2;
				System.out.println("Resolvido pela máquina " + numServ + " Resultado : " + resultado);
			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}
}
