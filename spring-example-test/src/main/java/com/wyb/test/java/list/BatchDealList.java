package com.wyb.test.java.list;

import com.wyb.test.java.common.BaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    }


    //    1.使用 subList 方法：您可以使用 subList 方法将 List 分成多个子列表。例如，以下代码将 List 分成大小为 batchSize 的子列表：
    public static <T> List<List<T>> batchList(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return batches;
    }

    //    2.使用 stream API：您可以使用 stream API 将 List 分成多个子列表。例如，以下代码将 List 分成大小为 batchSize 的子列表：
    public static <T> List<List<T>> streamBatchList(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(i * batchSize, Math.min(list.size(), (i + 1) * batchSize)))
                .collect(Collectors.toList());
    }


    public static <T> List<List<T>> streamBatchList2(List<T> list, int partialLimit) {
        int limit = (list.size() + partialLimit - 1) / partialLimit;
        // 使用流遍历操做
        List<List<T>> mglist = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            mglist.add(list.stream().skip(i * partialLimit).limit(partialLimit).collect(Collectors.toList()));
        });
        return mglist;
    }

    public static <T> List<List<T>> parallelStreamBatchList(List<T> list, int partialLimit) {
        // 获取需要分割的次数，注意不能直接除以批次数量
        int limit = (list.size() + partialLimit - 1) / partialLimit;
        // 获取分割后的集合
        List<List<T>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel()
                .map(a -> list.stream().skip(a * partialLimit).limit(partialLimit).parallel()
                        .collect(Collectors.toList())).collect(Collectors.toList());
        // 执行具体业务方法。 打印代替
        return splitList;
    }


}