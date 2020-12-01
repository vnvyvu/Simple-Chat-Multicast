package controller;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Utils {
	public static InetAddress getLocalIP() throws SocketException, UnknownHostException {
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			return socket.getLocalAddress();
		}
	}
}
