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

import net.minecraft.inventory.EntityEquipmentSlot;

public enum Components {
    AXE(CraftableType.AXE, EntityEquipmentSlot.MAINHAND),
    HOE(CraftableType.HOE, EntityEquipmentSlot.MAINHAND),
    PICKAXE(CraftableType.PICKAXE, EntityEquipmentSlot.MAINHAND),
    SHOVEL(CraftableType.SHOVEL, EntityEquipmentSlot.MAINHAND),
    SWORD(CraftableType.SWORD, EntityEquipmentSlot.MAINHAND),
    STICK(CraftableType.STICK, EntityEquipmentSlot.MAINHAND),
    BOOTS(CraftableType.BOOTS, EntityEquipmentSlot.FEET),
    CHESTPLATE(CraftableType.CHESTPLATE, EntityEquipmentSlot.CHEST),
    HELMET(CraftableType.HELMET, EntityEquipmentSlot.HEAD),
    LEGGINGS(CraftableType.LEGGINGS, EntityEquipmentSlot.LEGS),
    BRICKS(CraftableType.BRICKS, EntityEquipmentSlot.MAINHAND),
    MATERIAL(null, EntityEquipmentSlot.MAINHAND),
    ORE(null, EntityEquipmentSlot.MAINHAND);

    private CraftableType type;
    private EntityEquipmentSlot slot;

    Components(CraftableType type, EntityEquipmentSlot slot) {
        this.type = type;
        this.slot = slot;
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

    public EntityEquipmentSlot getSlot() {
        return this.slot;
    }

    public CraftableType getType() {
        return this.type;
    }

}
