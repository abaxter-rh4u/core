/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.parser.java.resources;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaSource;

/**
 * A {@link Resource} representing a Java {@link java.lang.reflect.Field}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaFieldResource extends Resource<Field<? extends JavaSource<?>>>
{
}
