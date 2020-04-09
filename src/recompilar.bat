cd superAwpBot
javac *.java
cd ..
jar cfmv SuperAwpBot.jar superAwpBot\manifest.mf superAwpBot\SuperAwpBot.class
copy SuperAwpBot.jar ..\SuperAwpBot.jar
