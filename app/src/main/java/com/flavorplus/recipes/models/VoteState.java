package com.flavorplus.recipes.models;

public class VoteState {
    private int upvotes, downvotes, vote = 0;

    public VoteState() {
    }

    public VoteState(int upvotes, int downvotes, int vote) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.vote = vote;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
