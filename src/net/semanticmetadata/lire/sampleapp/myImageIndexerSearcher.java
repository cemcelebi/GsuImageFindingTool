package net.semanticmetadata.lire.sampleapp;

import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.indexers.parallel.ParallelIndexer;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple class showing the use of the ParallelIndexer, which uses up as much
 * CPU as it can get.
 *
 * @author Mathias Lux, mathias@juggle.at and Nektarios Anagnostopoulos,
 * nek.anag@gmail.com
 */
public class myImageIndexerSearcher {

    public static void main(String[] args) throws IOException {
        // Checking if arg[0] is there and if it is a directory.
        boolean passed = false;
        if (args.length > 0) {
            File f = new File(args[0]);
            System.out.println("Indexing images in " + args[0]);
            if (f.exists() && f.isDirectory()) {
                passed = true;
            }
        }
        if (!passed) {
            System.out.println("No directory given as first argument.");
            System.out.println("Run \"myImageIndexerSearcher <directory>\" to index files of a directory.");
            System.exit(1);
        }
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String pwd = System.getProperty("user.dir");
        pwd = pwd + "/indexOfIaprtc";
        File PwDir = new File(pwd);
        if (PwDir.exists()) {
            System.out.println("testiye does indeed exist");
        } else {
            //use ParallelIndexer to index all photos from args[0] into "indexOfIaprtc" ... use 6 threads (actually 7 with the I/O thread).
            
            ParallelIndexer indexer = new ParallelIndexer(6, "indexOfIaprtc", args[0]);
            //use this to add you preferred builders. For now we go for CEDD, FCTH and AutoColorCorrelogram
            indexer.addExtractor(CEDD.class);
            indexer.addExtractor(FCTH.class);
            indexer.addExtractor(AutoColorCorrelogram.class);
            indexer.run();
        }
        System.out.println("Finished indexing.");
        System.out.println("Here comes the search part!");
        net.semanticmetadata.lire.sampleapp.Searcher.main();
        System.out.println("Were you satisfied by the search? Y/N?");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String girdi = br.readLine();
        System.out.println("girdi: " + girdi);
//        while(girdi.equals("y")==false || girdi.equals("n")==false){
//           System.out.println("Only Y/N");
//           br = new BufferedReader(
//            new InputStreamReader(System.in));
//        girdi = br.readLine();
//        }
        if (girdi.equals("y")) {
            System.out.println("awesome! goodbye!");
            System.exit(1);
        } else {
            System.out.println("Text based search is beginning");
            net.semanticmetadata.lire.sampleapp.tutoIndexer.main();
        }

    }
}
