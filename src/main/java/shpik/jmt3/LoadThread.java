package shpik.jmt3;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

class LoadThread extends Thread
{
    private final ProgressBar progressBar;
    private static int progress;
    private static boolean isRestarted = false;
    private static boolean isPaused = false;

    private static boolean isStopped = false;

    public LoadThread(ProgressBar inputProgressBar)
    {
        progressBar = inputProgressBar;
    }

    @Override
    public void run()
    {
        if (Thread.currentThread().getName().equals("LoadThread"))
        {
            isRestarted = true;
        }
        else if (Thread.currentThread().getName().equals("LoadThread1"))
        {
            isPaused = true;
        }
        else
        {
            for (int i = 1; i <= 1000; i++)
            {
                if (isRestarted)
                {
                    Platform.runLater(new Runnable() {public void run()
                        {
                            progressBar.setProgress(0);
                        }});

                    isRestarted = false;
                    i = 1;
                }
                else if (isPaused)
                {
                    try
                    {
                        synchronized (System.out)

                        {
                            System.out.wait();
                        }
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }

                    isPaused = false;
                }
                else if (isInterrupted())
                {
                    return;
                }
                else
                {
                    try
                    {
                        Thread.sleep(20);
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }

                    progress = i;

                    Platform.runLater(new Runnable() {
                        public void run()
                        {
                            progressBar.setProgress(progress / 1000.0);
                        }
                    });
                }
            }
        }
    }
}