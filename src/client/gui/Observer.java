package client.gui;

/**
 *
 * @param <Subject>
 */
public interface Observer<Subject> {
    /**
     *
     * @param subject
     */
    void update(Subject subject);
}
