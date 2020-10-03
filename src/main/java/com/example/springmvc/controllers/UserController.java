package com.example.springmvc.controllers;


import com.example.springmvc.models.User;
import com.example.springmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

import static com.example.springmvc.controllers.UserController.Routes.*;

@Controller
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/greeting/{name}")
    @ResponseBody
    public String sayHello ( @PathVariable(name = "name") String name ) {
        return "Helo" + name;
    }

    @GetMapping
    public String getIndexPage ( Model model ) {
        List<User> users = repository.findAll();
        if (!users.isEmpty()) {
            model.addAttribute( "users", users );
        }

        return INDEX;
    }

    @GetMapping("/signup")
    public String getSignupPage ( User user ) {
        return ADD_USER;
    }


    @PostMapping("/add-user")
    public String createNewUser ( @Valid User user, BindingResult result, Model model ) {
        if (result.hasErrors()) {
            return ADD_USER;
        }

        repository.save( user );

//        List<User> users = repository.findAll(); // pot scoate si Model model din ()
//        if(!users.isEmpty()){
//            model.addAttribute("users", users);
//        }
        return REDIRECT;
    }

    @GetMapping(value = "delete/{id}")
    public String deleteUser ( @PathVariable("id") Long id, User user, Model model ) {
        repository.deleteById( id );
        //    model.addAttribute( "users",repository.findAll() );

        return REDIRECT+"/";
    }

    @GetMapping("/edit/{id}") //doa merge in pag de modificare
    public String getEditPage ( @PathVariable("id") Long id, User user, Model model ) {
        User existingUser = repository.findById( id ).<IllegalArgumentException>orElseThrow( () -> {
            throw new IllegalArgumentException( String.format( "missing user with is %s", id ) );
        } );
        model.addAttribute( "user", existingUser );

        return Routes.UPDATE_USER;
    }


    @PostMapping("/update/{id}")
    public String updateUserAndReturnToIndex (@PathVariable("id") Long id, @Valid User user, BindingResult result,Model model){
        if (result.hasErrors()){
           // user.setId( id );
            return UPDATE_USER;
        }
        repository.save( user );
        return REDIRECT+"/";

    }

    static class Routes {
        static final String INDEX = "index";
        static final String ADD_USER = "add-user";
        static final String UPDATE_USER = "update-user";
        static final String REDIRECT = "redirect:";
    }
    //sau cu enum:
    // REDIRECT("redirect:");


}

