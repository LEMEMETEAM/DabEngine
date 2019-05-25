package DabEngine.System;

import java.util.Objects;

import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Entities.Entity;
import DabEngine.Observer.Event;
import DabEngine.Utils.Pair;

public class CollisionEvent implements Event {
	public Pair<Entity, Entity> entities;
	public Vector2f diff;
	
	public CollisionEvent(Pair<Entity, Entity> entities, Vector2f diff) {
		this.entities = entities;
		this.diff = diff;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return obj instanceof CollisionEvent && Objects.equals(entities, ((CollisionEvent)obj).entities) && Objects.equals(diff, ((CollisionEvent)obj).diff);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 31 * entities.hashCode() + diff.hashCode();
	}
}
