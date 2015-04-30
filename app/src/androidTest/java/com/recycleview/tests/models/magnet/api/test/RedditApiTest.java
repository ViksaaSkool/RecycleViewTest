/**
 * File generated by Magnet rest2mobile 1.1 - Apr 30, 2015 12:51:10 AM
 * @see {@link http://developer.magnet.com}
 */

package com.recycleview.tests.models.magnet.api.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Suppress;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.exception.SchemaException;
import com.recycleview.tests.models.magnet.api.RedditApi;
import com.recycleview.tests.models.magnet.api.RedditApiFactory;
import com.recycleview.tests.models.magnet.beans.SubRedditResult;

import java.util.concurrent.ExecutionException;

/**
* This is generated stub to test {@link RedditApi}
* <p>
* All test cases are suppressed by defaullt. To run the test, you need to fix all the FIXMEs first :
* <ul>
* <li>Set proper value for the parameters
* <li>Remove out the @Suppress annotation
* <li>(optional)Add more asserts for the result
* <ul><p>
*/

public class RedditApiTest extends InstrumentationTestCase {
  private RedditApi redditApi;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // Instantiate a controller
    MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getInstrumentation().getTargetContext());
    RedditApiFactory controllerFactory = new RedditApiFactory(magnetClient);
    redditApi = controllerFactory.obtainInstance();

    assertNotNull(redditApi);
  }

  /**
    * Generated unit test for {@link RedditApi#getSubReddit}
    */
  @Suppress //FIXME : set proper parameter value and un-suppress this test case
  @SmallTest
  public void testGetSubReddit() throws SchemaException, ExecutionException, InterruptedException {
    // FIXME : set proper value for the parameters
    String subreddit = null;

    Call<SubRedditResult> callObject = redditApi.getSubReddit(
      subreddit, null, null, null);
    assertNotNull(callObject);

    SubRedditResult result = callObject.get();
    assertNotNull(result);
    //TODO : add more asserts
  }

}
