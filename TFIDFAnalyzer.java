package com.ly.dangdiren.textanalysis.cure;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算TFIDF
 * Created by yh21126 on 2016/12/15.
 */
@Service
public class TFIDFAnalyzer {

    //维度
    private Set<String> dimension;

    public Set<String> getDimension() {
        return dimension;
    }

    /**
     * 计算TF
     * @param sampleTexts
     */
    public List<Map<String, Double>> getTFIDF(List<String> sampleTexts){
        int size = sampleTexts.size();
        //没有正规化的TF
        List<Map<String, Integer>> tfNormal = new ArrayList<>(size);
        //IDF
        Map<String, Double> idf = new HashMap<>();
        //正规化的TF
        List<Map<String, Double>> tf = new ArrayList<>(size);
        //用来对应传入参数的顺序
        List<Integer> sequence = new ArrayList<>(size);
        //用来存储TF_IDF
        List<Map<String, Double>> TF_IDF = new ArrayList<>(size);
        //包含某词汇的文档总数
        Map<String, Integer> wordDocCounts = new HashMap<>();
        //维度
        dimension = new HashSet<>();

        Map<String, Integer> eachTfNormal;
        Map<String, Double> eachTf, eachTFIDF;
        //分词后的词汇List
        List<String> words;
        Integer wordCount, totalWords, docCount;
        String text;
        for(int i=0;i<sampleTexts.size();i++){
            sequence.add(i);
            text = sampleTexts.get(i);
            words = wordTokenizer(text);
            eachTfNormal = new HashMap<>();
            eachTf = new HashMap<>();
            for(String word : words){
                //存储维度
                if(!dimension.contains(word)){
                    dimension.add(word);
                }
                //计算非正规化的TF
                if((wordCount = eachTfNormal.get(word)) != null){
                    eachTfNormal.put(word, ++wordCount);
                }else{
                    eachTfNormal.put(word, 1);
                }
            }
            tfNormal.add(eachTfNormal);
            totalWords = eachTfNormal.size();
            for(Map.Entry<String, Integer> entry : eachTfNormal.entrySet()){
                //计算正规化的TF
                eachTf.put(entry.getKey(), (entry.getValue().doubleValue()/totalWords));
                //计算包含某词汇的文档总数
                if((docCount = wordDocCounts.get(entry.getKey())) != null){
                    wordDocCounts.put(entry.getKey(), ++docCount);
                }else{
                    wordDocCounts.put(entry.getKey(), 1);
                }
            }
            tf.add(eachTf);
        }

        //计算IDF
        for(Map.Entry<String, Integer> entry : wordDocCounts.entrySet()){
            double p = (entry.getValue() + 0.0) / size + 0.01;
            idf.put(entry.getKey(), (-p * Math.log10(p) - (1 - p) * Math.log10(1 - p)));
//            idf.put(entry.getKey(), Math.log10((size + 0.0)/entry.getValue()));
        }

        for(Map<String, Double> map : tf){
            eachTFIDF = new HashMap<>();
            for (Map.Entry<String, Double> entry : map.entrySet()){
                eachTFIDF.put(entry.getKey(), entry.getValue() * idf.get(entry.getKey()));
            }
            TF_IDF.add(eachTFIDF);
        }

        return TF_IDF;
    }


    /**
     * 分词，使用去掉停用词的方法
     * @param text
     * @return
     */
    public List<String> wordTokenizer(String text){
//        List<Term> terms = NotionalTokenizer.segment(text);
//        return terms.stream().map(t -> t.word).collect(Collectors.toList());
        List<String> keyword = HanLP.extractKeyword(text, 5);
        return keyword;
    }
}
