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
    AXE(0, RandoresTranslations.Keys.AXE, "axe", " XX", " SX", " S "),
    HOE(0, RandoresTranslations.Keys.HOE, "hoe", " XX", " S ", " S "),
    PICKAXE(0, RandoresTranslations.Keys.PICKAXE, "pickaxe", "XXX", " S ", " S "),
    SHOVEL(0, RandoresTranslations.Keys.SHOVEL, "shovel", "X  ", "S  ", "S  "),
    SWORD(0, RandoresTranslations.Keys.SWORD, "sword", "X  ", "X  ", "S  "),
    STICK(0, RandoresTranslations.Keys.STICK, "stick", " X ", "X  ", "   "),
    BOOTS(0, RandoresTranslations.Keys.BOOTS, "boots", "X X", "X X", "   "),
    CHESTPLATE(0, RandoresTranslations.Keys.CHESTPLATE, "chestplate", "X X", "XXX", "XXX"),
    HELMET(0, RandoresTranslations.Keys.HELMET, "helmet", "XXX", "X X", "   "),
    LEGGINGS(0, RandoresTranslations.Keys.LEGGINGS, "leggings", "XXX", "X X", "X X"),
    BATTLEAXE(0, RandoresTranslations.Keys.BATTLEAXE, "battleaxe", "XXX", "XSX", " S "),
    SLEDGEHAMMER(0, RandoresTranslations.Keys.SLEDGEHAMMER, "sledgehammer", " XX", " SX", "S  "),
    BOW(0, RandoresTranslations.Keys.BOW, "bow", " XR", "S R", " XR"),
    BRICKS(-1, RandoresTranslations.Keys.BRICKS, "brick", "XX ", "XX ", "   "),
    TORCH(-1, RandoresTranslations.Keys.TORCH, "torch", " X ", "XTX", " X ");

    private String template;
    private String name;
    private String[] recipe;
    private int subtraction;

    CraftableType(int subtraction, String name, String template, String... recipe) {
        this.template = template + "_base";
        this.recipe = recipe;
        this.name = name;
        this.subtraction = subtraction;
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
