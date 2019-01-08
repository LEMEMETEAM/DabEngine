package Entities.Components;

public interface Destroyable {
	static boolean destroyed = false;
	public void onDestroy();
}
