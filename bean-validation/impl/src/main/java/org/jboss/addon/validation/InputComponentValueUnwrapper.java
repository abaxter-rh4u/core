/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.addon.validation;

import java.lang.reflect.Type;

import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
import org.jboss.forge.addon.ui.input.UIInput;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

/**
 * 
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class InputComponentValueUnwrapper extends ValidatedValueUnwrapper<UIInput<?>>
{
   private final TypeResolver typeResolver = new TypeResolver();

   @Override
   public Object handleValidatedValue(UIInput<?> value)
   {
      return value.getValue();
   }

   @Override
   public Type getValidatedValueType(Type valueType)
   {
      ResolvedType resolvedType = typeResolver.resolve(valueType);
      return resolvedType.typeParametersFor(UIInput.class).get(0).getErasedType();
   }

}
