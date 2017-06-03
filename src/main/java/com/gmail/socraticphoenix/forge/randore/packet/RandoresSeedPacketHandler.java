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
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RandoresSeedPacketHandler implements IMessageHandler<RandoresSeedPacket, IMessage> {
    private static int index = 0;

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final RandoresSeedPacket message, final MessageContext ctx) {
        Runnable runnable = new Runnable() {
            @Override
            @SideOnly(Side.CLIENT)
            public void run() {
                final Logger logger = Randores.getInstance().getLogger();
                final long seed = message.getSeed();
                final int count = message.getOreCount();
                RandoresClientSideRegistry.setCurrentSeed(seed);
                Randores.PROXY.setOreCount(count);
                Randores.getInstance().getConfiguration().load();
                logger.info("Received seed information: " + seed);
                logger.info("Recieved ore-count information: " + count);
                logger.info("Obtaining definitions...");
                final List<MaterialDefinition> definitions;
                if (!MaterialDefinitionRegistry.contains(seed)) {
                    logger.info("No definitions found, generating...");
                    MaterialDefinitionGenerator.registerDefinitionsIfNeeded(seed);
                    definitions = MaterialDefinitionRegistry.get(seed);
                    logger.info("Definitions generated.");
                } else {
                    logger.info("Definitions already registered, loading...");
                    definitions = MaterialDefinitionRegistry.get(seed);
                }
                logger.info("Definitions Statistics:");
                MaterialDefinitionGenerator.logStatistics(definitions);

                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    @SideOnly(Side.CLIENT)
                    public void run() {
                        EntityPlayer player = RandoresClientSideRegistry.getClientPlayer();
                        if (!FlexibleTextureRegistry.isInitialized() || FlexibleTextureRegistry.getTextureSeed() != seed) {
                            player.sendMessage(new TextComponentString("[Randores] " + RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), RandoresTranslations.Keys.RESOURCES_RELOADING)));
                            new Thread(new Runnable() {
                                @Override
                                @SideOnly(Side.CLIENT)
                                public void run() {
                                    try {
                                        TimeUnit.SECONDS.sleep(5);
                                    } catch (InterruptedException ignore) {

                                    }
                                    Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                                        @Override
                                        @SideOnly(Side.CLIENT)
                                        public void run() {
                                            RandoresNetworking.INSTANCE.sendToServer(new RandoresTexturePacket().setLoading(true));
                                            Minecraft.getMinecraft().refreshResources();
                                            RandoresNetworking.INSTANCE.sendToServer(new RandoresTexturePacket().setLoading(false));
                                        }
                                    });
                                }
                            }).start();
                        } else {
                            player.sendMessage(new TextComponentString("[Randores] " + RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), RandoresTranslations.Keys.RESOURCES_LOADED)));
                        }
                        FlexibleTextureRegistry.setInitialized(true);
                        FlexibleTextureRegistry.setTextureSeed(seed);
                        logger.info("Resources reloading, definitions loaded, and textures properly set up. Randores is ready!");
                    }
                });

            }
        };

        Thread thread = new Thread(runnable, "Randores-Texture-Thread-" + index++);
        thread.start();

        return null;
    }

}
