package com.aaronr92.tanksgame.util;

public class RewardResponse {
    private String reward;

    public RewardResponse() {}

    public RewardResponse(String reward) {
        this.reward = reward;
    }

    public String getReward() {
        return reward;
    }

    public RewardResponse setReward(String reward) {
        this.reward = reward;
        return this;
    }
}
