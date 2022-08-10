/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.examples;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.CloudEvent;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ReactiveDataIndexMockEventReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveDataIndexMockEventReceiver.class);

    @Incoming("incoming-job-events")
    public Uni<Void> onCloudEvent(Message<CloudEvent> message) {
        LOGGER.debug("Received event: {}", message.getPayload());
        String data = new String(Objects.requireNonNull(message.getPayload().getData()).toBytes());
        LOGGER.debug("Received event.data: {}", data);
        return Uni.createFrom().completionStage(message.ack());
    }
}
