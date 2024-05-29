public class NewThread extends Thread
{
    private int figure;

    private final int MIN_SLEEP_TIME = 100;

    private final int MAX_SLEEP_TIME = 500;

    private boolean isActive = true;

    @Override
    public void run()
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
                        System.out.println(getName() + " throw scissors");
                        break;
                    case 2:
                        System.out.println(getName() + " throw rock");
                        break;
                    case 3:
                        System.out.println(getName() + " throw paper");
                        break;
                }
                synchronized (this)
                {
                    wait();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public synchronized int getFigure()
    {
        return figure;
    }

    public void nextMatch()
    {
        notify();
    }

    public void disable()
    {
        isActive = false;
        notify();
    }
}
