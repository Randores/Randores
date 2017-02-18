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

public enum Components {
    AXE(CraftableType.AXE),
    HOE(CraftableType.HOE),
    PICKAXE(CraftableType.PICKAXE),
    SHOVEL(CraftableType.SHOVEL),
    SWORD(CraftableType.SWORD),
    STICK(CraftableType.STICK),
    BOOTS(CraftableType.BOOTS),
    CHESTPLATE(CraftableType.CHESTPLATE),
    HELMET(CraftableType.HELMET),
    LEGGINGS(CraftableType.LEGGINGS),
    BRICKS(CraftableType.BRICKS),
    MATERIAL(null),
    ORE(null);

    private CraftableType type;

    Components(CraftableType type) {
        this.type = type;
    }

    public static Components fromCraftable(CraftableType type) {
        for (Components components : Components.values()) {
            if(components.getType() == type) {
                return components;
            }
        }
        return null;
    }

    public boolean isCraftable() {
        return this.type != null;
    }

    public CraftableType getType() {
        return this.type;
    }

}
