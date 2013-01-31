package org.jboss.forge.container.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.forge.container.exception.ContainerException;
import org.jboss.forge.container.services.ExportedInstance;
import org.jboss.forge.container.services.ExportedInstanceImpl;
import org.jboss.forge.container.services.ServiceRegistry;
import org.jboss.forge.container.util.Addons;
import org.jboss.forge.container.util.Assert;
import org.jboss.forge.container.util.Sets;

public class ServiceRegistryImpl implements ServiceRegistry
{
   private Set<Class<?>> services = Sets.getConcurrentSet();

   private BeanManager manager;

   private AddonImpl addon;

   private Logger log = Logger.getLogger(getClass().getName());

   public ServiceRegistryImpl(AddonImpl addon, BeanManager manager, ContainerServiceExtension extension)
   {
      this.addon = addon;
      this.manager = manager;

      for (Class<?> clazz : extension.getServices())
      {
         addService(clazz);
      }
   }

   public <T> void addService(Class<T> clazz)
   {
      services.add(clazz);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> ExportedInstance<T> getExportedInstance(String clazz)
   {
      Addons.waitUntilStarted(addon);
      Class<T> type;
      try
      {
         type = (Class<T>) loadAddonClass(clazz);
         return getExportedInstance(type, type);
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }

   @Override
   public <T> ExportedInstance<T> getExportedInstance(Class<T> clazz)
   {
      Addons.waitUntilStarted(addon);
      return getExportedInstance(clazz, clazz);
   }

   /**
    * @param requestedType interface
    * @param actualType Implementation
    * @return
    */
   @SuppressWarnings("unchecked")
   private <T> ExportedInstance<T> getExportedInstance(Class<T> requestedType, Class<T> actualType)
   {
      Assert.notNull(requestedType, "Requested Class type may not be null");
      Assert.notNull(actualType, "Actual Class type may not be null");
      Addons.waitUntilStarted(addon);

      final Class<T> requestedLoadedType;
      final Class<? extends T> actualLoadedType;
      try
      {
         requestedLoadedType = loadAddonClass(requestedType);
      }
      catch (ClassNotFoundException cnfe)
      {
         log.fine("Class " + requestedType.getName() + " is not present in this addon classloader");
         return null;
      }
      try
      {
         actualLoadedType = loadAddonClass(actualType);
      }
      catch (ClassNotFoundException cnfe)
      {
         log.fine("Class " + actualType.getName() + " is not present in this addon classloader");
         return null;
      }

      try
      {
         ExportedInstance<T> result = null;
         Set<Bean<?>> beans = manager.getBeans(requestedLoadedType);
         if (!beans.isEmpty())
         {
            result = new ExportedInstanceImpl<T>(
                     addon.getClassLoader(),
                     manager, (Bean<T>)
                     manager.resolve(beans),
                     requestedLoadedType,
                     actualLoadedType
                     );
         }
         return result;
      }
      catch (Exception e)
      {
         throw new ContainerException("Error while fetching exported instance", e);
      }
   }

   @Override
   public Set<Class<?>> getServices()
   {
      return services;
   }

   @Override
   public boolean hasService(String clazz)
   {
      try
      {
         Class<?> type = loadAddonClass(clazz);
         return hasService(type);
      }
      catch (ClassNotFoundException e)
      {
         return false;
      }
   }

   @Override
   public boolean hasService(Class<?> clazz)
   {
      Addons.waitUntilStarted(addon);
      Class<?> type;
      try
      {
         type = loadAddonClass(clazz);
      }
      catch (ClassNotFoundException e)
      {
         return false;
      }
      for (Class<?> service : services)
      {
         if (type.isAssignableFrom(service))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Set<ExportedInstance<T>> getExportedInstances(String clazz)
   {
      try
      {
         Class<T> type = (Class<T>) loadAddonClass(clazz);
         return getExportedInstances(type);
      }
      catch (ClassNotFoundException e)
      {
         return Collections.emptySet();
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> Set<ExportedInstance<T>> getExportedInstances(Class<T> requestedType)
   {
      Addons.waitUntilStarted(addon);
      Set<ExportedInstance<T>> result = new HashSet<ExportedInstance<T>>();
      Class<T> requestedLoadedType;
      try
      {
         requestedLoadedType = loadAddonClass(requestedType);
      }
      catch (ClassNotFoundException e)
      {
         return result;
      }
      for (Class<?> type : services)
      {
         if (requestedLoadedType.isAssignableFrom(type))
         {
            Set<Bean<?>> beans = manager.getBeans(type);
            Class<? extends T> assignableClass = (Class<? extends T>) type;
            result.add(new ExportedInstanceImpl<T>(
                     addon.getClassLoader(),
                     manager,
                     (Bean<T>) manager.resolve(beans),
                     requestedLoadedType,
                     assignableClass
                     ));
         }
      }
      return result;
   }

   /**
    * Ensures that the returned class is loaded from the addon
    *
    * @param actualType
    * @return
    * @throws ClassNotFoundException
    */
   @SuppressWarnings("unchecked")
   private <T> Class<T> loadAddonClass(Class<T> actualType) throws ClassNotFoundException
   {
      final Class<T> type;
      if (actualType.getClassLoader() == addon.getClassLoader())
      {
         type = actualType;
      }
      else
      {
         type = (Class<T>) loadAddonClass(actualType.getName());
      }
      return type;
   }

   private Class<?> loadAddonClass(String className) throws ClassNotFoundException
   {
      return Class.forName(className, true, addon.getClassLoader());
   }

   @Override
   public String toString()
   {
      return services.toString();
   }

}
