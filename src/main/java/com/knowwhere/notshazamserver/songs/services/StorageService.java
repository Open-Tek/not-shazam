package com.knowwhere.notshazamserver.songs.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private final static String STORAGE_PATH = "/var/shaz/";
    public StorageService(){
        this.init();
    }

    private void init(){
        try{
            File file = new File(STORAGE_PATH);
            file.mkdirs();
        }catch (Exception ioe){
            throw new RuntimeException("Couldnt create base directory");
        }
    }

    /*
    THE DIRECTORY STRUCTURE IS SUPPOSED TO LOOK LIKE THIS
    /var/classroom/
            organization-a/
                classroom-id-title/
                    post-id-title/
                        [files 0-n]
                        abc.pdf
                        xyz.pdf
                    assignment-id/
                        [files 0-n]
                        hello.pdf
                        world.pdf
                        submission-email/
                            [files 0-n]
                            tuna.pdf
                            fish.pdf
                        submission-email/
                            [files 0-n]
                            boot.pdf
                            loader.pdf

                classroom-id-title
            organization-b

     */

    /**
     * This method stores a multipart file in the file system.
     * @param subPath- -> The string that must append base path.
     * @param multipartFile- -> The file to be stored.
     */
    public void store(String subPath, MultipartFile multipartFile){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        try{
            if (fileName.isEmpty())
                throw new RuntimeException("File is empty");

            if (fileName.contains(".."))
                throw new RuntimeException("Cant store filenames with relative paths "+fileName);

            try (InputStream inputStream = multipartFile.getInputStream()){

                File f = new File(STORAGE_PATH+subPath+fileName);
                f.mkdirs();
                Files.copy(inputStream, f.toPath().resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }

        }catch (IOException e){
            throw new RuntimeException("Failed to store file");
        }
    }


    /**
     * This method is to be used to read a file from the system.
     * @param filePath- -> The path to the file to be read
     * @return Resource
     */
    public Resource loadAsResource(String filePath){
        try{
            File file = new File(filePath);
            Resource resource = new UrlResource(file.toURI());
            if ( resource.exists() || resource.isReadable())
                return resource;
            throw new RuntimeException("Couldnt read file "+filePath);
        }catch (MalformedURLException e){
            throw new RuntimeException("Couldnt read file "+filePath);
        }
    }


}
