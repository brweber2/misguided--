package com.brweber2.term;

/**
 * @author brweber2
 *         Copyright: 2012
 */
public class RuleImpl implements Rule{
    Term head;
    RuleBody body;


    public RuleImpl(Term head, RuleBody body) {
        this.head = head;
        this.body = body;
    }

    public Term getHead() {
        return head;
    }

    public RuleBody getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!body.equals(rule.getBody())) return false;
        if (!head.equals(rule.getHead())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
