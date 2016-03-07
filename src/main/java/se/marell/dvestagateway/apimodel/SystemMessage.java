/*
 * Created by Daniel Marell 03/03/16.
 */
package se.marell.dvestagateway.apimodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage {
    private String messageId;
    private String messageBody;
}
