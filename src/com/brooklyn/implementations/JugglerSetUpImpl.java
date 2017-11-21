package com.brooklyn.implementations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.brooklyn.interfaces.JugglerSetUp;

public class JugglerSetUpImpl implements JugglerSetUp {
	
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    static final Logger logger = LogManager.getLogger(JugglerSetUpImpl.class);

    ReentrantLock leftHand = new ReentrantLock();
    ReentrantLock rightHand = new ReentrantLock();
    static boolean canCatch = true;
    int sleepTime = 2000;
    int lockingTime = 150;

    JugglingBallThreadImpl jugglingballs;

    @Override
    public void startJuggling() {

        long startTime = System.currentTimeMillis();
        int balls = 0;        
        char ballName = 'A';

        try {

            while (canCatch == true) {

                if(ballName > 'Z')
                    ballName = 'A';

                jugglingballs = (new JugglingBallThreadImpl(leftHand, rightHand, balls, ballName));

                balls = balls + 1;
                ballName = (char) (ballName + 1);
                threadPool.execute(jugglingballs);                
                Thread.sleep(sleepTime);
            }
            System.out.println("\n");
            
            logger.debug("Locking both the hands...");

            leftHand.tryLock(lockingTime, TimeUnit.SECONDS);
            
            logger.debug("Now Left Hand is stopped juggling..");
            
            rightHand.tryLock(lockingTime, TimeUnit.SECONDS);
            
            logger.debug("Now Right Hand is stopped juggling..");

            threadPool.shutdown();
            
        }
        catch (InterruptedException e) {

            logger.error("Error in the JUGGLER :  ", e);
        }

        finally {

            long duration = System.currentTimeMillis() - startTime;
            
            logger.info(balls + " balls juggled for " + duration + " Milliseconds");
            
            logger.info("THE END..!!");
            System.exit(0);

          
        }

    }

}
