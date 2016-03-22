/*
 * This file is part of the LIRE project: http://lire-project.net
 * LIRE is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LIRE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LIRE; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 360, Boston, MA  02111-1607  USA
 *
 * We kindly ask you to refer the any or one of the following publications in
 * any publication mentioning or employing Lire:
 *
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval –
 * An Extensible Java CBIR Library. In proceedings of the 16th ACM International
 * Conference on Multimedia, pp. 1085-1088, Vancouver, Canada, 2008
 * URL: http://doi.acm.org/10.1145/1459359.1459577
 *
 * Lux Mathias. Content Based Image Retrieval with LIRE. In proceedings of the
 * 19th ACM International Conference on Multimedia, pp. 735-738, Scottsdale,
 * Arizona, USA, 2011
 * URL: http://dl.acm.org/citation.cfm?id=2072432
 *
 * Mathias Lux, Oge Marques. Visual Information Retrieval using Java and LIRE
 * Morgan & Claypool, 2013
 * URL: http://www.morganclaypool.com/doi/abs/10.2200/S00468ED1V01Y201601ICR025
 *
 * Copyright statement:
 * --------------------
 * (c) 2002-2013 by Mathias Lux (mathias@juggle.at)
 *     http://www.semanticmetadata.net/lire, http://www.lire-project.net
 */

package net.semanticmetadata.lire.sampleapp;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;

import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * User: Mathias Lux, mathias@juggle.at
 * Date: 25.05.12
 * Time: 12:19
 */
public class Searcher {
        int hitsNeeded=60;
    public static void main() throws IOException {
        // Checking if arg[0] is there and if it is an image.
        BufferedImage img = null;
        boolean passed = true;//modified to true
            String thePathToBeSearched="/Users/cemcelebi/Desktop/iaprtc12/images/37/37142.jpg";
            File f = new File(thePathToBeSearched);
            System.out.println(thePathToBeSearched+" is the image i'm looking for.");
            //bu asagidaki if'in iceriginin duzenlenip, try catch kullanilmasi gerekmekte
            //olmayan bir resimi aramaya calistiginda null pointer exception almamali!!
            if (f.exists()) {
                
                try {
                    
                    img = ImageIO.read(f);
                    
                    passed = true;
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            else{
                System.out.println("img read edilemedi !");
            }
        
        if (!passed) {
            System.out.println("No image given as first argument.");
            System.out.println("Run \"Searcher <query image>\" to search for <query image>.");
            System.exit(1);
        }
        /*INDEXER CODE HERE ???????? */
        
        //net.semanticmetadata.lire.sampleapp.Indexer.main();
        IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get("indexOfIaprtc")));
        ImageSearcher searcherCEDD = new GenericFastImageSearcher(60, CEDD.class);
        ImageSearcher searcherACC= new GenericFastImageSearcher(60, AutoColorCorrelogram.class);
        ImageSearcher searcherFCTH= new GenericFastImageSearcher(60, FCTH.class);
       // theRealSearchPart("CEDD",searcherCEDD,ir,img);
       // theRealSearchPart("ACC",searcherACC,ir,img);
       // theRealSearchPart("FCTH",searcherFCTH,ir,img);
        String[] CEDDhits = new String[60];
        String[] ACChits = new String[60];
        String[] FCTHhits = new String[60];
        
        ACChits=theRealSearchPart("ACC",searcherACC,ir,img);
        CEDDhits=theRealSearchPart("CEDD",searcherCEDD,ir,img);
        FCTHhits=theRealSearchPart("FCTH",searcherFCTH,ir,img);
        String[] stringOfCEDDandACChits=new String[60];
        String[] stringOfCEDDandFCTHhits=new String[60];
        String[] stringOfACCandFCTHhits=new String[60];
        
        ArrayList listOfHitsCombined=new ArrayList();
        for(int i=0;i<60;i++){
            stringOfCEDDandACChits[i]=(findSameValues(CEDDhits,ACChits)[i]);
            stringOfCEDDandFCTHhits[i]=(findSameValues(CEDDhits,FCTHhits)[i]);
            stringOfACCandFCTHhits[i]=(findSameValues(ACChits,FCTHhits)[i]);
          //  System.out.println("findSameValues CEDD/ACC: "+stringOfCEDDandACChits[i]);
          //  System.out.println("findSameValues CEDD/fcTH: "+stringOfCEDDandFCTHhits[i]);
          //  System.out.println("findSameValues ACC/fcTH: "+stringOfACCandFCTHhits[i]);
          //  findSameValues(ACChits,FCTHhits);
     }
        for(int i=0;i<60;i++){
           //System.out.println("findSameValues hitsCombined: "+findSameValues(stringOfCEDDandACChits,stringOfCEDDandFCTHhits)[i]);
            
            listOfHitsCombined.add(findSameValues(stringOfCEDDandACChits,stringOfCEDDandFCTHhits)[i]);
            
            
        }
        
        System.out.println("The content of listOfHitsCombined is: "+ listOfHitsCombined);
  }
//        ImageSearcher searcher = new GenericFastImageSearcher(60, AutoColorCorrelogram.class);

        // searching with a image file ...
        public static String[] theRealSearchPart(String featureType, ImageSearcher searcher, IndexReader ir,BufferedImage img)throws IOException {
        String[] hitString = new String[60];
        ImageSearchHits hits = searcher.search(img, ir);
        System.out.println("hits.length type is: '"+hits.length()+"'expected: 60-33");
        // searching with a Lucene document instance ...
//        ImageSearchHits hits = searcher.search(ir.document(0), ir);
        for (int i = 0; i < hits.length(); i++) {
            String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
            //System.out.println(featureType+": "+hits.score(i) + ": \t" + fileName);
            hitString[i]=fileName;
            //System.out.println(featureType+"hitString: "+hitString[i]);
        }
        return hitString;
        
    }
        /*public static String[] averageRankCalculator(String hitsOfCEDD,String hitsOfACC,String hitsOfFCTH){
            String[] hitCombined = new String[60];
            for(int i=0;i<60;i++){
                
            }
        }*/
        public static String[] findSameValues(String[] str1,String[] str2){
            //This search and match algorithm is O(n^2), and it's not good. it MUST be optimized.
            int excEP$inizm=0;
            String[] sameOnes = new String[60];
            for(int i=0;i<60;i++){
                for(int j=0;j<60;j++){
                   // System.out.println("str1 and str2"+str1[i]+str2[i]);
                    try{
                        if(str1[i].equals(str2[j])){
                        //System.out.println("found the same values: "+str1[i]+str2[i]);
                        if(str1[i]!=null){
                            sameOnes[i]=str1[i];
                        }
                        
                    }
                    }
                    catch(Exception e){
                            excEP$inizm++;
                            
                   }
                 }
            }
            
                //System.out.println("exceptioniZM:"+excEP$inizm);
            return sameOnes;
        }


}