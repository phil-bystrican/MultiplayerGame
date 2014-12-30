//Client side networking initialization

package Packet;


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.HashedWheelTimer;

import HelperClasses.Constants;
import HelperClasses.Globals;

public class Client {
	//client's UID
	String str_UID = "";

	public void run(final String str_UID) {
		
		//set the uid
		this.str_UID = str_UID;
		
		//create a datagram channel factory for UDP packet creation
		DatagramChannelFactory dcf_factory =
				new NioDatagramChannelFactory(Executors.newCachedThreadPool());

		//create a ConnectionlessBootstrap for UDP packet sending and receiving operations
		ConnectionlessBootstrap cnlssBstp_bootstrap = new ConnectionlessBootstrap(dcf_factory);

		// Configure the pipeline factory.
		cnlssBstp_bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						//set the packet timeout to 25 milliseconds between received packets
						new ReadTimeoutHandler(new HashedWheelTimer(), 25, TimeUnit.MILLISECONDS),
						new StringEncoder(CharsetUtil.ISO_8859_1),
						new StringDecoder(CharsetUtil.ISO_8859_1),
						//set the packet handler
						new ClientHandler(str_UID));
			}
		});

		// Enable broadcast
		cnlssBstp_bootstrap.setOption("broadcast", "true");

		// Allow packets up to 1024 bytes
		cnlssBstp_bootstrap.setOption(
				"receiveBufferSizePredictorFactory",
				new FixedReceiveBufferSizePredictorFactory(1024));

		//create a channel (similar to a connection) for writing data to
		DatagramChannel dc_channel = (DatagramChannel) cnlssBstp_bootstrap.bind(new InetSocketAddress(Constants.CLIENT_PORT));

		// send the start request to the server
		dc_channel.write("Start: ".concat(str_UID.concat("-SPLIT-".concat(Packet.encodeClientPacket(Globals.plr_localPlayer)))), new InetSocketAddress(Globals.serverIP, Constants.SERVER_PORT));
	}

	//error handler
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		//if the error was a timeout
		if (e.getCause() instanceof ReadTimeoutException) {
			//re-send client data back to the server
			ctx.getChannel().write("Start: ".concat(str_UID), new InetSocketAddress(Globals.serverIP, Constants.SERVER_PORT));
		}
		else
		{
			e.getCause().printStackTrace();
			e.getChannel().close();
		}
	}
}
