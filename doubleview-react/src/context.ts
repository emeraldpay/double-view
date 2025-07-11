/*
 * Copyright 2025 EmeraldPay Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'
import { WebContextType, DoubleView } from './types'

/**
 * Retrieves the DoubleView instance from the global scope
 * @returns The DoubleView instance containing component, props, and context
 */
export function getDoubleView(): DoubleView {
  const instance = (globalThis as any).doubleView as DoubleView
  if (!instance) {
    console.error('No DoubleView instance found in global scope')
  }
  return instance
}

/**
 * Gets the WebContext instance from globalThis.doubleView.WebContext
 * This ensures the same context instance is used across SSR and client-side
 * @returns The WebContext React.Context instance
 */
export function getWebContext(): React.Context<WebContextType | null> {
  if (!(globalThis as any).doubleView) {
    (globalThis as any).doubleView = {}
  }
  
  let WebContext = (globalThis as any).doubleView.WebContext
  if (!WebContext) {
    const defaultContext = (globalThis as any).doubleView.context || null
    WebContext = React.createContext<WebContextType | null>(defaultContext);
    (globalThis as any).doubleView.WebContext = WebContext
  }
  
  return WebContext
}