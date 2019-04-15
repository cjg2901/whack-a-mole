package client.gui;

/**
 * @author Craig Gebo
 * @param <Subject>
 */
public interface Observer<Subject> {
    /**
     *
     * @param subject
     */
    void update(Subject subject);
}
