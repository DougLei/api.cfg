package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Parent {
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public void sayName() {
		System.out.println(name);
		;
	}

	public static void main(String[] args) throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		System.out.println(address.getHostAddress());
	}
}
