package com.test.data;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.test.server.TCPSender;
import com.test.tags.TagDataTransformer;
import com.test.tags.TagsManager;
import com.test.util.ArrayUtil;

/**
 * This class holds core logic for processing tags in generic manner. Logic does
 * not depends on tag-value or number of tags Class is runnable for supporting
 * async execution from caller.
 * 
 * @author Irfan
 *
 */
public class DataHandler implements Callable<String> {

	private byte[] inputArray;
	private TCPSender tcpSender = new TCPSender();

	private static final Logger LOGGER = Logger.getLogger(DataHandler.class.getName());
	private static Map<Character, TagDataTransformer> TAG_TRANSFORMERS = TagsManager.getTagCollections();

	public DataHandler(byte[] dataArray) {
		this.inputArray = dataArray;
	}

	@Override
	public String call() {
		try {
			int index = 0;
			byte[] outputArray = new byte[1000]; // Max array size is 1000b

			for (int i = 0; i < inputArray.length;) {
				if (inputArray[i] == 0) // Ignore zeros i.e. null input
					break;

				// Copy to output-array, byte by byte
				outputArray[index++] = inputArray[i];

				// Check for Tag matching, TAG_TRANSFORMERS keyset has all
				// tag-start characters
				if (TAG_TRANSFORMERS.keySet().contains((char) inputArray[i])) {

					TagDataTransformer dataTransformer = TAG_TRANSFORMERS.get((char) inputArray[i]);

					// Max transformed data size limit is set to 200
					// This is a sub-array which holds all bytes between tags
					// start and end
					byte[] arrayToBeTransformed = new byte[200];

					int index2 = 0;
					
					//skip start tag conversion
					i++;
					
					// Continue fetching all bytes till end of tag or till limit
					// of 200
					while (index2 < 200 && !(dataTransformer.getEndTag() == (char) inputArray[i])) {
						arrayToBeTransformed[index2++] = inputArray[i++];
						continue;
					}
					
					// While loop is over, transform the sub-array using
					// TagDataTransformer interface
					for (byte xformBtye : dataTransformer.transform(arrayToBeTransformed)) {

						// Append transformed bytes in places
						outputArray[index++] = xformBtye;
					}
				}

				// tags did not match, continue without transforming
				else
					i++;

			}
			// Send to forward proxy server
			tcpSender.send(ArrayUtil.removeNull(outputArray));
			return new String(outputArray).trim();

		} catch (Exception e) {
			LOGGER.error("Error while data processing. Data -> " + new String(inputArray), e);
		}
		return null;
	}
}
