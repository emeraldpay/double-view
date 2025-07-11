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

/**
 * Utility functions for DoubleView React integration
 */

/**
 * Checks if the code is running in a server-side rendering environment
 * @returns true if running on server side, false if on client side
 */
export function isSSR(): boolean {
  return typeof window === 'undefined'
}

/**
 * Checks if the code is running in a client-side environment
 * @returns true if running on client side, false if on server side
 */
export function isClient(): boolean {
  return typeof window !== 'undefined'
}

