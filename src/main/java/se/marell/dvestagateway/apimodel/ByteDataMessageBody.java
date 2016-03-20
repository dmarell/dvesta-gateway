/*
 * Created by Daniel Marell 20/03/16.
 */
package se.marell.dvestagateway.apimodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ByteDataMessageBody {
    private int responseCode;
    private String mediaType;
    private String base64EncodedData;
}
