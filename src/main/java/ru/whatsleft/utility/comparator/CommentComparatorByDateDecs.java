package ru.whatsleft.utility.comparator;

import ru.whatsleft.domain.Comment;

import java.util.Comparator;

public class CommentComparatorByDateDecs implements Comparator<Comment> {

    private static CommentComparatorByDateDecs instance = new CommentComparatorByDateDecs();

    public static CommentComparatorByDateDecs getInstance() {
        return instance;
    }

    @Override
    public int compare(Comment o1, Comment o2) {
        return -o1.getCreated().compareTo(o2.getCreated());
    }
}
