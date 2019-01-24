package Entities.Components;

import Entities.GameObject;

public abstract class Component {
	public GameObject gameObject;
	public String name;
	public void addedToGameObject(GameObject g) {
		gameObject = g;
	}
}
