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
import net.minecraft.inventory.EntityEquipmentSlot;

public enum Components {
    AXE(CraftableType.AXE, EntityEquipmentSlot.MAINHAND, 1),
    HOE(CraftableType.HOE, EntityEquipmentSlot.MAINHAND, 1),
    PICKAXE(CraftableType.PICKAXE, EntityEquipmentSlot.MAINHAND, 1),
    SHOVEL(CraftableType.SHOVEL, EntityEquipmentSlot.MAINHAND, 1),
    SWORD(CraftableType.SWORD, EntityEquipmentSlot.MAINHAND, 1),
    STICK(CraftableType.STICK, EntityEquipmentSlot.MAINHAND, 2),
    BOOTS(CraftableType.BOOTS, EntityEquipmentSlot.FEET, 1),
    CHESTPLATE(CraftableType.CHESTPLATE, EntityEquipmentSlot.CHEST, 1),
    HELMET(CraftableType.HELMET, EntityEquipmentSlot.HEAD, 1),
    LEGGINGS(CraftableType.LEGGINGS, EntityEquipmentSlot.LEGS, 1),
    BRICKS(CraftableType.BRICKS, EntityEquipmentSlot.MAINHAND, 4),
    TORCH(CraftableType.TORCH, EntityEquipmentSlot.MAINHAND, 4),
    MATERIAL(null, EntityEquipmentSlot.MAINHAND, 1),
    ORE(null, EntityEquipmentSlot.MAINHAND, 1);

    private CraftableType type;
    private EntityEquipmentSlot slot;
    private int quantity;

    Components(CraftableType type, EntityEquipmentSlot slot, int quantity) {
        this.type = type;
        this.slot = slot;
        this.quantity = quantity;
    }

    public int quantity() {
        return this.quantity;
    }

    public static Components fromIndex(int index, boolean block) {
        if(block) {
            if(CraftableType.TORCH.contains(index)) {
                return Components.TORCH;
            } else if (CraftableType.BRICKS.contains(index)) {
                return Components.BRICKS;
            } else if (index < Randores.registeredAmount()) {
                return Components.ORE;
            }
        } else {
            if(index < Randores.registeredAmount()) {
                return Components.MATERIAL;
            } else {
                for(Components component : Components.values()) {
                    if(component.isCraftable() && !component.getType().isBlock() && component.getType().contains(index)) {
                        return component;
                    }
                }
            }
        }

        throw new IllegalArgumentException("No component for index: " + index);
    }

    public boolean isBlock() {
        return (this.isCraftable() && this.getType().isBlock()) || this == Components.ORE;
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
