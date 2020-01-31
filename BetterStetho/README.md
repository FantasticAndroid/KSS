# Better Stetho

## What is Better Stetho?
Better Stetho is written on the top of Facebook Stetho library.

### Usages
Stetho is very useful tool to debug Network APis and Shared Preferences, but developer must need to disable it from the production build each time for security concern. But APK or AAB always contains the whole source code of Stetho and so increase approx. half MB in APK size. But

Better Stetho only integrate in Project source code for Debug build only and automatically detached for Release build.

To check how it works, please go through the app module.

### Libraries integrated-
Stetho 1.5.1 from (http://facebook.github.io/stetho/)
