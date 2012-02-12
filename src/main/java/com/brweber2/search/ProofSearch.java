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

    public QuestionResult ask( Term question )
    {
        return ask( new QuestionResult(), new UnificationScope(), question );
    }
    
    private QuestionResult satisfies( RuleBody ruleToSatisfy, UnificationScope scope )
    {
        return satisfies( new QuestionResult(), ruleToSatisfy, scope );
    }

    private QuestionResult satisfies( QuestionResult result, RuleBody ruleToSatisfy, UnificationScope scope )
    {
        if ( ruleToSatisfy instanceof Term )
        {
            return ask( result, scope, (Term) ruleToSatisfy );
        }
        else if ( ruleToSatisfy instanceof RuleAnd )
        {
            RuleAnd ruleAnd = (RuleAnd) ruleToSatisfy;
            QuestionResult leftResult = satisfies(result, ruleAnd.getLeft(), scope);
            if ( leftResult.successful() )
            {
                return satisfies(result, ruleAnd.getRight(), scope);
            }
            else
            {
                return leftResult;
            }
        }
        else if ( ruleToSatisfy instanceof RuleOr )
        {
            RuleOr ruleOr = (RuleOr) ruleToSatisfy;
            QuestionResult leftResult = satisfies(new QuestionResult(), ruleOr.getLeft(), new UnificationScope(scope));
            if ( leftResult.successful() )
            {
                return leftResult;
            }
            else
            {
                return satisfies(result, ruleOr.getRight(), scope );
            }
        }
        throw new RuntimeException("Unknown rule to satisfy type " + ruleToSatisfy);
    }

    public QuestionResult ask( QuestionResult result, UnificationScope scope, Term question )
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
        return result;
    }
}
