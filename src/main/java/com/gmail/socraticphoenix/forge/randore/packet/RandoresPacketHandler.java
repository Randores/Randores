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
package com.gmail.socraticphoenix.forge.randore.packet;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandoresPacketHandler implements IMessageHandler<RandoresPacket, IMessage> {
    private static int index = 0;

    @Override
    public IMessage onMessage(final RandoresPacket message, final MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntityPlayer player = RandoresClientSideRegistry.getClientPlayer();
                player.sendMessage(new TextComponentString("[Randores] Randores is setting up textures (in a separate thread)... Your resources will be reloaded in a moment."));
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Logger logger = Randores.getInstance().getLogger();
                final long seed = message.getSeed();
                RandoresClientSideRegistry.setCurrentSeed(seed);
                logger.info("Received seed information: " + seed);
                logger.info("Obtaining definitions...");
                List<MaterialDefinition> definitions;
                if (!MaterialDefinitionRegistry.contains(seed)) {
                    logger.info("No definitions found, generating...");
                    definitions = MaterialDefinitionGenerator.makeDefinitions(MaterialDefinitionGenerator.generateColors(new Random(seed)), seed);
                    MaterialDefinitionRegistry.put(seed, definitions);
                    logger.info("Definitions generated.");
                } else {
                    logger.info("Definitions already registered, loading...");
                    definitions = MaterialDefinitionRegistry.get(seed);
                }
                for (int i = 0; i < 300; i++) {
                    Item.ToolMaterial toolMaterial = definitions.get(i).getToolMaterial();
                    ItemArmor.ArmorMaterial armorMaterial = definitions.get(i).getArmorMaterial();
                    FlexibleItemRegistry.getHoe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getSword(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getAxe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getSpade(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getPickaxe(i).registerBacker(seed, toolMaterial);
                    FlexibleItemRegistry.getHelmet(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getChestplate(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getLeggings(i).registerBacker(seed, armorMaterial);
                    FlexibleItemRegistry.getBoots(i).registerBacker(seed, armorMaterial);
                }
                logger.info("Definitions Statistics:");
                MaterialDefinitionGenerator.logStatistics(definitions);
                File texture = Randores.getInstance().getTextureFile(seed);
                Configuration configuration = Randores.getInstance().getConfiguration();
                ConfigCategory category = configuration.getCategory("TextureCache");
                category.put(String.valueOf(seed), new Property(String.valueOf(seed), String.valueOf(System.currentTimeMillis()), Property.Type.STRING));
                configuration.save();
                if (texture.exists()) {
                    logger.info("Textures are cached for seed: " + seed + ", setting up textures and reloading resources...");
                    MaterialDefinitionGenerator.setupTextures(definitions, seed);
                } else {
                    logger.info("Textures are not cached for seed: " + seed + ", generating textures");
                    try {
                        MaterialDefinitionGenerator.generateAndSetupTextures(definitions, seed);
                    } catch (IOException e) {
                        Minecraft.getMinecraft().crashed(new CrashReport("Failed to generate Randores textures.", e));
                        return;
                    }
                    logger.info("Generated textures... reloading resources...");
                }

                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        EntityPlayer player = RandoresClientSideRegistry.getClientPlayer();
                        if (!FlexibleTextureRegistry.isInitialized() || FlexibleTextureRegistry.getTextureSeed() != seed) {
                            player.sendMessage(new TextComponentString("[Randores] Reloading your resources! Expect a screen freeze for ~30 seconds. (Also, if you move your mouse or attempt to give the game any sort of input, it will probably crash.)"));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException ignore) {

                                    }

                                    Minecraft.getMinecraft().scheduleResourcesRefresh();
                                }
                            }).start();
                        } else {
                            player.sendMessage(new TextComponentString("[Randores] It looks like your resources are already set up... They won't be reloaded."));
                        }
                        FlexibleTextureRegistry.setInitialized(true);
                        FlexibleTextureRegistry.setTextureSeed(seed);
                        logger.info("Resources loaded, definitions loaded, and textures properly set up. Randores is ready!");
                    }
                });

            }
        };

        Thread thread = new Thread(runnable, "Randores-Texture-Thread-" + index++);
        thread.start();

        return null;
    }

}
