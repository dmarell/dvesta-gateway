/*
 * Created by Daniel Marell 13/03/16.
 */
package se.marell.dvestagateway.apimodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBody {
    private String command;
}
