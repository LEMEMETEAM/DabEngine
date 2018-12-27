package Observer;

public interface Observerable {
	
	public void addObserver(Observer o);
	public void removeObserver(Observer o);
	public void notify(String s);

}
