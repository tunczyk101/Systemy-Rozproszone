package sr.grpc.server;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

//import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
//import "google.golang.org/grpc/reflection"

public class grpcServer 
{
	private static final Logger logger = Logger.getLogger(grpcServer.class.getName());

	private String address = "localhost";
	private int port = 50050;
	private Server server;

	private SocketAddress socket;





	private void start() throws IOException 
	{
		try { socket = new InetSocketAddress(InetAddress.getByName(address), port);	} catch(UnknownHostException e) {};

		//You will want to employ flow-control so that the queue doesn't blow up your memory. You can cast StreamObserver to CallStreamObserver to get flow-control API
		server = /*ServerBuilder*/NettyServerBuilder.forAddress(socket).executor(Executors.newFixedThreadPool(16))
				.addService(new CalculatorImpl())
				.addService(ProtoReflectionService.newInstance())
				//.addService(new CalculatorImpl())
				//.addService(new AdvancedCalculatorImpl())
				.addService(new StreamTesterImpl())
				.build()
				.start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				grpcServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		final grpcServer server = new grpcServer();
		server.start();
		server.blockUntilShutdown();
	}

}
