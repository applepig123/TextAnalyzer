package com.ly.dangdiren.textanalysis.cure;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 余弦定理计算
 * Created by yh21126 on 2016/12/16.
 */
@Service
public class CosineSimilarAlgorithm {

    /**
     * 计算余弦定理
     * @param vector1
     * @param vector2
     * @return
     */
    public double getSimilarity(Map<String, Double> vector1, Map<String, Double> vector2){
        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;
        Set<String> keySet1 = vector1.keySet();
        Set<String> keySet2 = vector2.keySet();
        for(String key : keySet1){
            if(vector1.containsKey(key) && vector2.containsKey(key)){
                numerator += vector1.get(key) * vector2.get(key);
            }

            denominator1 += vector1.get(key) * vector1.get(key);
        }

        for(String key : keySet2){
            denominator2 += vector2.get(key) * vector2.get(key);
        }

        return numerator / (Math.sqrt(denominator1) * Math.sqrt(denominator2));
    }
}
