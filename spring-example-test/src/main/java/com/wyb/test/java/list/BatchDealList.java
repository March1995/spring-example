package com.wyb.test.java.list;

import com.wyb.test.java.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author: Marcher丶
 * @Date: 2022-06-14 19:33
 **/
public class BatchDealList {


    public static void main(String[] args) {
        int batchSize = 100;

        List<BaseEntity> lists = new ArrayList<>();
        for (long i = 0; i < 1000; i++) {
            lists.add(new BaseEntity(i));
        }

        int size = lists.size();
        List<BaseEntity> tmpList = new ArrayList<>(batchSize);
        List<BaseEntity> re = new ArrayList<>(batchSize);
        if (size > batchSize) {
            for (int i = 0; i < size; i++) {
                BaseEntity baseEntity = lists.get(i);
                tmpList.add(baseEntity);
                List<BaseEntity> resultList  = doDeal(tmpList);
                re.addAll(lists.stream().filter(resultList::contains).collect(Collectors.toList()));
                tmpList.clear();
            }
        } else {
            doDeal(lists);
        }
        re.forEach(v -> {
            System.out.println(v.getId());
        });

    }


    private static List<BaseEntity> doDeal(List<BaseEntity> subList) {
        return subList.stream().filter(v -> v.getId() % 2 == 0).collect(Collectors.toList());
    }
}