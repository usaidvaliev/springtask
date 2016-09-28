package com.example;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetProfileController {
	
	@RequestMapping("/getlinprof")
	public LinkedInProfile profile(@RequestParam(value="id") String id){
		LinkedInProfile profile=new LinkedInProfile(id);
		
		LinkedInProfileParser parser=new LinkedInProfileParser(profile);
		try {
			parser.parseProfileParams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return profile;
	}
	

}
