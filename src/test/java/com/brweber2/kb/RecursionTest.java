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
 *         Copyright: 2012
 */
public class RecursionTest {

    @Test
    public void recursiveRule()
    {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        // is_digesting(X,Y) :- just_ate(X,Y).
        knowledgeBase.add(new RuleImpl(new ComplexTerm("is_digesting", new Variable("M"), new Variable("N")), new ComplexTerm("just_ate", new Variable("M"), new Variable("N"))));
        // is_digesting(X,Y) :- just_ate(X,Z), is_digesting(Z,Y).
        knowledgeBase.add(new RuleImpl(new ComplexTerm("is_digesting", new Variable("X"), new Variable("Y")), new RuleAnd(new ComplexTerm("just_ate", new Variable("X"), new Variable("Z")), new ComplexTerm("is_digesting", new Variable("Z"), new Variable("Y")))));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("mosquito"), new ComplexTerm("blood", new Atom("john"))));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("frog"), new Atom("mosquito")));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("stork"), new Atom("frog")));

        Unify unifier = new Unifier();
        ProofSearch proofSearch = new ProofSearch(unifier,knowledgeBase);

        // ?- is_digesting(mosquito,stork).
        QuestionResult questionResult = proofSearch.ask( new ComplexTerm("is_digesting", new Atom("mosquito"), new Atom("stork")) );
        Assert.assertFalse(questionResult.successful());
        List<UnificationResult> results = questionResult.getResults();
        Assert.assertEquals(results.size(),0);

        QuestionResult questionResult1 = proofSearch.ask( new ComplexTerm("is_digesting", new Atom("stork"), new Atom("mosquito")) );

        Assert.assertTrue( questionResult1.successful() );
        List<UnificationResult> results1 = questionResult1.getResults();
        Assert.assertEquals(results1.size(), 1);
        UnificationResult unificationResult = results1.get(0);
        Assert.assertTrue(unificationResult.getScope().has(new Variable("X")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("X")), new Atom("stork") );
        Assert.assertTrue(unificationResult.getScope().has(new Variable("Y")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("Y")), new Atom("mosquito") );

    }

    @Test
    public void sameVariables()
    {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        // is_digesting(X,Y) :- just_ate(X,Y).
        knowledgeBase.add(new RuleImpl(new ComplexTerm("is_digesting", new Variable("X"), new Variable("Y")), new ComplexTerm("just_ate", new Variable("X"), new Variable("Y"))));
        // is_digesting(X,Y) :- just_ate(X,Z), is_digesting(Z,Y).
        knowledgeBase.add(new RuleImpl(new ComplexTerm("is_digesting", new Variable("X"), new Variable("Y")), new RuleAnd(new ComplexTerm("just_ate", new Variable("X"), new Variable("Z")), new ComplexTerm("is_digesting", new Variable("Z"), new Variable("Y")))));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("mosquito"), new ComplexTerm("blood", new Atom("john"))));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("frog"), new Atom("mosquito")));
        knowledgeBase.add(new ComplexTerm("just_ate", new Atom("stork"), new Atom("frog")));

        Unify unifier = new Unifier();
        ProofSearch proofSearch = new ProofSearch(unifier,knowledgeBase);

        QuestionResult questionResult = proofSearch.ask( new ComplexTerm("is_digesting", new Atom("mosquito"), new Atom("stork")) );

        Assert.assertTrue( questionResult.successful() );
        List<UnificationResult> results = questionResult.getResults();
        Assert.assertEquals(results.size(), 0);

        QuestionResult questionResult1 = proofSearch.ask( new ComplexTerm("is_digesting", new Atom("stork"), new Atom("mosquito")) );

        Assert.assertTrue( questionResult1.successful() );
        List<UnificationResult> results1 = questionResult1.getResults();
        UnificationResult unificationResult = results1.get(0);
        Assert.assertTrue(unificationResult.getScope().has(new Variable("X")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("X")), new Atom("stork") );
        Assert.assertTrue(unificationResult.getScope().has(new Variable("Y")));
        Assert.assertEquals(unificationResult.getScope().get(new Variable("Y")), new Atom("mosquito") );

    }
}
