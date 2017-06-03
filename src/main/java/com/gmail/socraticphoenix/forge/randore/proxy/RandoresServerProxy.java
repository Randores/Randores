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
package com.gmail.socraticphoenix.forge.randore.proxy;

import com.gmail.socraticphoenix.forge.randore.Randores;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class RandoresServerProxy implements RandoresProxy {
    private AtomicInteger oreCount = new AtomicInteger();

    @Override
    public void preInit(FMLPreInitializationEvent ev) throws IOException, IllegalAccessException {
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Randores is running server-side.");
    }

    @Override
    public void init(FMLInitializationEvent ev) throws IOException {

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public String getCurrentLocale() {
        return "en_us";
    }

    @Override
    public int oreCount() {
        return this.oreCount.get();
    }

    @Override
    public void setOreCount(int count) {
        this.oreCount.set(count);
    }

    @Override
    public long seed() {
        return 0;
    }

}
