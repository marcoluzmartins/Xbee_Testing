package xbeetesting;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.models.XBee64BitAddress;


public class NewTesting {
    XBeeDevice myXBeeDevice = new XBeeDevice("COM1", 9600);
    myXBeeDevice.open();
    
}
