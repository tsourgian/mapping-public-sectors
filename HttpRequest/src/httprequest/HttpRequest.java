/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.*;
/**
 *
 * @author kosta
 */
public class HttpRequest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> websites=new ArrayList<>();
         ArrayList<String> apiKeys=new ArrayList<>();
         int keyIndex=0;
         int webIndex=0;
        try{
            //declaring the output file
             FileWriter fw = new FileWriter("builtwithoutput.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw);
             /*the csv.txt file contains the following fields:name,ministry,legal form-type of service,political sector,public sector,more information,website*/
             File file=new File("csv.txt");
             Scanner sc = new Scanner(file);
             while (sc.hasNextLine()){
             String s=sc.nextLine();
			 String [] fields = s.split(",");
             websites.add(fields[6]);
             }
             //scanning the file which contains a key for builtwith.com on every line
             File keysFile=new File("keys.txt");
             Scanner sc2=new Scanner(keysFile);
             while (sc2.hasNextLine()){
             String s=sc2.nextLine();
             apiKeys.add(s);
             }
      try{
          while(webIndex<websites.size()){
             //Sending request and getting response
             if(!websites.get(webIndex).equalsIgnoreCase("noWebsite")){
             String BWQuery="https://api.builtwith.com/v12/api.json?KEY="+apiKeys.get(keyIndex)+"&LOOKUP="+websites.get(webIndex);
			 URL url = new URL(BWQuery);
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 int responseCode = con.getResponseCode();
		     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     
                     //Getting to the technologies path needed 
                     JSONObject myResponse = new JSONObject(response.toString());
                     JSONArray js=(JSONArray) myResponse.get("Results");
                     if(js.length()!=0){
                   String server="";
                   String framework="";
                   String hosting="self hosted";
                   String copyright="N/A";
                   String ssl="N/A";
                   String cdn="N/A";
                   JSONObject obj=(JSONObject) js.get(0);
                   JSONObject name=(JSONObject) obj.get("Result");
                   JSONArray paths=(JSONArray) name.get("Paths");
                   JSONObject firstPath=(JSONObject) paths.get(0);
                   JSONArray technologies=(JSONArray) firstPath.get("Technologies");
                   for(int i=0;i<technologies.length();i++){
                   JSONObject tempObj=(JSONObject) technologies.get(i);
                   String tag=tempObj.get("Tag").toString();
                   if(tag.equalsIgnoreCase("framework")){
                       framework =framework +tempObj.get("Name").toString()+"/";
                   }
                   if(tag.equalsIgnoreCase("Web Server")){
                       server=server +tempObj.get("Name").toString()+"/";
                   }
                   if(tag.equalsIgnoreCase("cdn")){
                   cdn="yes";
                   }
                   if(tag.equalsIgnoreCase("ssl")){
                   ssl="yes";
                   }
                   if(tag.equalsIgnoreCase("hosting")){
                   hosting=tempObj.get("Name").toString();
                   }
                   if(tag.equalsIgnoreCase("copyright")){
                   copyright=tempObj.get("Name").toString();
                   }
                   }
                   String output="\""+websites.get(webIndex)+"\",\""+framework+"\","+"\""+server+"\","+"\""+cdn+"\","+"\""+ssl+"\","+"\""+hosting+"\","+"\""+copyright+"\""; 
                   out.println(output);
                   webIndex=webIndex+1;
                     }else{
                     //api key allocation used
                         keyIndex=keyIndex+1;
                         //System.out.println("key index:"+keyIndex);
                     }
          }else{
          String output="\""+websites.get(webIndex)+"\",\"\",\"\",\"\",\"\",\"\",\"\"";
          System.out.println(output);
          out.println(output);
          webIndex=webIndex+1;
          }}
      }catch(Exception e){e.printStackTrace();}
    out.close();
    }catch(Exception e){e.printStackTrace();}
    }
}
