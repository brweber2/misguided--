package com.brweber2.term;

import com.brweber2.kb.KnowledgeBaseEntry;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public interface Term extends KnowledgeBaseEntry, RuleBody {
    String getFunctor();
}
