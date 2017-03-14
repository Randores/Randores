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

import com.gmail.socraticphoenix.forge.randore.component.Component;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.google.common.base.Supplier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RandoresTab extends CreativeTabs {
    private Supplier<Item> icon;
    private boolean checkDefinitions;

    public RandoresTab(String name, Supplier<Item> icon, boolean checkDefinitions) {
        super(name);
        this.icon = icon;
        this.checkDefinitions = checkDefinitions;
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(this.icon.get());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        if(this.checkDefinitions) {
            for (Item item : Item.REGISTRY) {
                if (item != null) {
                    for (CreativeTabs tab : item.getCreativeTabs()) {
                        if (tab == this) {
                            ItemStack stack = new ItemStack(item);
                            if (RandoresClientSideRegistry.isInitialized()) {
                                Randores.applyData(stack, RandoresClientSideRegistry.getCurrentSeed());
                                boolean found = false;
                                for (MaterialDefinition definition : MaterialDefinitionRegistry.get(RandoresClientSideRegistry.getCurrentSeed())) {
                                    for (Component component : definition.getComponents()) {
                                        if (component.makeItem().equals(item)) {
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                                if (found) {
                                    items.add(stack);
                                }
                            } else {
                                items.add(stack);
                            }
                        }
                    }
                }
            }
        } else {
            super.displayAllRelevantItems(items);
            if(RandoresClientSideRegistry.isInitialized()) {
                for (int i = 0; i < items.size(); i++) {
                    items.set(i, Randores.applyData(items.get(i), RandoresClientSideRegistry.getCurrentSeed()));
                }
            }
        }
    }

}
