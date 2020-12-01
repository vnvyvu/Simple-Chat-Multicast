package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import controller.Utils;

public class Node implements Runnable{
	private int port;
	private MulticastSocket mSocket;
	private InetAddress group;
	
	public Node() {
		super();
	}

	public Node(int port, String group) throws UnknownHostException {
		super();
		this.port = port;
		this.group = InetAddress.getByName(group);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.mSocket=new MulticastSocket(this.port);
			this.mSocket.setBroadcast(true);
			this.mSocket.setInterface(Utils.getLocalIP());
			this.mSocket.joinGroup(this.group);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(byte[] data) throws IOException {
		DatagramPacket packet=new DatagramPacket(data, data.length, this.group, this.port);
		this.mSocket.send(packet);
	}
	
	public byte[] receive(int length) throws IOException {
		byte[] res=new byte[length];
		DatagramPacket packet=new DatagramPacket(res, res.length);
		this.mSocket.receive(packet);
		return packet.getData();
	}
	
	public void leave() throws IOException {
		this.mSocket.leaveGroup(group);
		this.mSocket.close();
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getGroup() {
		return group;
	}

	public void setGroup(InetAddress group) {
		this.group = group;
	}
	
}
