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
import { WebContextType } from './types'
import { getWebContext } from './context'

/**
 * Hook to access the Web Context within React components
 * Uses the singleton WebContext instance
 * @returns The Web Context containing request attributes or null if not available
 */
export function useWebContext(): WebContextType | null {
  const WebContext = getWebContext()
  return React.useContext(WebContext)
}

/**
 * Hook to access request attributes from the Web Context
 * @returns A copy of the attributes object to avoid direct mutation
 */
export function useAttributes(): Record<string, any> {
  const webContext = useWebContext()

  if (!webContext) {
    return {}
  }

  return { ...webContext.attributes }
}

/**
 * Hook to access a specific attribute from the Web Context
 * @param key - The attribute key to retrieve
 * @returns The attribute value or undefined if not found
 */
export function useAttribute<T>(key: string): T | undefined {
  const attributes = useAttributes()
  return attributes[key] as T
}