/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.container.addons;

import org.jboss.forge.container.impl.AddonImpl;
import org.jboss.forge.container.util.Visitor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MarkLoadedAddonsDirtyVisitor implements Visitor<Addon>
{
   private AddonTree tree;
   private AddonImpl source;

   public MarkLoadedAddonsDirtyVisitor(AddonTree tree, AddonImpl addon)
   {
      this.tree = tree;
      this.source = addon;
   }

   @Override
   public void visit(Addon instance)
   {
      if (instance instanceof AddonImpl)
      {
         AddonImpl addon = (AddonImpl) instance;
         if (!addon.isDirty() && addon.getStatus().isLoaded())
         {
            for (AddonDependency dep : addon.getDependencies())
            {
               if (dep.getDependency().equals(source))
               {
                  addon.setDirty(true);
                  tree.depthFirst(new MarkLoadedAddonsDirtyVisitor(tree, addon));
               }
            }
         }
      }
   }

}