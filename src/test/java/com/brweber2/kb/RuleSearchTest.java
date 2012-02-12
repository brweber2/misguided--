package com.brweber2.kb;

import com.brweber2.search.ProofSearch;
import com.brweber2.term.Atom;
import com.brweber2.term.ComplexTerm;
import com.brweber2.term.RuleAnd;
import com.brweber2.term.RuleImpl;
import com.brweber2.term.Variable;
import com.brweber2.unify.QuestionResult;
import com.brweber2.unify.UnificationResult;
import com.brweber2.unify.Unifier;
import com.brweber2.unify.Unify;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author brweber2
 * Copyright: 2012
 */
public class RuleSearchTest {
    @Test
    public void simpleKnowledgeBase()
    {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.add(new ComplexTerm("hello", new Atom("dave")));
        knowledgeBase.add(new ComplexTerm("hello", new Atom("gary")));
        knowledgeBase.add(new ComplexTerm("bye", new Atom("gary")));

        // what(Z) :- hello(Z), bye(Z).
        knowledgeBase.add(new RuleImpl(new ComplexTerm("what", new Variable("Z")), new RuleAnd(new ComplexTerm("hello", new Variable("Z")), new ComplexTerm("bye", new Variable("Z")))));

        Unify unifier = new Unifier();
        ProofSearch proofSearch = new ProofSearch(unifier,knowledgeBase);
        // ?- what(Z).
        QuestionResult questionResult = proofSearch.ask( new ComplexTerm("what", new Variable("Z") ) );

        Assert.assertTrue( questionResult.successful() );
        List<UnificationResult> unificationResults = questionResult.getResults();
        Assert.assertEquals(unificationResults.size(), 1);
        UnificationResult unificationResult = unificationResults.get(0);
        Assert.assertTrue(unificationResult.getScope().has(new Variable("Z")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("Z")), new Atom("gary") );
    }
}
