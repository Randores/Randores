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
package com.gmail.socraticphoenix.forge.randore.compatability.jei;

import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresTabBlocks;
import com.gmail.socraticphoenix.forge.randore.RandoresTabItems;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIRandoresConfig implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabItem));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabAxe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHelmet));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHoe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabPickaxe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabShovel));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabStick));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabSword));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabBlocks.tabOre));

        if(RandoresClientSideRegistry.isInitialized()) {
            for(MaterialDefinition definition : MaterialDefinitionRegistry.get(RandoresClientSideRegistry.getCurrentSeed())) {
                for(CraftableType type : CraftableType.values()) {
                    if(!definition.hasComponent(Components.fromCraftable(type))) {
                        if(type == CraftableType.BRICKS) {
                            blacklist.addIngredientToBlacklist(new ItemStack(FlexibleBlockRegistry.getBricks().get(definition.getIndex())));
                        } else {
                            blacklist.addIngredientToBlacklist(new ItemStack(FlexibleItemRegistry.get(type, definition.getIndex())));
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }

}
