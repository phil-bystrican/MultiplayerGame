//server initialization class

package Packet;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

import HelperClasses.Constants;

public class Server {

	public void run() {
		//create a datagram channel factory for UDP packet creation
		DatagramChannelFactory dcf_factory = new NioDatagramChannelFactory();
		//create a ConnectionlessBootstrap for UDP packet sending and receiving operations
		ConnectionlessBootstrap cnlssBstp_bootstrap = new ConnectionlessBootstrap(dcf_factory);

		// Configure the pipeline factory.
		cnlssBstp_bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new StringEncoder(CharsetUtil.ISO_8859_1),
						new StringDecoder(CharsetUtil.ISO_8859_1),
						//set the packet handler
						new ServerHandler());
			}
		});

		// disable broadcast
		cnlssBstp_bootstrap.setOption("broadcast", "false");

		// Allow packets up to 1024 bytes
		cnlssBstp_bootstrap.setOption(
				"receiveBufferSizePredictorFactory",
				new FixedReceiveBufferSizePredictorFactory(1024));

		// Bind to the port and start the service.
		cnlssBstp_bootstrap.bind(new InetSocketAddress(Constants.SERVER_PORT));
	}
}
