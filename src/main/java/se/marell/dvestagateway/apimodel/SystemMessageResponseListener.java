/*
 * Created by Daniel Marell 05/03/16.
 */
package se.marell.dvestagateway.apimodel;

public interface SystemMessageResponseListener {
    void responseReceived(String messageBody);
}
