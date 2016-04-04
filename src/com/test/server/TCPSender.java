package com.test.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.test.util.ConfigLoader;
import com.test.util.StringPool;

/**
 * This class forward the data to configured forward proxy server
 * 
 * @author Irfan
 *
 */
public class TCPSender {

	private static final String HOST = ConfigLoader.props.getProperty(StringPool.FWD_HOST);
	private static final String PORT = ConfigLoader.props.getProperty(StringPool.FWD_PORT);

	private static final Logger LOGGER = Logger.getLogger(TCPSender.class.getName());

	private SocketChannel channel;

	public TCPSender() {
		connect();
	}

	private void connect() {
		try {
			LOGGER.debug("Initializing reverse proxy server");
			channel = SocketChannel.open();

			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress(HOST, new Integer(PORT)));

			while (!channel.finishConnect()) {
				// waiting for connection
			}

		} catch (NumberFormatException e) {
			LOGGER.error("Error while parsing port " + PORT, e);
		} catch (IOException e) {
			LOGGER.error("IO exception while starting forward proxy server " + PORT, e);
		}
	}

	public void send(byte[] message) throws IOException {
		try {
			if (message.length > 0) {
				ByteBuffer buffer = ByteBuffer.wrap(message);
				System.out.println("Output data -> " + new String(message));
				channel.write(buffer);
			}
		} catch (Exception e) {
			LOGGER.error("Error while sending message : ", e);
		} finally {
			channel.close();
		}
	}
}
