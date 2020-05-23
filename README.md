# Map-reduce
Map reduce program for Pixel Histograms written in java

Pixel Histograms

A pixel in an image can be represented using 3 colors: red, green, and blue, where each color intensity is an integer between 0 and 255. In this project, you are asked to write a Map-Reduce program that derives a histogram for each color. For red, for example, the histogram will indicate how many pixels in the dataset have a green value equal to 0, equal to 1, etc (256 values). The pixel file is a text file that has one text line for each pixel. For example, the line

23,140,45

represents a pixel with red=23, green=140, and blue=45. 

pseudo code:

class Color {
    public short type;       /* red=1, green=2, blue=3 */
    public short intensity;  /* between 0 and 255 */
}

map ( key, line ):
  read 3 numbers from the line and store them in the variables red, green, and blue. Each number is between 0 and 255.
  emit( Color(1,red), 1 )
  emit( Color(2,green), 1 )
  emit( Color(3,blue), 1 )

reduce ( color, values )
  sum = 0
  for ( v in values )
      sum += v
  emit( color, sum )

In your Java program args[0] is the file with the pixels (pixels-small.txt or pixels-large.txt) and args[1] is the output directory.
            
How to run this project on your project?

If you have Mac OSX or Linux, make sure you have Java and Maven installed (on Mac, you can install Maven using Homebrew brew install maven, on Ubuntu Linux, use apt install maven). If you have Windows 10, you need to install Ubuntu Shell and do: sudo apt update, sudo apt upgrade, and sudo apt install openjdk-8-jdk maven.

To install Hadoop and the project, cut&paste and execute on the unix shell:

cd
wget https://archive.apache.org/dist/hadoop/common/hadoop-2.6.5/hadoop-2.6.5.tar.gz
tar xfz hadoop-2.6.5.tar.gz 

You should also set your JAVA_HOME to point to your java installation. For example, on Windows do:
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

To test Map-Reduce, go to project1/examples/src/main/java and look at the two Map-Reduce examples Simple.java and Join.java. 
You can compile both Java files using:
mvn install
and you can run Simple in standalone mode using:
~/hadoop-2.6.5/bin/hadoop jar target/*.jar Simple simple.txt output-simple
The file output-simple/part-r-00000 will contain the results.

To compile and run project1:

cd project1
mvn install
rm -rf output
~/hadoop-2.6.5/bin/hadoop jar target/*.jar Histogram pixels-small.txt output
