package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetProfileController {
	@RequestMapping("/getlinprof")
	public LinkedInProfile profile(@RequestParam(value="id") String id){
		LinkedInProfile profile=new LinkedInProfile(id);
		
		LinkedInProfileParser parser=new LinkedInProfileParser(profile);
		parser.parseProfileParams();

		MongoDBManager mgr=new MongoDBManager(profile);
		mgr.insertProfileIntoDB();

		return profile;
	}
	

}
