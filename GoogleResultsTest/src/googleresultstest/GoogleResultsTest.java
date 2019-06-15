/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googleresultstest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author kosta
 */
public class GoogleResultsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BufferedReader br = null;
       String line = "";
        String cvsSplitBy = ",";
        ArrayList<String> names=new ArrayList<>();
		
      try{
		  //Variable file is the csv that comes from the python program and the fw variable is where the file where the output will go
        File file=new File("C:/Users/kosta/downloads/scannedcsv.txt");
        FileWriter fw = new FileWriter("C:/Users/kosta/downloads/csvWebsites.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        Scanner sc = new Scanner(file);
         
		 //scanning the csv file
         while (sc.hasNextLine()){
             String s=sc.nextLine();
             /*the fields array contains every element of the csv  scanned element so the 6 indexes contain
			  name ,ministry,legal form-type of service,political sector ,public sector,more information*/
             String[] fields = s.split(cvsSplitBy);
			
          
			 //Getting google results
             String searchlink="https://www.google.gr/search?q="+URLEncoder.encode(fields[0],"UTF-8");
                Document doc=Jsoup.connect(searchlink).get();
                Element link=doc.select("cite").first();
               
             //appending the website found to every line
             out.println(s+",\""+link.text()+"\"");
             
         }
		
	
    }
      }
      catch(Exception e){System.out.println(e);}
      
      
      
}
