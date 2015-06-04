package SpaceInvaders_V4.Util;

import com.badlogic.jglfw.Glfw;




//System timer implementation
public class SystemTimer {
    
    //timer sleep variables
    private static double variableYieldTime, lastTime;

    //get high resolution time in seconds
    //@return double time in seconds
    public static double getTime() {
       
        return Glfw.glfwGetTime();
        
    }

    //implement sleep interval to sync to given fps
    //@param fps the desired frame trate in frames per second
    public static void sync(int fps) {
        //must be positve int. Cannot divide by zero
        if (fps < 0) {
            return;
        }
         
        //time in seconds to sleep this frams
        double sleepTime = 1.0 / fps;
        // yieldTime + remainder micro & nano seconds if smaller than sleepTime
        double yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % 0.001);
        //time the sync goes over by
        double overSleep = 0;

        //calculate and sleep the desired sleepTime
        try {
            while (true) {
                //check time ellapsed
                double t = getTime() - lastTime;

                //if ellapsed is less than wait interval
                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);//sleep 1 ms
                } else if (t < sleepTime) {
                    Thread.yield();
                } else {
                    overSleep = t - sleepTime;
                    break;//exit loop
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //set end-of-loop time
            lastTime = getTime() - Math.min(overSleep, sleepTime);

            //auto tune the time sync should yield
            if (overSleep > variableYieldTime) {
                //increase by 200 microsecs (1/5 ms)
                variableYieldTime = Math.min(variableYieldTime + 0.0002, sleepTime);
            } else if (overSleep < variableYieldTime - 0.0002) {
                //decrease by 200 microsecs 
                variableYieldTime = Math.max(variableYieldTime - 0.0002, 0);
            }
        }
    }
}
