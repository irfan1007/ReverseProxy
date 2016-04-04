package com.test.tags;

import com.test.util.ConfigLoader;
import com.test.util.StringPool;

/** Transformer class for tag1. Replace chars between tag start and end with a pre-defined text
 * Class is singleton as it doesn't hold instance specific data
 * @author Irfan
 *
 */
public class Tag1Transformer implements TagDataTransformer {

	//Text to be used for replacing matching char between start and end of tag1
	private static final String TAG1TEXT = ConfigLoader.props.getProperty(StringPool.TAG1TEXT);
	
	//Simple thread-safe singleton
	private static Tag1Transformer INSTANCE = new Tag1Transformer();

	private Tag1Transformer() {
		// Private constructor
	}

	public static Tag1Transformer getInstance() {
		return INSTANCE;
	}

	@Override
	public byte[] transform(byte[] input) {
		return TAG1TEXT.getBytes();
	}

	@Override
	public char getEndTag() {
		return ConfigLoader.props.getProperty(StringPool.TAG1_END).charAt(0);
	}

}
