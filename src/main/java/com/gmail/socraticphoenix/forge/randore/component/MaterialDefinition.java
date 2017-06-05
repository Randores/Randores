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

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresNameAlgorithm;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.component.ability.Ability;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilitySeries;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityType;
import com.gmail.socraticphoenix.forge.randore.component.property.MaterialProperty;
import com.gmail.socraticphoenix.forge.randore.component.property.Properties;
import com.gmail.socraticphoenix.forge.randore.probability.RandoresProbability;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.tome.TomeGui;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MaterialDefinition {
    public static final Map<Character, Item> CRAFTING_MAPPINGS = new HashMap<Character, Item>();
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

    private AbilitySeries abilitySeries;

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

    public MaterialDefinition(Color color, OreComponent ore, List<CraftableComponent> craftables, List<MaterialProperty> properties, AbilitySeries series, long seed, int index) {
        this.abilitySeries = series;
        this.color = color;
        this.ore = ore;
        this.material = ore.getMaterial();
        this.craftables = craftables;
        this.name = RandoresNameAlgorithm.name(this.color);
        this.toolMaterial = EnumHelper.addToolMaterial(this.name, this.material.getHarvestLevel(), this.material.getMaxUses(), this.material.getEfficiency(), this.material.getDamage(), this.material.getEnchantability());
        this.toolMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        this.totalArmor = sum(this.material.getArmorReduction());
        this.armorMaterial = EnumHelper.addArmorMaterial(this.name, RandoresArmorResourcePack.DOMAIN + ":randores.armor." + index, this.material.getMaxUses() / 10, this.material.getArmorReduction(), this.material.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, this.material.getToughness());
        this.armorMaterial.setRepairItem(new ItemStack(this.material.makeItem()));
        this.seed = seed;
        this.index = index;

        this.components = new ArrayList<Component>();
        this.components.addAll(this.craftables);
        this.components.add(this.ore);
        this.components.add(this.material);

        this.properties = new HashMap<String, MaterialProperty>();
        for (MaterialProperty property : properties) {
            this.properties.put(property.name(), property);
        }
    }

    @SideOnly(Side.CLIENT)
    public List<TomeGui.Element> buildPages() {
        List<TomeGui.Element> pages = new ArrayList<TomeGui.Element>();
        String title = TextFormatting.DARK_AQUA + RandoresTranslations.get(RandoresTranslations.Keys.TOME).replace("${name}", this.getName()) + TextFormatting.RESET;

        List<String> s = new ArrayList<String>();
        s.add(title);
        s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.ORE_HARVEST_LEVEL) + ": " + this.ore.getHarvestLevel());
        s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.TYPE) + ": " + this.material.getLocalName(Randores.PROXY.getCurrentLocale()));
        if (this.hasTool()) {
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.HARVEST_LEVEL) + ": " + this.material.getHarvestLevel());
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.EFFICIENCY) + ": " + this.material.getEfficiency());
        }

        if (this.hasDurable()) {
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.DURABILITY) + ": " + this.material.getMaxUses());
        }

        if (this.hasEnchant()) {
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.ENCHANTABILITY) + ": " + this.material.getEnchantability());
        }

        if (this.hasDamage()) {
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.DAMAGE) + ": " + this.material.getDamage());
        }

        if (this.hasArmor()) {
            s.add(TextFormatting.DARK_GREEN + RandoresTranslations.get(RandoresTranslations.Keys.FULL_ARMOR) + ": " + this.totalArmor);
        }

        StringBuilder b = new StringBuilder();
        for (String k : s) {
            b.append(k).append("\n");
        }

        pages.add(new TomeGui.StringElement(b.substring(0, b.length() - 1), RandoresTranslations.Keys.INFORMATION));

        pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + name(this.material), new ItemStack(Items.STICK), new ItemStack(this.ore.makeItem()), new ItemStack(this.material.makeItem()), RandoresTranslations.Keys.OBTAINING));
        for (CraftableComponent component : this.craftables) {
            ItemStack[][] recipe = new ItemStack[3][3];
            String[] template = component.getType().getRecipe();
            for (int i = 0; i < template.length; i++) {
                String row = template[i];
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);
                    ItemStack stack = null;
                    if (c == 'X') {
                        stack = new ItemStack(this.material.makeItem());
                    } else if (CRAFTING_MAPPINGS.containsKey(c)) {
                        stack = new ItemStack(CRAFTING_MAPPINGS.get(c));
                    }
                    if (stack != null) {
                        recipe[i][j] = stack;
                    }
                }
            }

            ItemStack result = new ItemStack(component.makeItem());
            pages.add(new TomeGui.CraftingElement(title + "\n" + TextFormatting.DARK_GREEN + name(component), recipe, result, RandoresTranslations.Keys.RECIPES));
        }

        if (this.hasProperty(Properties.FLAMMABLE)) {
            pages.add(new TomeGui.FurnaceElement(title + "\n" + TextFormatting.DARK_GREEN + name(this.material), new ItemStack(this.material.makeItem()), new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT), RandoresTranslations.Keys.PROPERTIES));
        }

        if(this.hasDamage()) {
            this.applyAbilityToTome(title, AbilityType.MELEE, pages);
        }

        if(this.hasComponent(Components.BOW)) {
            this.applyAbilityToTome(title, AbilityType.PROJECTILE, pages);
        }

        if(this.hasArmor()) {
            this.applyAbilityToTome(title, AbilityType.ARMOR_ACTIVE, pages);
            this.applyAbilityToTome(title, AbilityType.ARMOR_PASSIVE, pages);
        }

        return pages;
    }

    @SideOnly(Side.CLIENT)
    private void applyAbilityToTome(String title, AbilityType type, List<TomeGui.Element> pages) {
        List<String> a = new ArrayList<String>();
        a.add(title);
        a.add(TextFormatting.GOLD + RandoresTranslations.get(RandoresTranslations.Keys.ABILITIES) + ": " + type.getLocalName());
        List<Ability> abilities = this.abilitySeries.getSeries(type);
        int i = 0;
        for (Ability ab : abilities) {
            i++;
            a.add(TextFormatting.DARK_GREEN + " " + i + ". " + ab.getLocalName());
        }

        StringBuilder bu = new StringBuilder();
        for (String k : a) {
            bu.append(k).append("\n");
        }
        pages.add(new TomeGui.StringElement(bu.substring(0, bu.length() - 1), RandoresTranslations.Keys.ABILITIES));
    }

    public boolean hasTool() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isTool()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasDurable() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isDurable()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasEnchant() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isEnchant()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasArmor() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isArmor()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasDamage() {
        for (CraftableComponent component : this.getCraftables()) {
            if (component.getType().isDamage()) {
                return true;
            }
        }

        return false;
    }

    private String name(Component component) {
        String local = component.getLocalName(Randores.PROXY.getCurrentLocale());
        String name = this.getName();
        String format = RandoresTranslations.get(RandoresTranslations.Keys.FORMAT);
        return format.replace("${name}", name).replace("${item}", local);
    }

    public MaterialDefinition setTotalArmor(int totalArmor) {
        this.totalArmor = totalArmor;
        return this;
    }

    public AbilitySeries getAbilitySeries() {
        return this.abilitySeries;
    }

    public MaterialDefinition setAbilitySeries(AbilitySeries abilitySeries) {
        this.abilitySeries = abilitySeries;
        return this;
    }

    public MaterialProperty getProperty(String name) {
        return this.properties.get(name);
    }

    public Collection<MaterialProperty> getProperties() {
        return this.properties.values();
    }

    public MaterialDefinition setProperties(Map<String, MaterialProperty> properties) {
        this.properties = properties;
        return this;
    }

    public boolean hasProperty(String name) {
        return this.properties.containsKey(name);
    }

    public int getIndex() {
        return this.index;
    }

    public MaterialDefinition setIndex(int index) {
        this.index = index;
        return this;
    }

    private String t(String s, String locale) {
        return RandoresTranslations.get(locale, s);
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

    public MaterialDefinition setToolMaterial(Item.ToolMaterial toolMaterial) {
        this.toolMaterial = toolMaterial;
        return this;
    }

    public ItemArmor.ArmorMaterial getArmorMaterial() {
        return this.armorMaterial;
    }

    public MaterialDefinition setArmorMaterial(ItemArmor.ArmorMaterial armorMaterial) {
        this.armorMaterial = armorMaterial;
        return this;
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

    public MaterialDefinition setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public MaterialDefinition setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public MaterialDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public OreComponent getOre() {
        return this.ore;
    }

    public MaterialDefinition setOre(OreComponent ore) {
        this.ore = ore;
        return this;
    }

    public MaterialComponent getMaterial() {
        return this.material;
    }

    public MaterialDefinition setMaterial(MaterialComponent material) {
        this.material = material;
        return this;
    }

    public List<CraftableComponent> getCraftables() {
        return this.craftables;
    }

    public MaterialDefinition setCraftables(List<CraftableComponent> craftables) {
        this.craftables = craftables;
        return this;
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

    public MaterialDefinition setComponents(List<Component> components) {
        this.components = components;
        return this;
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
