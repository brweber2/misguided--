package com.brweber2.unify;

import com.brweber2.term.Term;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public interface Unify {
    UnificationResult unify( Term term1, Term term2);
    UnificationResult unify( UnificationScope scope, Term term1, Term term2);
}
