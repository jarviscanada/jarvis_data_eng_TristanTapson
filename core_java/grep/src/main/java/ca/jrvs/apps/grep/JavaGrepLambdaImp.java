package ca.jrvs.apps.grep;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.apache.log4j.BasicConfigurator;

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

    // NOTE: not final lambda/stream - this is implemented in JavaGrepImp
    // as a ticket required the function signature to change from List<File> to Stream<File>
    @Override
    public Stream<String> readLines(File inputFile) throws IOException {

        System.out.println("lambda (readLines) override...");

        String filename = inputFile.getPath();
        List<String> allLines = new ArrayList<String>();

        // TODO - use the implementation from JavaGrepImp class here...
        // stream to add all lines in a file to a list of lines
       Stream<String> stringStream = Files.lines(Paths.get(filename));
           stringStream.forEach(allLines::add);

        return stringStream;
    }

    // NOTE: not final lambda/stream - this is implemented in JavaGrepImp
    // as a ticket required the function signature to change from List<File> to Stream<File>
    @Override
    public Stream<File> listFiles(String rootDir, List<File> listOfFiles) throws IOException{

        System.out.println("lambda (listFiles) override...");

        String directory = rootDir;

        // TODO - use the implementation from JavaGrepImp class here...
        // stream api walking through files in a directory
        // filter if valid file, and add the filepath to the list of valid files
        Stream<File> fileStream = Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toFile);

        return fileStream;
    }
}