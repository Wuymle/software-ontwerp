# software-ontwerp

## how to run

```
> dir /s /b *.java > sources.txt
> javac -d out -cp "test-lib/*" @sources.txt
> jar cfm system.jar manifest.txt -C out .
> java -jar system.jar
```
