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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBrick;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandoresRegistryListener {

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> ev) {
        Randores.getInstance().getLogger().info("Initializing Blocks...");
        for (int i = 0; i < 300; i++) {
            FlexibleOre block = new FlexibleOre(i);
            block.setUnlocalizedName(Randores.blockName(i)).setRegistryName(Randores.blockName(i)).setCreativeTab(Randores.TAB_BLOCKS);
            FlexibleBlockRegistry.addOres(block);
            ev.getRegistry().register(block);
        }

        for (int i = 0; i < 300; i++) {
            FlexibleBrick block = new FlexibleBrick(i + 300);
            block.setUnlocalizedName(Randores.blockName(i + 300)).setRegistryName(Randores.blockName(i + 300)).setCreativeTab(Randores.TAB_BLOCKS);
            FlexibleBlockRegistry.addBrick(block);
            ev.getRegistry().register(block);
        }

        ev.getRegistry().register(CraftingBlocks.CRAFTINIUM_ORE);
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> ev) {
        Randores.getInstance().getLogger().info("Initializing Items...");

        for (int i = 0; i < 300; i++) {
            Item material = new FlexibleItem(i).setUnlocalizedName(Randores.itemName(i)).setRegistryName(Randores.itemName(i)).setCreativeTab(Randores.TAB_ITEMS);
            FlexibleItemRegistry.addMaterial(material);
            ev.getRegistry().register(material);
        }

        for(FlexibleOre block : FlexibleBlockRegistry.getOres()) {
            Item item = new ItemBlock(block).setUnlocalizedName(block.getUnlocalizedName()).setRegistryName(block.getRegistryName());
            ev.getRegistry().register(item);
        }

        for(Block brick : FlexibleBlockRegistry.getBricks()) {
            Item item = new ItemBlock(brick).setUnlocalizedName(brick.getUnlocalizedName()).setRegistryName(brick.getRegistryName());
            ev.getRegistry().register(item);
        }

        ev.getRegistry().register(CraftingItems.CRAFTINIUM_LUMP);
        ev.getRegistry().register(new ItemBlock(CraftingBlocks.CRAFTINIUM_ORE).setUnlocalizedName(CraftingBlocks.CRAFTINIUM_ORE.getUnlocalizedName()).setRegistryName(CraftingBlocks.CRAFTINIUM_ORE.getRegistryName()));

    }

}
