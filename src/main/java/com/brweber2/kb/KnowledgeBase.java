package com.brweber2.kb;

import com.brweber2.term.Rule;
import com.brweber2.term.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public class KnowledgeBase {
    
    private List<KnowledgeBaseEntry> entries = new ArrayList<KnowledgeBaseEntry>();

    // should we make the functor and arity the key???
    private Map<String,List<KnowledgeBaseEntry>> mappedEntries = new HashMap<String,List<KnowledgeBaseEntry>>();
    
    public void add( KnowledgeBaseEntry entry )
    {
        entries.add( entry );
        String functor = getFunctor( entry );
        if ( functor != null )
        {
            if ( !mappedEntries.containsKey(functor) )
            {
                mappedEntries.put( functor, new ArrayList<KnowledgeBaseEntry>());
            }
            mappedEntries.get( functor ).add( entry );
        }
    }

    public void remove( KnowledgeBaseEntry entry )
    {
        entries.remove( entry );
        String functor = getFunctor( entry );
        if ( functor != null )
        {
            if ( mappedEntries.containsKey(functor) )
            {
                mappedEntries.get(functor).remove(entry);
            }
        }
    }

    public List<KnowledgeBaseEntry> getEntries() {
        return entries;
    }
    
    public List<KnowledgeBaseEntry> getEntriesByFunctor( String functor ) {
        return mappedEntries.get( functor );
    }
    
    protected String getFunctor( KnowledgeBaseEntry entry )
    {
        String functor;
        if ( entry instanceof Rule )
        {
            functor = ((Rule) entry).getHead().getFunctor();
        }
        else
        {
            functor = ((Term) entry).getFunctor();
        }
        return functor;
    }
}
