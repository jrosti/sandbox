package com.affiini.meta;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

@IdClass(CompositeId.class)
@Entity
public class CompositeKeyEntity {


	@Id private String a;
	@Id private String b; 
//create unique index emp_uidx on employee (firstname, lastname, hired_at);
	
	@Column private String text; 
	
	@Column(name="QSOME") @Enumerated(EnumType.ORDINAL) 
	private Some some; 
	
	@Column(name="ANTCOUNT") private BigInteger antCount; 
	
	public BigInteger getAntCount() {
		return antCount;
	}

	public void setAntCount(BigInteger antCount) {
		this.antCount = antCount;
	}

	@Override
	public int hashCode() {
		return (a + b).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof CompositeKeyEntity)) {
			return false; 
		}
		CompositeKeyEntity c = (CompositeKeyEntity) obj; 
		return (a+b).equals(c.getA() + c.getB());
	}

	public CompositeKeyEntity() {
	}
	
	public CompositeKeyEntity(String a, String b, String text, BigInteger antCount) {
		this.a = a;
		this.b = b;
		this.text = text; 
		this.antCount = antCount; 
		
	}
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

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setSome(Some some) {
		this.some = some;
	}

	public Some getSome() {
		return some;
	}

}
