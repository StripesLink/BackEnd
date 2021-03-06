package edu.eci.escuelaing.StripesLink.Model.Mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TematicaRepository extends MongoRepository<TematicaModel, String> {
	
	TematicaModel findByName(String name);
}

	
