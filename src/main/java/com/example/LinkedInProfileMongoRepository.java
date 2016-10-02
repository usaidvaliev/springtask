package com.example;

import org.springframework.data.repository.CrudRepository;
import org.yaml.snakeyaml.events.Event.ID;

public interface LinkedInProfileMongoRepository extends CrudRepository<LinkedInProfile, ID>{}
