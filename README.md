# Jamplate [![](https://jitpack.io/v/org.jamplate/processor.svg)](https://jitpack.io/#org.jamplate/processor)

Jamplate is a C-Style pre-processor. Although it is a C-Style, this does not mean it is
following the C standard. This pre-processor has almost the same expected behaviour as a
standard C pre-processors with some features added and some missing.

### Distribution

This repository is the core processor. You might use the
[Jamplate Gradle Plugin](https://github.com/jamplate/gradle) instead. If you want to use
the processor directly or just extend it, you might download the repository or
use `jitpack.io`. For more info, please visit [jamplate.org](https://jamplate.org)

```gradle
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	//replace `Tag` with the targeted version.
	implementation 'org.jamplate:processor:Tag'
}
```

### Usage

An example of using the Glucose Implementation:

```groovy 
    Document document = new PseudoDocument(
        //name
        "main.jamplate",
        //content
        "#message Hello ' ' World"
    )
    
    Unit unit = new UnitImpl(new GlucoseSpec())
    
    if (
        !unit.initialize(document) ||
        !unit.parse(document) ||
        !unit.analyze(document) ||
        !unit.compile(document) ||
        !unit.execute(document)
    ) {
    	unit.diagnostic()
    }
```

An example of using the Glucose Implementation with the DebugSpec:

```groovy 
    Document document = new PseudoDocument(
        //name
        "main.jamplate",
        //content
        "#{ Hello ' ' World }#"
    )

    Unit unit = new UnitImpl(new GlucoseSpec(
        DebugSpec.INSTANCE
    ))

    if (
        !unit.initialize(document) ||
        !unit.parse(document) ||
        !unit.analyze(document) ||
        !unit.compile(document) ||
        !unit.execute(document)
    ) {
        unit.diagnostic()
    }
```

### Project Info

- GitHub Repository:
  [https://github.com/jamplate/processor](https://github.com/jamplate/processor)
- Website: [https://jamplate.org](https://jamplate.org)
- Author: [LSafer](https://lsafer.net)
- Licences: [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)

### Licence

    Copyright 2021 Cufy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
