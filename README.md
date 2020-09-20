### Jamplates is a C-style pre-processor that mainly made for Java.

## How To
 - `#define` to define a variable
 - `#if` `#elif` `#else` to control the flow and `#endif` to close `#if` statemenets
 - `#text` for debuging purposes, makes plain text.
 - `#paste` to paste a variable defined in the scope it is located on. 
 - `#with` to replace multiple sequences in its scope with other ones and `#endwith` to close `#with` statements
 - `#make` to replace mutlipe sequences in the name of the file with other ones

## How To Use `#with`
 This code: 
 ```
 #with name: "android", package: "apk" | name: "windows", package: "exe"
 System.out.println("name");
 System.out.println("package");
 #endwith
 ```
 Will result to:
 ```
 System.out.println("android");
 System.out.println("apk");
 System.out.println("windows");
 System.out.println("exe");
 ```
 
## How To Use `#make`
 This code (at the top of the file!):
 ```
 #make name: "android", package: "apk" | name: "widnows", package: "exe"
 System.out.println("name");
 System.out.println("package");
 ```
 If the file's name was `name.package`
 Will make two identical files `android.apk` and `windows.exe`.
 ```
 System.out.println("name");
 System.out.println("pacakge");
 ```
 
## How To Combine `#make` and `#with`
 This code (at the top of the file!):
 ```
 #make name: "android", package: "apk" | name: "windows", package: "exe"
 #with name:name package:package
 System.out.println("name");
 System.out.println("package");
 ```
 Will make the same file names and count as the `#make` example
 And the half the file outputs as the `#with` example!
 
 The file with the name `android.apk`
 ```
 System.out.println("android");
 System.out.println("apk");
 ```
 
 The file with the name `windows.exe`
 ```
 System.out.println("windows");
 System.out.println("exe");
 ```

## Why `#with name:name package:package`?
 becouse `#with` accepts array of pairs of `string:logic`, and referencing the variable `name` and `package` will make `#with` change its behaviour depending on witch file it is on.
