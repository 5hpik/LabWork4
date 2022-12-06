package shpik.jmt3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

public class MainController {
    @FXML
    private Label label;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private ProgressBar progressBar = new ProgressBar();

    private boolean isThreadRunning = false;

    private Thread thread;
    @FXML
    private void onStartButtonClick()
    {
        startButton.setText("Restart");
        if (isThreadRunning)
        {
            Thread t1 = new Thread(new LoadThread(progressBar), "LoadThread");
            t1.start();
        }
        else
        {
            Thread t = new Thread(new LoadThread(progressBar));
            thread = t;
            t.start();
            isThreadRunning = true;
        }
    }

    @FXML
    private void onPauseButtonClick()
    {
        if (pauseButton.getText().equals("Pause"))
        {
            Thread t1 = new Thread(new LoadThread(progressBar), "LoadThread1");
            t1.start();
            pauseButton.setText("Continue");
            startButton.setDisable(true);
        }
        else
        {
            synchronized (System.out)
            {
                System.out.notifyAll();
            }

            pauseButton.setText("Pause");
            startButton.setDisable(false);
        }
    }

    @FXML
    private void onStopButtonClick()
    {
        thread.interrupt();
        progressBar.setProgress(0);
        isThreadRunning = false;
        startButton.setText("Start");
    }
}