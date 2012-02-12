package com.brweber2.search;

import com.brweber2.kb.KnowledgeBase;
import com.brweber2.kb.KnowledgeBaseEntry;
import com.brweber2.term.Rule;
import com.brweber2.term.RuleAnd;
import com.brweber2.term.RuleBody;
import com.brweber2.term.RuleOr;
import com.brweber2.term.Term;
import com.brweber2.unify.QuestionResult;
import com.brweber2.unify.UnificationResult;
import com.brweber2.unify.UnificationScope;
import com.brweber2.unify.Unify;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public class ProofSearch {
    private static Logger log = Logger.getLogger(ProofSearch.class.getName());

    private Unify unifier;
    private KnowledgeBase knowledgeBase;

    public ProofSearch(Unify unifier, KnowledgeBase knowledgeBase) {
        this.unifier = unifier;
        this.knowledgeBase = knowledgeBase;
    }

    public Unify getUnifier() {
        return unifier;
    }
    
    private QuestionResult satisfies( RuleBody ruleToSatisfy, UnificationScope scope )
    {
        QuestionResult result = new QuestionResult();
        satisfies( result, ruleToSatisfy, scope );
        return result;
    }

    private void satisfies( QuestionResult result, RuleBody ruleToSatisfy, UnificationScope scope )
    {
        if ( ruleToSatisfy instanceof Term )
        {
            ask( result, scope, (Term) ruleToSatisfy );
        }
        else if ( ruleToSatisfy instanceof RuleAnd )
        {
            RuleAnd ruleAnd = (RuleAnd) ruleToSatisfy;
            satisfies(result, ruleAnd.getLeft(), scope);
            if ( result.successful() )
            {
                satisfies(result, ruleAnd.getRight(), scope);
            }
            else
            {
                return;
            }
        }
        else if ( ruleToSatisfy instanceof RuleOr )
        {
            RuleOr ruleOr = (RuleOr) ruleToSatisfy;
            // we need a temporary result in case we need to throw this out...
            QuestionResult leftResult = new QuestionResult();
            UnificationScope leftScope = new UnificationScope(scope);
            satisfies(leftResult, ruleOr.getLeft(), leftScope);
            if ( leftResult.successful() )
            {
                for (UnificationResult unificationResult : leftResult.getResults()) {
                    result.addIfUnifies(unificationResult);
                }
                return;
            }
            else
            {
                satisfies(result, ruleOr.getRight(), scope );
            }
        }
        throw new RuntimeException("Unknown rule to satisfy type " + ruleToSatisfy);
    }

    public QuestionResult ask( Term question )
    {
        QuestionResult result = new QuestionResult();
        ask( result, new UnificationScope(), question );
        return result;
    }

    public void ask( QuestionResult result, UnificationScope scope, Term question )
    {
        List<KnowledgeBaseEntry> knowledgeBaseEntries = knowledgeBase.getEntriesByFunctor(question.getFunctor());
        for (KnowledgeBaseEntry knowledgeBaseEntry : knowledgeBaseEntries) {
            if ( knowledgeBaseEntry instanceof Term )
            {
                result.addIfUnifies(unifier.unify(new UnificationScope(scope), question, (Term) knowledgeBaseEntry));
            }
            else if ( knowledgeBaseEntry instanceof Rule )
            {
                // if the head unifies
                Rule rule = (Rule) knowledgeBaseEntry;
                UnificationScope headScope = new UnificationScope(scope);
                UnificationResult headResult = unifier.unify(headScope, question, rule.getHead());
                if ( !headResult.unifies() )
                {
                    continue;
                }
                // check if the body unifies
                QuestionResult bodyResults = satisfies( rule.getBody(), headScope );
                if ( bodyResults.successful() )
                {
                    for (UnificationResult bodyResult : bodyResults.getResults()) {
                        result.addIfUnifies( bodyResult );
                    }
                }
            }
        }
    }
}
