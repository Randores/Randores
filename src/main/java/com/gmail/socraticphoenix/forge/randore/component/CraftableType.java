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
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;

public enum CraftableType {
    AXE(0, RandoresTranslations.Keys.AXE, "axe", true, true, false, true, true, " XX", " SX", " S "),
    HOE(0, RandoresTranslations.Keys.HOE, "hoe", true, true, false, true, false, " XX", " S ", " S "),
    PICKAXE(0, RandoresTranslations.Keys.PICKAXE, "pickaxe", true, true, false, true, true, "XXX", " S ", " S "),
    SHOVEL(0, RandoresTranslations.Keys.SHOVEL, "shovel", true, true, false, true, true, "X  ", "S  ", "S  "),
    SWORD(0, RandoresTranslations.Keys.SWORD, "sword", true, true, false, true, false, "X  ", "X  ", "S  "),
    STICK(0, RandoresTranslations.Keys.STICK, "stick", false, false, false, false, false, " X ", "X  ", "   "),
    BOOTS(0, RandoresTranslations.Keys.BOOTS, "boots", true, true, true, true, false, "X X", "X X", "   "),
    CHESTPLATE(0, RandoresTranslations.Keys.CHESTPLATE, "chestplate", true, true, true, true, false, "X X", "XXX", "XXX"),
    HELMET(0, RandoresTranslations.Keys.HELMET, "helmet", true, true, true, true, false, "XXX", "X X", "   "),
    LEGGINGS(0, RandoresTranslations.Keys.LEGGINGS, "leggings", true, true, true, true, false, "XXX", "X X", "X X"),
    BATTLEAXE(0, RandoresTranslations.Keys.BATTLEAXE, "battleaxe", true, true, false, true, true, "XXX", "XSX", " S "),
    SLEDGEHAMMER(0, RandoresTranslations.Keys.SLEDGEHAMMER, "sledgehammer", true, true, false, true, false, " XX", " SX", "S  "),
    BOW(0, RandoresTranslations.Keys.BOW, "bow", true, true, false, false, false, " XR", "S R", " XR"),
    BRICKS(-1, RandoresTranslations.Keys.BRICKS, "brick", false, false, false, false, false, "XX ", "XX ", "   "),
    TORCH(-1, RandoresTranslations.Keys.TORCH, "torch", false, false, false, false, false, " X ", "XTX", " X ");

    private String template;
    private String name;
    private String[] recipe;
    private int subtraction;

    private boolean enchant;
    private boolean durable;
    private boolean armor;
    private boolean damage;
    private boolean tool;

    CraftableType(int subtraction, String name, String template, boolean enchant, boolean durable, boolean armor, boolean damage, boolean tool, String... recipe) {
        this.template = template + "_base";
        this.recipe = recipe;
        this.name = name;
        this.subtraction = subtraction;
        this.enchant = enchant;
        this.durable = durable;
        this.armor = armor;
        this.damage = damage;
        this.tool = tool;
    }

    public boolean isTool() {
        return this.tool;
    }

    public boolean isArmor() {
        return this.armor;
    }

    public boolean isDamage() {
        return this.damage;
    }

    public boolean isEnchant() {
        return this.enchant;
    }

    public boolean isDurable() {
        return this.durable;
    }

    public boolean isBlock() {
        return this.getSubtraction() == BRICKS.ordinal();
    }

    public String getName() {
        return this.name;
    }

    public String getLocalName(String locale) {
        return RandoresTranslations.get(locale, this.getName());
    }

    public String getTemplate() {
        return this.template;
    }

    public String[] getRecipe() {
        return this.recipe;
    }

    public boolean contains(int index) {
        return this.getIndex(0) <= index && index < this.getIndex(0) + Randores.registeredAmount();
    }

    public int getIndex(int index) {
        return index + (Randores.registeredAmount() * (this.ordinal() + 1 - this.getSubtraction()));
    }

    public int getSubtraction() {
        if (this.subtraction == -1) {
            this.subtraction = BRICKS.ordinal();
        }
        return this.subtraction;
    }


}
