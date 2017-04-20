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
package com.gmail.socraticphoenix.forge.randore.module.starter;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;
import java.util.Random;

public class RandoresStarterKit {
    private static Random random = new Random();
    private static Components[] armor = {Components.HELMET, Components.CHESTPLATE, Components.LEGGINGS, Components.BOOTS};
    private static Components[] tools = {Components.PICKAXE, Components.AXE, Components.SHOVEL, Components.HOE};

    @SubscribeEvent
    public void onMobSpawn(PlayerEvent.PlayerLoggedInEvent ev) {
        EntityPlayer entity = ev.player;
        if (!entity.world.isRemote&& !entity.getEntityData().getBoolean("randores_applied_kit")) {
            ConfigCategory config = Randores.getInstance().getConfiguration().getCategory("modules");
            entity.getEntityData().setBoolean("randores_applied_kit", true);
            if (config.get("starterkit").getBoolean()) {
                List<MaterialDefinition> definitions = MaterialDefinitionRegistry.get(Randores.getRandoresSeed(entity.world));
                MaterialDefinition material;
                int times = 0;
                do {
                    material = definitions.get(random.nextInt(definitions.size()));
                    times++;
                }
                while ((!material.hasComponent(Components.HELMET) || !material.hasComponent(Components.PICKAXE)) && times < 500);
                if (material.hasComponent(Components.SWORD)) {
                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.SWORD).makeItem()));
                } else if (material.hasComponent(Components.BOW)) {
                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.BOW).makeItem()));
                    entity.inventory.addItemStackToInventory(new ItemStack(Items.ARROW, 16));
                } else if (material.hasComponent(Components.AXE)) {
                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(material.getComponent(Components.AXE).makeItem()));
                }

                if (material.hasComponent(Components.AXE)) {
                    for (Components component : tools) {
                        entity.inventory.addItemStackToInventory(new ItemStack(material.getComponent(component).makeItem()));
                    }
                }

                if (material.hasComponent(Components.HELMET)) {
                    for (Components component : armor) {
                        entity.setItemStackToSlot(component.getSlot(), new ItemStack(material.getComponent(component).makeItem()));
                    }
                }
            }
        }
    }

}
