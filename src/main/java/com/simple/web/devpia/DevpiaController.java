/**
 * 
 */
package com.simple.web.devpia;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author hothead
 */
@RequestMapping("/devpia")
@Controller
public class DevpiaController {

	@RequestMapping("/form")
	public String form(ModelMap modelMap) {
		return "devpia/form";
	}

	@RequestMapping(value = "/sendMessage")
	public String sendMessag(@RequestParam String id, @RequestParam String pw,
			@RequestParam String rcvrIds, @RequestParam String message,
			ModelMap modelMap) {

		Set<String> rcvrSet = removeDup(rcvrIds.trim().split(","));
		
		DevpiaService service = new DevpiaService();
		try {
			Header[] cookie = service.loginDevpia(id, pw);

			for (String receiver : rcvrSet) {
				service.sendMessageWithNewLine(message, cookie, id, receiver);
			}
		} catch (Exception e) {

		} finally {
			service.destroy();
		}

		modelMap.addAttribute("message", "success");
		modelMap.addAttribute("rurl", "/devpia/form/");

		return "devpia/sendMessage";
	}

	private Set<String> removeDup(String[] array) {
		Set<String> set = new HashSet<String>(Arrays.asList(array));
		return set;
	}

}
