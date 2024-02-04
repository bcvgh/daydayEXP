package com.bcvgh.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class FileUtil {
    private static final Logger LOGGER = LogManager.getLogger(FileUtil.class.getName());

    public static boolean Mkdir(String path,String dirName){
        try {
            File file = new File(path+File.separator+dirName);
            file.mkdir();
            return true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public static <T> T FileRead(String filePath,T type){
        Object content = null;
            Path FilePath = Paths.get(filePath);
            if (type instanceof byte[]){
                try {
                    content = Files.readAllBytes(FilePath);
                    return (T) content;
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (type instanceof String){
                try {
                    List<String> list = Files.readAllLines(FilePath, StandardCharsets.UTF_8);
                    content = String.join("\n",list);
                    return (T) content;
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            return null;
    }

    public static <T> Boolean FileWrite(String filePath,T content ) {
        Path FilePath = Paths.get(filePath);

        try {
            if (content instanceof String) {
                Files.write(FilePath, ((String) content).getBytes());
            }
            if (content instanceof byte[]) {
                Files.write(FilePath, (byte[]) content);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }


    public static String[] DirList(String dirPath){
        File file = new File(dirPath);
        return file.list();
    }

    public static Boolean DeleteDir(String path){
        Path directory = Paths.get(path);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file); // 有效，因为它始终是一个文件
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir); //这将起作用，因为目录中的文件已被删除
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

}
