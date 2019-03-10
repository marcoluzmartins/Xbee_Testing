
package xbeetesting;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;

public class XbeeTesting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XBeeException {
        // TODO code application logic here

        XBee xbee = new XBee();

        try {
// replace with the com port or your receiving XBee
// this is the com port of my end device on my mac
            xbee.open("COM3", 9600);

            while (true) {

                try {
// we wait here until a packet is received.
                    XBeeResponse response = xbee.getResponse();
                    ZNetRxResponse rx = (ZNetRxResponse) response;
                    System.out.println("received response " + response.toString());
                    String sample = "";
                    for (int i = 0; i < rx.getLength().getLength(); i++) {
                        sample += rx.getData();
                    }
                    System.out.println(sample);

                    if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
// we received a packet from ZNetSenderTest.java

                        System.out.println("Received RX packet, option is " + rx.getOption() + ", sender 64 address is " + ByteUtils.toBase16(rx.getRemoteAddress64().getAddress()) + ", remote 16-bit address is " + ByteUtils.toBase16(rx.getRemoteAddress16().getAddress()) + ", data is " + ByteUtils.toBase16(rx.getData()));

// optionally we may want to get the signal strength (RSSI) of the last hop.
// keep in mind if you have routers in your network, this will be the signal of the last hop.
                        AtCommand at = new AtCommand("DB");
                        xbee.sendAsynchronous(at);
                        XBeeResponse atResponse = xbee.getResponse();

                        if (atResponse.getApiId() == ApiId.AT_RESPONSE) {
// remember rssi is a negative db value
                            System.out.println("RSSI of last response is " + -((AtCommandResponse) atResponse).getValue()[0]);
                        } else {
// we didn't get an AT response
                            System.out.println("expected RSSI, but received " + atResponse.toString());
                        }
                    } else {
                        System.out.println("received unexpected packet " + response.toString());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } finally {
            if (xbee != null && xbee.isConnected()) {
                xbee.close();
            }
        }

    }
}
