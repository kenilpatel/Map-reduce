import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;


/* single color intensity */
class Color implements WritableComparable<Color> {
    public int type;       /* red=1, green=2, blue=3 */
    public int intensity;  /* between 0 and 255 */
    /* need class constructors, toString, write, readFields, and compareTo methods */
    Color()
    {
	}
	Color(int type,int intensity)
	{
		this.type=type;
		this.intensity=intensity;
	} 
	public String toString () { 
		return type+" "+intensity; 
		}
	public void write ( DataOutput out ) throws IOException {
        out.writeInt(type); 
        out.writeInt(intensity);
    } 
    public void readFields( DataInput in ) throws IOException {
        type = in.readInt();
        intensity = in.readInt(); 
    } 
	public int compareTo ( Color c1 ){
		int x1=this.type*1000+this.intensity;
         int x2=c1.type*1000+c1.intensity;
         if(x1<x2)
         {
			 return -1;
		 }
		 else if(x1==x2)
		 {
			 return 0;
		 }
		 else
		 {
			 return 1;
		 }
	}
     
}


public class Histogram {
    public static class HistogramMapper extends Mapper<Object,Text,Color,IntWritable> {
        @Override
        public void map ( Object key, Text value, Context context )
                        throws IOException, InterruptedException {
            Scanner s = new Scanner(value.toString()).useDelimiter(",");
            int r = s.nextInt();
            int g = s.nextInt();
            int b = s.nextInt();  
            context.write(new Color(1,r),new IntWritable(1));
            context.write(new Color(2,g),new IntWritable(1));
            context.write(new Color(3,b),new IntWritable(1));
            s.close();
        }
    }

    public static class HistogramReducer extends Reducer<Color,IntWritable,Color,LongWritable> {
        @Override
        public void reduce ( Color key, Iterable<IntWritable> values, Context context )
                           throws IOException, InterruptedException {
            int sum=0;
            for (IntWritable v: values) {
                sum =sum+1; 
            }
            context.write(key,new LongWritable(sum));
        }
    }

    public static void main ( String[] args ) throws Exception {
        /* write your main program code */
        Job job = Job.getInstance();
        job.setJobName("Colors");
        job.setJarByClass(Histogram.class);
        job.setOutputKeyClass(Color.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapOutputKeyClass(Color.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setMapperClass(HistogramMapper.class);
        job.setReducerClass(HistogramReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        job.waitForCompletion(true);
   }
}
