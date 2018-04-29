package com.example.a15520.newdictionary_ver2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UserChainedComparator implements Comparator<UserModel> {

    private List<Comparator<UserModel>> listComparators;

            @SafeVarargs

   public UserChainedComparator(Comparator<UserModel>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

            @Override
    public int compare(UserModel emp1, UserModel emp2) {
        for (Comparator<UserModel> comparator : listComparators) {
            int result = comparator.compare(emp1, emp2);
            if (result != 0) {
                return -result;
            }
        }
        return 0;
    }
}
