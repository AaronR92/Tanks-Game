package com.aaronr92.tanksgame.util;

public class RewardResponse {
    private String reward;
    private String tank;

    public RewardResponse() {}

    public RewardResponse(String reward) {
        this.reward = reward;
    }

    public RewardResponse(String reward, String tank) {
        this.reward = reward;
        this.tank = tank;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getTank() {
        return tank;
    }

    public void setTank(String tank) {
        this.tank = tank;
    }
}
