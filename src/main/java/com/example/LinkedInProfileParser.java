package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class LinkedInProfileParser {
	
	private LinkedInProfile profile;
	private String profileHTML;
	
	public LinkedInProfileParser(LinkedInProfile profile){
		this.profile=profile;
	}
	

	public void parseProfileParams(){
		downloadProfileHTML();
		
//		find photo URL
		String imageInfo=profileHTML.substring(profileHTML.indexOf("image photo lazy-load")+26,profileHTML.indexOf("width")).trim();
		String imageLink=imageInfo.substring(imageInfo.indexOf("data-delayed-url")+18, imageInfo.length()-1);
		profile.setPhotoURL(imageLink);

//		find name
		
	}
	
	public void addProfileToDB(){
		
	}
	
	private void downloadProfileHTML(){
		
		Scanner sc=null;
		File profileData=null;
		URL conUrl=null;
		HttpsURLConnection connection=null;
		
		String httpsURL="https://www.linkedin.com/in/"+profile.getId();

		try {
			conUrl = new URL(httpsURL);
			connection=(HttpsURLConnection)conUrl.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
			connection.setRequestProperty("Accept", "image/webp,image/*,*/*;q=0.8");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			
			InputStream is=connection.getInputStream();
			profileData=new File(profile.getId());

			FileOutputStream fos=new FileOutputStream(profileData);
			GZIPInputStream gzis=new GZIPInputStream(is);

			int length;
			byte b[]=new byte[2048];
			
			while ((length = gzis.read(b))!=-1) {
				fos.write(b);
			}
			
			is.close();
			fos.close();
			gzis.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		if(profileData==null)return "";
		
		try {
			sc=new Scanner(profileData);
			while (sc.hasNextLine()) {
				String line=sc.nextLine().trim();
				if(line.indexOf("profile-overview-content")!=-1)profileHTML=line;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		return profileHTML;
	}
}
