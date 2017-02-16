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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingGuiHandler;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.packet.RandoresNetworking;
import com.google.common.base.Supplier;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod(modid = "randores", name = "Socratic_Phoenix's Randores")
public class Randores {
    public static final String RESET = "§" + 'r';
    public static final RandoresTab TAB_CRAFTING = new RandoresTab("randores_tab_0", new Supplier<Item>() {
        @Override
        public Item get() {
            return CraftingItems.CRAFTINIUM_LUMP;
        }
    });
    public static final RandoresTab TAB_BLOCKS = new RandoresTab("randores_tab_1", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(FlexibleBlockRegistry.getOres().get(0));
        }
    });
    public static final RandoresTab TAB_ITEMS = new RandoresTab("randores_tab_2", new Supplier<Item>() {
        @Override
        public Item get() {
            return FlexibleItemRegistry.getMaterials().get(0);
        }
    });
    private static Randores instance;
    private static Map<Long, Long> worldSeeds = new HashMap<Long, Long>();
    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.forge.randore.RandoresClientProxy", serverSide = "com.gmail.socraticphoenix.forge.randore.RandoresProxy")
    private static RandoresProxy proxy;

    private Logger logger;
    private File confDir;
    private File conf;
    private File tex;
    private Configuration configuration;

    public Randores() {
        Randores.instance = this;
        this.confDir = new File("config", "randores");
        this.conf = new File(this.confDir, "config.cfg");
        this.configuration = new Configuration(this.conf);
        this.tex = new File(this.confDir, "textures");
        this.logger = LogManager.getLogger("Randores");
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new RandoresClientSideListener());
        }
        MinecraftForge.EVENT_BUS.register(new RandoresRegistryListener());
        MinecraftForge.EVENT_BUS.register(new RandoresClientListener());
        MinecraftForge.EVENT_BUS.register(new RandoresWorldEventListener());
        MinecraftForge.EVENT_BUS.register(new RandoresItemListener());
    }

    public static long getRandoresSeedFromWorld(long worldSeed) {
        Long seed = Randores.worldSeeds.get(worldSeed);
        if (seed == null) {
            seed = new Random(worldSeed).nextLong();
            Randores.worldSeeds.put(worldSeed, seed);
        }
        return seed;
    }

    public static long getRandoresSeed(World world) {
        if (!world.isRemote) {
            return Randores.getRandoresSeedFromWorld(world.getSeed());
        } else if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            return RandoresClientSideRegistry.getCurrentSeed();
        } else {
            throw new IllegalArgumentException("World is remote and we're not on the client!");
        }
    }

    public static Randores getInstance() {
        return instance;
    }

    public static RandoresProxy getProxy() {
        return proxy;
    }

    public static String textureName(int num) {
        return "randores:blocks/randores.block." + num;
    }

    public static String blockName(int num) {
        return "randores.block." + num;
    }

    public static String itemTextureName(int num) {
        return "randores:items/randores.item." + num;
    }

    public static String itemName(int num) {
        return "randores.item." + num;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) throws IOException {
        this.tex.mkdirs();
        if (!this.conf.exists()) {
            this.conf.createNewFile();
        }

        this.logger.info("Testing the names algorithm...");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            this.logger.info(RandoresNameAlgorithm.name(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
        }
        this.logger.info("Finished testing names algorithm");
        RandoresNetworking.initNetwork();
        this.logger.info("Running proxy pre-initialization...");
        Randores.proxy.preInitSided();
        this.logger.info("Proxy pre-initialized.");

    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CraftingGuiHandler());
        GameRegistry.registerWorldGenerator(new RandoresWorldGenerator(), 0);
        this.logger.info("Running proxy initialization...");
        Randores.proxy.initSided();
        this.logger.info("Proxy initialized.");
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

    public File getTextureFile(long seed) {
        return new File(this.tex, String.valueOf(seed).replaceAll("-", "_"));
    }

}
