package com.example.absoluteweb.server.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class PatchService {

    // Шлях до вашої локальної теки з файлами
    private final Path rootLocation = Paths.get("C:/patchL2");

    /**
     * Завантаження файлу як ресурсу для повернення в HTTP-відповіді.
     */
    public Resource loadAsResource(String filename) throws Exception {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found : "+filename);
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
