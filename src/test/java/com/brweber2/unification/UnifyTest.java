package com.brweber2.unification;

import com.brweber2.term.Atom;
import com.brweber2.term.ComplexTerm;
import com.brweber2.term.Numeric;
import com.brweber2.term.Variable;
import com.brweber2.unify.UnificationResult;
import com.brweber2.unify.Unifier;
import com.brweber2.unify.Unify;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author brweber2
 * Copyright: 2012
 */
public class UnifyTest {
    @Test
    public void unifyAtoms()
    {
        Atom atom1 = new Atom("foo");
        Atom atom2 = new Atom("foo");
        
        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(atom1,atom2);
        Assert.assertTrue(result.unifies());
        
        Atom atom3 = new Atom("bar");
        UnificationResult result2 = unifier.unify(atom1,atom3);
        Assert.assertFalse(result2.unifies());
    }

    @Test
    public void unifyNumerics()
    {
        Numeric numeric1 = new Numeric("1.43");
        Numeric numeric2 = new Numeric("1.43");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(numeric1,numeric2);
        Assert.assertTrue(result.unifies());

        Numeric numeric3 = new Numeric("5");
        UnificationResult result2 = unifier.unify(numeric1,numeric3);
        Assert.assertFalse(result2.unifies());
    }
    
    @Test
    public void unifyVariableWithAtom()
    {
        Variable variable1 = new Variable("foo");
        Atom atom1 = new Atom("quux");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(variable1,atom1);
        Assert.assertTrue( result.unifies() );
        Assert.assertTrue( result.getScope().has(variable1) );
        Assert.assertTrue( result.getScope().get(variable1).equals(atom1));
    }

    @Test
    public void unifyAtomWithVariable()
    {
        Variable variable1 = new Variable("foo");
        Atom atom1 = new Atom("quux");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(atom1,variable1);
        Assert.assertTrue( result.unifies() );
        Assert.assertTrue( result.getScope().has(variable1) );
        Assert.assertTrue( result.getScope().get(variable1).equals(atom1));
    }

    @Test
    public void unifyVariableWithNumeric()
    {
        Variable variable1 = new Variable("foo");
        Numeric numeric1 = new Numeric("2");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(variable1,numeric1);
        Assert.assertTrue( result.unifies() );
        Assert.assertTrue( result.getScope().has(variable1) );
        Assert.assertTrue( result.getScope().get(variable1).equals(numeric1));
    }

    @Test
    public void unifyNumericWithVariable()
    {
        Variable variable1 = new Variable("foo");
        Numeric numeric1 = new Numeric("46531");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(numeric1,variable1);
        Assert.assertTrue( result.unifies() );
        Assert.assertTrue( result.getScope().has(variable1) );
        Assert.assertTrue( result.getScope().get(variable1).equals(numeric1));
    }
    
    @Test
    public void unifyAtomWithNumeric()
    {
        Atom atom1 = new Atom("foo");
        Numeric numeric1 = new Numeric("123.45");

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(numeric1,atom1);
        Assert.assertFalse(result.unifies());
    }
    
    @Test
    public void unifyComplexTerms()
    {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Atom atom1 = new Atom("baz");
        Numeric numeric1 = new Numeric("10");
        
        ComplexTerm complexTerm1 = new ComplexTerm("foo", atom1, y);
        ComplexTerm complexTerm2 = new ComplexTerm("foo", x, numeric1 );

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(complexTerm1,complexTerm2);
        Assert.assertTrue( result.unifies() );
        Assert.assertTrue( result.getScope().has(x));
        Assert.assertTrue( result.getScope().get(x).equals(atom1));
        Assert.assertTrue( result.getScope().has(y));
        Assert.assertTrue( result.getScope().get(y).equals(numeric1));
    }

    @Test
    public void unifyComplexTermsDifferentFunctor()
    {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Atom atom1 = new Atom("baz");
        Numeric numeric1 = new Numeric("10");

        ComplexTerm complexTerm1 = new ComplexTerm("foo", atom1, y);
        ComplexTerm complexTerm2 = new ComplexTerm("bar", x, numeric1 );

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(complexTerm1,complexTerm2);
        Assert.assertFalse(result.unifies());
    }

    @Test
    public void unifyComplexTermsDifferentArity()
    {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Atom atom1 = new Atom("baz");
        Numeric numeric1 = new Numeric("10");

        ComplexTerm complexTerm1 = new ComplexTerm("foo", atom1, y);
        ComplexTerm complexTerm2 = new ComplexTerm("foo", numeric1 );

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(complexTerm1,complexTerm2);
        Assert.assertFalse(result.unifies());
    }

    @Test
    public void unifyComplexTermsDifferentValues()
    {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Atom atom1 = new Atom("baz");
        Numeric numeric1 = new Numeric("10");

        ComplexTerm complexTerm1 = new ComplexTerm("foo", atom1, y);
        ComplexTerm complexTerm2 = new ComplexTerm("foo", y, numeric1 );

        Unify unifier = new Unifier();
        UnificationResult result = unifier.unify(complexTerm1,complexTerm2);
        Assert.assertFalse(result.unifies());
    }
}
