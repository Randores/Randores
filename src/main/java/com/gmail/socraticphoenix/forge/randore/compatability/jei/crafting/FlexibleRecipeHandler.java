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
package com.gmail.socraticphoenix.forge.randore.compatability.jei.crafting;

import com.gmail.socraticphoenix.forge.randore.compatability.jei.JEIRandoresConfig;
import com.gmail.socraticphoenix.forge.randore.crafting.table.FlexibleRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.List;

public class FlexibleRecipeHandler implements IRecipeHandler<FlexibleRecipe> {
    private IJeiHelpers helpers;

    public FlexibleRecipeHandler(IJeiHelpers helpers) {
        this.helpers = helpers;
    }

    @Override
    public Class<FlexibleRecipe> getRecipeClass() {
        return FlexibleRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(FlexibleRecipe flexibleRecipe) {
        return JEIRandoresConfig.CRAFTING_CATEGORY;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(FlexibleRecipe flexibleRecipe) {
        return new FlexibleRecipeWrapper(flexibleRecipe, this.helpers);
    }

    @Override
    public boolean isRecipeValid(FlexibleRecipe recipe) {
        if (!recipe.tryClientIsRegistered()) {
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.getInputs()) {
            if (input instanceof List && ((List) input).isEmpty()) {
                // missing items for an oreDict name. This is normal behavior, but the recipe is invalid.
                return false;
            }
            if (input != null) {
                inputCount++;
            }
        }
        if (inputCount > 9) {
            return false;
        }
        if (inputCount == 0) {
            return false;
        }
        return true;
    }

}
