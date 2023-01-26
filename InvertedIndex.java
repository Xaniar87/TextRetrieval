package edu.arizona.cs;
import edu.arizona.cs.Document;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class InvertedIndex {

    private final HashMap<String, HashMap<Integer, LinkedList<Integer>>> hashmap;
    boolean indexExists=false;
    String inputFilePath ="";
    public InvertedIndex(String inputFile) throws FileNotFoundException {
//        inputFilePath ="hw2-java-Xaniar87/src/main/resources/"+inputFile;
        BufferedReader doc=new BufferedReader(new FileReader(inputFile));
        HashMap<String,HashMap<Integer, LinkedList<Integer>>> map=new HashMap();
        String s;
        while(true){
            try {
                if ((s=doc.readLine())==null){
                    break;
                }else{
                    String[] lineArr=s.split(" ");
                    int index;
                    for (index=1; index<lineArr.length; index++) {
                        String word = lineArr[index];
                        if (!map.containsKey(word)) {
                            HashMap<Integer, LinkedList<Integer>> innerMap = new HashMap<>();
                            LinkedList<Integer> indLL = new LinkedList<>();
                            indLL.add(index-1);
                            innerMap.put(Integer.parseInt(lineArr[0].substring(3)), indLL);
                            map.put(word, innerMap);
                        } else {
                            if (!map.get(word).containsKey(Integer.parseInt(lineArr[0].substring(3)))) {
                                LinkedList<Integer> indLL = new LinkedList<>();
                                indLL.add(index-1);
                                HashMap<Integer, LinkedList<Integer>> innerMap = map.get(word);
                                map.get(word).put(Integer.parseInt(lineArr[0].substring(3)), indLL);
                            } else {
                                map.get(word).get(Integer.parseInt(lineArr[0].substring(3))).add(index-1);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        for (String key:map.keySet()){
//            System.out.println(key +": "+map.get(key));
//        }

        this.hashmap=map;
    }

    public static void main(String[] args ) {
        try {
            //a boiler plate main function if you want to test without using mvn test
            //build index
            String fileName = "hw2-java-Xaniar87/src/main/resources/Docs.txt";
            System.out.println("********Welcome to  Homework 2!");
            String query = "schizophrenia /2 drug";
            InvertedIndex objInvertedIndex = new InvertedIndex(fileName);
//            Document[]  ans = objInvertedIndex.runQ8_1_1(query);

            Document[]  ans = objInvertedIndex.runQ8_2_directional(query);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Document[] runQ8_1_1(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        //check if index exists, else create one
//        System.out.println("Q1-1: ");
//        Document ans1=new Document("Doc1",3,1);
//        Document ans2=new Document("Doc2",1,2);
//        Document[] dummy_ans= {ans1,ans2};
        Document[] answer=new Document[2];
        String[] line=query.split(" ");
        String word1=line[0];
        String word2=line[2];
        int k=Integer.parseInt(String.valueOf(line[1].charAt(1)));
        HashMap<Integer,LinkedList<Integer>> map1=this.hashmap.get(word1);
        HashMap<Integer,LinkedList<Integer>> map2=this.hashmap.get(word2);
//        System.out.println(this.hashmap.get(word1)+ " "+this.hashmap.get(word2));
        int len1=map1.keySet().size();
        int[] docsArr1=new int[len1];
        int i=0;
        for (int setitem:map1.keySet()){
            docsArr1[i]=setitem;
            i++;
        }
        int len2=map2.keySet().size();
        int[] docsArr2=new int[len2];
        int j=0;
        for (int setitem2:map2.keySet()){
            docsArr2[j]=setitem2;
            j++;
        }
//        System.out.println(Arrays.toString(docsArr1)+" "+Arrays.toString(docsArr2)+"\n");
        int p1=0;
        int p2=0;
        int count=0;
        while (p1<docsArr1.length && p2<docsArr2.length){
//            System.out.println("p1: "+docsArr1[p1]+ " p2: "+docsArr2[p2]);
            if (docsArr1[p1]==docsArr1[p2]){
                LinkedList<Integer> l=new LinkedList<>();
                int pp1=0;
                int pp2=0;
                LinkedList<Integer> posPP1=null;
                LinkedList<Integer> posPP2=null;
//                System.out.println(map1.get(docsArr1[p1]).size());
//                System.out.println(map2.get(docsArr2[p2]).size());
                while (pp1<map1.get(docsArr1[p1]).size()){
                    while(pp2<map2.get(docsArr2[p2]).size()){
//                        System.out.println(pp1+" "+pp2);
                        posPP1=map1.get(docsArr1[p1]);
                        posPP2=map2.get(docsArr2[p2]);
//                        System.out.println(posPP1.get(pp1)-posPP2.get(pp2));
                        if(Math.abs(posPP1.get(pp1)-posPP2.get(pp2))<=k){
//                            System.out.println("here");
                            l.add(posPP2.get(pp2));
                            if (posPP1.get(pp1)<posPP2.get(pp2)){
//                                System.out.println("break "+posPP1.get(pp1)+" "+posPP2.get(pp2));
                                break;
                            }
                        }
                        pp2++;
                    }
//                    System.out.println(l.toString());
                    while (!l.isEmpty() && Math.abs(l.get(0)-posPP1.get(pp1))>k){
                        l.remove(0);
                    }
                    for(Integer ps:l){
                        Document ans=new Document("Doc"+docsArr1[p1],posPP1.get(pp1),ps);
//                        System.out.println("Document: "+ans.docid+" "+ ans.position1+" "+ans.position2+"\n");

                        answer[count]=ans;
                        count++;
                    }
                    pp1++;
                }
                p1++;
                p2++;
            }else if(docsArr1[p1]<docsArr1[p2]){
                p1++;
            }else{
                p2++;
            }
        }
        return answer;
    }

    public Document[] runQ8_1_2(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        //check if index exists, else create one
//        Document ans1=new Document("Doc1",3,1);
//        Document ans2=new Document("Doc2",1,2);
//        Document ans3=new Document("Doc3",5,1);
//        Document[] dummy_ans= {ans1,ans2,ans3};
        Document[] answer=new Document[3];
        String[] line=query.split(" ");
        String word1=line[0];
        String word2=line[2];
        int k=Integer.parseInt(String.valueOf(line[1].charAt(1)));
        HashMap<Integer,LinkedList<Integer>> map1=this.hashmap.get(word1);
        HashMap<Integer,LinkedList<Integer>> map2=this.hashmap.get(word2);
//        System.out.println(this.hashmap.get(word1)+ " "+this.hashmap.get(word2));
        int len1=map1.keySet().size();
        int[] docsArr1=new int[len1];
        int i=0;
        for (int setitem:map1.keySet()){
            docsArr1[i]=setitem;
            i++;
        }
        int len2=map2.keySet().size();
        int[] docsArr2=new int[len2];
        int j=0;
        for (int setitem2:map2.keySet()){
            docsArr2[j]=setitem2;
            j++;
        }
//        System.out.println(Arrays.toString(docsArr1)+" "+Arrays.toString(docsArr2)+"\n");
        int p1=0;
        int p2=0;
        int count=0;
        while (p1<docsArr1.length && p2<docsArr2.length){
//            System.out.println("p1: "+docsArr1[p1]+ " p2: "+docsArr2[p2]);
            if (docsArr1[p1]==docsArr1[p2]){
                LinkedList<Integer> l=new LinkedList<>();
                int pp1=0;
                int pp2=0;
                LinkedList<Integer> posPP1=null;
                LinkedList<Integer> posPP2=null;
//                System.out.println(map1.get(docsArr1[p1]).size());
//                System.out.println(map2.get(docsArr2[p2]).size());
                while (pp1<map1.get(docsArr1[p1]).size()){
                    while(pp2<map2.get(docsArr2[p2]).size()){
//                        System.out.println(pp1+" "+pp2);
                        posPP1=map1.get(docsArr1[p1]);
                        posPP2=map2.get(docsArr2[p2]);
//                        System.out.println(posPP1.get(pp1)-posPP2.get(pp2));
                        if(Math.abs(posPP1.get(pp1)-posPP2.get(pp2))<=k){
//                            System.out.println("here");
                            l.add(posPP2.get(pp2));
                            if (posPP1.get(pp1)<posPP2.get(pp2)){
//                                System.out.println("break "+posPP1.get(pp1)+" "+posPP2.get(pp2));
                                break;
                            }
                        }
                        pp2++;
                    }
//                    System.out.println(l.toString());
                    while (!l.isEmpty() && Math.abs(l.get(0)-posPP1.get(pp1))>k){
                        l.remove(0);
                    }
                    for(Integer ps:l){
                        Document ans=new Document("Doc"+docsArr1[p1],posPP1.get(pp1),ps);
//                        System.out.println("Document: "+ans.docid+" "+ ans.position1+" "+ans.position2+"\n");

                        answer[count]=ans;
                        count++;
                    }
                    pp1++;
                }
                p1++;
                p2++;
            }else if(docsArr1[p1]<docsArr1[p2]){
                p1++;
            }else{
                p2++;
            }
        }
        return answer;
    }

    public Document[] runQ8_2_directional(String query) throws java.io.FileNotFoundException,ArrayIndexOutOfBoundsException {
        //check if index exists, else create one
//        Document ans1=new Document("Doc2",1,2);
//        Document[] dummy_ans= {ans1};
        Document[] answer=new Document[1];
        String[] line=query.split(" ");
        String word1=line[0];
        String word2=line[2];
        int k=Integer.parseInt(String.valueOf(line[1].charAt(1)));
        HashMap<Integer,LinkedList<Integer>> map1=this.hashmap.get(word1);
        HashMap<Integer,LinkedList<Integer>> map2=this.hashmap.get(word2);
//        System.out.println(this.hashmap.get(word1)+ " "+this.hashmap.get(word2));
        int len1=map1.keySet().size();
        int[] docsArr1=new int[len1];
        int i=0;
        for (int setitem:map1.keySet()){
            docsArr1[i]=setitem;
            i++;
        }
        int len2=map2.keySet().size();
        int[] docsArr2=new int[len2];
        int j=0;
        for (int setitem2:map2.keySet()){
            docsArr2[j]=setitem2;
            j++;
        }
//        System.out.println(Arrays.toString(docsArr1)+" "+Arrays.toString(docsArr2)+"\n");
        int p1=0;
        int p2=0;
        int count=0;
        while (p1<docsArr1.length && p2<docsArr2.length){
//            System.out.println("p1: "+docsArr1[p1]+ " p2: "+docsArr2[p2]);
            if (docsArr1[p1]==docsArr1[p2]){
                LinkedList<Integer> l=new LinkedList<>();
                int pp1=0;
                int pp2=0;
                LinkedList<Integer> posPP1=null;
                LinkedList<Integer> posPP2=null;
//                System.out.println(map1.get(docsArr1[p1]).size());
//                System.out.println(map2.get(docsArr2[p2]).size());
                while (pp1<map1.get(docsArr1[p1]).size()){
                    while(pp2<map2.get(docsArr2[p2]).size()){
//                        System.out.println(pp1+" "+pp2);
                        posPP1=map1.get(docsArr1[p1]);
                        posPP2=map2.get(docsArr2[p2]);
//                        System.out.println(posPP2.get(pp2)-posPP1.get(pp1));
                        if((posPP2.get(pp2)-posPP1.get(pp1))<=k && (posPP2.get(pp2)-posPP1.get(pp1))>0){
//                            System.out.println("here");
                            l.add(posPP2.get(pp2));
                            if (posPP1.get(pp1)<posPP2.get(pp2)){
                                System.out.println("break "+posPP1.get(pp1)+" "+posPP2.get(pp2));
                                break;
                            }
                        }
                        pp2++;
                    }
//                    System.out.println(l.toString());
                    while (!l.isEmpty() && ((l.get(0)-posPP1.get(pp1))<0 || (l.get(0)-posPP1.get(pp1))>k)){
                        l.remove(0);
                    }
                    for(Integer ps:l){
                        Document ans=new Document("Doc"+docsArr1[p1],posPP1.get(pp1),ps);
//                        System.out.println("Document: "+ans.docid+" "+ ans.position1+" "+ans.position2+"\n");

                        answer[count]=ans;
                        count++;
                    }
                    pp1++;
                }
                p1++;
                p2++;
            }else if(docsArr1[p1]<docsArr1[p2]){
                p1++;
            }else{
                p2++;
            }
        }
        return answer;
    }




}
