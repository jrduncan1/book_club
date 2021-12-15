package com.jduncan.bookclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.jduncan.bookclub.models.Book;
import com.jduncan.bookclub.services.BookService;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	// **** DISPLAY ROUTES ****
	
	// Displays form that allows user to add a book
	@GetMapping("/book/create")
	public String showCreateForm(@ModelAttribute("newBook") Book newBook) {
		return "createBook.jsp";
	}
	
	
	// **** ACTION ROUTES ****

}
