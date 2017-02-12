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
package com.gmail.socraticphoenix.forge.randore;

import com.gmail.socraticphoenix.forge.randore.resource.ResourceManager;
import com.gmail.socraticphoenix.forge.randore.template.TextureTemplate;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod(modid = "randores", name = "Socratic_Phoenix's Randores")
public class Randores {
    private static Randores instance;

    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.forge.randore.RandoreClientProxy", serverSide = "com.gmail.socraticphoenix.forge.randore.RandoreProxy")
    private static RandoreProxy proxy;

    private Logger logger;
    private File confDir;
    private File conf;
    private File tex;
    private Map<String, TextureTemplate> templates;
    private RandoresTab tab;

    public static Randores getInstance() {
        return instance;
    }

    public static RandoreProxy getProxy() {
        return proxy;
    }

    public Randores() {
        Randores.instance = this;
        this.tab = new RandoresTab();
        this.confDir = new File("config", "randores");
        this.conf = new File(this.confDir, "config.cfg");
        this.tex = new File(this.confDir, "textures");
        this.templates = new HashMap<String, TextureTemplate>();
        this.logger = LogManager.getLogger("randores");
        MinecraftForge.EVENT_BUS.register(new RandoresRegister());
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) throws IOException {
        this.tex.mkdirs();
        this.logger.info("Loading texture templates...");
        try {
            List<String> dictionary = ResourceManager.getResourceLines("aa_dict.txt");
            for (String entry : dictionary) {
                this.logger.info("Loading texture template \"" + entry + "\"");
                List<String> config = ResourceManager.getResourceLines(entry + ".txt");
                BufferedImage texture = ResourceManager.getImageResource(entry + ".png");
                TextureTemplate template = new TextureTemplate(config, texture);
                this.templates.put(entry, template);
                this.logger.info("Successfully loaded texture template \"" + entry + "\"");
            }
        } catch (IOException e) {
            this.logger.error("Fatal error: Unable to load resources");
            throw e;
        }
        this.logger.info("Testing the names algorithm...");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            this.logger.info(NameAlgorithm.name(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
        }
        this.logger.info("Finished testing names algorithm");
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        MinecraftForge.EVENT_BUS.register(new WorldEventListener());
        this.logger.info("Running proxy initialization...");
        Randores.proxy.init();
        this.logger.info("Proxy initialized.");
    }

    public static String textureName(int num) {
        return "randores.block." + num;
    }

    public static String blockName(int num) {
        return "randores.block." + num;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public File getConfDir() {
        return this.confDir;
    }

    public File getConf() {
        return this.conf;
    }

    public File getTexFile() {
        return this.tex;
    }

    public Map<String, TextureTemplate> getTemplates() {
        return this.templates;
    }

    public RandoresTab getTab() {
        return this.tab;
    }
}
