package com.test.tags;

import com.test.util.ArrayUtil;
import com.test.util.ConfigLoader;
import com.test.util.StringPool;

/**
 * Transformer class for Tag2 with logic of replacing all matching upper-case
 * letters with lower-case
 * Class is singleton as it doesn't hold instance specific data
 * 
 * @author Irfan
 *
 */
public class Tag2Transformer implements TagDataTransformer {

	private static Tag2Transformer INSTANCE;

	private Tag2Transformer() {
		// Singleton support
	}

	public static Tag2Transformer getInstance() {
		// Double checked locking
		if (INSTANCE == null) {
			synchronized (Tag2Transformer.class) {
				if (INSTANCE == null) {
					INSTANCE = new Tag2Transformer();
				}
			}
		}
		return INSTANCE;
	}

	@Override
	public byte[] transform(byte[] input) {
		byte[] out = new String(input).toLowerCase().getBytes();
		return ArrayUtil.removeNull(out);
	}

	@Override
	public char getEndTag() {
		return ConfigLoader.props.getProperty(StringPool.TAG2_END).charAt(0);
	}

}
