package com.autofrog.xbee.api.listeners;

import com.autofrog.xbee.api.messages.XbeeMessageBase;

/**
 *
 * @author chrisp
 */
public interface XbeeMessageListener {
	
		public void onXbeeMessage(Object sender, XbeeMessageBase msg);

}
