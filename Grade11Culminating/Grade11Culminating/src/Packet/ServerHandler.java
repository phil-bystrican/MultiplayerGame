//handles all incoming packets on the server side

package Packet;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;


public class ServerHandler extends SimpleChannelUpstreamHandler {

	//hashmap of connected players
	public Map<String, DataObject> players = new HashMap<String, DataObject>();

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		//cast the received message to a string
		String msg = (String) e.getMessage();

		//check if the message came from a client
		if (msg.startsWith("Client: ")) {
			//split the string into data and a UID
			String[] str_data = msg.substring(8).split("-SPLIT-");

			//decode the data and place it in the hashmap
			players.get(str_data[0])
			.set(Packet.decodeClientPacket(str_data[1]));

			// send the other players location data to the client to be drawn
			e.getChannel().write(
					"Server: ".concat(Packet.encodeServerPacket(players)),
					e.getRemoteAddress());
		}
		//if a player wants to connect
		else if (msg.startsWith("Start: ")) {
			// split the string into a UID ([0]) and player data ([1])
			String[] str_data = msg.substring(7).split("-SPLIT-");

			if (players.get(str_data[0]) == null) {
				// Put the player in the player list hash map
				players.put(str_data[0], new DataObject(str_data[0], "", 0, 0,
						new Point[5]));
			} else {
				// the player is already in the array update the data
				players.get(str_data[0]).set(
						Packet.decodeClientPacket(str_data[1]));
			}

			// provide the server user with a connected notification
			System.out.println("Client " + str_data[0] + " connected");
			// send the other players location data to the user to be drawn
			e.getChannel().write(
					"Server: ".concat(Packet.encodeServerPacket(players)),
					e.getRemoteAddress());

		} else if (msg.startsWith("Close: ")) {
			// remove the player from the player list hash map
			players.remove(msg.substring(7));
			// provide the server user with a disconnect notification
			System.out.println("Client " + msg.substring(7) + " disconnected");
			// tell the client to stop
			e.getChannel().write("Stop");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}
}
