package ca.jrvs.apps.grep;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrepLambda regex rootPath outFile");
        }

        else{
            System.out.println("-- JavaGrepLambdaImp Class --");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try{
            javaGrepLambdaImp.process();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<String> readLines(File inputFile) throws IOException {

        System.out.println("lambda (readLines) override...");

        String filename = inputFile.getPath();

        List<String> allLines = new ArrayList<String>();

       try (Stream<String> stream = Files.lines(Paths.get(filename))) {
           stream.forEach(allLines::add);
       } catch (IOException ex){
           ex.printStackTrace();
       }

       return allLines;
    }

    @Override
    public List<File> listFiles(String rootDir, List<File> listOfFiles) throws IOException{

        System.out.println("lambda (listFiles) override...");

        String directory = rootDir;
        List<File> validFiles = listOfFiles;

        try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
            stream.map(Path::normalize)
                    .filter(path-> Files.isRegularFile(path))
                    .forEach(path -> validFiles.add(path.toFile()));

        } catch (IOException ex){
            ex.printStackTrace();
        }

        // validFiles.forEach(System.out::println);
        return validFiles;
    }
}