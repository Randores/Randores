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
package com.gmail.socraticphoenix.forge.randore.module.loot;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Component;
import com.gmail.socraticphoenix.forge.randore.component.CraftableComponent;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import com.google.common.base.Predicate;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FlexibleLootEntry extends LootEntry {
    private EnchantWithLevels enchantFunc;

    protected FlexibleLootEntry(int weightIn, int qualityIn, boolean doEnchant, int minLevel, int maxLevel, LootCondition[] conditionsIn, String entryName) {
        super(weightIn, qualityIn, conditionsIn, entryName);
        if (doEnchant) {
            this.enchantFunc = new EnchantWithLevels(new LootCondition[0], new RandomValueRange(minLevel, maxLevel), true);
        }
    }

    @Override
    public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context) {
        List<MaterialDefinition> materials = MaterialDefinitionRegistry.get(Randores.getRandoresSeed(context.getWorld()));
        MaterialDefinition definition = materials.get(rand.nextInt(materials.size()));
        if (rand.nextBoolean()) {
            IntRange size = new IntRange(definition.getOre().getMinDrops(), definition.getOre().getMaxDrops());
            ItemStack stack = new ItemStack(definition.getMaterial().makeItem(), size.randomElement(rand));
            Randores.applyData(stack, context.getWorld());
            stacks.add(stack);
        }

        for (int i = 0; i < rand.nextInt(this.quality) + 1; i++) {
            List<CraftableComponent> components = definition.getCraftables(new Predicate<CraftableType>() {
                @Override
                public boolean apply(@Nullable CraftableType input) {
                    return input != CraftableType.BRICKS;
                }
            });
            if (!components.isEmpty()) {
                Component toAdd = components.get(rand.nextInt(components.size()));
                ItemStack stack = new ItemStack(toAdd.makeItem(), toAdd.quantity() == 1 ? 1 : rand.nextInt(14) + 1);
                if (this.enchantFunc != null) {
                    stack = this.enchantFunc.apply(stack, rand, context);
                }
                Randores.applyData(stack, context.getWorld());
                stacks.add(stack);
            }
        }
    }

    @Override
    protected void serialize(JsonObject json, JsonSerializationContext context) {

    }

}
