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
package com.gmail.socraticphoenix.forge.randore;

import com.gmail.socraticphoenix.forge.randore.component.CraftableComponent;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class RandoresItemListener {

    @SubscribeEvent
    public void onPickup(PlayerEvent.ItemPickupEvent ev) {
        EntityPlayer player = ev.player;
        World world = player.world;
        if(!world.isRemote) {
            EntityItem entityItem = ev.pickedUp;
            ItemStack item = entityItem.getEntityItem();
            Item raw = item.getItem();
            if(item.getItem().getUnlocalizedName().contains("randores.item.") && item.getSubCompound("display") == null) {
                definitionSearch:
                for(MaterialDefinition definition : MaterialDefinitionRegistry.get(Randores.getRandoresSeed(world))) {
                    if(definition.getOre().makeItem().equals(raw)) {
                        item.setStackDisplayName(ChatFormatting.RESET + definition.getName() + " Ore");
                        break;
                    } else if (definition.getMaterial().makeItem().equals(raw)) {
                        item.setStackDisplayName(ChatFormatting.RESET + definition.getName() + " " + definition.getMaterial().getType().getName());
                        break;
                    } else {
                        for(CraftableComponent craftable : definition.getCraftables()) {
                            if(craftable.makeItem().equals(raw)) {
                                item.setStackDisplayName(ChatFormatting.RESET + definition.getName() + " " + craftable.getType().getName());
                                break definitionSearch;
                            }
                        }
                    }
                }
            }
        }
    }

}
