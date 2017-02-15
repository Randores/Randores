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

import com.gmail.socraticphoenix.forge.randore.texture.FlexibleAtlasSprite;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class RandoresTextureListener {

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre ev) {
        Logger logger = Randores.getInstance().getLogger();

        if(FlexibleTextureRegistry.itemQuantity() == 0) {
            for (int i = 0; i < 3300; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.itemTextureName(i), "test");
                FlexibleTextureRegistry.registerItem(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for(FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getItemSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

        if(FlexibleTextureRegistry.blockQuantity() == 0) {
            for (int i = 0; i < 600; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.textureName(i), "test");
                FlexibleTextureRegistry.registerBlock(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for(FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getBlockSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

        if(FlexibleTextureRegistry.specificQuantity() == 0) {
            r(new FlexibleAtlasSprite("randores:blocks/craftinium_ore", "craftinium_ore"), ev);
            r(new FlexibleAtlasSprite("randores:items/craftinium_ingot", "craftinium_ingot"), ev);
        } else {
            for(FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getSpecific()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }
    }

    private void r(FlexibleAtlasSprite sprite, TextureStitchEvent.Pre ev) {
        FlexibleTextureRegistry.registerSpecific(sprite);
        ev.getMap().setTextureEntry(sprite);
    }

}
