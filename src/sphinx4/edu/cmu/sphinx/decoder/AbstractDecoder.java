package edu.cmu.sphinx.decoder;

import edu.cmu.sphinx.decoder.search.SearchManager;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.result.ResultListener;
import edu.cmu.sphinx.util.props.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** An abstract decoder which implements all functionality which is indpendent of the used decoding-pardigm (pull/push). */
public abstract class AbstractDecoder implements ResultProducer, Configurable {

    /** The sphinx property name for the name of the search manager to use */
    @S4Component(type = SearchManager.class)
    public final static String PROP_SEARCH_MANAGER = "searchManager";
    protected SearchManager searchManager;

    //note:
    // although only classes implementing <code>ResultListener</code> are allowed here we can not use
    // the type of the list because it does not extends <code>Configurable</code>
    @S4ComponentList(type = Configurable.class)
    public static final String PROP_RESULT_LISTENERS = "resultListeners";
    protected List<ResultListener> resultListeners = new ArrayList<ResultListener>();

    /** If set to true the used search-manager will be automatically allocated in <code>newProperties()</code>. */
    @S4Boolean(defaultValue = false)
    public static final String AUTO_ALLOCATE = "autoAllocate";

    private String name;


    /**
     * Decode frames until recognition is complete
     *
     * @param referenceText the reference text (or null)
     * @return a result
     */
    public abstract Result decode(String referenceText);


    public void newProperties(PropertySheet ps) throws PropertyException {
        name = ps.getInstanceName();

        searchManager = (SearchManager) ps.getComponent(PROP_SEARCH_MANAGER);

        if (ps.getBoolean(AUTO_ALLOCATE)) {
            try {
                searchManager.allocate();
            } catch (IOException e) {
                throw new PropertyException(e, ps.getInstanceName(), PROP_SEARCH_MANAGER, "search manager allocation failed");
            }
        }

        List<? extends Configurable> list = ps.getComponentList(PROP_RESULT_LISTENERS);
        for (Configurable configurable : list) {
            assert configurable instanceof ResultListener;
            addResultListener((ResultListener) configurable);
        }
    }


    /** Allocate resources necessary for decoding */
    public void allocate() throws IOException {
        searchManager.allocate();
    }


    /** Deallocate resources */
    public void deallocate() {
        searchManager.deallocate();
    }


    /**
     * Adds a result listener to this recognizer. A result listener is called whenever a new result is generated by the
     * recognizer. This method can be called in any state.
     *
     * @param resultListener the listener to add
     */
    public void addResultListener(ResultListener resultListener) {
        resultListeners.add(resultListener);
    }


    /**
     * Removes a previously added result listener. This method can be called in any state.
     *
     * @param resultListener the listener to remove
     */
    public void removeResultListener(ResultListener resultListener) {
        resultListeners.remove(resultListener);
    }


    /**
     * Fires new results as soon as they become available.
     *
     * @param result the new result
     */
    protected void fireResultListeners(Result result) {
        for (ResultListener resultListener : resultListeners) {
            resultListener.newResult(result);
        }
    }


    public String toString() {
        return getName();
    }


    public String getName() {
        return name;
    }
}
