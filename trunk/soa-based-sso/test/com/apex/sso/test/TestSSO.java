package com.apex.sso.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestSSO {

	public static void main(String[] args) throws Exception {
		Test test = new Test();
		test.setA("a");
		test.setB("b");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(test);

		byte[] bytes = baos.toByteArray();
		for(byte b : bytes) {
			System.out.println(b);
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		test = (Test) ois.readObject();
		System.out.println(test.getA() + "\t"+ test.getB());
	}
}

class Test implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4734883765648597212L;

	private String a;
	private String b;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

}
