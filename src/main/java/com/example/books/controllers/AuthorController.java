package com.example.books.controllers;

import com.example.books.service.AuthorService;
import com.example.books.models.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/author")
public class AuthorController {

    private final Logger LOGGER= LoggerFactory.getLogger(AuthorController.class);

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    private AuthorService service;

    @GetMapping
    public ResponseEntity<List<Author>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/index")
    public String getIndex(){
        return "index";
    }


    @GetMapping("/addAuthor")
    public String addAuthor(Model model){
        model.addAttribute("author", new Author());
        return "createAuthor";
    }

    @GetMapping("/allAuthors")
    public String allAuthors(Model model){
        model.addAttribute("authors", service.getAll());
        return "allAuthors";
    }

    @GetMapping("/{id}/delete")
    public String deleteAuthor(@PathVariable long id, Model model){
        service.deleteById(id);
        model.addAttribute("authors", service.getAll());
        return "allAuthors";
    }

    @PostMapping("/createAuthor")
    public String createAuthor(Model model, Author author, BindingResult result, @RequestParam("imageFile")MultipartFile file) throws IOException {
        if(result.hasErrors()){
            LOGGER.error("binding result has errors");
            return "createAuthor";
        }
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        author.setImage("\\images\\"+file.getOriginalFilename());
        LOGGER.info(fileNameAndPath.toString());
        service.createAuthor(author);
        return "redirect:/author/allAuthors";
    }

    @GetMapping("/{id}/edit")
    public String editAuthor(@PathVariable long id, Model model){
        Optional<Author> author = service.getById(id);
        if(author.isPresent()) {
            model.addAttribute("author", author.get());
            return "editAuthor";
        } else {
            return "redirect:/author/allAuthors";
        }
    }

    @PostMapping("/{id}/edit")
    public String saveChanges(Model model, @PathVariable long id, Author author){
        service.updateAuthor(author);
        model.addAttribute("authors", service.getAll());
        return "allAuthors";
    }

    @GetMapping("/{id}/deleteAuthor")
    public String areYouSure(Model model, @PathVariable long id){
        model.addAttribute("id", id);
        return "deleteAuthor";
    }

    @GetMapping("/{id}/details")
    public String details(Model model, @PathVariable long id){

        model.addAttribute("author",service.getById(id).get());

        return "Details";
    }

    @PostMapping("/allAuthors")
    public String searchAuthors(Model model, @RequestParam String criteria, @RequestParam String choice){
        List<Author> authors = new ArrayList<>();
        for(Author a: service.getAll()){
            if(choice.equals("firstname")) {
                if (a.getFirstName().toUpperCase().contains(criteria.toUpperCase())) {
                    authors.add(a);
                }
            }
            else if(choice.equals("lastname")){
                if(a.getLastName().toUpperCase().contains(criteria.toUpperCase())){
                    authors.add(a);
                }
            }
            else if(choice.equals("language")){
                String year=a.getYearOfBirth()+"";
                if(year.contains(criteria)){
                    authors.add(a);
                }
            }}
        model.addAttribute("authors", authors);
        return "allAuthors";
    }

    @PostMapping("/allAuthors/sort")
    public String sortAuthors(Model model, @RequestParam String order, @RequestParam String choice2){
        List<Author> authors = service.getAll();

        if(choice2.equals("firstname")) {
            if(order.equals("asc")){
                authors.sort((a1, a2) -> a1.getFirstName().compareTo(a2.getFirstName()));
            } else {
                authors.sort((a1, a2) -> a2.getFirstName().compareTo(a1.getFirstName()));
            }
        } else if(choice2.equals("lastname")) {
            if(order.equals("asc")){
                authors.sort((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()));
            } else {
                authors.sort((a1, a2) -> a2.getLastName().compareTo(a1.getLastName()));
            }
        } else if(choice2.equals("birthYear")) {
            if(order.equals("asc")){
                authors.sort((a1, a2) -> Integer.compare(a1.getYearOfBirth(), a2.getYearOfBirth()));
            } else {
                authors.sort((a1, a2) -> Integer.compare(a2.getYearOfBirth(), a1.getYearOfBirth()));
            }
        }

        model.addAttribute("authors", authors);
        return "allAuthors";
    }
}
