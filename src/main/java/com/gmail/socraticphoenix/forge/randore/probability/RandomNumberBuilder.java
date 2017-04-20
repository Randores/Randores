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
package com.gmail.socraticphoenix.forge.randore.probability;

import com.google.common.base.Function;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomNumberBuilder {
    private Map<String, Object> results;
    private Random random;
    private String prevKey;

    public RandomNumberBuilder(Random random) {
        this.results = new HashMap<String, Object>();
        this.random = random;
    }

    public Object get(String key) {
        return this.results.get(key);
    }

    public Number getNumber(String key) {
        Object obj = this.results.get(key);
        if(obj instanceof Number) {
            return (Number) obj;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj ? 1 : 0;
        } else {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        Object obj = this.results.get(key);
        if(obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0;
        } else {
            return false;
        }
    }

    public int getInt(String key) {
        return this.getNumber(key).intValue();
    }

    public double getDouble(String key) {
        return this.getNumber(key).doubleValue();
    }

    public RandomNumberBuilder percentChance(String key, double chance) {
        put(key, RandoresProbability.percentChance(chance, this.random));
        return this;
    }

    public RandomNumberBuilder rand(String key, int upper) {
        put(key, this.random.nextInt(upper));
        return this;
    }

    public RandomNumberBuilder rand(String key, double min, double max) {
        put(key, RandoresProbability.rand(min, max, this.random));
        return this;
    }

    public RandomNumberBuilder expRand(String key, double b, double min, double max) {
        put(key, RandoresProbability.expRand(b, min, max, this.random));
        return this;
    }

    public RandomNumberBuilder oneSidedNormalRand(String key, double min, double stdev) {
        put(key, RandoresProbability.oneSidedNormalRand(min, stdev, this.random));
        return this;
    }

    public RandomNumberBuilder normalRand(String key, double mean, double stdev) {
        put(key, RandoresProbability.normalRand(mean, stdev, this.random));
        return this;
    }

    public RandomNumberBuilder oneSidedInflectedNormalRand(String key, double min, double max, double stdev) {
        put(key, RandoresProbability.oneSidedInflectedNormalRand(min, max, stdev, this.random));
        return this;
    }

    public RandomNumberBuilder inflectedNormalRand(String key, double min, double max, double mean, double stdev) {
        put(key, RandoresProbability.inflectedNormalRand(min, max, mean, stdev, this.random));
        return this;
    }

    public RandomNumberBuilder clamp(int min, int max) {
        put(this.prevKey, RandoresProbability.clamp(this.getInt(this.prevKey), min, max));
        return this;
    }

    public RandomNumberBuilder clamp(double min, double max) {
        put(this.prevKey, RandoresProbability.clamp(this.getDouble(this.prevKey), min, max));
        return this;
    }

    public RandomNumberBuilder scale(double min, double max) {
        put(this.prevKey, RandoresProbability.scale(min, max, this.getDouble(this.prevKey)));
        return this;
    }

    public RandomNumberBuilder op(Function<Number, Number> func) {
        put(this.prevKey, func.apply(this.getNumber(this.prevKey)));
        return this;
    }

    public RandomNumberBuilder copy(String src, String dest) {
        put(dest, this.results.get(src));
        return this;
    }

    public RandomNumberBuilder put(String key, Object x) {
        this.results.put(key, x);
        this.prevKey = key;
        return this;
    }

}
