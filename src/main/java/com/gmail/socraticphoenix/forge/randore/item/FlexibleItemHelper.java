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
package com.gmail.socraticphoenix.forge.randore.item;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class FlexibleItemHelper {

    public static void addInformation(FlexibleItem item, ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && item.hasDefinition(Randores.getRandoresSeed(playerIn.world)) && item.getDefinition(playerIn.world).hasComponent(item.getType())) {
            MaterialDefinition definition = item.getDefinition(playerIn.world);
            if (!stack.hasDisplayName()) {
                tooltip.remove(0);
                tooltip.add(0, definition.getName() + " " + definition.getComponent(item.getType()).getLocalName(RandoresClientSideRegistry.getCurrentLocale()));
            }

            if((item instanceof FlexibleItemBlock && ((FlexibleItemBlock) item).getBlock() instanceof FlexibleOre) || item instanceof FlexibleMaterial) {
                tooltip.addAll(definition.generateLore(RandoresClientSideRegistry.getCurrentLocale()));
            }
        }
    }

    public static String getItemStackDisplayName(FlexibleItem item, ItemStack stack) {
        if (Randores.hasRandoresSeed(stack) && item.hasDefinition(Randores.getRandoresSeed(stack)) && item.getDefinition(Randores.getRandoresSeed(stack)).hasComponent(item.getType())) {
            MaterialDefinition definition = item.getDefinition(Randores.getRandoresSeed(stack));
            String format = RandoresTranslations.get(RandoresClientSideRegistry.getCurrentLocale(), RandoresTranslations.Keys.FORMAT);
            String name = definition.getName();
            String itemName = definition.getComponent(item.getType()).getLocalName(RandoresClientSideRegistry.getCurrentLocale());
            return format.replace("${name}", name).replace("${item}", itemName);
        }
        return null;
    }

    public static void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!Randores.hasRandoresSeed(stack) || Randores.getRandoresSeed(worldIn) != Randores.getRandoresSeed(stack)) {
            Randores.applyData(stack, worldIn);
        }
    }

}
