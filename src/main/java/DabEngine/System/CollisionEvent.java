package DabEngine.System;

import java.util.Objects;

import org.joml.Vector3f;

import DabEngine.Observer.Event;
import DabEngine.Utils.Pair;

public class CollisionEvent implements Event {
	public Pair<Integer, Integer> entities;
	public Vector3f depth;
	public Vector3f dir;
	
	public CollisionEvent(Pair<Integer, Integer> entities, Vector3f depth, Vector3f dir) {
		this.entities = entities;
		this.depth = depth;
		this.dir = dir;
	}
}
