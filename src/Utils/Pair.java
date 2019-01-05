package Utils;

import java.util.Objects;

public class Pair<L, R> {

	public final L left;
	public final R right;
	
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return o instanceof Pair && Objects.equals(left, ((Pair<?, ?>)o).left) && Objects.equals(right, ((Pair<?, ?>)o).right);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 31 * Objects.hashCode(left) + Objects.hashCode(right);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "left = " + left + " | right = " + right;
	}
	
}
