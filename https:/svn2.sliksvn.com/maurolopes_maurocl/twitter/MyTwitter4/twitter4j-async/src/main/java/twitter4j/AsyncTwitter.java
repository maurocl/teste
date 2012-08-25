/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package twitter4j;

import twitter4j.api.AccountMethodsAsync;
import twitter4j.api.BlockMethodsAsync;
import twitter4j.api.DirectMessageMethodsAsync;
import twitter4j.api.FavoriteMethodsAsync;
import twitter4j.api.FriendsFollowersMethodsAsync;
import twitter4j.api.FriendshipMethodsAsync;
import twitter4j.api.GeoMethodsAsync;
import twitter4j.api.HelpMethodsAsync;
import twitter4j.api.LegalResourcesAsync;
import twitter4j.api.ListMembersMethodsAsync;
import twitter4j.api.ListMethodsAsync;
import twitter4j.api.ListSubscribersMethodsAsync;
import twitter4j.api.LocalTrendsMethodsAsync;
import twitter4j.api.NewTwitterMethodsAsync;
import twitter4j.api.NotificationMethodsAsync;
import twitter4j.api.SavedSearchesMethodsAsync;
import twitter4j.api.SearchMethodsAsync;
import twitter4j.api.SpamReportingMethodsAsync;
import twitter4j.api.StatusMethodsAsync;
import twitter4j.api.TimelineMethodsAsync;
import twitter4j.api.TrendsMethodsAsync;
import twitter4j.api.UserMethodsAsync;
import twitter4j.auth.OAuthSupport;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface AsyncTwitter extends java.io.Serializable, OAuthSupport,
		TwitterBase, SearchMethodsAsync, TrendsMethodsAsync,
		TimelineMethodsAsync, StatusMethodsAsync, UserMethodsAsync,
		ListMethodsAsync, ListMembersMethodsAsync, ListSubscribersMethodsAsync,
		DirectMessageMethodsAsync, FriendshipMethodsAsync,
		FriendsFollowersMethodsAsync, AccountMethodsAsync,
		FavoriteMethodsAsync, NotificationMethodsAsync, BlockMethodsAsync,
		SpamReportingMethodsAsync, SavedSearchesMethodsAsync,
		LocalTrendsMethodsAsync, GeoMethodsAsync, LegalResourcesAsync,
		NewTwitterMethodsAsync, HelpMethodsAsync {

	/**
	 * Adds twitter listener
	 * 
	 * @param listener
	 *            TwitterListener
	 */
	void addListener(TwitterListener listener);
}
