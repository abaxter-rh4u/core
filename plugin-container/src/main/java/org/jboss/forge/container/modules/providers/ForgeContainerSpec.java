package org.jboss.forge.container.modules.providers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec.Builder;
import org.jboss.modules.filter.PathFilters;

public class ForgeContainerSpec extends BaseModuleSpecProvider
{
   public static final ModuleIdentifier ID = ModuleIdentifier.create("org.jboss.forge.container.api");

   public static Set<String> paths = new HashSet<String>();

   static
   {
      paths.add("META-INF");
      paths.add("META-INF/services");
   }

   @Override
   protected void configure(ModuleLoader loader, Builder builder)
   {
      builder.addDependency(DependencySpec.createModuleDependencySpec(
               PathFilters.acceptAll(),
               PathFilters.any(Arrays
                        .asList(
                                 PathFilters.is("org/jboss/forge/container"),
                                 PathFilters.isChildOf("org/jboss/forge/container"),
                                 PathFilters.is("net/sf/cglib"), PathFilters.isChildOf("net/sf/cglib")
                        )),
               loader, RuntimeClasspathSpec.ID, false));
   }

   @Override
   protected ModuleIdentifier getId()
   {
      return ID;
   }

   @Override
   protected Set<String> getPaths()
   {
      return paths;
   }

}