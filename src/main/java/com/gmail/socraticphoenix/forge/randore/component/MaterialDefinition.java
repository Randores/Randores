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
package com.gmail.socraticphoenix.forge.randore.component;

import com.gmail.socraticphoenix.forge.randore.NameAlgorithm;
import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.template.TextureTemplate;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MaterialDefinition {
    private Color color;
    private String name;

    private OreComponent ore;
    private MaterialComponent material;
    private List<CraftableComponent> craftables;

    public MaterialDefinition(Color color, OreComponent ore, List<CraftableComponent> craftables) {
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = NameAlgorithm.name(this.color);
    }

    public void registerRecipes() {
        if(this.ore.isRequiresSmelting()) {
            GameRegistry.addSmelting(this.ore.makeItem(), new ItemStack(this.material.makeItem(), this.ore.getMaxDrops()), this.ore.getSmeltingXp());
        }

        for(CraftableComponent component : this.craftables) {
            boolean stick = false;
            for(String s : component.recipe()) {
                if(s.contains("S")) {
                    stick = true;
                }
            }

            int compLen = component.recipe().length;
            Object[] params = new Object[component.recipe().length + (stick ? 4 : 2)];
            params[compLen] = 'X';
            params[compLen + 1] = this.material.makeItem();
            if(stick) {
                params[compLen + 2] = 'S';
                params[compLen + 3] = Items.STICK;
            }
            ShapedOreRecipe oreDictRecipe = new ShapedOreRecipe(new ItemStack(component.makeItem(), component.quantity()), params);
            GameRegistry.addRecipe(oreDictRecipe);
        }
    }

    public Map<String, BufferedImage> generateTextures(Random random) {
        Map<String, TextureTemplate> templates = Randores.getInstance().getTemplates();
        Map<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
        textures.put(ore.template(), templates.get(ore.template()).applyWith(this.color, random));
        textures.put(material.template(), templates.get(material.template()).applyWith(this.color, random));
        for(CraftableComponent component : this.craftables) {
            textures.put(component.template(), templates.get(component.template()).applyWith(this.color, random));
        }
        return textures;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public OreComponent getOre() {
        return this.ore;
    }

    public MaterialComponent getMaterial() {
        return this.material;
    }

    public List<CraftableComponent> getCraftables() {
        return this.craftables;
    }
}
