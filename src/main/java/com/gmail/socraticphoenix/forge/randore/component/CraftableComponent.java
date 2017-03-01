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

import net.minecraft.item.Item;

public class CraftableComponent implements Component {
    private CraftableType type;
    private Components component;
    private int quantity;
    private Item item;

    public CraftableComponent(CraftableType type, int quantity, Item item) {
        this.type = type;
        this.quantity = quantity;
        this.item = item;
        this.component = Components.fromCraftable(type);
    }

    @Override
    public Item makeItem() {
        return this.item;
    }

    @Override
    public int quantity() {
        return this.quantity;
    }

    @Override
    public String getName() {
        return this.type.getName();
    }

    @Override
    public String getLocalName(String locale) {
        return this.type.getLocalName(locale);
    }

    @Override
    public Components type() {
        return this.component;
    }

    public String template() {
        return this.type.getTemplate();
    }

    public String[] recipe() {
        return this.type.getRecipe();
    }

    public CraftableType getType() {
        return this.type;
    }
}
