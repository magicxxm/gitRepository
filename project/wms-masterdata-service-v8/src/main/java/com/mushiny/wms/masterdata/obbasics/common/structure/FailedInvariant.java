package com.mushiny.wms.masterdata.obbasics.common.structure;

/**
 * This error is thrown by the Assert class in the event of a failed
 * invariant test. Errors are thrown rather than exceptions because
 * failed invariants are assumed to be an indication of such
 * an egregious program failure that recovery is impossible.
 *
 * @see Assert#fail
 */
public class FailedInvariant extends FailedAssertion {

    final static long serialVersionUID = 0L;

    /**
     * Constructs an error indicating failure to meet an invariant
     *
     * @param reason String describing failed condition.
     * @post Constructs a new failed invariant error
     */
    public FailedInvariant(String reason) {
        super("\nInvariant that failed: " + reason);
    }
}
