/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.ui.metadata;

import java.net.URL;

import org.jboss.forge.addon.ui.command.UICommand;

/**
 * Describes a {@link UICommand} implementation
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public interface UICommandMetadata
{
   /**
    * Label used when no description is set
    */
   public static final String NO_DESCRIPTION = "No Description";

   /**
    * Returns the {@link Class} of the corresponding {@link UICommand}.
    */
   Class<?> getType();

   /**
    * Return the name of the corresponding {@link UICommand}.
    */
   String getName();

   /**
    * Returns the description of the corresponding {@link UICommand}.
    */
   String getDescription();

   /**
    * Returns the {@link UICategory} of the corresponding {@link UICommand}.
    */
   UICategory getCategory();

   /**
    * Returns the location of the documentation of the corresponding {@link UICommand}. (can be null.)
    */
   URL getDocLocation();

}
