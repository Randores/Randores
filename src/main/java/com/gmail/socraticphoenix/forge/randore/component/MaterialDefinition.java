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

import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresNameAlgorithm;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

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

    private Item.ToolMaterial toolMaterial;
    private ItemArmor.ArmorMaterial armorMaterial;

    private long seed;

    public MaterialDefinition(Color color, OreComponent ore, List<CraftableComponent> craftables, long seed) {
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = RandoresNameAlgorithm.name(this.color);
        this.toolMaterial = EnumHelper.addToolMaterial(this.name, this.material.getHarvestLevel(), this.material.getMaxUses(), this.material.getEfficiency(), this.material.getDamage(), this.material.getEnchantability());
        this.seed = seed;
    }

    public long getSeed() {
        return this.seed;
    }

    public Map<String, BufferedImage> generateTextures(Random random) {
        Map<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
        textures.put(ore.template(), RandoresClientSideRegistry.getTemplate(ore.template()).applyWith(this.color, random));
        textures.put(material.template(), RandoresClientSideRegistry.getTemplate(material.template()).applyWith(this.color, random));
        for (CraftableComponent component : this.craftables) {
            if (component.getType() == CraftableType.HELMET) {
                textures.put("armor_1", RandoresClientSideRegistry.getTemplate("armor_over_base").applyWith(this.color, random));
                textures.put("armor_2", RandoresClientSideRegistry.getTemplate("armor_under_base").applyWith(this.color, random));
            }
            textures.put(component.template(), RandoresClientSideRegistry.getTemplate(component.template()).applyWith(this.color, random));
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
