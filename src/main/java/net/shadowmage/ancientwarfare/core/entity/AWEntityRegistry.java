package net.shadowmage.ancientwarfare.core.entity;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class AWEntityRegistry
{

public static final String NPC_WORKER = "aw_npc_worker";
public static final String NPC_COMBAT = "aw_npc_combat";
public static final String NPC_COURIER = "aw_npc_courier";

public static final String NPC_FACTION_BANDIT_ARCHER = "aw_npc_bandit_archer";
public static final String NPC_FACTION_BANDIT_SOLDIER = "aw_npc_bandit_soldier";
public static final String NPC_FACTION_BANDIT_PRIEST = "aw_npc_bandit_priest";
public static final String NPC_FACTION_BANDIT_TRADER = "aw_npc_bandit_trader";
public static final String NPC_FACTION_BANDIT_COMMANDER = "aw_npc_bandit_commander";
public static final String NPC_FACTION_VIKING = "aw_npc_viking";
public static final String NPC_FACTION_PIRATE = "aw_npc_pirate";

public static final String VEHICLE_TEST = "vehicle_test";

private static HashMap<String, EntityDeclaration> entityRegistrations = new HashMap<String, EntityDeclaration>();
private static HashMap<Class, String> classToRegistration = new HashMap<Class, String>();

public static void registerEntity(EntityDeclaration reg)
  {
  entityRegistrations.put(reg.entityName, reg);
  classToRegistration.put(reg.entityClass, reg.entityName);
  cpw.mods.fml.common.registry.EntityRegistry.registerModEntity(reg.entityClass, reg.entityName, reg.id, reg.mod, reg.trackingRange, reg.updateFrequency, reg.sendsVelocityUpdates);
  }

public static String getRegistryNameFor(Class clz)
  {
  return classToRegistration.get(clz);
  }

public static Entity createEntity(String type, World world)
  {
  if(entityRegistrations.containsKey(type))
    {
    return entityRegistrations.get(type).createEntity(world);
    }
  return null;
  }

public static abstract class EntityDeclaration
{

Class<? extends Entity> entityClass;
String entityName;
int id;
Object mod;
int trackingRange;
int updateFrequency;
boolean sendsVelocityUpdates;

public EntityDeclaration(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
  {
  this.entityClass = entityClass;
  this.entityName = entityName;
  this.id = id;
  this.mod = mod;
  this.trackingRange = trackingRange;
  this.updateFrequency = updateFrequency;
  this.sendsVelocityUpdates = sendsVelocityUpdates;
  }

public abstract Entity createEntity(World world);

public String getEntityName()
  {
  return entityName;
  }
}

}
