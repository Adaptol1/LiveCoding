import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewThread extends Thread
{
    private int figure;

    private final int MIN_SLEEP_TIME = 100;

    private final int MAX_SLEEP_TIME = 500;

    private boolean isActive = true;

    static List<String> gameLog = new ArrayList<>();
    static
    {
        gameLog = Collections.synchronizedList(gameLog);
        gameLog.add("Game results:");
    }

    @Override
    public void run()
    {
        synchronized (gameLog)
        {
            while(isActive)
            {
                try
                {
                    figure = (int) (1 + 2 * Math.random());
                    long sleepTime = (long) (MIN_SLEEP_TIME + Math.random() * (MAX_SLEEP_TIME - MIN_SLEEP_TIME));
                    sleep(sleepTime);

                    switch (figure)
                    {
                        case 1:
                            gameLog.add(Thread.currentThread().getName() +
                                    " throw scissors");
                            break;
                        case 2:
                            gameLog.add(Thread.currentThread().getName() +
                                    " throw rock");
                            break;
                        case 3:
                            gameLog.add(Thread.currentThread().getName() +
                                    " throw paper");
                            break;
                    }

                    gameLog.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized int getFigure()
    {
        return figure;
    }

    public void nextMatch()
    {
        synchronized (gameLog)
        {
            gameLog.notify();
        }
    }

    public void disable()
    {
        isActive = false;
        synchronized (gameLog)
        {
            gameLog.notify();
        }
    }
}
