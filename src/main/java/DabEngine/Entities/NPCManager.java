package DabEngine.Entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Vector4f;

import DabEngine.Entities.Components.CTransform;

public class NPCManager {
	
	private ArrayList<Entity> NPCs;
	private static final float MAX_TALKING_DISTANCE = 150;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public NPCManager(String npcinfo) {
		NPCs = new ArrayList<>();
		initInfo(npcinfo);
	}
	
	private void initInfo(String npcinfo) {
		try (BufferedReader in = new BufferedReader(new FileReader(new File(npcinfo)))) {
			String line;
			String[] lineInfo;
			while((line = in.readLine()) != null) {
				lineInfo = line.split(" ");
				NPCs.add(NPCFactory.spawnNPC(lineInfo[0],
						ResourceManager.getTexture(lineInfo[1]),
						Float.parseFloat(lineInfo[2]),
						Float.parseFloat(lineInfo[3]),
						0,
						Float.parseFloat(lineInfo[4]),
						Float.parseFloat(lineInfo[5]),
						0,
						new Vector4f(1, 1, 1, 1),
						true));
			}
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, "NPC info not found", ex);
		}
	}
	
	public Entity closestNPC(Entity entity) {
		Entity closest_npc = null;
		CTransform transform_e = (CTransform) entity.getComponent(CTransform.class);
		float distance;
		for(int a = 0; a < NPCs.size(); a++) {
			distance = transform_e.pos.distance(NPCs.get(a).getComponent(CTransform.class).pos);
			if(distance <= MAX_TALKING_DISTANCE) {
				if(closest_npc == null) {
					closest_npc = NPCs.get(a);
				}
				if(transform_e.pos.distance(closest_npc.getComponent(CTransform.class).pos) < distance) {
					closest_npc = NPCs.get(a);
				}
			}
		}
		return closest_npc;
	}
	
	public ArrayList<Entity> getNPCs() {
		return NPCs;
	}
}
