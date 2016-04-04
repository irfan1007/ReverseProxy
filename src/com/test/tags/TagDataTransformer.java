package com.test.tags;

/** Interface for tag transformer
 * @author Irfan
 *
 */
public interface TagDataTransformer {

	public byte[] transform(byte[] input) throws Exception;
	
	/** Callback for checking end tag char value
	 * @return char for end-tag
	 */
	public char getEndTag();

}
