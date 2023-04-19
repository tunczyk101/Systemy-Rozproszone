package sr.ice.server;

import Demo.A;
import Demo.Calc;
import com.zeroc.Ice.Current;

import java.util.Arrays;

public class CalcI implements Calc {
	private static final long serialVersionUID = -2448962912780867770L;
	long counter = 0;

	@Override
	public long add(int a, int b, Current __current) {
		System.out.println("ID: " + __current.id);
		System.out.println("ADD: a = " + a + ", b = " + b + ", result = " + (a + b));

		if (a > 1000 || b > 1000) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		if (__current.ctx.values().size() > 0) {
			System.out.println("There are some properties in the context");
		}

		return a + b;
	}

	@Override
	public long subtract(int a, int b, Current __current) {
		System.out.println("ID: " + __current.id);
		System.out.println("SUBTRACT: a = " + a + ", b = " + b + ", result = " + (a - b));

		if (a > 1000 || b > 1000) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		if (__current.ctx.values().size() > 0) {
			System.out.println("There are some properties in the context");
		}

		return a - b;
	}


	@Override
	public /*synchronized*/ void op(A a1, short b1, Current __current) {
		System.out.println("ID: " + __current.id);
		System.out.println("OP" + (++counter));
		System.out.println(a1.a);
//		System.out.println(a1.ice_readMembers(););
		try {
			Thread.sleep(500);
		} catch (java.lang.InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public float avg(long[] array, Current __current) {
		if(array.length < 1){
			throw new IllegalArgumentException("Array size must be greater than 0");
		}

		float result = (float) Arrays.stream(array).average().orElse(Float.NaN);
		System.out.println("ID: " + __current.id);
		System.out.println("AVG: array: " + result);


		if (__current.ctx.values().size() > 0) {
			System.out.println("There are some properties in the context");
		}

		return result;
	}
}