package org.apache.solr.cloud;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.concurrent.TimeUnit;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionThrottleTest extends SolrTestCaseJ4 {
  protected static Logger log = LoggerFactory.getLogger(ActionThrottleTest.class);
  
  
  @Test
  public void testBasics() throws Exception {

    ActionThrottle at = new ActionThrottle("test", 1000);
    long start = System.nanoTime();
    
    at.minimumWaitBetweenActions();
    
    // should be no wait
    assertTrue(TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS) < 1000);
    at.markAttemptingAction();
    
    if (random().nextBoolean()) Thread.sleep(100);
    
    at.minimumWaitBetweenActions();
    
    long elaspsedTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    
    assertTrue(elaspsedTime + "ms", elaspsedTime >= 995);
    
    start = System.nanoTime();
    
    at.markAttemptingAction();
    at.minimumWaitBetweenActions();
    
    Thread.sleep(random().nextInt(1000));
    
    elaspsedTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    
    assertTrue(elaspsedTime + "ms", elaspsedTime >= 995);
  }

}
