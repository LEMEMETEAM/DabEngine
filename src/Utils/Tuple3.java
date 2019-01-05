package Utils;

import java.util.Objects;

public class Tuple3<A, B, C> {
	
	public A first;
	public B second;
	public C third;
	
	public Tuple3(A one, B two, C three) {
		this.first = one;
		this.second = two;
		this.third = three;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return o instanceof Pair && Objects.equals(first, ((Tuple3<?, ?, ?>)o).first) && Objects.equals(second, ((Tuple3<?, ?, ?>)o).second) && Objects.equals(third, ((Tuple3<?, ?, ?>)o).third);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 31 * Objects.hashCode(first) + Objects.hashCode(second) + Objects.hashCode(third);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "first = " + first + " | second = " + second + " | third = " + third;
	}
}
