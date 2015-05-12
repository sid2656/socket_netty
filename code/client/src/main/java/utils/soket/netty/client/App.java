package utils.soket.netty.client;

import socket.netty.client.TcpClient;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		TcpClient.getInstance().run();
	}
}
