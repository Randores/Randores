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

import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;

public enum  MaterialType {
    INGOT(RandoresTranslations.Keys.INGOT, "ingot"),
    GEM(RandoresTranslations.Keys.GEM, "gem"),
    EMERALD(RandoresTranslations.Keys.EMERALD, "emerald"),
    CIRCLE_GEM(RandoresTranslations.Keys.CIRCLE_GEM, "circular_gem"),
    SHARD(RandoresTranslations.Keys.SHARD, "shard"),
    DUST(RandoresTranslations.Keys.DUST, "dust");

    private String template;
    private String name;

    MaterialType(String name, String template) {
        this.template = template + "_base";
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getTemplate() {
        return this.template;
    }

    public String getLocalName(String locale) {
        return RandoresTranslations.get(locale, this.getName());
    }

}
