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
import com.gmail.socraticphoenix.forge.randore.component.property.MaterialProperty;
import com.gmail.socraticphoenix.forge.randore.probability.RandoresProbability;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.TextureData;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MaterialDefinition {
    public static final Function<Random, Boolean> DEFAULT_HUE_CHOICE = new Function<Random, Boolean>() {
        @Nullable
        @Override
        public Boolean apply(@Nullable Random input) {
            return RandoresProbability.percentChance(66.66666666666666666666, input);
        }
    };
    public static final Function<Random, Boolean> ARMOR_HUE_CHOICE = new Function<Random, Boolean>() {
        @Nullable
        @Override
        public Boolean apply(@Nullable Random input) {
            return RandoresProbability.percentChance(5, input);
        }
    };
    public static final Function<Random, Boolean> BRICK_HUE_CHOICE = new Function<Random, Boolean>() {
        @Nullable
        @Override
        public Boolean apply(@Nullable Random input) {
            return RandoresProbability.percentChance(15, input);
        }
    };

    private Color color;
    private String name;

    private OreComponent ore;
    private MaterialComponent material;
    private List<CraftableComponent> craftables;
    private List<Component> components;

    private Map<String, MaterialProperty> properties;

    private Item.ToolMaterial toolMaterial;
    private ItemArmor.ArmorMaterial armorMaterial;

    private long seed;
    private int totalArmor;

    private int index;

    public MaterialDefinition(Color color, OreComponent ore, List<CraftableComponent> craftables, List<MaterialProperty> properties, long seed, int index) {
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = RandoresNameAlgorithm.name(this.color);
        this.toolMaterial = EnumHelper.addToolMaterial(this.name, this.material.getHarvestLevel(), this.material.getMaxUses(), this.material.getEfficiency(), this.material.getDamage(), this.material.getEnchantability());
        this.toolMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        this.totalArmor = sum(this.material.getArmorReduction());
        this.armorMaterial = EnumHelper.addArmorMaterial(this.name, RandoresArmorResourcePack.DOMAIN + ":armor." + index, this.material.getMaxUses() / 10, this.material.getArmorReduction(), this.material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, this.material.getToughness());
        this.armorMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        this.seed = seed;
        this.index = index;

        this.components = new ArrayList<Component>();
        this.components.addAll(this.craftables);
        this.components.add(this.ore);
        this.components.add(this.material);

        this.properties = new HashMap<String, MaterialProperty>();
        for(MaterialProperty property : properties) {
            this.properties.put(property.name(), property);
        }
    }

    public MaterialProperty getProperty(String name) {
        return this.properties.get(name);
    }

    public Collection<MaterialProperty> getProperties() {
        return this.properties.values();
    }

    public boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }

    public int getIndex() {
        return this.index;
    }

    private String t(String s, String locale) {
        return RandoresTranslations.get(locale, s);
    }

    public List<String> generateBlockLore(String locale) {
        List<String> list = new ArrayList<String>();
        list.add(TextFormatting.GREEN + t(RandoresTranslations.Keys.INFORMATION, locale) + ":");
        list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.ORE_HARVEST_LEVEL, locale) + ": " + this.ore.getHarvestLevel());
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
        if(this.hasComponent(Components.BATTLEAXE)) {
            recipes += t(RandoresTranslations.Keys.BATTLEAXE_RECIPE, locale) + ", ";
        }
        if(this.hasComponent(Components.BOW)) {
            recipes += t(RandoresTranslations.Keys.BOW_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.STICK)) {
            recipes += t(RandoresTranslations.Keys.STICK_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.BRICKS)) {
            recipes += t(RandoresTranslations.Keys.BRICKS_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.TORCH)) {
            recipes += t(RandoresTranslations.Keys.TORCH_RECIPE, locale) + ", ";
        }
        recipes = recipes.substring(0, recipes.length() - 2);
        list.add(recipes);

        if(!this.properties.isEmpty()) {
            String properties = TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.PROPERTIES, locale) + ": ";
            for(MaterialProperty property : this.getProperties()) {
                properties += property.getLocalName(locale) + ", ";
            }
            properties = properties.substring(0, properties.length() - 2);
            list.add(properties);
        }

        return list;
    }

    public List<String> generateLore(String locale) {
        List<String> list = new ArrayList<String>();
        list.add(TextFormatting.GREEN + t(RandoresTranslations.Keys.INFORMATION, locale) + ":");
        if (this.hasComponent(Components.PICKAXE)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.EFFICIENCY, locale) + ": " + this.material.getEfficiency());
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.HARVEST_LEVEL, locale) + ": " + this.material.getHarvestLevel());
        }
        if (this.hasComponent(Components.HELMET)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.FULL_ARMOR, locale) + ": " + this.totalArmor);
        }
        if (this.hasComponent(Components.PICKAXE) || this.hasComponent(Components.SWORD)) {
            list.add(TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.DAMAGE, locale) + ": " + this.toolMaterial.getDamageVsEntity());
        }
        if (this.hasComponent(Components.PICKAXE) || this.hasComponent(Components.SWORD) || this.hasComponent(Components.HELMET)) {
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
        if(this.hasComponent(Components.BATTLEAXE)) {
            recipes += t(RandoresTranslations.Keys.BATTLEAXE_RECIPE, locale) + ", ";
        }
        if(this.hasComponent(Components.BOW)) {
            recipes += t(RandoresTranslations.Keys.BOW_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.STICK)) {
            recipes += t(RandoresTranslations.Keys.STICK_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.BRICKS)) {
            recipes += t(RandoresTranslations.Keys.BRICKS_RECIPE, locale) + ", ";
        }
        if (this.hasComponent(Components.TORCH)) {
            recipes += t(RandoresTranslations.Keys.TORCH_RECIPE, locale) + ", ";
        }
        recipes = recipes.substring(0, recipes.length() - 2);
        list.add(recipes);

        if(!this.properties.isEmpty()) {
            String properties = TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.PROPERTIES, locale) + ": ";
            for(MaterialProperty property : this.getProperties()) {
                properties += property.getLocalName(locale) + ", ";
            }
            properties = properties.substring(0, properties.length() - 2);
            list.add(properties);
        }

        return list;
    }

    private int sum(int[] arr) {
        int a = 0;
        for (int i : arr) {
            a += i;
        }
        return a;
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

    public long getSeed() {
        return this.seed;
    }

    public Map<String, TextureData> generateTextures() {
        Map<String, TextureData> textures = new HashMap<String, TextureData>();
        textures.put(ore.template(), RandoresClientSideRegistry.getTemplate(ore.template()).applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
        textures.put(material.template(), RandoresClientSideRegistry.getTemplate(material.template()).applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
        for (CraftableComponent component : this.craftables) {
            if (component.getType() == CraftableType.BOW) {
                textures.put("bow_standby", RandoresClientSideRegistry.getTemplate("bow_standby_base").applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_0", RandoresClientSideRegistry.getTemplate("bow_pulling_0_base").applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_1", RandoresClientSideRegistry.getTemplate("bow_pulling_1_base").applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_2", RandoresClientSideRegistry.getTemplate("bow_pulling_2_base").applyWith(this.color, MaterialDefinition.DEFAULT_HUE_CHOICE));
            } else {
                if (component.getType() == CraftableType.HELMET) {
                    textures.put("armor_1", RandoresClientSideRegistry.getTemplate("armor_over_base").applyWith(this.color, MaterialDefinition.ARMOR_HUE_CHOICE));
                    textures.put("armor_2", RandoresClientSideRegistry.getTemplate("armor_under_base").applyWith(this.color, MaterialDefinition.ARMOR_HUE_CHOICE));
                }

                Function<Random, Boolean> hueChoice;
                if (component.getType() == CraftableType.BRICKS) {
                    hueChoice = MaterialDefinition.BRICK_HUE_CHOICE;
                } else {
                    hueChoice = MaterialDefinition.DEFAULT_HUE_CHOICE;
                }
                textures.put(component.template(), RandoresClientSideRegistry.getTemplate(component.template()).applyWith(this.color, hueChoice));
            }
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

    public List<CraftableComponent> getCraftables(Predicate<CraftableType> filter) {
        List<CraftableComponent> components = new ArrayList<CraftableComponent>();
        for (CraftableComponent component : this.craftables) {
            if (filter.apply(component.getType())) {
                components.add(component);
            }
        }
        return components;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public List<Component> getComponents(Predicate<Components> filter) {
        List<Component> components = new ArrayList<Component>();
        for (Component component : this.components) {
            if (filter.apply(component.type())) {
                components.add(component);
            }
        }
        return components;
    }

}
