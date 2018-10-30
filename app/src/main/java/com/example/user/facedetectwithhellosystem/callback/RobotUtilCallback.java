package com.example.user.facedetectwithhellosystem.callback;

/**
 * Created by jenny on 2017/5/5.
 */

public interface RobotUtilCallback {
    void RobotonStateChange(String state);
    void RobotonEventUserUtterance(int position);
}
