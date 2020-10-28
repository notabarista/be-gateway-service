package com.notabarrista.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple smoke test secured endpoint to be
 * accessed after login to test whether the user can access 
 * this with his token
 * 
 * This will be removed later
 * 
 * @author codrea.tudor
 *
 */
@RestController
@RequestMapping("/my")
public class MyRestController {

	@GetMapping("/hello")
	public String hello() {
		return "home";
	}

}
