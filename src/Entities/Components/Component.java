package Entities.Components;

import java.lang.ref.WeakReference;

import Entities.GameObject;

public abstract class Component {
	public WeakReference<GameObject> gameObject;
	public void addedToGameObject(GameObject g) {
		gameObject = new WeakReference<>(g);
	}
}
