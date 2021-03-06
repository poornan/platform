/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sandesha2.storage.beanmanagers;

import java.util.List;

import org.apache.sandesha2.SandeshaException;
import org.apache.sandesha2.storage.SandeshaStorageException;
import org.apache.sandesha2.storage.beans.RMSBean;

/**
 * This is used to manage CreateSequence beans.
 */


public interface RMSBeanMgr extends RMBeanManager {

	public boolean insert(RMSBean bean) throws SandeshaStorageException;

	public boolean delete(String msgId) throws SandeshaStorageException;

	public RMSBean retrieve(String msgId) throws SandeshaStorageException;

	public boolean update(RMSBean bean) throws SandeshaStorageException;

	public List<RMSBean> find(RMSBean bean) throws SandeshaStorageException;
	
	public RMSBean findUnique (RMSBean bean) throws SandeshaException;

	public RMSBean retrieveBySequenceID(String seqId) throws SandeshaStorageException;
		
	public RMSBean retrieveByInternalSequenceID(String internalSeqId) throws SandeshaStorageException;
}
