/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.db.dao.server;

import java.util.Collection;
import java.util.List;

import org.apache.openmeetings.db.dto.basic.SearchResult;
import org.apache.openmeetings.db.dto.server.ClientSessionInfo;
import org.apache.openmeetings.db.entity.room.StreamClient;
import org.apache.openmeetings.db.entity.server.Server;

/**
 * Methods to add/get/remove {@link StreamClient}s to the session
 *
 *
 * @author sebawagner
 *
 */
public interface ISessionManager {
	void clearCache();

	/**
	 * Notified on server start, when the session manager should be started and
	 * eventually caches cleared/setup
	 */
	void sessionStart();

	StreamClient add(StreamClient c, Server server);
	/**
	 * add a new client item
	 *
	 * @param streamId
	 * @param scopeName
	 * @param remotePort
	 * @param remoteAddress
	 * @param swfUrl
	 * @param server
	 * @return
	 */
	StreamClient addClientListItem(String streamId, String scopeName, int remotePort, String remoteAddress, String swfUrl, Server server);

	Collection<StreamClient> getClients();

	/**
	 * loads the server into the client (only if database cache is used)
	 *
	 * @return
	 */
	Collection<StreamClient> getClientsWithServer();

	/**
	 * Get a client by its streamId
	 *
	 * @param streamId
	 * @param server
	 * @return
	 */
	StreamClient getClientByStreamId(String streamId, Server server);

	/**
	 * get a client by its publicSID and the server,
	 *
	 * @param publicSID
	 * @param server
	 * @return
	 */
	StreamClient getClientByPublicSID(String publicSID, Server server);

	/**
	 * same as {@link #getClientByPublicSID(String, boolean, Server)} but it ignores
	 * if the server part, so it will deliver any client just by its publicSID.<br/>
	 * <br/>
	 * <b>Note:</b>
	 * This method requires more time to find the user, so under normal circumstances
	 * you should use {@link #getClientByPublicSID(String, boolean, Server)}!
	 *
	 * @param publicSID
	 * @return
	 */
	ClientSessionInfo getClientByPublicSIDAnyServer(String publicSID);

	/**
	 *
	 * @param userId
	 * @return
	 *
	 * @deprecated There could be multiple users logged in with the same userid,
	 *             then this call would return a list not a single user
	 */
	@Deprecated
	StreamClient getClientByUserId(Long userId);

	/**
	 * Update the session object of the audio/video-connection and additionally
	 * swap the values to the session object of the user that holds the full
	 * session object
	 *
	 * @param streamId
	 * @param rcm
	 * @return
	 */
	boolean updateAVClientByStreamId(String streamId, StreamClient rcm, Server server);

	/**
	 * Update the session object
	 *
	 * updateRoomCount is only <i>one</i> time true, in
	 * ScopeApplicationAdapter#setRoomValues(Long, Boolean, Boolean, String)
	 * .
	 *
	 * @param streamId
	 * @param rcm
	 * @param updateRoomCount
	 *            true means the count for the room has to be updated
	 * @return
	 */
	boolean updateClientByStreamId(String streamId, StreamClient rcm, boolean updateRoomCount, Server server);

	/**
	 * Remove a client from the session store
	 *
	 * @param streamId
	 * @return
	 */
	boolean removeClient(String streamId, Server server);

	/**
	 * Get all ClientList Objects of that room and domain This Function is
	 * needed cause it is invoked internally AFTER the current user has been
	 * already removed from the ClientList to see if the Room is empty again and
	 * the PollList can be removed
	 * @param roomId
	 * @return
	 */
	List<StreamClient> getClientListByRoom(Long roomId);

	Collection<StreamClient> getClientListByRoomAll(Long roomId);

	/**
	 * get the current Moderator in this room
	 *
	 * @param roomname
	 * @return
	 */
	List<StreamClient> getCurrentModeratorByRoom(Long roomId);

	/**
	 * Get list of current client sessions
	 *
	 * @param start
	 * @param max
	 * @param orderby
	 * @param asc
	 * @return
	 */
	SearchResult<StreamClient> getListByStartAndMax(int start, int max, String orderby, boolean asc);

	/**
	 * returns number of current users recording
	 *
	 * @param roomId
	 * @return
	 */
	long getRecordingCount(long roomId);

	/**
	 * returns a number of current users publishing screensharing
	 *
	 * @param roomId
	 * @return
	 */
	long getPublishingCount(long roomId);

	/**
	 * Get a list of all servers of all rooms on that server, serverId = null
	 * means it is a local session on the master.
	 *
	 * @param server
	 * @return a set, a roomId can be only one time in this list
	 */
	List<Long> getActiveRoomIdsByServer(Server server);

	/**
	 * Get some statistics about the current sessions
	 *
	 * @return
	 */
	String getSessionStatistics();
}
