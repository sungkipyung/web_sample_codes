/**
 * 
 */
package com.simple.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home Controller
 * 
 * @author hothead
 */
@Controller
public class HomeController {
	@RequestMapping("/")
	public String index() {
		return "index";
	}
}
