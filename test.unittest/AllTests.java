import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.test.data.DataHandler;
import com.test.util.ConfigLoader;
import com.test.util.StringPool;

public class AllTests {

	private static final String TAG1TEXT_IN = "ABC<YY>DEF<ZZ>GH";
	private static String TAG1TEXT_OUT;

	private static final String TAG2TEXT_IN = "ABC[MoN]DEF[TUe]GH";
	private static String TAG2TEXT_OUT = "ABC[mon]DEF[tue]GH";

	private static final String NESTED_TAG_IN = "ABC<00>DEF[MoN]GHI<11>JKL[TUe]MN";
	private static String NESTED_TAG_OUT;

	private byte[] tag1Data = TAG1TEXT_IN.getBytes();
	private byte[] tag2Data = TAG2TEXT_IN.getBytes();
	private byte[] nestedTagData = NESTED_TAG_IN.getBytes();

	@BeforeClass
	public static void setup() {
		new ConfigLoader().loadConfigFile();
		final String TAG1TEXT = ConfigLoader.props.getProperty(StringPool.TAG1TEXT);

		TAG1TEXT_OUT = "ABC<" + TAG1TEXT + ">DEF<" + TAG1TEXT + ">GH";
		NESTED_TAG_OUT = "ABC<" + TAG1TEXT + ">DEF[mon]GHI<" + TAG1TEXT + ">JKL[tue]MN";
	}

	@Test
	public void testTag1() {
		DataHandler dataHandler = new DataHandler(tag1Data);
		Future<String> future = Executors.newSingleThreadExecutor().submit(dataHandler);
		try {
			String actual = future.get();
			Assert.assertEquals("Test failed", TAG1TEXT_OUT, actual);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testTag2() {
		DataHandler dataHandler = new DataHandler(tag2Data);
		Future<String> future = Executors.newSingleThreadExecutor().submit(dataHandler);
		try {
			String actual = future.get();
			Assert.assertEquals("Test failed", TAG2TEXT_OUT, actual);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testNestedTag() {
		DataHandler dataHandler = new DataHandler(nestedTagData);
		Future<String> future = Executors.newSingleThreadExecutor().submit(dataHandler);
		try {
			String actual = future.get();
			Assert.assertEquals("Test failed", NESTED_TAG_OUT, actual);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
