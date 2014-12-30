//this class handles all incoming packets on the client side

package Packet;


import java.util.HashMap;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;

import HelperClasses.Globals;
import Player.Player;

public class ClientHandler extends SimpleChannelUpstreamHandler {

	//this client's UID
	String str_UID;
	//last message event used for error handling
	MessageEvent msge_lastMessage;

	//Initializer method, takes UID as input
	public ClientHandler(String str_UID)
	{
		//set this client's UID
		this.str_UID = str_UID;
	}

	@Override
	//called when a packet is received
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		//cast the message to a string
		String msg = (String) e.getMessage();
		//if the message was from the server
		if (msg.startsWith("Server: ")) {
			msge_lastMessage = e;
			//take the data and decode it into the remote player hash map
			Globals.plr_networkedPlayers = new HashMap<String, Player>(Packet.decodeServerPacket(msg.substring(8)));

			//send client data back to the server
			e.getChannel().write("Client: ".concat(str_UID.concat("-SPLIT-".concat(Packet.encodeClientPacket(Globals.plr_localPlayer)))), e.getRemoteAddress());
		}
		//if the server requests the connection close
		else if (msg.startsWith("Stop"))
		{
			//close the connection
			e.getChannel().close();
			System.out.println("Connection Closed");
		}
	}

	@Override //error catching method
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		//a timeout occured
		if (e.getCause() instanceof ReadTimeoutException) {
			//re-send client data back to the server
			msge_lastMessage.getChannel().write("Client: ".concat(str_UID.concat("-SPLIT-".concat(Packet.encodeClientPacket(Globals.plr_localPlayer)))), msge_lastMessage.getRemoteAddress());
		}
		else
		{
			e.getCause().printStackTrace();
			e.getChannel().close();
		}
	}

}
