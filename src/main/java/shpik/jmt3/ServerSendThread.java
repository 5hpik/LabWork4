package shpik.jmt3;

public class ServerSendThread extends Thread
{
    private static boolean isPaused = false;
    private static boolean isStopped = false;

    private static double value;

    void Restart()
    {
        isPaused = false;
        this.interrupt();
    }

    void Pause()
    {

        isPaused = true;
    }

    void Resume()
    {
        isPaused = false;
    }

    void Stop()
    {
        isPaused = false;
        isStopped = true;
        synchronized (System.out) {
            System.out.notifyAll();
        }
    }

    public double getValue()
    {
        return value;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 1000; i++)
        {
            while (isPaused)
            {
                Thread.yield();
            }
            if (isStopped)
            {
                value = 0;
                isStopped = false;
                break;
            }

            value = (double) i / 1000;

            try
            {
                sleep(20);
            }
            catch (InterruptedException e)
            {
                this.interrupt();
                return;
            }
        }
    }
}
