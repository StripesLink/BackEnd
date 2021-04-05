package edu.eci.escuelaing.StripesLink.Model.Mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends MongoRepository<SalaModel, String> {
	
}
