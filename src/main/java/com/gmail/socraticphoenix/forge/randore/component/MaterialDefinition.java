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
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialDefinition {
    private Color color;
    private String name;

    private OreComponent ore;
    private MaterialComponent material;
    private List<CraftableComponent> craftables;

    private Item.ToolMaterial toolMaterial;
    private ItemArmor.ArmorMaterial armorMaterial;

    private long seed;
    private int totalArmor;

    public MaterialDefinition(Color color, OreComponent ore, List<CraftableComponent> craftables, long seed, int index) {
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = RandoresNameAlgorithm.name(this.color);
        this.toolMaterial = EnumHelper.addToolMaterial(this.name, this.material.getHarvestLevel() + 1, this.material.getMaxUses(), this.material.getEfficiency(), this.material.getDamage(), this.material.getEnchantability());
        this.toolMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        float armor = this.material.getEfficiency() * 3;
        int[] reduc = new int[]{(int) Math.abs(Math.ceil(armor * 0.15)), (int) Math.abs(Math.ceil(armor * 0.3)), (int) Math.abs(Math.ceil(armor * 0.4)), (int) Math.abs(Math.ceil(armor * 0.15))};
        while (sum(reduc) > 20) {
            for (int i = 0; i < reduc.length; i++) {
                reduce(reduc, 0);
                reduce(reduc, 1);
                reduce(reduc, 1);
                reduce(reduc, 2);
                reduce(reduc, 2);
                reduce(reduc, 3);

            }
        }
        this.totalArmor = sum(reduc);
        this.armorMaterial = EnumHelper.addArmorMaterial(this.name, "randores_armor:armor." + index, this.material.getMaxUses() / 100, reduc, this.material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, this.material.getToughness());
        this.armorMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        this.seed = seed;
    }

    private String t(String s, String locale) {
        return RandoresTranslations.get(locale, s);
    }

    public List<String> generateLore(String locale) {
        List<String> list = new ArrayList<String>();
        list.add(TextFormatting.GREEN + t(RandoresTranslations.Keys.INFORMATION, locale) + ":");
        if (this.hasComponent(Components.PICKAXE)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.EFFICIENCY, locale) + ": " + this.material.getEfficiency());
        }
        if(this.hasComponent(Components.HELMET)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.FULL_ARMOR, locale) + ": " + this.totalArmor);
        }
        if(this.hasComponent(Components.PICKAXE) || this.hasComponent(Components.SWORD)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.DAMAGE, locale) + ": " + this.toolMaterial.getDamageVsEntity());
        }
        if(this.hasComponent(Components.PICKAXE) || this.hasComponent(Components.SWORD) || this.hasComponent(Components.HELMET)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.DURABILITY, locale) + ": " + this.material.getMaxUses());
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.ENCHANTABILITY, locale) + ": " + this.material.getEnchantability());
        }
        String recipes = TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.RECIPES, locale) + ": ";
        if (this.hasComponent(Components.PICKAXE)) {
            recipes += t(RandoresTranslations.Keys.TOOLS_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.HELMET)) {
            recipes += t(RandoresTranslations.Keys.ARMOR_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.SWORD)) {
            recipes += t(RandoresTranslations.Keys.SWORD_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.BRICKS)) {
            recipes += t(RandoresTranslations.Keys.BRICKS_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.STICK)) {
            recipes += t(RandoresTranslations.Keys.STICK_RECIPE, locale) + ", ";
        }
        recipes = recipes.substring(0, recipes.length() - 2);
        list.add(recipes);
        return list;
    }

    private void reduce(int[] arr, int slot) {
        if (arr[slot] > 2) {
            arr[slot] = arr[slot] - 1;
        }
    }

    public Item.ToolMaterial getToolMaterial() {
        return this.toolMaterial;
    }

    public ItemArmor.ArmorMaterial getArmorMaterial() {
        return this.armorMaterial;
    }

    public boolean hasComponent(Components component) {
        return this.getComponent(component) != null;
    }

    public Component getComponent(Components component) {
        if (component.isCraftable()) {
            for (CraftableComponent craftable : this.getCraftables()) {
                if (craftable.getType() == component.getType()) {
                    return craftable;
                }
            }
        } else if (component == Components.ORE) {
            return this.ore;
        } else if (component == Components.MATERIAL) {
            return this.material;
        }
        return null;
    }

    private int sum(int[] arr) {
        int a = 0;
        for (int i : arr) {
            a += i;
        }
        return a;
    }

    public long getSeed() {
        return this.seed;
    }

    public Map<String, BufferedImage> generateTextures() {
        Map<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
        textures.put(ore.template(), RandoresClientSideRegistry.getTemplate(ore.template()).applyWith(this.color));
        textures.put(material.template(), RandoresClientSideRegistry.getTemplate(material.template()).applyWith(this.color));
        for (CraftableComponent component : this.craftables) {
            if (component.getType() == CraftableType.HELMET) {
                textures.put("armor_1", RandoresClientSideRegistry.getTemplate("armor_over_base").applyWith(this.color));
                textures.put("armor_2", RandoresClientSideRegistry.getTemplate("armor_under_base").applyWith(this.color));
            }
            textures.put(component.template(), RandoresClientSideRegistry.getTemplate(component.template()).applyWith(this.color));
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
