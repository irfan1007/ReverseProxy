package com.test.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.test.data.DataHandler;
import com.test.util.ConfigLoader;
import com.test.util.StringPool;

/**
 * This class is TCP server listening for clients on specified host and port.
 * 
 * @author Irfan
 *
 */
public class TCPServer implements Runnable {

	private ServerSocketChannel serverChannel;
	private Selector selector;

	private static final String HOST = ConfigLoader.props.getProperty(StringPool.HOSTNAME);
	private static final String PORT = ConfigLoader.props.getProperty(StringPool.PORT);

	private static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());
	private static ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	public TCPServer() {
		initialize();

		// call back for clean up thread pool
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			THREAD_POOL.shutdown();
		}));
	}

	private void initialize() {
		LOGGER.info("Initializing TCP server ...");
		try {
			selector = Selector.open();

			this.serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);

			InetSocketAddress addr = new InetSocketAddress(HOST, new Integer(PORT));
			serverChannel.socket().bind(addr);

			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Server started ...");
		} catch (IOException e) {
			LOGGER.error("Error while starting TCP server ", e);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				this.selector.select();

				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error in TCP server thread : ", e);
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer readBuffer = ByteBuffer.allocate(1000);

		int numRead;
		synchronized (key) {
			try {
				numRead = socketChannel.read(readBuffer);
			} catch (IOException e) {
				key.cancel();
				socketChannel.close();
				return;
			}

			if (numRead == -1) {
				key.channel().close();
				key.cancel();
				return;
			}
		}

		String data = new String(readBuffer.array()).trim();
		System.out.println("Input data -> " + data);
		
		//process data in new thread
		if (readBuffer != null && readBuffer.array() != null)
			THREAD_POOL.submit(new DataHandler(readBuffer.array()));
	}
}
