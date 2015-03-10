package com.autofrog.xbee.api.listeners;

import com.autofrog.xbee.api.messages.XbeeMessageBase;

/**
 *
 * @author chrisp
 */
public interface XbeeMessageListener<X extends XbeeMessageBase> {
	
		public void onXbeeMessage(Object sender, X msg);

}
