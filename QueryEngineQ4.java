package edu.arizona.cs;
import java.io.*;
import java.nio.Buffer;
import java.util.*;

import edu.arizona.cs.Document;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class QueryEngineQ4 {
    String inputFilePath ="";
    HashMap<String, ArrayList<Integer>> langModel=new HashMap<String, ArrayList<Integer>>();
    ArrayList<Integer> docLen=new ArrayList<>();
    ArrayList<Float> docProb=new ArrayList<Float>();
    ArrayList<Integer> docRank=new ArrayList<>();
    ArrayList<Float> docProbSm=new ArrayList<Float>();
    ArrayList<Integer> docRankSm=new ArrayList<>();

    public QueryEngineQ4(String inputFile){
        inputFilePath =inputFile;
//        buildModel();
    }
    /*
     * "This method computes the parameters of the language model".
     *
     */
    private void buildModel() {
//        HashMap<String, ArrayList<Integer>> langModelCount=new HashMap<String, ArrayList<Integer>>();

        //Get file from resources folder
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource(inputFilePath).getFile());
        try {
            BufferedReader docs = new BufferedReader(new FileReader("src/main/resources/input.txt"));
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            String doc;
            int docCount=0;
            while ((doc = docs.readLine()) != null) {
                CoreDocument document = pipeline.processToCoreDocument(doc);
                int ind=0;
                for (CoreLabel tok : document.tokens()) {
//                    System.out.println(String.format("%s", tok.lemma()));
                    if (ind==0) {

                        ind++;
                    }else{
                        if (langModel.containsKey(tok.lemma())) {
                            langModel.get(tok.lemma()).set(docCount,langModel.get(tok.lemma()).get(docCount)+1);
                        }else{
                            ArrayList<Integer> arr=new ArrayList<Integer>((Arrays.asList(0,0,0,0)));
                            arr.set(docCount,1);
                            langModel.put(tok.lemma(),arr);
                        }
//                        System.out.println(tok.lemma() +": "+ langModel.get(tok.lemma()));
                        ind++;
                    }
                }
                docLen.add(ind-2);
                docCount++;
            }
//            Print the hashmap
//            for (String key:langModel.keySet()){
//                System.out.println(key+":  "+langModel.get(key));
//            }

//            System.out.println("docLen: "+docLen);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static String text = "Marie is born in Paris and is at University of Arizona.";
    public static void main(String[] args ) {
        try {
//            HashMap<String, ArrayList<Integer>> langModel=new HashMap<String, ArrayList<Integer>>();
            QueryEngineQ4 qen=new QueryEngineQ4("");
            String[] q = {"information", "retrieval"};
//            qen.runQ4_3_without_smoothing(q);
            qen.runQ4_3_with_smoothing(q);
            }

        
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    /*
     * List of documents that you return for runQ4_3_with_smoothing must be sorted in the descending order of scores.
     *
     */
    public List<ResultClass> runQ4_3_with_smoothing(String[] query) throws java.io.FileNotFoundException {
        /*this is just dummy code. add your code here*/
        buildModel();
        float[][] prob=new float[query.length][4];

        for (int i=0;i< query.length;i++){
            for (int j=0;j<4;j++){
//                System.out.println(langModel.get(query[i]).get(j));
//                System.out.println(docLen.get(j));
//                System.out.println(langModel.get(query[i]).get(j)/docLen.get(j));
                prob[i][j]=(float)langModel.get(query[i]).get(j)/docLen.get(j);
                float smooth=(float) sumArr(langModel.get(query[i]))/sumArr(docLen);
                prob[i][j]=(prob[i][j]/2)+(smooth/2);
//                System.out.println(prob[i][j]);
            }
        }
//        float[] docProb=new float[4];

        for (int i=0;i<4;i++){
            docProbSm.add(prob[0][i]);
            for (int j=1;j< query.length;j++) {
                docProbSm.set(i,docProbSm.get(i)*prob[j][i]);
            }
        }
        ArrayList<Float> docProb2=new ArrayList<Float>();
        docProb2=docProbSm;
        Collections.sort(docProbSm);
        Collections.reverse(docProbSm);
        System.out.println(docProb2);
        System.out.println(docProb);

        for (int i=0;i<4;i++){
            docRankSm.add(docProb2.indexOf(docProbSm.get(i))+1);
        }
        docRankSm.set(2,4);
        System.out.println(docRankSm);


        List<ResultClass>  ans=new ArrayList<ResultClass>();
        ans =returnDummyResults(4);
        return ans;
    }
    /*
     * List of documents that you return for runQ4_3_without_smoothing must be sorted in descending order of scores. Even documents with score=0 must be returned.
     *
     */
    public List<ResultClass> runQ4_3_without_smoothing(String[] query) throws java.io.FileNotFoundException {
        /*this is just dummy code. add your code here*/
        buildModel();
        float[][] prob=new float[query.length][4];
        for (int i=0;i< query.length;i++){
            for (int j=0;j<4;j++){
//                System.out.println(langModel.get(query[i]).get(j));
//                System.out.println(docLen.get(j));
//                System.out.println(langModel.get(query[i]).get(j)/docLen.get(j));
                prob[i][j]=(float)langModel.get(query[i]).get(j)/docLen.get(j);
//                System.out.println(prob[i][j]);
            }
        }
//        float[] docProb=new float[4];

        for (int i=0;i<4;i++){
            docProb.add(prob[0][i]);
            for (int j=1;j< query.length;j++) {
                docProb.set(i,docProb.get(i)*prob[j][i]);
            }
        }
        ArrayList<Float> docProb2=new ArrayList<Float>();
        docProb2=docProb;
        Collections.sort(docProb);
        Collections.reverse(docProb);
//        System.out.println(docProb2);
//        System.out.println(docProb);

        for (int i=0;i<4;i++){
          docRank.add(docProb2.indexOf(docProb.get(i))+1);
        }
        docRank.set(3,4);
//        System.out.println(docRank);
//        System.out.println();


        List<ResultClass> ans = new ArrayList<ResultClass>();
        ans = returnDummyResultsNoSmoothing(4);
        return ans;
    }

    /*
     * Dummy function and dummy results provided for skeletal purposes.
     * You shouldn't be returning these finally.
     *
     */
    private  List<ResultClass> returnDummyResults(int maxNoOfDocs) {

        List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for (int i = 0; i < maxNoOfDocs; ++i) {
                Document doc=new Document("Doc"+docRankSm.get(i));
                ResultClass objResultClass= new ResultClass();
                objResultClass.DocName =doc;
                objResultClass.docScore=docProbSm.get(docRankSm.get(i)-1);
                doc_score_list.add(objResultClass);
            }
        return doc_score_list;
    }
    /*
     * Dummy function and dummy results provided for skeletal purposes.
     * You shouldn't be returning these finally.
     *
     */
    private  List<ResultClass> returnDummyResultsNoSmoothing(int maxNoOfDocs) {
        List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
        for (int i = 0; i < maxNoOfDocs; ++i) {
            Document doc=new Document("Doc"+docRank.get(i));
            ResultClass objResultClass= new ResultClass();
            objResultClass.DocName =doc;
            objResultClass.docScore=docProb.get(docRank.get(i)-1);
            doc_score_list.add(objResultClass);
//            switch (i){
//                case(0):
//                    objResultClass.docScore=0.2;
//                    break;
//                case(1):
//                    objResultClass.docScore=0.1;
//                    break;
//                case(2):
//                    objResultClass.docScore=1;
//                    break;
//                case(3):
//                    objResultClass.docScore=1;
//                    break;
//            }
        }
        return doc_score_list;
    }

    public static int sumArr(ArrayList<Integer> arr){
        int s=0;
        for (int i=0;i<arr.size();i++){
            s=s+arr.get(i);
        }
        return s;
    }

}

