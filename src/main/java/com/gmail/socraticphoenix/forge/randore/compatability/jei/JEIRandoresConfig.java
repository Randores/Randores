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

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresTabBlocks;
import com.gmail.socraticphoenix.forge.randore.RandoresTabItems;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.crafting.CraftingRecipeCategory;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.crafting.FlexibleCraftingRecipeHandler;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.crafting.FlexibleRecipeHandler;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.furnace.FlexibleSmeltHandler;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.furnace.FurnaceSmeltingCategory;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeGui;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumSmeltRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumRecipeRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableGui;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.transfer.PlayerRecipeTransferHandler;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;

@JEIPlugin
public class JEIRandoresConfig implements IModPlugin {
    public static final String CRAFTING_CATEGORY = "randores.crafting";
    public static final String SMELTING_CATEGORY = "randores.smelting";

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        for (FlexibleItem item : FlexibleItemRegistry.getAll()) {
            subtypeRegistry.registerSubtypeInterpreter(item.getThis(), new ISubtypeRegistry.ISubtypeInterpreter() {
                @Nullable
                @Override
                public String getSubtypeInfo(ItemStack itemStack) {
                    return "";
                }
            });
        }

        for (FlexibleItem item : FlexibleBlockRegistry.getAll()) {
            subtypeRegistry.registerSubtypeInterpreter(item.getThis(), new ISubtypeRegistry.ISubtypeInterpreter() {
                @Nullable
                @Override
                public String getSubtypeInfo(ItemStack itemStack) {
                    return "";
                }
            });
        }
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumTable), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumForge), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumForge), VanillaRecipeCategoryUid.FUEL);

        if (RandoresClientSideRegistry.isInitialized()) {
            registry.getIngredientRegistry().getIngredients(ItemStack.class);
            IJeiHelpers jeiHelpers = registry.getJeiHelpers();

            IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
            registry.addRecipeCategories(
                    new CraftingRecipeCategory(guiHelper),
                    new FurnaceSmeltingCategory(guiHelper)
            );

            registry.addRecipeHandlers(
                    new FlexibleRecipeHandler(jeiHelpers),
                    new FlexibleCraftingRecipeHandler(jeiHelpers),
                    new FlexibleSmeltHandler()
            );

            registry.addRecipeClickArea(CraftiniumTableGui.class, 88, 32, 28, 23, JEIRandoresConfig.CRAFTING_CATEGORY);
            registry.addRecipeClickArea(GuiInventory.class, 137, 29, 10, 13, JEIRandoresConfig.CRAFTING_CATEGORY);
            registry.addRecipeClickArea(CraftiniumForgeGui.class, 78, 32, 28, 23, JEIRandoresConfig.SMELTING_CATEGORY);

            IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

            recipeTransferRegistry.addRecipeTransferHandler(CraftiniumTableContainer.class, JEIRandoresConfig.CRAFTING_CATEGORY, 1, 9, 10, 36);
            recipeTransferRegistry.addRecipeTransferHandler(new PlayerRecipeTransferHandler(jeiHelpers.recipeTransferHandlerHelper()), VanillaRecipeCategoryUid.CRAFTING);
            recipeTransferRegistry.addRecipeTransferHandler(CraftiniumForgeContainer.class, JEIRandoresConfig.SMELTING_CATEGORY, 0, 1, 3, 36);

            registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumTable), JEIRandoresConfig.CRAFTING_CATEGORY);
            registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumForge), JEIRandoresConfig.SMELTING_CATEGORY);

            registry.addRecipes(CraftiniumRecipeRegistry.getFlexible());
            registry.addRecipes(CraftiniumSmeltRegistry.getSmelts());

            IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
            //IIngredientBlacklist oreBlacklist = registry.getJeiHelpers().getOreIngredientBlacklist();
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabItem));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabAxe));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHelmet));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHoe));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabPickaxe));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabShovel));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabStick));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabSword));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabBattleaxe));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabSledgehammer));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabBow));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabBlocks.tabOre));
            blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabBlocks.tabTorch));

            for (MaterialDefinition definition : MaterialDefinitionRegistry.get(RandoresClientSideRegistry.getCurrentSeed())) {
                for (CraftableType type : CraftableType.values()) {
                    if (!definition.hasComponent(Components.fromCraftable(type))) {
                        ItemStack stack;
                        if (type == CraftableType.BRICKS) {
                            stack = new ItemStack(FlexibleBlockRegistry.getBricks().get(definition.getIndex()));
                        } else if (type == CraftableType.TORCH) {
                            stack = new ItemStack(FlexibleBlockRegistry.getTorches().get(definition.getIndex()));
                        } else {
                            stack = new ItemStack(FlexibleItemRegistry.get(type, definition.getIndex()));
                        }
                        blacklist.addIngredientToBlacklist(stack);
                        //oreBlacklist.addIngredientToBlacklist(stack);
                    } else {
                        Item item = definition.getComponent(Components.fromCraftable(type)).makeItem();
                        ItemStack testTool = new ItemStack(item);
                        ItemStack testMaterial = new ItemStack(definition.getMaterial().makeItem());
                        Randores.applyData(testTool, RandoresClientSideRegistry.getCurrentSeed());
                        Randores.applyData(testMaterial, RandoresClientSideRegistry.getCurrentSeed());

                        ItemStack damaged1 = testTool.copy();
                        damaged1.setItemDamage(damaged1.getMaxDamage());
                        ItemStack damaged2 = testTool.copy();
                        damaged2.setItemDamage(damaged2.getMaxDamage() * 3 / 4);
                        ItemStack damaged3 = testTool.copy();
                        damaged3.setItemDamage(damaged3.getMaxDamage() * 2 / 4);

                        if (item.isRepairable() && item.getIsRepairable(testTool, testMaterial)) {
                            registry.addAnvilRecipe(damaged1, Arrays.asList(testMaterial), Arrays.asList(damaged2));
                            registry.addAnvilRecipe(damaged2, Arrays.asList(damaged2), Arrays.asList(damaged3));
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
