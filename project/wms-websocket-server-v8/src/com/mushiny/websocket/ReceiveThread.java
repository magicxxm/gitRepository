package com.mushiny.websocket;

import java.util.Set;

/**
 * Created by Tank.li on 2017/6/16.
 */

/**
 * @deprecated only for test
 */
public class ReceiveThread extends Thread {
    private Set sessions;

    public ReceiveThread(Set sessions) {
        this.sessions = sessions;
    }

    @Override
    public void run() {
        Receiver.receive(sessions);
    }
}
