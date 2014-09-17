package com.dc.tes.channel.adapter.udp;

import com.dc.tes.channel.adapter.tcp.ListenerConfigObject;
import com.dc.tes.channel.localchannel.AbstractLocalListenerChannel;
import com.dc.tes.component.tag.ComponentClass;
import com.dc.tes.component.tag.ComponentType;

@ComponentClass(type = ComponentType.Channel)
public class LocalListener extends AbstractLocalListenerChannel<ListenerConfigObject> {

}
