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
package com.gmail.socraticphoenix.forge.randore.texture;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionGenerator;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class RandoresDelegatingResourceManager implements IReloadableResourceManager {
    private IReloadableResourceManager delegate;

    public RandoresDelegatingResourceManager(IReloadableResourceManager delegate) {
        this.delegate = delegate;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void reloadResources(List<IResourcePack> resourcesPacksList) {
        Randores.getInstance().getConfiguration().load();
        if(RandoresClientSideRegistry.isInitialized() && MaterialDefinitionRegistry.contains(RandoresClientSideRegistry.getCurrentSeed())) {
            long seed = RandoresClientSideRegistry.getCurrentSeed();
            List<MaterialDefinition> definitions = MaterialDefinitionRegistry.get(seed);
            MaterialDefinitionGenerator.generateAndSetupTextures(definitions, seed);
            this.delegate.reloadResources(resourcesPacksList);
            MaterialDefinitionGenerator.setupArmorTextures(definitions);
        } else {
            this.delegate.reloadResources(resourcesPacksList);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerReloadListener(IResourceManagerReloadListener reloadListener) {
        this.delegate.registerReloadListener(reloadListener);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Set<String> getResourceDomains() {
        return this.delegate.getResourceDomains();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IResource getResource(ResourceLocation location) throws IOException {
        return this.delegate.getResource(location);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        return this.delegate.getAllResources(location);
    }

}
