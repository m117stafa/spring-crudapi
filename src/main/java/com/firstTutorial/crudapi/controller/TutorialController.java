package com.firstTutorial.crudapi.controller;


import com.firstTutorial.crudapi.model.Tutorial;
import com.firstTutorial.crudapi.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//TODO: Add sorting to endpoints
//TODO: Add Security layer to it
@RestController
@RequestMapping("/api/v1")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        try {
            Pageable paging = PageRequest.of(page,size);

            Page<Tutorial> pageTuto;

            if(title == null){
                pageTuto = tutorialRepository.findAll(paging);
            }
            else{
                pageTuto = tutorialRepository.findByTitleContaining(title, paging);
            }

            List<Tutorial> tutorials = pageTuto.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials",tutorials);
            response.put("currentPage", pageTuto.getNumber());
            response.put("totalItems", pageTuto.getTotalElements());
            response.put("totalPages", pageTuto.getTotalPages());

            return new ResponseEntity<>(response,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id){
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if(tutorialData.isPresent()){
            return new ResponseEntity<>(tutorialData.get(),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial){
        try {
            Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(),tutorial.isPublished()));
            return new ResponseEntity<>(_tutorial,HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id
                                            ,@RequestBody Tutorial tutorial){
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            Tutorial _tutorial = tutorialData.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(_tutorial),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<Map<String,Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "3") int size) {
        try {
            Pageable paging = PageRequest.of(page,size);
            Page<Tutorial> pageTuto = tutorialRepository.findByPublished(true, paging);
            List<Tutorial> tutorials = pageTuto.getContent();

            if (pageTuto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String,Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTuto.getNumber());
            response.put("totalItems", pageTuto.getTotalElements());
            response.put("totalPages", pageTuto.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
