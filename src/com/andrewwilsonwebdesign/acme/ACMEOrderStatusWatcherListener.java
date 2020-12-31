package com.andrewwilsonwebdesign.acme;

public interface ACMEOrderStatusWatcherListener {

    public void pending(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    public void ready(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    public void processing(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    public void valid(ACMEOrder order, ACMEOrderStatusWatcher watcher);

    public void invalid(ACMEOrder order, ACMEOrderStatusWatcher watcher);

}
