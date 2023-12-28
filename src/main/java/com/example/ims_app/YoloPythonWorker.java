package com.example.ims_app;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class YoloPythonWorker {

    public static String latestResultsDirectory;
    public static Map<String, Integer> latestResultsCollection;
    final static String programPath = "src\\main\\java\\com\\example\\ims_app\\yolov5";
    final static String customModelPath = "runs\\train\\exp11\\weights\\last.pt";

    public static void run(String imgPath){
        String pythonScript =  "detect.py";
        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("Current Working Directory: " + currentWorkingDirectory);
        //python detect.py --weights runs\train\exp11\weights\last.pt --img 640 --conf 0.40 --source data\images\inventory_images\IMG_2256.jpeg
        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript, "--weights",customModelPath, "--img", "640", "--conf", "0.40", "--source", imgPath, "--save-csv");
        processBuilder.directory(new File(programPath));
        System.out.println("Running Python YOLOv5 detect script on image: "+imgPath);
        try{
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Python YOLOv5 Script exited with code: "+ exitCode);
            updateLatestResultsDirectory();
            updateLatestResultsCollection();
            if (latestResultsDirectory != null){
                System.out.println("Object Detection and Image Classification Results stored in: "+latestResultsDirectory);
            }else{
                System.out.println("Object Detection and Image Classification Results not found!");
            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void updateLatestResultsCollection(){
        try (FileReader reader = new FileReader(latestResultsDirectory+"\\predictions.csv");
             CSVParser csvParser = CSVFormat.DEFAULT.parse(reader)) {

            // Create a map to store counts
            latestResultsCollection = new HashMap<>();

            // Iterate over CSV records
            for (CSVRecord csvRecord : csvParser) {
                String obj = csvRecord.get(1);
                latestResultsCollection.put(obj, latestResultsCollection.getOrDefault(obj, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateLatestResultsDirectory(){
        try {
            Path directory = Paths.get("src\\main\\java\\com\\example\\ims_app\\yolov5\\runs\\detect");
            Path latestFolder = Files.list(directory).filter(Files::isDirectory)
                    .max(Comparator.comparing(YoloPythonWorker::getCreationTime))
                    .orElse(null);
            assert latestFolder != null;
            latestResultsDirectory = latestFolder.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static FileTime getCreationTime(Path path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            return attributes.creationTime();
        } catch (IOException e) {
            throw new RuntimeException("Error getting creation time for: " + path, e);
        }
    }
}
