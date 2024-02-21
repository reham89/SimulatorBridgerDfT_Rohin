package uk.ncl.giacomobergami.utils.algorithms;

import java.util.*;

public class CartesianProduct {

    public static  <K, V, Z extends Collection<V>> Set<Map<K, V>> mapCartesianProductWithSortedKeySequence(Map<K, Z> lists, List<K> orderedCollectionOfKeys, int i) {
        Set<Map<K, V>> resultLists = new HashSet<>();
        if (lists.size() == 0) {
            resultLists.add(Collections.emptyMap());
            return resultLists;
        } else {
            var current = orderedCollectionOfKeys.get(i);
            var firstList = lists.remove(current);
            if (firstList.isEmpty()) {
                return mapCartesianProduct(lists);
            } else {
                var remainingLists = mapCartesianProductWithSortedKeySequence(lists, orderedCollectionOfKeys, i+1);
                for (var condition : firstList) {
                    for (var remainingList : remainingLists) {
                        var resultList = new HashMap<>(remainingList);
                        resultList.put(current, condition);
                        resultLists.add(resultList);
                    }
                }
            }
        }
        return resultLists;
    }

    public static  <K, V, Z extends Collection<V>> Set<Map<K, V>> mapCartesianProduct(Map<K, Z> lists) {
        Set<Map<K, V>> resultLists = new HashSet<>();
        if (lists.size() == 0) {
            resultLists.add(Collections.emptyMap());
            return resultLists;
        } else {
            var current = lists.keySet().iterator().next();
            var firstList = lists.remove(current);
            if (firstList.isEmpty()) {
                return mapCartesianProduct(lists);
            } else {
                var remainingLists = mapCartesianProduct(lists);
                for (var condition : firstList) {
                    for (var remainingList : remainingLists) {
                        var resultList = new HashMap<>(remainingList);
                        resultList.put(current, condition);
                        resultLists.add(resultList);
                    }
                }
            }
        }
        return resultLists;
    }
}
