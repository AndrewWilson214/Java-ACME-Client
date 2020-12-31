package com.andrewwilsonwebdesign.acme;

import java.util.Timer;
import java.util.TimerTask;

public class ACMEOrderStatusWatcher {

    private Timer timer;

    public ACMEOrderStatusWatcher(ACMEOrder order, ACMEOrderStatusWatcherListener listener){
        start(order, listener, 5000);
    }

    public ACMEOrderStatusWatcher(ACMEOrder order, ACMEOrderStatusWatcherListener listener, int pollingInterval){
        start(order, listener, pollingInterval);
    }

    private void start(ACMEOrder order, ACMEOrderStatusWatcherListener listener, int pollingInterval){
        TimerTask poller = new ACMEPollOrderEndpoint(order, listener, this);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(poller, 0, pollingInterval);
    }

    public void destroy(){
        timer.cancel();
    }

}

class ACMEPollOrderEndpoint extends TimerTask {

    private ACMEOrder order;
    private ACMEOrderStatusWatcherListener listener;
    private ACMEOrderStatusWatcher watcher;

    public ACMEPollOrderEndpoint(ACMEOrder order, ACMEOrderStatusWatcherListener listener, ACMEOrderStatusWatcher watcher){
        this.order = order;
        this.listener = listener;
        this.watcher = watcher;
    }

    @Override
    public void run() {
        try {
            ACMEOrder updatedOrder = order.fetch();
            if (!order.status.equals(updatedOrder.status)) {
                System.out.println("Will switch");
                switch (updatedOrder.status) {
                    case pending:
                        listener.pending(updatedOrder, watcher);
                        break;
                    case ready:
                        listener.ready(updatedOrder, watcher);
                        break;
                    case processing:
                        listener.processing(updatedOrder, watcher);
                        break;
                    case valid:
                        listener.valid(updatedOrder, watcher);
                        break;
                    case invalid:
                        listener.invalid(updatedOrder, watcher);
                        break;
                }
            }
            this.order = updatedOrder;
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
