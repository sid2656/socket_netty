package utils.soket.netty.server;

import socket.netty.server.TCPServer;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		TCPServer.getSingletonInstance().run();
	}
}
