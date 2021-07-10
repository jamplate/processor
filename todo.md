--- Repackaging

### Api -> Impl
- package org.jamplate.api -> org.jamplate.unit
- package org.jamplate.impl.api -> org.jamplate.impl.unit

### Repackaging
- org.jamplate.impl package for each interface
- exceptions -> org.jamplate

### Internal -> Glucose Internal
- internal.analyzer -> glucose.internal.analyzer
- internal.compiler -> glucose.internal.compiler
- internal.parser   -> glucose.internal.parser

### Internal -> Base Internal
- internal.util     -> util


--- Specs

### misc
- DefaultMemorySpec


--- Optimizations

- more strict TYPES