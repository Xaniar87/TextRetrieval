package edu.arizona.cs;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueryEngine {
    boolean indexExists=false;
    String inputFilePath ="";
    StandardAnalyzer analyzer = new StandardAnalyzer();
    Directory index = new RAMDirectory();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
//    config.setSimilarity(TFIDFSimilarity);

    public QueryEngine(String inputFile){
        inputFilePath =inputFile;
        buildIndex();
    }

    private void buildIndex() {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(inputFilePath).getFile());

        try (Scanner inputScanner = new Scanner(file)) {
            IndexWriter w = new IndexWriter(index, config);
            while (inputScanner.hasNextLine()) {
                String line=inputScanner.nextLine();
//                System.out.println(line);
                String[] str=line.split(" ",2);
//                System.out.println(str[0]);
                addDoc(w,str[0],str[1]);
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexExists = true;
    }

    public static void main(String[] args ) {
        try {
            String fileName = "input.txt";
            System.out.println("********Welcome to  Homework 3!");
            String[] query13a = {"information", "retrieval"};
            QueryEngine objQueryEngine = new QueryEngine(fileName);
            List<ResultClass> ans=objQueryEngine.runQ1_2_c(query13a);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<ResultClass> runQ1_1(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if (!indexExists) {
            buildIndex();
        }
        try {
            String querystr = query.length > 0 ? query[0] : "lucene";
            Query q = new QueryParser("body", analyzer).parse(querystr);

            int hitsPerPage = 2;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
//            System.out.println("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
//                System.out.println((i + 1) + ". " + d.get("docid") + "  body: "+"\t" + d.get("body"));
            }

            List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for(int i=0;i<hitsPerPage;++i) {
                ResultClass objResultClass= new ResultClass();
                int docId = hits[i].doc;
                float score=hits[i].score;
//                Document d = searcher.doc(docId);
                objResultClass.DocName =searcher.doc(docId);
                doc_score_list.add(objResultClass);
            }
            return doc_score_list;
        }
        catch(Exception ex){
        System.out.println("error occurred: "+ex.getMessage());
            List<ResultClass> ans = new ArrayList<ResultClass>();
            ans = returnDummyResults(1);
            return ans;
        }
    }

    public List<ResultClass> runQ1_2_a(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if (!indexExists) {
            buildIndex();
        }
        try {
//            String querystr = query.length > 0 ? query[0] : "lucene";
            Query q = new QueryParser("body", analyzer).parse(query[0]+" AND "+query[1]);

            int hitsPerPage = 2;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
//            System.out.println("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("docid") + "  body: "+"\t" + d.get("body"));
            }

            List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for(int i=0;i<hitsPerPage;++i) {
                ResultClass objResultClass= new ResultClass();
                int docId = hits[i].doc;
                float score=hits[i].score;
//                Document d = searcher.doc(docId);
                objResultClass.DocName =searcher.doc(docId);
                doc_score_list.add(objResultClass);
            }
            return doc_score_list;
        }
        catch(Exception ex){
            System.out.println("error occurred: "+ex.getMessage());
            List<ResultClass> ans = new ArrayList<ResultClass>();
            ans = returnDummyResults(1);
            return ans;
        }
    }

    public List<ResultClass> runQ1_2_b(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if (!indexExists) {
            buildIndex();
        }
        try {
//            String querystr = query.length > 0 ? query[0] : "lucene";
            Query q = new QueryParser("body", analyzer).parse(query[0]+" AND NOT "+query[1]);

            int hitsPerPage = 2;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
//            System.out.println("Found " + hits.length + " hits.");
            if (hits.length>0) {
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document d = searcher.doc(docId);
                    System.out.println((i + 1) + ". " + d.get("docid") + "  body: " + "\t" + d.get("body"));
                }
            }
            List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            if (hits.length>0) {
            for(int i=0;i<hitsPerPage;++i) {
                ResultClass objResultClass= new ResultClass();
                int docId = hits[i].doc;
                float score=hits[i].score;
//                Document d = searcher.doc(docId);
                objResultClass.DocName =searcher.doc(docId);
                doc_score_list.add(objResultClass);
            }}
            return doc_score_list;
        }
        catch(Exception ex){
            System.out.println("error occurred: "+ex.getMessage());
            List<ResultClass> ans = new ArrayList<ResultClass>();
            ans = returnDummyResults(1);
            return ans;
        }
    }

    public List<ResultClass> runQ1_2_c(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if (!indexExists) {
            buildIndex();
        }
        try {
//            String querystr = query.length > 0 ? query[0] : "lucene";
            Query q = new QueryParser("body", analyzer).parse(query[0]+" AND "+query[1]+" ~1");

//            Query q = new QueryParser("body", analyzer).parse("information AND awesome");

            int hitsPerPage = 1;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
//            System.out.println("Found " + hits.length + " hits.");
            if (hits.length>0) {
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document d = searcher.doc(docId);
                    System.out.println((i + 1) + ". " + d.get("docid") + "  body: " + "\t" + d.get("body"));
                }
            }
            List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            if (hits.length>0) {
                for(int i=0;i<hitsPerPage;++i) {
                    ResultClass objResultClass= new ResultClass();
                    int docId = hits[i].doc;
                    float score=hits[i].score;
//                Document d = searcher.doc(docId);
                    objResultClass.DocName =searcher.doc(docId);
                    doc_score_list.add(objResultClass);
                }}
            return doc_score_list;
        }
        catch(Exception ex){
            System.out.println("error occurred: "+ex.getMessage());
            List<ResultClass> ans = new ArrayList<ResultClass>();
            ans = returnDummyResults(1);
            return ans;
        }
    }

    public List<ResultClass> runQ1_3(String[] query) throws java.io.FileNotFoundException,java.io.IOException {
        if (!indexExists) {
            buildIndex();
        }
        try {
            String querystr = query.length > 0 ? query[0] : "lucene";
            Query q = new QueryParser("body", analyzer).parse(querystr);
            int hitsPerPage = 2;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
//            searcher.setSimilarity(TFIDFSimilarity);
//            code inspired by https://darakpanand.wordpress.com/2013/06/01/document-comparison-by-cosine-methodology-using-lucene/#more-53
//            and https://stackoverflow.com/questions/41090904/lucene-scoring-get-cosine-similarity-as-scores
            searcher.setSimilarity(new cosineTFIDF() {
                @Override
                public String toString() {
                    return null;
                }
            });

            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("docid") + "  body: "+"\t" + d.get("body"));
            }

            List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for(int i=0;i<hitsPerPage;++i) {
                ResultClass objResultClass= new ResultClass();
                int docId = hits[i].doc;
                float score=hits[i].score;
//                Document d = searcher.doc(docId);
                objResultClass.DocName =searcher.doc(docId);
                doc_score_list.add(objResultClass);
            }
            return doc_score_list;
        }
        catch(Exception ex){
            System.out.println("error occurred: "+ex.getMessage());
            List<ResultClass> ans = new ArrayList<ResultClass>();
            ans = returnDummyResults(1);
            return ans;
        }
//        if(!indexExists) {
//            buildIndex();
//        }
//        StringBuilder result = new StringBuilder("");
//        List<ResultClass>  ans=new ArrayList<ResultClass>();
//        ans =returnDummyResults(2);
//        return ans;
    }

    private  List<ResultClass> returnDummyResults(int maxNoOfDocs) {

        List<ResultClass> doc_score_list = new ArrayList<ResultClass>();
            for (int i = 0; i < maxNoOfDocs; ++i) {
                Document doc = new Document();
                doc.add(new TextField("title", "", Field.Store.YES));
                doc.add(new StringField("docid", "Doc"+Integer.toString(i+1), Field.Store.YES));
                ResultClass objResultClass= new ResultClass();
                objResultClass.DocName =doc;
                doc_score_list.add(objResultClass);
            }

        return doc_score_list;
    }
    private static void addDoc(IndexWriter w, String name, String body) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("docid", name, Field.Store.YES));
        doc.add(new TextField("body", body, Field.Store.YES));
        w.addDocument(doc);
    }

}




//            cosineTFIDF code inspired by https://darakpanand.wordpress.com/2013/06/01/document-comparison-by-cosine-methodology-using-lucene/#more-53
//            and https://stackoverflow.com/questions/41090904/lucene-scoring-get-cosine-similarity-as-scores

abstract class cosineTFIDF extends SimilarityBase {

    @Override
    protected float score(BasicStats stats, float termFreq, float docLength) {
        double tf = 1 + (Math.log(termFreq) / Math.log(2));
        double idf = Math.log((stats.getNumberOfDocuments() + 1) / stats.getDocFreq()) / Math.log(2);
        float dotProduct = (float) (tf * idf);
        return dotProduct;
    }

}
