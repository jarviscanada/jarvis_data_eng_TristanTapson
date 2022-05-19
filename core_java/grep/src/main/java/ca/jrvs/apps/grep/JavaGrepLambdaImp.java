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
            throw new IllegalArgumentException("USAGE: JavaGrepLambdaImp regex rootPath outFile");
        }

        else{
            System.out.println("--- JavaGrepLambdaImp Class ---");
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

    // NOTE: not final lambda/stream readLines - this is implemented in JavaGrepImp as a ticket required the function signature to change from List<File> to Stream<File>
    @Override
    public Stream<String> readLines(File inputFile) throws IOException {

        System.out.println("lambda (readLines) override...");

        // file path
        String filepath = inputFile.getPath();

        // stream to add all lines in a file to a stream of lines
        return Files.lines(Paths.get(filepath));
    }

    // NOTE: not final lambda/stream listFiles - this is implemented in JavaGrepImp as a ticket required the function signature to change from List<File> to Stream<File>
    @Override
    public Stream<File> listFiles(String rootDir) throws IOException{

        System.out.println("lambda (listFiles) override...");

        String directory = rootDir;
        List<File> validFiles = new ArrayList<File>();

        // stream walking through files in a directory
        // filter if valid file, and add the filepath to the list of valid files
        try (Stream<Path> tempStream = Files.walk(Paths.get(directory))) {
            tempStream.map(Path::normalize)
                    .filter(path-> Files.isRegularFile(path))
                    .forEach(path -> validFiles.add(path.toFile()));

        } catch (IOException ex){
            ex.printStackTrace();
        }

        // console output, for README...
        validFiles.forEach(file -> System.out.println("filepath: " + file));

        // stream walking through files in a directory
        // filter if valid file, and map path to file
        return Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toFile);
    }
}