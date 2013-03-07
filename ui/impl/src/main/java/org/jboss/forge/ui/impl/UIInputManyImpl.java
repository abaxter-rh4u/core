/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.ui.impl;

import java.util.concurrent.Callable;

import javax.enterprise.inject.Vetoed;

import org.jboss.forge.convert.Converter;
import org.jboss.forge.ui.input.UICompleter;
import org.jboss.forge.ui.input.UIInput;
import org.jboss.forge.ui.input.UIInputMany;
import org.jboss.forge.ui.util.Callables;

/**
 * Implementation of a {@link UIInput} object
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * 
 * @param <VALUETYPE>
 */
@Vetoed
public class UIInputManyImpl<VALUETYPE> extends UIInputComponentBase<UIInputMany<VALUETYPE>, VALUETYPE>
         implements UIInputMany<VALUETYPE>
{

   private Iterable<VALUETYPE> value;
   private Callable<Iterable<VALUETYPE>> defaultValue;
   private UICompleter<VALUETYPE> completer;
   private Converter<String, VALUETYPE> converter;

   public UIInputManyImpl(String name, Class<VALUETYPE> type)
   {
      super(name, type);
   }

   @Override
   @SuppressWarnings("unchecked")
   public UICompleter<VALUETYPE> getCompleter()
   {
      return this.completer == null ? new NoopCompleter() : this.completer;
   }

   @Override
   public UIInputMany<VALUETYPE> setCompleter(UICompleter<VALUETYPE> completer)
   {
      this.completer = completer;
      return this;
   }

   @Override
   public UIInputMany<VALUETYPE> setValue(Iterable<VALUETYPE> value)
   {
      this.value = value;
      return this;
   }

   @Override
   public UIInputMany<VALUETYPE> setDefaultValue(Callable<Iterable<VALUETYPE>> callback)
   {
      this.defaultValue = callback;
      return this;
   }

   @Override
   public UIInputMany<VALUETYPE> setDefaultValue(Iterable<VALUETYPE> value)
   {
      this.defaultValue = Callables.returning(value);
      return this;
   }

   @Override
   public Iterable<VALUETYPE> getValue()
   {
      return (value == null) ? Callables.call(defaultValue) : value;
   }

   @Override
   public Converter<String, VALUETYPE> getValueConverter()
   {
      return converter;
   }

   @Override
   public UIInputMany<VALUETYPE> setValueConverter(Converter<String, VALUETYPE> converter)
   {
      this.converter = converter;
      return this;
   }

}