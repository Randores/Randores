/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.forge.randore.module.equip;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Component;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.probability.RandoresProbability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandoresMobEquip {
    private static Random random = new Random();
    private static Components[] armor = {Components.HELMET, Components.CHESTPLATE, Components.LEGGINGS, Components.BOOTS};

    @SubscribeEvent
    public void onMobSpawn(EntityJoinWorldEvent ev) {
        Entity entity1 = ev.getEntity();
        if (entity1 instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) entity1;
            if (!entity.world.isRemote && !entity.getEntityData().getBoolean("randores_applied_equip")) {
                ConfigCategory config = Randores.getInstance().getConfiguration().getCategory("modules");
                entity.getEntityData().setBoolean("randores_applied_equip", true);
                if (config.get("mobequip").getBoolean()) {
                    World world = entity1.world;
                    if (RandoresProbability.percentChance(20, random)) {
                        List<MaterialDefinition> materials = MaterialDefinitionRegistry.get(Randores.getRandoresSeed(entity.world));
                        MaterialDefinition material = materials.get(random.nextInt(materials.size()));
                        if (entity instanceof AbstractSkeleton) {
                            List<Component> applicable = new ArrayList<Component>();
                            applicable.add(material.getComponent(Components.SWORD));
                            applicable.add(material.getComponent(Components.BATTLEAXE));
                            applicable.add(material.getComponent(Components.SLEDGEHAMMER));
                            applicable.add(material.getComponent(Components.AXE));

                            while (applicable.contains(null)) {
                                applicable.remove(null);
                            }

                            if(!applicable.isEmpty()) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 0.25f);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, Randores.applyData(new ItemStack(applicable.get(random.nextInt(applicable.size())).makeItem()), world));
                            }

                            if (material.hasComponent(Components.HELMET)) {
                                for (Components component : armor) {
                                    entity.setItemStackToSlot(component.getSlot(), Randores.applyData(new ItemStack(material.getComponent(component).makeItem()), world));
                                    entity.setDropChance(component.getSlot(), 0.25f);
                                }
                            }
                        } else if (entity instanceof EntityZombie) {
                            List<Component> applicable = new ArrayList<Component>();
                            applicable.add(material.getComponent(Components.SWORD));
                            applicable.add(material.getComponent(Components.BATTLEAXE));
                            applicable.add(material.getComponent(Components.SLEDGEHAMMER));
                            applicable.add(material.getComponent(Components.AXE));

                            while (applicable.contains(null)) {
                                applicable.remove(null);
                            }

                            if(!applicable.isEmpty()) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 0.25f);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, Randores.applyData(new ItemStack(applicable.get(random.nextInt(applicable.size())).makeItem()), world));
                            }

                            if (material.hasComponent(Components.HELMET)) {
                                for (Components component : armor) {
                                    entity.setDropChance(component.getSlot(), 0.25f);
                                    entity.setItemStackToSlot(component.getSlot(), Randores.applyData(new ItemStack(material.getComponent(component).makeItem()), world));
                                }
                            }
                        } else if (entity instanceof EntityVindicator) {
                            List<Component> applicable = new ArrayList<Component>();
                            applicable.add(material.getComponent(Components.BATTLEAXE));
                            applicable.add(material.getComponent(Components.AXE));

                            while (applicable.contains(null)) {
                                applicable.remove(null);
                            }

                            if(!applicable.isEmpty()) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 0.25f);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, Randores.applyData(new ItemStack(applicable.get(random.nextInt(applicable.size())).makeItem()), world));
                            }
                        }
                    }
                }
            }
        }
    }

}
