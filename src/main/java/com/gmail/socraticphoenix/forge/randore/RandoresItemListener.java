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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBrick;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandoresItemListener {

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent ev) {
        EntityPlayer player = ev.getEntityPlayer();
        World world = player.world;
        long seed = Randores.getRandoresSeed(world);
        if (!world.isRemote) {
            EntityItem entityItem = ev.getItem();
            ItemStack item = entityItem.getEntityItem();
            Item raw = item.getItem();
            if (item.getSubCompound("display") == null) {
                if (raw instanceof ItemBlock) {
                    Block block = ((ItemBlock) raw).getBlock();
                    if (block instanceof FlexibleOre) {
                        FlexibleOre target = (FlexibleOre) block;
                        MaterialDefinition definition = target.getDefinition(seed);
                        item.setStackDisplayName(Randores.RESET + definition.getName() + " Ore");
                    } else if (block instanceof FlexibleBrick) {
                        FlexibleBrick target = (FlexibleBrick) block;
                        MaterialDefinition definition = target.getDefinition(seed);
                        item.setStackDisplayName(Randores.RESET + definition.getName() + " " + CraftableType.BRICKS.getName());
                    }
                } else if (raw instanceof FlexibleItem) {
                    FlexibleItem target = (FlexibleItem) raw;
                    MaterialDefinition definition = target.getDefinition(seed);
                    if (target.isMaterial()) {
                        item.setStackDisplayName(Randores.RESET + definition.getName() + " " + definition.getMaterial().getType().getName());
                    } else {
                        item.setStackDisplayName(Randores.RESET + definition.getName() + " " + target.getType().getName());
                    }
                }
            }
        }
    }

}
