package uk.ncl.giacomobergami.utils.algorithms;

import uk.ncl.giacomobergami.utils.structures.ImmutablePair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClusterDifference<T> implements Predicate<T> {

    private static List local_empty_list = new ArrayList();
    public type getChange() {
        return change;
    }
    public Map<T, typeOfChange> getChanges() {
        return changes;
    }
    public void setChange(type change) {
        this.change = change;
    }
    public void setChanges(Map<T, typeOfChange> changes) {
        this.changes = changes;
    }

    @Override
    public boolean test(T t) {
        switch (change) {
            case UNCHANGED -> {
                return true;
            }
            case CHANGED -> {
                var test = changes.get(t);
                return ((test != typeOfChange.REMOVAL_OF));
            }
        }
        return false;
    }

    public enum type {
        UNCHANGED,
        CHANGED
    }

    public enum typeOfChange {
        REMOVAL_OF,
        ADDITION_OF
    }

    public ClusterDifference.type change;
    public Map<T, typeOfChange> changes;

    public double computeEditDistance(double scoreRemoval, double scoreAddition) {
        double totalChange = 0;
        if (change == type.CHANGED) {
            for (var changeType : changes.values()) {
                switch (changeType) {
                    case REMOVAL_OF -> totalChange += scoreRemoval;
                    case ADDITION_OF -> totalChange += scoreAddition;
                }
            }
        }
        return totalChange;
    }

    public static <T> double computeCumulativeChange(List<ClusterDifference<T>> ls, double scoreRemoval, double scoreAddition) {
        double totalChange = 0;
        for (var x : ls) {
            totalChange += x.computeEditDistance(scoreRemoval, scoreAddition);
        }
        return totalChange;
    }

    public ClusterDifference() {
        change = type.UNCHANGED;
        changes = new HashMap<>();
    }

    @Override
    public String toString() {
        return "ClusterDifference{" +
                "change=" + change +
                ", changes=" + changes +
                '}';
    }

    public ClusterDifference(type change, Map<T, typeOfChange> changes) {
        this.change = change;
        this.changes = changes;
    }

    public static <T> List<T> reconstructFrom(ClusterDifference<T> self, List<T> ls, Comparator<T> cmp) {
        if (self == null) return ls;
        else return self.reconstructFrom(ls, cmp);
    }

    public List<T> reconstructFrom(List<T> ls, Comparator<T> cmp) {
        if (ls == null) ls = (List<T>)local_empty_list;
        if (change == type.CHANGED) {
            var tmp = ls.stream().filter(this).collect(Collectors.toList());
            for (var x : changes.entrySet()) {
                if (x.getValue() == typeOfChange.REMOVAL_OF) continue;
                tmp.add(x.getKey());
            }
            tmp.sort(cmp);
            return tmp;
        }
        return ls;
    }

    public static <T> ClusterDifference<T> listOfChanges(List<T> ls,
                                                         List<T> rs,
                                                         Comparator<T> no) {
        if (ls == null)
            ls = (List<T>)local_empty_list;
        else
            ls.sort(no);
        if (rs == null)
            rs = (List<T>)local_empty_list;
        else
            rs.sort(no);
        Map<T, typeOfChange> change = new HashMap<>();
        int i = 0, j = 0;
        int N = ls.size(), M = rs.size();
        while ((i<N) && (j<M)) {
            var cost =
                    no.compare(ls.get(i), rs.get(j));
            if (cost == 0) {
                i++; j++;
            } else if (cost < 0) {
                change.put(ls.get(i++), typeOfChange.REMOVAL_OF);
            } else {
                change.put(rs.get(j++), typeOfChange.ADDITION_OF);
            }
        }
        while (i<N) {
            change.put(ls.get(i++), typeOfChange.REMOVAL_OF);
        }
        while (j<M) {
            change.put(rs.get(j++), typeOfChange.ADDITION_OF);
        }
        return change.isEmpty() ? new ClusterDifference<>() : new ClusterDifference<>(type.CHANGED, change);
    }

    public static <H, K, T> ImmutablePair<ImmutablePair<H, List<T>>, List<ClusterDifference<T>>>
    computeTemporalDifference(Map<H, Map<K, List<T>>> toDiff,
                              K holder,
                              Comparator<T>tmp) {
        boolean first = true;
        List<T> prevLs = null;
        ImmutablePair<H, List<T>> cp = null;
        List<ClusterDifference<T>> lsDiff = new ArrayList<>();
        for (var x : toDiff.entrySet()) {
            if (first) {
                prevLs = x.getValue().get(holder);
                if (prevLs == null) prevLs = (List<T>)local_empty_list;
                cp = new ImmutablePair<>(x.getKey(), prevLs);
                first = false;
            } else {
                List<T> currLs = x.getValue().get(holder);
                lsDiff.add(listOfChanges(prevLs, currLs, tmp));
                prevLs = currLs;
            }
        }
        return new ImmutablePair<>(cp, lsDiff);
    }

    public static <H, K, T> HashMap<K, ImmutablePair<ImmutablePair<H, List<T>>, List<ClusterDifference<T>>>>
    computeTemporalDifference(Map<H, Map<K, List<T>>> toDiff,
                              Collection<K> holder,
                              Comparator<T>tmp) {
        HashMap<K, ImmutablePair<ImmutablePair<H, List<T>>, List<ClusterDifference<T>>>> res = new HashMap<>();
        for (var h : holder) {
            res.put(h, computeTemporalDifference(toDiff, h, tmp));
        }return res
                ;
    }

//    public static <H, T> List<List<T>> reconstruct(ConcretePair<ConcretePair<H, List<T>>, List<ClusterDifference<T>>> reconstruction,
//                                                   Comparator<T> cmp) {
//        List<List<T>> result = new ArrayList<>(reconstruction.getRight().size()+1);
//        result.add(reconstruction.getLeft().getValue());
//        for (int i = 0, N = reconstruction.getRight().size(); i<N; i++) {
//            result.add(reconstruction.getRight().get(i).reconstructFrom(result.get(result.size()-1), cmp));
//        }
//        return result;
//    }
//    public static <H, K, T> void test(Map<H, Map<K, List<T>>> toDiff,
//                                                            List<K> holderList,
//                                                            Comparator<T> cmp) {
//        for (K k : holderList) {
//            var res = diff(toDiff, k, cmp);
//            var backup = reconstruct(res, cmp);
//            int i = 0;
//            for (var x : toDiff.entrySet()) {
//                var obj = backup.get(i++);
//                if (obj == null) obj = Collections.emptyList();
//                var LS = x.getValue().getOrDefault(k, Collections.emptyList());
//                if (!LS.containsAll(obj))
//                    System.err.println(LS+" vs1" +obj);
//                if (!obj.containsAll(LS))
//                    System.err.println(LS+" vs2" +obj);
//            }
//        }
//    }
}
