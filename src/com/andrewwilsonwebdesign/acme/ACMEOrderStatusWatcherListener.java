package com.andrewwilsonwebdesign.acme;

public interface ACMEOrderStatusWatcherListener {

    void pending(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    void ready(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    void processing(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    void valid(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    void invalid(ACMEOrder order, ACMEOrderStatusWatcher watcher);

}
