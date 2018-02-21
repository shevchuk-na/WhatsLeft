package ru.whatsleft.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int amount;

    private int defaultChange;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Change> changeList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    @NotNull
    private Category category;

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private User teamLeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Change> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<Change> changeList) {
        this.changeList = changeList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDefaultChange() {
        return defaultChange;
    }

    public void setDefaultChange(int defaultChange) {
        this.defaultChange = defaultChange;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }
}
