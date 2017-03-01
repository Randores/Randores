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
import com.gmail.socraticphoenix.forge.randore.RandoresProbability;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
                    if (RandoresProbability.percentChance(20, random)) {
                        List<MaterialDefinition> materials = MaterialDefinitionRegistry.get(Randores.getRandoresSeed(entity.world));
                        MaterialDefinition material = materials.get(random.nextInt(materials.size()));
                        if (entity instanceof AbstractSkeleton) {
                            if (material.hasComponent(Components.SWORD)) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 1);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.SWORD).makeItem()));
                            } else if (material.hasComponent(Components.AXE)) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 1);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.AXE).makeItem()));
                            } else {
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                            }

                            if (material.hasComponent(Components.HELMET)) {
                                for (Components component : armor) {
                                    entity.setItemStackToSlot(component.getSlot(), new ItemStack(material.getComponent(component).makeItem()));
                                    entity.setDropChance(component.getSlot(), 1);
                                }
                            }
                        } else if (entity instanceof EntityZombie) {
                            if (material.hasComponent(Components.SWORD)) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 1);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.SWORD).makeItem()));
                            } else if (material.hasComponent(Components.AXE)) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 1);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.AXE).makeItem()));
                            }

                            if (material.hasComponent(Components.HELMET)) {
                                for (Components component : armor) {
                                    entity.setDropChance(component.getSlot(), 1);
                                    entity.setItemStackToSlot(component.getSlot(), new ItemStack(material.getComponent(component).makeItem()));
                                }
                            }
                        } else if (entity instanceof EntityVindicator) {
                            if (material.hasComponent(Components.AXE)) {
                                entity.setDropChance(EntityEquipmentSlot.MAINHAND, 1);
                                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.AXE).makeItem()));
                            }
                        }
                    }
                }
            }
        }
    }

}
