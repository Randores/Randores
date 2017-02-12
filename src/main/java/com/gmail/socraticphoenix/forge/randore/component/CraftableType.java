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

public enum  CraftableType {
    AXE("Axe", "axe", " XX", " SX", " S "),
    HOE("Hoe", "hoe", " XX", " S ", " S "),
    PICKAXE("Pickaxe", "pickaxe", "XXX", " S ", " S "),
    SHOVEL("Shovel", "shovel", " X ", " S ", " S "),
    SWORD("Sword", "sword", " X ", " X ", " S "),
    STICK("Stick", "stick", " X ", "X  "),
    BOOTS("Boots", "boots", "X X", "X X"),
    CHESTPLATE("Chestplate", "chestplate", "X X", "XXX", "XXX"),
    HELMET("Helmet", "helmet", "XXX", "X X"),
    LEGGINGS("Leggings", "leggings", "XXX", "X X", "X X"),
    BRICKS("Bricks", "brick", "XX ", "XX ");

    private String template;
    private String name;
    private String[] recipe;

    CraftableType(String name, String template, String... recipe) {
        this.template = template + "_base";
        this.recipe = recipe;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getTemplate() {
        return this.template;
    }

    public String[] getRecipe() {
        return this.recipe;
    }

}
