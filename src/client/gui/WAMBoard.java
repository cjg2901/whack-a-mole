package client.gui;

import java.util.List;

public class WAMBoard
{
    /**
     * the observers of this model
     */

    private List<Observer<WAMBoard>> observers;

    /**
     * The view calls this method to add themselves as an observer of the model
     */
    public void addObserver(Observer<WAMBoard> observer)
    {
        this.observers.add(observer);
    }

    /**
     * When the model changes, the observers are notified via their update methid
     */
    private void alertObservers()
    {
        for(Observer<WAMBoard> obs: this.observers)
        {
            obs.update(this);
        }
    }
}
