package medizin.shared;

import java.util.List;

import com.google.common.collect.Lists;

public enum KprimValidityEnum {

	A(Validity.Wahr,Validity.Wahr,Validity.Wahr,Validity.Wahr),
	B(Validity.Wahr,Validity.Wahr,Validity.Wahr,Validity.Falsch),
	C(Validity.Wahr,Validity.Wahr,Validity.Falsch,Validity.Wahr),
	D(Validity.Wahr,Validity.Falsch,Validity.Wahr,Validity.Wahr),
	E(Validity.Falsch,Validity.Wahr,Validity.Wahr,Validity.Wahr),
	F(Validity.Wahr,Validity.Wahr,Validity.Falsch,Validity.Falsch),
	G(Validity.Wahr,Validity.Falsch,Validity.Wahr,Validity.Falsch),
	H(Validity.Wahr,Validity.Falsch,Validity.Falsch,Validity.Wahr),
	I(Validity.Falsch,Validity.Wahr,Validity.Wahr,Validity.Falsch),
	J(Validity.Falsch,Validity.Wahr,Validity.Falsch,Validity.Wahr),
	K(Validity.Falsch,Validity.Falsch,Validity.Wahr,Validity.Wahr),
	L(Validity.Wahr,Validity.Falsch,Validity.Falsch,Validity.Falsch),
	M(Validity.Falsch,Validity.Wahr,Validity.Falsch,Validity.Falsch),
	N(Validity.Falsch,Validity.Falsch,Validity.Wahr,Validity.Falsch),
	O(Validity.Falsch,Validity.Falsch,Validity.Falsch,Validity.Wahr),
	P(Validity.Falsch,Validity.Falsch,Validity.Falsch,Validity.Falsch);

	private final Validity first;
	private final Validity second;
	private final Validity third;
	private final Validity fourth;

	
	KprimValidityEnum(Validity first, Validity second, Validity third, Validity fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	public Validity getFirst() {
		return first;
	}
	
	public Validity getSecond() {
		return second;
	}
	
	public Validity getThird() {
		return third;
	}
	
	public Validity getFourth() {
		return fourth;
	}
	
	public List<Validity>getAllValidity() {
		return Lists.newArrayList(first,second,third,fourth);
	}
}
