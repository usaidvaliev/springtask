package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LinkedInProfileParser {
	
	private LinkedInProfile profile;
	private File profileHTML;
	
	public LinkedInProfileParser(LinkedInProfile profile){
		this.profile=profile;
	}
	
	public void parseProfileParams() throws IOException{
		downloadProfileHTML();
		
		Document doc=Jsoup.parse(profileHTML,"UTF-8");
		
//		find photo URL
		String picURL=doc.select("div[class=\"profile-picture\"] img").attr("data-delayed-url").trim();
		if(picURL!=""){
			profile.setPhotoURL(picURL);
		}else {
			profile.setPhotoURL("NO_PHOTO");
		}
//		System.out.println(profile.getPhotoURL());
		
//		find name
		String profileName=doc.select("div[class=\"profile-overview-content\"] h1").html().trim();
		profile.setName(profileName);
//		System.out.println(profile.getName());
		
//		find current workplace
		String curWorkPlace=doc.select("tr[data-section=\"currentPositionsDetails\"]").select("span").html().trim();
		profile.setWorkplace(curWorkPlace);
		
//		System.out.println(profile.getWorkplace());

		deleteFileOnCompletion();
	}
	
	private void deleteFileOnCompletion(){
		try {
			Files.delete(profileHTML.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void downloadProfileHTML(){
		
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
			profileHTML=new File(profile.getId()+".html");

			FileOutputStream fos=new FileOutputStream(profileHTML);
			GZIPInputStream gzis=new GZIPInputStream(is);

			int length=0;
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
	}
}
