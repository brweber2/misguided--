package com.brweber2.kb;

import com.brweber2.search.ProofSearch;
import com.brweber2.term.Atom;
import com.brweber2.term.ComplexTerm;
import com.brweber2.term.Variable;
import com.brweber2.unify.QuestionResult;
import com.brweber2.unify.UnificationResult;
import com.brweber2.unify.Unifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author brweber2
 * Copyright: 2012
 */
public class ProofSearchTest {
    @Test
    public void simpleKnowledgeBase()
    {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.add(new ComplexTerm("hello", new Atom("dave")));
        knowledgeBase.add(new ComplexTerm("hello", new Atom("gary")));

        ProofSearch proofSearch = new ProofSearch(new Unifier(),knowledgeBase);
        QuestionResult questionResult = proofSearch.ask(new ComplexTerm("hello", new Variable("x")));
        Assert.assertTrue(questionResult.successful());
        List<UnificationResult> results = questionResult.getResults();
        Assert.assertEquals(results.size(),2);
        UnificationResult unificationResult = results.get(0);
        Assert.assertTrue(unificationResult.getScope().has(new Variable("x")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("x")), new Atom("dave") );

        UnificationResult next = results.get(1);
        Assert.assertTrue(next.unifies());
        Assert.assertTrue(next.getScope().has(new Variable("x")));
        Assert.assertEquals(next.getScope().get(new Variable("x")), new Atom("gary") );
    }
}
