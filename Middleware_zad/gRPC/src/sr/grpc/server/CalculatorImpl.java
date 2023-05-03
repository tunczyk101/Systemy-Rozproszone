package sr.grpc.server;

import sr.grpc.gen.ArithmeticOpResult;
import sr.grpc.gen.CalculatorGrpc.CalculatorImplBase;
import sr.grpc.gen.ExistingType;
import sr.grpc.gen.NoArithmeticResults;

import java.util.Random;

public class CalculatorImpl extends CalculatorImplBase 
{
	@Override
	public void add(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<ArithmeticOpResult> responseObserver)
	{
		System.out.println("addRequest (" + request.getArg1() + ", " + request.getArg2() +")");
		int val = request.getArg1() + request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		if(request.getArg1() > 100 && request.getArg2() > 100) try { Thread.sleep(5000); } catch(InterruptedException ex) { }
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void subtract(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<ArithmeticOpResult> responseObserver)
	{
		System.out.println("subtractRequest (" + request.getArg1() + ", " + request.getArg2() +")");
		int val = request.getArg1() - request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	private ExistingType getOtherType(ExistingType type){
		return switch (type) {
			case MAGICIAN -> ExistingType.ADD;
			case ADD -> ExistingType.SUB;
			case SUB -> ExistingType.AV;
			case AV -> ExistingType.MAGICIAN;
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}

	@Override
	public void noArithmeticOp(sr.grpc.gen.NoArithmeticArguments request,
							   io.grpc.stub.StreamObserver<sr.grpc.gen.NoArithmeticResults> responseObserver)
	{
		System.out.println("Stop with basic arithmetic Request (" + request.getInfo() + ")");

		int val;
		switch (request.getThisType()) {
			case MAGICIAN -> {
				Random random = new Random();
				if (random.nextInt(2) == 1)
					val = request.getNumber1();
				else val = request.getNumber2();
			}
			case ADD -> val = request.getNumber1() + request.getNumber2();
			case SUB -> val = request.getNumber1() - request.getNumber2();
			case AV -> val = (request.getNumber1() + request.getNumber2()) / 2;
			default -> throw new IllegalStateException("Unexpected value: " + request.getThisType());
		}
		NoArithmeticResults result = NoArithmeticResults.newBuilder().setInfo(request.getInfo()).setOtherType(getOtherType(request.getThisType())).setResult(val).build();
		System.out.println(result.getResult());
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}


}
