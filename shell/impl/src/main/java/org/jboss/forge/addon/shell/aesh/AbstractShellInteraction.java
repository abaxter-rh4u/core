/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.shell.aesh;

import org.jboss.aesh.cl.parser.CommandLineParser;
import org.jboss.forge.addon.shell.ui.ShellContext;
import org.jboss.forge.addon.shell.util.ShellUtil;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;

/**
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class AbstractShellInteraction implements Comparable<AbstractShellInteraction>
{
   private final String name;
   private final CommandController controller;
   private final UICommandMetadata metadata;
   protected final CommandLineUtil commandLineUtil;
   private final UIContext context;

   protected AbstractShellInteraction(CommandController controller, ShellContext shellContext,
            CommandLineUtil commandLineUtil)
   {
      this.context = shellContext;
      this.controller = controller;
      this.metadata = controller.getMetadata();
      this.name = ShellUtil.shellifyName(metadata.getName());
      this.commandLineUtil = commandLineUtil;
   }

   public abstract CommandLineParser getParser(ShellContext shellContext, String completeLine) throws Exception;

   public UIContext getContext()
   {
      return context;
   }

   public CommandController getController()
   {
      return controller;
   }

   public final String getName()
   {
      return name;
   }

   @Override
   public int compareTo(AbstractShellInteraction o)
   {
      return getName().compareTo(o.getName());
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof AbstractShellInteraction))
         return false;

      AbstractShellInteraction that = (AbstractShellInteraction) o;

      if (!getName().equals(that.getName()))
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      return getName().hashCode();
   }

   @Override
   public String toString()
   {
      return getName();
   }

}