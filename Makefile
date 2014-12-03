all:
	javac -Xlint:unchecked *.java
	echo Main-Class: Driver > manifest.txt
	jar cvfm Lab6.jar manifest.txt *.class *.jpg sounds/* 
	chmod +x Lab6.jar
	rm -f manifest.txt *.class *~

clean:
	rm -f *~ *.class *.jar
