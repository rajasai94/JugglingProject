package com.brooklyn.implementations;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.brooklyn.interfaces.JugglingBallThread;
import com.brooklyn.thejuggler.Hand;

public class JugglingBallThreadImpl implements Runnable, JugglingBallThread {

    static final Logger logger = LogManager.getLogger(JugglingBallThreadImpl.class);

    Hand hand;
    ReentrantLock lefthand;
    ReentrantLock righthand;
    Random random;
    int balls;
    char ballName;

    public JugglingBallThreadImpl(ReentrantLock leftHand, ReentrantLock rightHand, int balls, char ballName) {

        this.lefthand = leftHand;
        this.righthand = rightHand;
        this.balls = balls;
        this.ballName = ballName;
        logger.info("No. of balls juggling : " + balls);
        System.out.println("\n");
        logger.info("Newly added ball is : " + ballName);
    }
    
    @Override
    public void run() {

        while (true) {

            hand = Hand.HANDSEMPTY;

            startAction();

            logger.info("Current hand is " + hand);

            if (hand == Hand.LEFT) {

                logger.info("Left hand balls " + balls);
                continue;
            }
            else if (hand == Hand.RIGHT) {

                logger.info("Right hand balls " + balls);
                continue;
            }
            else {

                logger.info("Ball hit the ground " + ballName);
                JugglerSetUpImpl.canCatch = false;
                break;
            } 
        }
    }

    private long sleepTime() {
        random = new SecureRandom();
        long time = random.nextInt(1000) + 1500;
        return time;

    }

    private long waitTime() {
        random = new SecureRandom();
        long time = random.nextInt(100) + 200;
        return time;
    }

    @Override
    public void startAction() {

        try {
            Thread.sleep(sleepTime());
            logger.info("No.of balls in catchable zone are " + balls);

                if (!lefthand.isHeldByCurrentThread()) {
                    logger.info("Left hand catches a Ball and Ball name is " + ballName);
                    righthand.tryLock(150, TimeUnit.MILLISECONDS);
                    int Duration = 1000 + ((int) (Math.random()*500));
                    logger.info( ballName + " Ball is in the air " + Duration + " Milliseconds");

                }
                if(!righthand.isHeldByCurrentThread()) {
                    logger.info("Right hand catches a Ball and Ball name is " + ballName);
                    lefthand.tryLock(150, TimeUnit.MILLISECONDS);
                    int Duration = 1000 + ((int) (Math.random()*500));
                    logger.info( ballName + " Ball is in the air " + Duration + " Milliseconds");
                }

            Thread.sleep(waitTime());

            if (lefthand.isHeldByCurrentThread()) {
                lefthand.unlock();
                hand = Hand.LEFT;
                logger.info("Left Hand is free..");

            }
            else if (righthand.isHeldByCurrentThread()) {
                righthand.unlock();
                hand = Hand.RIGHT;
                logger.info("Right Hand is free..");
            }

        }
        catch (InterruptedException e) {
            logger.error("Error in the startAction method");
        }
    }

}
