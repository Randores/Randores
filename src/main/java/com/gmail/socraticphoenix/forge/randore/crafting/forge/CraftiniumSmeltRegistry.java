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
package com.gmail.socraticphoenix.forge.randore.crafting.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CraftiniumSmeltRegistry {
    private static List<CraftiniumSmelt> smelts = new ArrayList<CraftiniumSmelt>();

    public static void register(CraftiniumSmelt smelt) {
        CraftiniumSmeltRegistry.smelts.add(smelt);
    }

    public static List<FlexibleSmelt> getFlexible() {
        List<FlexibleSmelt> smelts = new ArrayList<FlexibleSmelt>();
        for(CraftiniumSmelt smelt : CraftiniumSmeltRegistry.smelts) {
            if(smelt instanceof FlexibleSmelt) {
                smelts.add((FlexibleSmelt) smelt);
            }
        }
        return smelts;
    }

    public static List<CraftiniumSmelt> getSmelts() {
        return CraftiniumSmeltRegistry.smelts;
    }

    public static CraftiniumSmelt findMatching(ItemStack in, World worldIn, BlockPos forge) {
        for (CraftiniumSmelt smelt : CraftiniumSmeltRegistry.smelts) {
            if (smelt.matches(in, worldIn, forge)) {
                return smelt;
            }
        }

        return null;
    }


    public static CraftiniumSmelt findMatchingXp(ItemStack out, World worldIn, BlockPos forge) {
        for (CraftiniumSmelt smelt : CraftiniumSmeltRegistry.smelts) {
            if (smelt.matchesExperience(out, worldIn, forge)) {
                return smelt;
            }
        }

        return null;
    }

}
