package com.example.cy_hill;

public class LeaderBoard implements Comparable<LeaderBoard> {
    private final String name;
    private final int score;
    private final String college;

    public LeaderBoard(String name, int score, String college) {
        this.name = name;
        this.score = score;
        this.college = college;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public String getCollege() {
        return college;
    }

    @Override
    public int compareTo(LeaderBoard leaderBoard) {
        LeaderBoard other = (LeaderBoard) leaderBoard;
        if (this.score > other.score) {
            return -1;
        } else if (this.score < other.score) {
            return 1;
        }
        return 0;
    }
}
