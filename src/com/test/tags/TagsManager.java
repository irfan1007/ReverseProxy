package com.test.tags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.test.util.ConfigLoader;
import com.test.util.StringPool;

/** This class holds all the tag information. Adding-deleting tag is done in this class
 * @author Irfan
 *
 */
public class TagsManager {

	// This collection holds tag and respective tag-transformer
	private static final Map<Character, TagDataTransformer> TagCollections = new HashMap<>(2);

	static {
		TagCollections.put(ConfigLoader.props.getProperty(StringPool.TAG1).charAt(0), Tag1Transformer.getInstance());
		TagCollections.put(ConfigLoader.props.getProperty(StringPool.TAG2).charAt(0), Tag2Transformer.getInstance());
	}

	public static Map<Character, TagDataTransformer> getTagCollections() {
		return Collections.unmodifiableMap((TagCollections));
	}
}
