package com.affiini.meta;


public class CompositeId {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeId other = (CompositeId) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	private String b;
	private String a;

	public void setB(String b) {
		this.b = b;
	}

	public void setA(String a) {
		this.a = a;
	}

	public CompositeId() {

	}

	public CompositeId(String a, String b) {
		this.b = b;
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public String getA() {
		return a;
	}
}
