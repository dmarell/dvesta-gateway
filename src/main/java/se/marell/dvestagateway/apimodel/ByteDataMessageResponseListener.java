/*
 * Created by Daniel Marell 2016-03-20
 */
package se.marell.dvestagateway.apimodel;

public interface ByteDataMessageResponseListener {
    void responseReceived(int responseCode, String mediaType, byte[] data);
}
