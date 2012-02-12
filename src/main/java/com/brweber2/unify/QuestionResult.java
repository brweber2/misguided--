package com.brweber2.unify;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public class QuestionResult {

    private List<UnificationResult> results = new ArrayList<UnificationResult>();

    public boolean successful()
    {
        return !results.isEmpty();
    }

    public List<UnificationResult> getResults()
    {
        return results;
    }

    public void addIfUnifies( UnificationResult result )
    {
        if ( result.unifies() )
        {
            results.add( result );
        }
    }
}
