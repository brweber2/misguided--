package com.brweber2.unify;

import com.brweber2.term.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public class UnificationResult {
    

    private boolean unifies;
    private UnificationScope scope;
    private Term term1;
    private Term term2;

    public UnificationResult(boolean unifies, UnificationScope scope, Term term1, Term term2) {
        this.unifies = unifies;
        this.scope = scope;
        this.term1 = term1;
        this.term2 = term2;
    }

    public boolean unifies() {
        return unifies;
    }

    public UnificationScope getScope() {
        return scope;
    }
}
