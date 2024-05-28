import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Game
{
    private List<NewThread> players;

    public Game()
    {
        players = new LinkedList<>();

        for(int i = 0; i < 3; i++)
        {
            players.add(new NewThread());
        }
    }
    public void startGame(int winsNecessary)
    {
        int winner;
        int[] victoriesCount = new int[3];

        for(int n = 0; n < players.size(); n++)
            players.get(n).start();

        boolean firstIteration = true;

        for(int i = 0; i < winsNecessary;)
        {
            try
            {
                int logCount = 1;

                if(!firstIteration)
                {
                    logCount = NewThread.gameLog.size();

                    for (int n = 0; n < players.size(); n++)
                        players.get(n).nextMatch();
                }

                while (NewThread.gameLog.size() != logCount + 3)
                    sleep(100);

                winner = getCurrentMatchWinner();
                firstIteration = false;

                if (winner == -1)
                    continue;

                victoriesCount[winner] += 1;
                i = getMaxVictoriesCount(victoriesCount);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        synchronized (NewThread.gameLog)
        {
            NewThread.gameLog.add("����� " + getGameWinner(victoriesCount) + " ������ ������ "
                    + winsNecessary + " ����� � ���� ����������� ����������.");
        }
        gameOver();
    }

    private String getGameWinner(int[] victoriesCount)
    {
        int maxValue = getMaxVictoriesCount(victoriesCount);
        int winner = -1;

        for(int i = 0; i < victoriesCount.length; i++)
        {
            if (maxValue == victoriesCount[i])
                winner = i;
        }

        return players.get(winner).getName();
    }

    private int getCurrentMatchWinner()
    {
        ArrayList<Integer> figures = new ArrayList<Integer>();

        for(NewThread thread : players)
            figures.add(thread.getFigure());

        boolean isFirstWinner = (figures.get(0) == 1 && figures.get(1) == 3 && figures.get(2) == 3) ||
                (figures.get(0) == 2 && figures.get(1) == 1 && figures.get(2) == 1) ||
                (figures.get(0) == 3 && figures.get(1) == 2 && figures.get(2) == 2);
        boolean isSecondWinner = (figures.get(1) == 1 && figures.get(0) == 3 && figures.get(2) == 3) ||
                (figures.get(1) == 2 && figures.get(0) == 1 && figures.get(2) == 1) ||
                (figures.get(1) == 3 && figures.get(0) == 2 && figures.get(3) == 2);
        boolean isThirdWinner = (figures.get(2) == 1 && figures.get(0) == 3 && figures.get(1) == 3) ||
                (figures.get(2) == 2 && figures.get(0) == 1 && figures.get(1) == 1) ||
                (figures.get(2) == 3 && figures.get(0) == 2 && figures.get(1) == 2);

            if (isFirstWinner)
            {
                synchronized (NewThread.gameLog)
                {
                    NewThread.gameLog.add("� ������ ������� ������ �����");
                    return 0;
                }
            }

            if (isSecondWinner)
            {
                synchronized (NewThread.gameLog)
                {
                    NewThread.gameLog.add("� ������ ������� ������ �����");
                    return 1;
                }
            }

            if (isThirdWinner)
            {
                synchronized (NewThread.gameLog)
                {
                    NewThread.gameLog.add("� ������ ������� ������ �����");
                    return 2;
                }
            }

            synchronized (NewThread.gameLog)
            {
                NewThread.gameLog.add("� ������ �����.");
                return -1;
            }
    }
    private int getMaxVictoriesCount(int[] victoriesCount)
    {
        int maxValue = victoriesCount[0];

        for(int i = 1; i < victoriesCount.length; i++)
            maxValue = Math.max(maxValue, victoriesCount[i]);

        return maxValue;
    }

    private void gameOver()
    {
        for(NewThread thread : players)
        {
            thread.disable();
            thread.interrupt();
        }

    }
}
