package ru.whatsleft.utility.comparator;

import ru.whatsleft.domain.Change;

import java.util.Comparator;

public class ChangeComparatorByDateDecs implements Comparator<Change> {

    private static ChangeComparatorByDateDecs instance = new ChangeComparatorByDateDecs();

    public static ChangeComparatorByDateDecs getInstance() {
        return instance;
    }

    @Override
    public int compare(Change o1, Change o2) {
        return -o1.getCreated().compareTo(o2.getCreated());
    }
}
