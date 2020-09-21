### Jamplate is a C-style pre-processor that mainly made for Java.

## How To Use
 - `#define` to define a variable
 - `#if` `#elif` `#else` to control the flow and `#endif` to close `#if` statements
 - `#text` for debugging purposes, makes plain text.
 - `#paste` to paste a variable defined in the scope it is located on. 
 - `#ln` and `#line` to add `\n` multiple times in the product file.
 - `#with` to replace multiple sequences in its scope with other ones and `#endwith` to close `#with` statements
 - `#make` to replace multiple sequences in the name of the file with other ones

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
 #with name:name, package:package
 System.out.println("name");
 System.out.println("package");
 ```
 Will make the same file names and count as the `#make` example, And the half the file outputs as the `#with` example!
 
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
 
 Why `#with name:name, package:package`?
 because `#with` accepts array of pairs of `string:logic`, and referencing the variable `name` and `package` will make `#with` change its behaviour depending on witch file it is on.

## How to invoke
 It could be invoked with the gradle plug (still not completed). 
 Or it could be invoked using the class `org.jamplate.Jamplate`.
