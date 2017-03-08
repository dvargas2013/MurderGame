# Murder Game

### TroubleShooting: Getting Resources to Work
Because different GUIs put .class files in different places (staring at the differences between Eclipse and IntelliJ primarily) that means the resources would have to move around a little. The code that finds the resources is defined as root in statics.Globals you must move resources around into the place where the console says it must be. The directory of the .class files and up two directories is where it will be. 

**Eclipse example** - Eclipse puts files in a ./bin so the static.Globals will find itself on ./bin/static/Globals.class going up two would leave it on the ./ directory
**IntelliJ Example** - IntelliJ puts files in a ./out/production/[ProjectName] so the static.Globals will find itself on ./out/production/[ProjectName]/static/Globals.class going two up will make it on ./out/production directory so if you move resources into that location ...

The reason I do it this way is so when you are exporting into a .jar file you can place the .jar file inside of resources and it will be able to find itself easily