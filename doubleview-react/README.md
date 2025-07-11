# @emeraldpay/doubleview-react

Common utilities for Doubleview React SSR implementation.

## Installation

```bash
npm install @emeraldpay/doubleview-react
# or
pnpm add @emeraldpay/doubleview-react
```

## Usage

### Types

```typescript
import { WebContextType, DoubleView } from '@emeraldpay/doubleview-react'
```

### Hooks

```typescript
import { useWebContext, useAttributes, useAttribute } from '@emeraldpay/doubleview-react'

function MyComponent() {
  // Access the full web context
  const context = useWebContext()
  
  // Access all attributes at once
  const attributes = useAttributes()
  
  // Access specific attributes with type safety
  const userId = useAttribute<string>('userId')
  const locale = useAttribute<string>('locale')
  
  return (
    <div>
      <p>User ID: {userId}</p>
      <p>Locale: {locale}</p>
    </div>
  )
}
```

### Utilities

```typescript
import { isSSR, isClient } from '@emeraldpay/doubleview-react'
```

## Development

```bash
# Install dependencies
pnpm install

# Build the package
pnpm build

# Run in development mode (watch mode)
pnpm dev

# Type check
pnpm typecheck

# Lint
pnpm lint
```

## License

Apache 2.0