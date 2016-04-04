package com.test.util;

import java.util.Arrays;

public class ArrayUtil {

	/** Remove 0 or null from byte array
	 * @param in
	 * @return
	 */
	public static byte[] removeNull(byte[] in) {
		if(in == null)
			return in;
		
		int i = in.length - 1;

		// Trimming. Remove null i.e.0 from array
		while (i >= 0 && in[i] == 0) {
			--i;
		}
		return Arrays.copyOf(in, i + 1);
	}
}
