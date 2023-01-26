package edu.arizona.cs;

import edu.stanford.nlp.pipeline.CoreDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QueryEngineQ5 {
    boolean indexExists=false;
    String inputFilePath ="";
    HashMap<String, ArrayList<Integer>> countMap=new HashMap<String, ArrayList<Integer>>();
    HashMap<String, ArrayList<Double>> probMap=new HashMap<String, ArrayList<Double>>();
    int spamDocCount=0;
    int spamWordCount=0;
    int spamDocCountN=0;
    int spamWordCountN=0;
    int[][] confusionMat=new int[][]{{0,0},{0,0}};


    public QueryEngineQ5(){
        /*add your code*/
        buildModel();
    }

    private void buildModel() {

        /*add your code*/
        long startTime = System.nanoTime();
        String[] pathnames;
        File f = new File("src/spamDataset/spam-train");
        pathnames = f.list();
//        for (String pathname : pathnames) {
//            System.out.println(pathname);
//        }
        for (int i=0;i<pathnames.length;i++){
            try {
                BufferedReader docs=new BufferedReader(new FileReader("src/spamDataset/spam-train/"+pathnames[i]));
                String doc;
                while ((doc = docs.readLine()) != null) {
                    String[] words=doc.split(" ");
                    for (String word:words){
                        if(!countMap.containsKey(word)){
                            ArrayList<Integer> arr=new ArrayList<Integer>();
                            arr.add(2);
                            arr.add(1);
                            countMap.put(word,arr);
                        }else{
//                            Integer newVal=countMap.get(word).get(0)+1;
                            countMap.get(word).set(0,countMap.get(word).get(0)+1);
                        }
                        spamWordCount++;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            spamDocCount++;

        }
//        System.out.println(spamDocCount+" "+spamWordCount);
//        System.out.println(countMap.keySet().size());

        String[] pathnamesN;
        File fs = new File("src/spamDataset/nonspam-train");
        pathnamesN = fs.list();
//        for (String pathname : pathnamesN) {
//            System.out.println(pathname);
//        }
        for (int i=0;i<pathnamesN.length;i++){
            try {
                BufferedReader docs=new BufferedReader(new FileReader("src/spamDataset/nonspam-train/"+pathnamesN[i]));
                String doc;
                while ((doc = docs.readLine()) != null) {
                    String[] words=doc.split(" ");
                    for (String word:words){
                        if(!countMap.containsKey(word)){
                            ArrayList<Integer> arr=new ArrayList<Integer>();
                            arr.add(1);
                            arr.add(2);
                            countMap.put(word,arr);
                        }else{
//                            Integer newVal=countMap.get(word).get(0)+1;
                            countMap.get(word).set(1,countMap.get(word).get(1)+1);
                        }
                        spamWordCountN++;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            spamDocCountN++;

        }
//        System.out.println(spamDocCountN+" "+spamWordCountN);
//        System.out.println(countMap.keySet().size());
        for (String key:countMap.keySet()){
            if (!probMap.containsKey(key)){
                ArrayList<Double> probArr=new ArrayList<Double>();
                probArr.add((double) Math.log(1000000*countMap.get(key).get(0)/spamWordCount));
                probArr.add((double) Math.log(1000000*countMap.get(key).get(1)/spamWordCountN));
                probMap.put(key,probArr);
            }
//            System.out.println(key+": "+countMap.get(key));
//            System.out.println(key+": "+probMap.get(key));
        }
        long endTime   = System.nanoTime();
        long totalTime = (endTime - startTime)/1000000;
        System.out.println("training time in milliseconds: "+totalTime);
        String[] pathTestSpam;
        File fSpamTest = new File("src/spamDataset/spam-test");
        pathTestSpam = fSpamTest.list();
        int k=0;
        for (int i=0;i<pathTestSpam.length;i++){
            k++;
            try {
                BufferedReader docs=new BufferedReader(new FileReader("src/spamDataset/spam-test/"+pathTestSpam[i]));
                double SP=0.5d;
                double NS=0.5d;
                String doc;
                while ((doc = docs.readLine()) != null) {
                    String[] words=doc.split(" ");


                    for (String word:words){
                        if(probMap.containsKey(word)){
                            SP=probMap.get(word).get(0)+SP;
                            NS=probMap.get(word).get(1)+NS;

                        }
                    }
                }
//                System.out.println("SP: "+SP+" NS:"+NS);
                if(SP>=NS){
                    confusionMat[0][0]=confusionMat[0][0]+1;
                }else {
                    confusionMat[0][1]=confusionMat[0][1]+1;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        String[] pathTestNonspam;
        File fNonspamTest = new File("src/spamDataset/nonspam-test");
        pathTestNonspam = fNonspamTest.list();
        k=0;
        for (int i=0;i<pathTestNonspam.length;i++){
            k++;
            try {
                BufferedReader docs=new BufferedReader(new FileReader("src/spamDataset/nonspam-test/"+pathTestNonspam[i]));
                double SP=0.5d;
                double NS=0.5d;
                String doc;
                while ((doc = docs.readLine()) != null) {
                    String[] words=doc.split(" ");


                    for (String word:words){
                        if(probMap.containsKey(word)){
                            SP=probMap.get(word).get(0)+SP;
                            NS=probMap.get(word).get(1)+NS;

                        }
                    }
                }
//                System.out.println("SP: "+SP+" NS:"+NS);
                if(NS>=SP){
                    confusionMat[1][1]=confusionMat[1][1]+1;
                }else {
                    confusionMat[1][0]=confusionMat[1][0]+1;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("Confusion: "+confusionMat[0][0]+" "+confusionMat[0][1]+" "+confusionMat[1][0]+" "+confusionMat[1][1]);



    }

    public static void main(String[] args ) {
        try {
            /*add your code*/
            QueryEngineQ5 qen = new QueryEngineQ5();
//            qen.buildModel();
            qen.runQ5_2_f1score();

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    /*
    * "This method should return the F1 score of the classifier when run over test partition".
    * */
    public double runQ5_2_f1score()  {
//        System.out.println("Confusion: "+confusionMat[0][0]+" "+confusionMat[0][1]+" "+confusionMat[1][0]+" "+confusionMat[1][1]);

//        precision=TP/(TP+FP)
        double precision=(double) confusionMat[0][0]/(confusionMat[0][0]+confusionMat[0][1]);
//        System.out.println("precision: "+precision);
//        Recall=TP/(TP+FN)
        double recall=(double)confusionMat[0][0]/(confusionMat[0][0]+confusionMat[1][0]);
//        System.out.println("recall: "+recall);

//        f1=2*(precision*recall)/(precision+recall)
        double f1=(double) 2*(precision*recall)/(precision+recall);
        System.out.println("f1: "+f1);
//        double f1 = 0.7;
        double accuracy=(double) (confusionMat[0][0]+confusionMat[1][1])/(confusionMat[0][0]+confusionMat[0][1]+confusionMat[1][0]+confusionMat[1][1]);
//        System.out.println("accuracy: "+accuracy);

        return f1;
    }
}
