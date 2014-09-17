/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.i18n.client;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Internationalized messages used by {@link MessagesExampleController}.
 * Used to demonstrate plural forms support.
 */
@DefaultLocale("en_US")
public interface PluralMessages extends Messages {
  @DefaultMessage("You have {0} trees.")
  @PluralText({"one", "You have one tree."})
  String treeCount(@PluralCount int count);
}
