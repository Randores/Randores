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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlock;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandoresRegister {

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> ev) {
        Randores.getInstance().getLogger().info("Initializing FlexibleBlocks...");
        for (int i = 0; i < 600; i++) {
            FlexibleBlock block = new FlexibleBlock();
            block.setUnlocalizedName(Randores.blockName(i));
            block.setRegistryName(Randores.blockName(i));
            FlexibleBlockRegistry.add(block);
            ev.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> ev) {
        Randores.getInstance().getLogger().info("Initializing FlexibleBlock Items...");
        for(FlexibleBlock block : FlexibleBlockRegistry.getBlocks()) {
            Item item = new ItemBlock(block).setCreativeTab(Randores.getInstance().getTab()).setUnlocalizedName(block.getUnlocalizedName()).setRegistryName(block.getRegistryName());
            ev.getRegistry().register(item);
        }
    }

}
