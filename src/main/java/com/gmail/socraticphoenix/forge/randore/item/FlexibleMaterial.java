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
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class FlexibleMaterial extends Item implements FlexibleItem {
    private int index;

    public FlexibleMaterial(int index) {
        this.index = index;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && this.getDefinition(playerIn.world).hasComponent(this.getType())) {
            MaterialDefinition definition = this.getDefinition(playerIn.world);
            if (!stack.hasDisplayName()) {
                tooltip.remove(0);
                tooltip.add(0, definition.getName() + " " + definition.getComponent(this.getType()).getLocalName(RandoresClientSideRegistry.getCurrentLocale()));
            }
            tooltip.addAll(definition.generateLore(RandoresClientSideRegistry.getCurrentLocale()));
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (Randores.hasRandoresSeed(stack) && this.getDefinition(Randores.getRandoresSeed(stack)).hasComponent(this.getType())) {
            MaterialDefinition definition = this.getDefinition(Randores.getRandoresSeed(stack));
            return definition.getName() + " " + definition.getComponent(this.getType()).getLocalName(RandoresClientSideRegistry.getCurrentLocale());
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public MaterialDefinition getDefinition(long seed) {
        return MaterialDefinitionRegistry.get(seed).get(this.index);
    }

    @Override
    public MaterialDefinition getDefinition(World world) {
        return this.getDefinition(Randores.getRandoresSeed(world));
    }

    @Override
    public Components getType() {
        return Components.MATERIAL;
    }

    @Override
    public int index() {
        return this.index;
    }

}
