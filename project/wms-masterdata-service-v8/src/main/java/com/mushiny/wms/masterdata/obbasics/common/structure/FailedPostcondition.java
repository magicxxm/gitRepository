package com.mushiny.wms.masterdata.obbasics.common.structure;

/**
 * This error is thrown by the Assert class in the event of a failed
 * postcondition. Errors are thrown rather than exceptions because
 * failed postconditions are assumed to be an indication of such
 * an egregious program failure that recovery is impossible.
 */
public class FailedPostcondition extends FailedAssertion {

    final static long serialVersionUID = 0L;

    /**
     * Constructs an error indicating failure to meet a postcondition.
     *
     * @param reason String describing postcondition.
     * @post Constructs a new failed postcondition
     */
    public FailedPostcondition(String reason) {
        super("\nA postcondition: " + reason);
    }
}
