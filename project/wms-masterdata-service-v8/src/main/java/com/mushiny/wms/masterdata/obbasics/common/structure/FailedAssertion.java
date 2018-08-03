package com.mushiny.wms.masterdata.obbasics.common.structure;

/**
 * This error is thrown by the Assert class in the event of any failed
 * assertion test. Errors are thrown rather than exceptions because
 * failed assertions are assumed to be an indication of such
 * an egregious program failure that recovery is impossible.
 *
 * @see Assert#fail
 */
public class FailedAssertion extends Error {

    final static long serialVersionUID = 0L;

    /**
     * Constructs an error indicating failure to meet condition.
     *
     * @param reason String describing failed condition.
     * @post Constructs a new failed assertion error
     */
    public FailedAssertion(String reason) {
        super("\nAssertion that failed: " + reason);
    }
}
