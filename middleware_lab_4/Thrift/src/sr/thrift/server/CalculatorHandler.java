package sr.thrift.server;

import org.apache.thrift.TException;
import sr.rpc.thrift.Calculator;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

public class CalculatorHandler implements Calculator.Iface {

	int id;

	public CalculatorHandler(int id) {
		this.id = id;
	}

	@Override
	public int add(int n1, int n2) {
		System.out.println("CalcHandler#" + id + " add(" + n1 + "," + n2 + ")");
		if(n1 > 1000 || n2 > 1000) { 
			try { Thread.sleep(6000); } catch(java.lang.InterruptedException ex) { }
			System.out.println("DONE");
		}
		return n1 + n2;
	}

	@Override
	public int subtract(int num1, int num2) throws TException {
		System.out.println("CalcHandler#" + id + " subtract(" + num1 + "," + num2 + ")");
		if(num1 > 1000 || num2 > 1000) {
			try { Thread.sleep(6000); } catch(java.lang.InterruptedException ex) { }
			System.out.println("DONE");
		}
		return num1 - num2;
	}

	@Override
	public double avg(List<Integer> array) throws TException {
		if(array.size() < 1){
			throw new IllegalArgumentException("Array size must be greater than 0");
		}

		double result = array.stream().mapToDouble(a -> a).average().getAsDouble();
		System.out.println("CalcHandler#" + id + "AVG: array: " + result);

		return result;
	}

}

