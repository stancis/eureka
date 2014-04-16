/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.netflix.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.LookupService;

/**
 * <tt>Discovery Manager</tt> configures <tt>Discovery Client</tt> based on the
 * properties specified.
 * 
 * <p>
 * The configuration file is searched for in the classpath with the name
 * specified by the property <em>eureka.client.props</em> and with the suffix
 * <em>.properties</em>. If the property is not specified,
 * <em>eureka-client.properties</em> is assumed as the default.
 * 
 * @author Karthik Ranganathan
 * 
 */
public class DiscoveryManager {
    private static final Logger logger = LoggerFactory.getLogger(DiscoveryManager.class);
    private DiscoveryClient discoveryClient;
    
    private EurekaInstanceConfig eurekaInstanceConfig;
    private EurekaClientConfig eurekaClientConfig;
    private static final DiscoveryManager s_instance = new DiscoveryManager();

    private DiscoveryManager() {
    }

    public static DiscoveryManager getInstance() {
        return s_instance;
    }

    @Deprecated
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }
    
    @Deprecated
    public void setEurekaClientConfig(EurekaClientConfig eurekaClientConfig) {
        this.eurekaClientConfig = eurekaClientConfig;
    }
    
    @Deprecated
    public void setEurekaInstanceConfig(EurekaInstanceConfig eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }
    
    /**
     * Initializes the <tt>Discovery Client</tt> with the given configuration.
     * 
     * @param config
     *            the instance info configuration that will be used for
     *            registration with Eureka.
     * @param eurekaConfig the eureka client configuration of the instance.
     */
    @Deprecated
    public void initComponent(EurekaInstanceConfig config,
            EurekaClientConfig eurekaConfig) {
        this.eurekaInstanceConfig = config;
        this.eurekaClientConfig = eurekaConfig;
        if (ApplicationInfoManager.getInstance().getInfo() == null) {
            // Initialize application info
            ApplicationInfoManager.getInstance().initComponent(config);
        }
        InstanceInfo info = ApplicationInfoManager.getInstance().getInfo();
        discoveryClient = new DiscoveryClient(info, eurekaConfig);
    }
    
    /**
     * Shuts down the <tt>Discovery Client</tt> which unregisters the
     * information about this instance from the <tt>Discovery Server</tt>.
     */
    @Deprecated
    public void shutdownComponent() {
        if (discoveryClient != null) {
            try {
                discoveryClient.shutdown();
                discoveryClient = null;
            } catch (Throwable th) {
                logger.error("Error in shutting down client", th);
            }
        }
    }

    public LookupService getLookupService() {
        return discoveryClient;
    }

    /**
     * Get the {@link DiscoveryClient}
     * @return the client that is used to talk to eureka.
     */
    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }

    /**
     * Get the instance of {@link EurekaClientConfig} this instance was initialized with.
     * @return the instance of {@link EurekaClientConfig} this instance was initialized with.
     */
    public EurekaClientConfig getEurekaClientConfig() {
        return eurekaClientConfig;
    }

    /**
     * Get the instance of {@link EurekaInstanceConfig} this instance was initialized with.
     * @return the instance of {@link EurekaInstanceConfig} this instance was initialized with.
     */
    public EurekaInstanceConfig getEurekaInstanceConfig() {
        return eurekaInstanceConfig;
    }

}
