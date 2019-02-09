package Entities.Components;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import Entities.GameObject;

public abstract class Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1910311515690798240L;
	public WeakReference<GameObject> gameObject;
	public void addedToGameObject(GameObject g) {
		gameObject = new WeakReference<>(g);
	}
}
